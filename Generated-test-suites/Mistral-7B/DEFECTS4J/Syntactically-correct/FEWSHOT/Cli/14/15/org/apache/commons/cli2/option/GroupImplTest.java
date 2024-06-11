package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.apache.commons.cli2.option.GroupImpl;

/**
 * Test class for GroupImpl
 */
public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Test constructor
        final List options = new ArrayList();
        options.add(createOption("--option1", null, null, false, null, null));
        options.add(createOption("--option2", "shortOpt2", "L", true, "longOpt2", null));
        options.add(createOption("--option3", null, null, false, null, null));

        final GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 1, 3);

        assertEquals("groupName", group.getPreferredName());
        assertEquals("groupDescription", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertSame(options, group.getOptions());
        assertSame(Collections.emptyList(), group.getAnonymous());

        // Test getters
        assertEquals("groupName", group.getName());
        assertEquals("groupDescription", group.getDescription());

        // Test canProcess method
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option3"));
        assertTrue(group.canProcess(commandLine, "groupName"));
        assertFalse(group.canProcess(commandLine, "unknownOption"));

        // Test getPrefixes method
        assertEquals(Collections.emptySet(), group.getPrefixes());

        // Test getTriggers method
        final Set triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("--option1"));
        assertTrue(triggers.contains("--option2"));
        assertTrue(triggers.contains("--option3"));

        // Test process method
        final List<String> arguments = new ArrayList<>();
        arguments.add("option1");
        arguments.add("--option2");
        arguments.add("--unknown");

        group.process(commandLine, arguments.listIterator());

        assertEquals(2, commandLine.getArgList().size());
        assertEquals("option2", commandLine.getArgList().get(0));
        assertEquals("unknown", commandLine.getArgList().get(1));

        // Test validate method
        commandLine.clear();

        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        commandLine.setOption("option1", true);
        commandLine.setOption("option2", "value");
        commandLine.setOption("option3", "value2");

        group.validate(commandLine);

        assertEquals(3, commandLine.getArgList().size());
        assertEquals("value", commandLine.getArgList().get(0));
        assertEquals("value2", commandLine.getArgList().get(1));
        assertNull(commandLine.getArgList().get(2));

        // Test appendUsage method
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("groupName:\n" +
                     "\tgroupDescription\n" +
                     "\n" +
                     "--option1 <value>\n" +
                     "--option2 [--longOpt2 <value>]\n" +
                     "--option3 <value>\n", buffer.toString());

        // Test helpLines method
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(3, helpLines.size());

        // Test findOption method
        final Option option1 = group.findOption("--option1");
        assertSame(createOption("--option1", null, null, false, null, null), option1);

        final Option option2 = group.findOption("--option2");
        assertSame(createOption("--option2", "shortOpt2", "L", true, "longOpt2", null), option2);
    }

    @Test
    public void testGroupImpl_anonymousArguments() {
        // Test constructor
        final List options = new ArrayList();
        options.add(createOption("--option1", null, null, false, null, null));
        options.add(createArgument("--arg"));

        final GroupImpl group = new GroupImpl(options, "groupName", "groupDescription", 0, 1);

        assertEquals("groupName", group.getPreferredName());
        assertEquals("groupDescription", group.getDescription());
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertSame(options, group.getOptions());
        assertSame(Collections.singletonList(createArgument("--arg")), group.getAnonymous());

        // Test getters
        assertEquals("groupName", group.getName());
        assertEquals("groupDescription", group.getDescription());

        // Test canProcess method
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // Anonymous argument can be processed alone
        assertTrue(group.canProcess(commandLine, "--arg"));

        // Option can be processed only with the anonymous argument
        commandLine.setOption("--option1", true);
        assertFalse(group.canProcess(commandLine, "--arg"));

        // Option and anonymous argument can be processed together
        commandLine.setOption("--option1", true);
        commandLine.setArgList(Collections.singletonList("--arg"));
        assertTrue(group.canProcess(commandLine, "--arg"));

        // Test process method
        final List<String> arguments = new ArrayList<>();
        arguments.add("--arg");

        group.process(commandLine, arguments.listIterator());

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("--arg", commandLine.getArgList().get(0));

        // Test validate method
        commandLine.clear();

        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        commandLine.setArgList(Collections.singletonList("--arg"));

        group.validate(commandLine);

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("--arg", commandLine.getArgList().get(0));

        // Test appendUsage method
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("groupName:\n" +
                     "\tgroupDescription\n" +
                     "\n" +
                     "--option1\n" +
                     "--arg\n", buffer.toString());

        // Test helpLines method
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(3, helpLines.size());

        // Test findOption method
        final Option option1 = group.findOption("--option1");
        assertSame(createOption("--option1", null, null, false, null, null), option1);

        final Argument arg = group.findArgument("--arg");
        assertSame(createArgument("--arg"), arg);
    }

    private Option createOption(final String longOpt,
                               final String shortOpt,
                               final String optPrefix,
                               final boolean required,
                               final String longArgName,
                               final String description) {
        return new OptionBuilder()
                .withLongOpt(longOpt)
                .withShortOpt(shortOpt)
                .withOptPrefix(optPrefix)
                .withRequiredArg()
                .withArgName(longArgName)
                .withDescription(description)
                .create();
    }

    private Argument createArgument(final String name) {
        return new ArgumentBuilder()
                .withName(name)
                .withDescription("Anonymous argument")
                .create();
    }
}