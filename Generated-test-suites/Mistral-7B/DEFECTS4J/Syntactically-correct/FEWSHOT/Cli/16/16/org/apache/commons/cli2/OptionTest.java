package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.commons.cli2.util.HelpFormatter.DefaultHelpFormatter;
import org.junit.Test;

public class OptionTest {

    private static final String TEST_OPTION_PREFERRED_NAME = "testOption";
    private static final String TEST_OPTION_DESCRIPTION = "This is a test option";
    private static final int TEST_OPTION_ID = 1;
    private static final Set TEST_OPTION_TRIGGERS = new LinkedHashSet(Arrays.asList( new String[]{"--testOption", "-t"} ));
    private static final Set TEST_OPTION_PREFIXES = new LinkedHashSet(Arrays.asList( new String[]{"--", "-"} ));

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator args = Arrays.asList(new String[]{"test", "testOption", "testValue"}).listIterator();
        Option option = new TestOption();
        option.process(commandLine, args);
        assertTrue(commandLine.hasOption(TEST_OPTION_PREFERRED_NAME));
        assertEquals(TEST_OptionValue.TEST_VALUE, commandLine.getOptionValue(TEST_OPTION_PREFERRED_NAME));
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new TestOption();
        option.defaults(commandLine);
        assertFalse(commandLine.hasOption(TEST_OPTION_PREFERRED_NAME));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator args = Arrays.asList(new String[]{"test", "testOption", "testValue"}).listIterator();
        Option option = new TestOption();
        assertTrue(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessWithMultipleArgs() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator args = Arrays.asList(new String[]{"test", "--testOption=testValue1", "testOption=testValue2", "test"}).listIterator();
        Option option = new TestOption();
        assertTrue(option.canProcess(commandLine, args));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithInvalidArg() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator args = Arrays.asList(new String[]{"test", "invalid", "test"}).listIterator();
        Option option = new TestOption();
        option.canProcess(commandLine, args);
    }

    @Test
    public void testGetTriggers() {
        Option option = new TestOption();
        Set triggers = option.getTriggers();
        assertTrue(triggers.containsAll(TEST_OPTION_TRIGGERS));
    }

    @Test
    public void testGetPrefixes() {
        Option option = new TestOption();
        Set prefixes = option.getPrefixes();
        assertTrue(prefixes.containsAll(TEST_OPTION_PREFIXES));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new TestOption();
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new TestOption();
        ListIterator args = Arrays.asList(new String[]{"test", "testOption", "testValue"}).listIterator();
        args.next(); // consume the first argument
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new TestOption();
        int depth = 0;
        Set helpSettings = new LinkedHashSet();
        Comparator comp = null;
        List helpLines = option.helpLines(depth, helpSettings, comp);
        assertFalse(helpLines.isEmpty());
    }

    @Test
    public void testAppendUsage() {
        Option option = new TestOption();
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new LinkedHashSet();
        Comparator comp = null;
        option.appendUsage(buffer, helpSettings, comp);
        assertTrue(buffer.toString().contains(TEST_OPTION_DESCRIPTION));
    }

    @Test
    public void testGetPreferredName() {
        Option option = new TestOption();
        assertEquals(TEST_OPTION_PREFERRED_NAME, option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new TestOption();
        assertEquals(TEST_OPTION_DESCRIPTION, option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new TestOption();
        assertEquals(TEST_OPTION_ID, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option1 = new TestOption();
        Option option2 = new TestOption();
        Option parent = new Option() {
            @Override
            public Set getTriggers() {
                return new LinkedHashSet(Arrays.asList(new String[]{"--parent"}));
            }
        };
        option1.setParent(parent);
        option2.setParent(parent);
        ListIterator args = Arrays.asList(new String[]{"--parent", "--testOption"}).listIterator();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option foundOption1 = option1.findOption(TEST_OPTION_TRIGGERS.iterator().next());
        Option foundOption2 = option2.findOption(TEST_OPTION_TRIGGERS.iterator().next());
        assertSame(option1, foundOption1);
        assertSame(option2, foundOption2);
    }

    @Test
    public void testIsRequired() {
        Option option = new TestOption();
        assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option option = new TestOption();
        assertNull(option.getParent());
    }

    @Test
    public void testSetParent() {
        Option option = new TestOption();
        Option parent = new Option() {
            @Override
            public Set getTriggers() {
                return new LinkedHashSet(Arrays.asList(new String[]{"--parent"}));
            }
        };
        option.setParent(parent);
        assertSame(parent, option.getParent());
    }

    private static class TestOption implements Option {

        private static final String TEST_VALUE = "testValue";
        private static class TEST_OptionValue implements Value {

            public static final String TEST_VALUE = "testValue";

            @Override
            public String getStringValue() {
                return TEST_VALUE;
            }
        }

        @Override
        public void process(final WriteableCommandLine commandLine, final ListIterator args)
            throws OptionException {
            // empty implementation
        }

        @Override
        public void defaults(final WriteableCommandLine commandLine) {
            // empty implementation
        }

        @Override
        public boolean canProcess(final WriteableCommandLine commandLine, final String argument) {
            return false;
        }

        @Override
        public boolean canProcess(final WriteableCommandLine commandLine, final ListIterator arguments) {
            return false;
        }

        @Override
        public Set getTriggers() {
            return TEST_OPTION_TRIGGERS;
        }

        @Override
        public Set getPrefixes() {
            return TEST_OPTION_PREFIXES;
        }

        @Override
        public void validate(final WriteableCommandLine commandLine)
            throws OptionException {
            // empty implementation
        }

        @Override
        public List helpLines(
            final int depth,
            final Set helpSettings,
            final Comparator comp) {
            return new HelpFormatter().helpLines(this, depth, helpSettings, comp);
        }

        @Override
        public void appendUsage(
            final StringBuffer buffer,
            final Set helpSettings,
            final Comparator comp) {
            new DefaultHelpFormatter().appendUsage(this, buffer, helpSettings, comp);
        }

        @Override
        public String getPreferredName() {
            return TEST_OPTION_PREFERRED_NAME;
        }

        @Override
        public String getDescription() {
            return TEST_OPTION_DESCRIPTION;
        }

        @Override
        public int getId() {
            return TEST_OPTION_ID;
        }

        @Override
        public Option findOption(final String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(final Option parent) {
            // empty implementation
        }

        @Override
        public Value parseValue(final String value)
            throws ParseException {
            return new TEST_OptionValue();
        }
    }
}