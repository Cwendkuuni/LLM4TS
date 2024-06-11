package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.parse.MultiValuedOption;
import org.apache.commons.cli2.parser.ArgumentParser;
import org.junit.Test;

/**
 * Test class for GroupImpl
 */
public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // create sample Group
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("g")
                .withDescription("group option")
                .createOption());
        options.add(OptionBuilder.withName("a")
                .withDescription("anonymous option")
                .createOption());
        options.add(OptionBuilder.withName("b")
                .withDescription("another option")
                .createOption());
        final OptionGroup group = new OptionGroupImpl(options, "MyGroup", "My group");

        // create sample CommandLine
        final CommandLine cmd = new CommandLine();
        cmd.parse(new DefaultParser().parse(group, new String[]{"g", "a", "b"}));

        // test basic functionality
        assertEquals("My group", group.getPreferredName());
        assertFalse(group.isRequired());
        assertEquals(3, group.getMinimum());
        assertEquals(3, group.getMaximum());

        // test canProcess
        final String[] args = {"g", "a", "b"};
        for (final String arg : args) {
            assertTrue(group.canProcess(cmd, arg));
        }

        // test getPrefixes
        assertEquals(Collections.emptySet(), group.getPrefixes());

        // test getTriggers
        assertEquals(Collections.singletonList("g"), group.getTriggers());

        // test process
        int index = 0;
        for (final String arg : args) {
            final Option option = group.findOption(arg);
            option.process(cmd, new ListIterator<String>() {
                @Override
                public boolean hasNext() {
                    return index < args.length;
                }

                @Override
                public String next() {
                    return args[index++];
                }
            });
        }

        // test validate
        cmd.validate();

        // test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, DisplaySetting.ALL, null);
        assertEquals("My group:\n" +
                "  -g <GROUP-OPTION>: group option\n" +
                "  -a <ANONYMOUS-OPTION>: anonymous option\n" +
                "  -b <OPTION>: another option\n", sb.toString());

        // test helpLines
        final List helpLines = group.helpLines(1, DisplaySetting.ALL, null);
        assertEquals(1, helpLines.size());
        assertEquals("My group:\n" +
                "  -g, --group-option           group option\n" +
                "  -a, --anonymous-option       anonymous option\n" +
                "  -b, --another-option         another option",
                ((HelpLine) helpLines.get(0)).getShortDescription());

        // test getOptions and getAnonymous
        assertEquals(2, group.getOptions().size());
        assertEquals(1, group.getAnonymous().size());

        // test findOption
        final Option foundOption = group.findOption("a");
        assertNotNull(foundOption);

        // test getMinimum and getMaximum
        assertEquals(3, group.getMinimum());
        assertEquals(3, group.getMaximum());

        // test isRequired
        assertFalse(group.isRequired());

        // test defaults
        group.defaults(cmd);
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));

        // edge cases
        // empty list of options
        final List emptyOptions = new ArrayList();
        final OptionGroup emptyGroup = new OptionGroupImpl(emptyOptions, "EmptyGroup", "");
        assertEquals(0, emptyGroup.getMinimum());
        assertEquals(0, emptyGroup.getMaximum());
        assertTrue(emptyGroup.canProcess(cmd, ""));
        assertFalse(emptyGroup.findOption("") != null);

        // null name
        final OptionGroup nullNameGroup = new OptionGroupImpl(options, null, "Mygroup");
        assertEquals(3, nullNameGroup.getMinimum());
        assertEquals(3, nullNameGroup.getMaximum());
        assertEquals("Mygroup", nullNameGroup.getPreferredName());

        // null description
        final OptionGroup nullDescGroup = new OptionGroupImpl(options, "MyGroup", null);
        assertEquals(3, nullDescGroup.getMinimum());
        assertEquals(3, nullDescGroup.getMaximum());
        assertEquals("MyGroup", nullDescGroup.getPreferredName());

        // negativetive minimum
        final OptionGroup negativeMinGroup = new OptionGroupImpl(options, "NegativeMinGroup", "Mygroup") {
            @Override
            public int getMinimum() {
                return -1;
            }
        };
        assertEquals(0, negativeMinGroup.getMinimum());
        assertEquals(3, negativeMinGroup.getMaximum());
        assertFalse(negativeMinGroup.canProcess(cmd, "g"));
        assertFalse(negativeMinGroup.findOption("g") != null);

        // negativetive maximum
        final OptionGroup negativeMaxGroup = new OptionGroupImpl(options, "NegativeMaxGroup", "Mygroup") {
            @Override
            public int getMaximum() {
                return -1;
            }
        };
        assertEquals(3, negativeMaxGroup.getMinimum());
        assertEquals(0, negativeMaxGroup.getMaximum());
        assertFalse(negativeMaxGroup.canProcess(cmd, "g"));
        assertFalse(negativeMaxGroup.findOption("g") != null);

        // test exception handling
        // missing minimum
        final OptionGroup missingMinGroup = new OptionGroupImpl(options, "MissingMinGroup", "Mygroup") {
            @Override
            public int getMinimum() {
                return 0;
            }
        };
        assertTrue(missingMinGroup.isRequired());
        try {
            missingMinGroup.validate(cmd);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option(s): g, a, b", e.getMessage());
        }

        // missing maximum
        final OptionGroup missingMaxGroup = new OptionGroupImpl(options, "MissingMaxGroup", "Mygroup") {
            @Override
            public int getMaximum() {
                return 4;
            }
        };
        try {
            missingMaxGroup.validate(cmd);
            fail();
        } catch (OptionException e) {
            assertEquals("Too many options: 3, allowed: 4", e.getMessage());
        }

        // too few options
        final OptionGroup tooFewGroup = new OptionGroupImpl(options, "TooFewGroup", "Mygroup") {
            @Override
            public int getMinimum() {
                return 4;
            }
        };
        try {
            tooFewGroup.validate(cmd);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option(s): a, b", e.getMessage());
        }
    }
}