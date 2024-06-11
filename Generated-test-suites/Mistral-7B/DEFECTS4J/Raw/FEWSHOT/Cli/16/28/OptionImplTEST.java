package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.commandline.ParserResult;
import org.apache.commons.cli2.option.Option;
import org.apache.commons.cli2.option.OptionBuilder;
import org.apache.commons.cli2.option.OptionFactory;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.HelpFormatter;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionBuilder("optionName")
                .build();
        OptionImpl optionImpl = (OptionImpl) option;

        assertEquals(0, optionImpl.getId());
        assertFalse(optionImpl.isRequired());
        assertNull(optionImpl.getPreferredName());
        assertNull(optionImpl.getDescription());
        assertTrue(optionImpl.getPrefixes().isEmpty());
        assertTrue(optionImpl.getTriggers().isEmpty());
    }

    @Test
    public void testOptionImplConstructorWithId() {
        Option option = new OptionBuilder("optionName")
                .build();
        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "preferredName";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("trigger1", "trigger2");
            }
        };

        assertEquals(1, optionImpl.getId());
        assertTrue(optionImpl.isRequired());
        assertEquals("preferredName", optionImpl.getPreferredName());
        assertEquals("description", optionImpl.getDescription());
        assertEquals(2, optionImpl.getPrefixes().size());
        assertTrue(optionImpl.getPrefixes().containsAll(Arrays.asList("prefix1", "prefix2")));
        assertEquals(2, optionImpl.getTriggers().size());
        assertTrue(optionImpl.getTriggers().containsAll(Arrays.asList("trigger1", "trigger2")));
    }

    @Test
    public void testCanProcess() {
        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return argument.equals("expectedValue");
            }
        };

        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = Arrays.asList("argument1", "argument2", "expectedValue").listIterator();

        assertTrue(optionImpl.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "preferredName";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("trigger1", "trigger2");
            }
        };

        String expectedUsage = "optionName [OPTION_VALUE]\n" +
                "   Optional option with prefixes prefix1 and prefix2.\n" +
                "   This option triggers actions based on the following values:\n" +
                "      trigger1\n" +
                "      trigger2\n";

        String optionString = optionImpl.toString();

        assertEquals(expectedUsage, optionString);
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionBuilder("option1")
                .required(true)
                .build();

        OptionImpl option2 = new OptionBuilder("option2")
                .required(true)
                .build();

        assertFalse(option1.equals(option2));

        OptionImpl option3 = new OptionBuilder("option1")
                .required(true)
                .build();

        assertTrue(option1.equals(option3));

        OptionImpl option4 = new OptionBuilder("option1")
                .id(1)
                .required(true)
                .build();

        assertTrue(option1.equals(option4));

        OptionImpl option5 = new OptionBuilder("option1")
                .id(1)
                .required(true)
                .description("description")
                .build();

        assertTrue(option1.equals(option5));

        OptionImpl option6 = new OptionBuilder("option1")
                .id(1)
                .required(true)
                .description("description")
                .prefixes(Arrays.asList("prefix1", "prefix2"))
                .triggers(Arrays.asList("trigger1", "trigger2"))
                .build();

        assertTrue(option1.equals(option6));
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionBuilder("option1")
                .required(true)
                .build();

        OptionImpl option2 = new OptionBuilder("option2")
                .required(true)
                .build();

        int option1HashCode = option1.hashCode();

        OptionImpl option3 = new OptionBuilder("option1")
                .required(true)
                .build();

        assertEquals(option1HashCode, option3.hashCode());

        OptionImpl option4 = new OptionBuilder("option1")
                .id(1)
                .required(true)
                .build();

        assertNotEquals(option1HashCode, option4.hashCode());

        OptionImpl option5 = new OptionBuilder("option1")
                .id(1)
                .required(true)
                .description("description")
                .build();

        assertNotEquals(option1HashCode, option5.hashCode());

        OptionImpl option6 = new OptionBuilder("option1")
                .id(1)
                .required(true)
                .description("description")
                .prefixes(Arrays.asList("prefix1", "prefix2"))
                .triggers(Arrays.asList("trigger1", "trigger2"))
                .build();

        assertNotEquals(option1HashCode, option6.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionBuilder optionBuilder = new OptionBuilder("option");
        Option option = optionBuilder.build();

        OptionImpl optionImpl = (OptionImpl) option;

        Set<String> triggers = new HashSet<>(Arrays.asList("trigger1", "trigger2"));

        OptionImpl optionWithTriggers1 = new OptionBuilder("option2")
                .id(1)
                .required(true)
                .description("description1")
                .prefixes(Arrays.asList("prefix1"))
                .triggers(triggers)
                .build();

        OptionImpl optionWithTriggers2 = new OptionBuilder("option3")
                .id(2)
                .required(true)
                .description("description2")
                .prefixes(Arrays.asList("prefix2"))
                .triggers(triggers)
                .build();

        OptionImpl optionWithNoTriggers = new OptionBuilder("option4")
                .id(3)
                .required(true)
                .description("description3")
                .prefixes(Arrays.asList("prefix3"))
                .build();

        OptionFactory optionFactory = new OptionFactory();
        Parser parser = optionFactory.createParser("TestParser", OptionImpl.class, null);

        OptionImpl foundOption = optionImpl.findOption(parser, "trigger1");
        assertSame(optionWithTriggers1, foundOption);

        foundOption = optionImpl.findOption(parser, "trigger2");
        assertSame(optionWithTriggers2, foundOption);

        foundOption = optionImpl.findOption(parser, "trigger3");
        assertNull(foundOption);

        foundOption = optionImpl.findOption(parser, "prefix1");
        assertNull(foundOption);

        foundOption = optionImpl.findOption(parser, "prefix2");
        assertNull(foundOption);

        foundOption = optionImpl.findOption(parser, "prefix3");
        assertSame(optionWithNoTriggers, foundOption);

        foundOption = optionImpl.findOption(parser, null);
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        OptionImpl option1 = new OptionBuilder("option1")
                .required(false)
                .build();

        assertFalse(option1.isRequired());

        OptionImpl option2 = new OptionBuilder("option2")
                .required(true)
                .build();

        assertTrue(option2.isRequired());
    }

    @Test
    public void testDefaults() {
        OptionImpl optionImpl = new OptionBuilder("option")
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLine();

        optionImpl.defaults(commandLine);

        assertNull(commandLine.getOption("option"));
    }

    @Test
    public void testCheckPrefixes() {
        OptionBuilder optionBuilder = new OptionBuilder("option");
        Option option = optionBuilder.build();

        OptionImpl optionImpl = (OptionImpl) option;

        Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));

        OptionImpl optionWithPrefixes1 = new OptionBuilder("option2")
                .id(1)
                .required(true)
                .description("description1")
                .prefixes(prefixes)
                .build();

        OptionImpl optionWithPrefixes2 = new OptionBuilder("option3")
                .id(2)
                .required(true)
                .description("description2")
                .prefixes(Arrays.asList("prefix3"))
                .build();

        OptionImpl optionWithNoPrefixes = new OptionBuilder("option4")
                .id(3)
                .required(true)
                .description("description3")
                .build();

        OptionFactory optionFactory = new OptionFactory();
        Parser parser = optionFactory.createParser("TestParser", OptionImpl.class, null);

        optionImpl.checkPrefixes(prefixes);

        try {
            optionImpl.checkPrefixes(Arrays.asList("prefix3"));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("option3 needs prefixes prefix1, prefix2", e.getMessage());
        }

        try {
            optionImpl.checkPrefixes(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Option option needs at least one prefix", e.getMessage());
        }
    }
}