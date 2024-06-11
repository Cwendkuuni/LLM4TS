package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new HashSet<>();
        triggers.add("--opt");
        Option option = new MockOption("opt", triggers, null, null);
        Set<String> result = option.getTriggers();
        assertTrue(result.equals(triggers));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-o");
        Option option = new MockOption("opt", null, prefixes, null);
        Set<String> result = option.getPrefixes();
        assertTrue(result.equals(prefixes));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        List<String> args = new ArrayList<>();
        args.add("--opt value");
        ListIterator<String> argsIterator = args.listIterator();

        Option option = new MockOption("opt", null, null, null);
        assertTrue(option.canProcess(commandLine, argsIterator));
        assertTrue(option.canProcess(commandLine, commandLine.getArgList().iterator()));

        option = new MockOption("opt", null, null, "value");
        assertTrue(option.canProcess(commandLine, argsIterator));
        assertTrue(option.canProcess(commandLine, commandLine.getArgList().iterator()));

        args.clear();
        args.add("--opt");
        assertTrue(option.canProcess(commandLine, argsIterator));
        assertFalse(option.canProcess(commandLine, commandLine.getArgList().iterator()));

        args.clear();
        args.add("--opt value1 value2");
        assertFalse(option.canProcess(commandLine, argsIterator));
        assertFalse(option.canProcess(commandLine, commandLine.getArgList().iterator()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidCanProcess() {
        Option option = new MockOption("opt", null, null, null);
        List<String> args = new ArrayList<>();
        option.canProcess(null, args.iterator());
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        List<String> args = new ArrayList<>();
        args.add("--opt value");
        ListIterator<String> argsIterator = args.listIterator();

        Option option = new MockOption("opt", null, null, null);
        option.process(commandLine, argsIterator);
        assertTrue(commandLine.getOptionValues("opt").equals(Arrays.asList("value")));

        commandLine.clear();
        option.process(commandLine, argsIterator);
        assertTrue(commandLine.getArgList().isEmpty());

        args.clear();
        args.add("--opt value1 value2");
        option.process(commandLine, argsIterator);
        assertTrue(commandLine.getOptionValues("opt").equals(Arrays.asList("value1", "value2")));

        commandLine.clear();
        args.clear();
        option.process(commandLine, args.iterator());
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test(expected = OptionException.class)
    public void testInvalidProcess() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        List<String> args = new ArrayList<>();
        args.add("--opt value");
        ListIterator<String> argsIterator = args.listIterator();

        Option option = new MockOption("opt", null, null, null) {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                    throws OptionException {
                throw new OptionException("This option should never be processed.");
            }
        };
        option.process(commandLine, argsIterator);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("opt", null, null, null);
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValues("opt"));

        commandLine.clear();
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValues("opt"));

        commandLine.putOptionValue("opt", "defaultValue");
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValues("opt"));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("opt", null, null, null);
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testInvalidValidate() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("opt", null, null, null) {
            @Override
            public void validate(WriteableCommandLine commandLine)
                    throws OptionException {
                throw new OptionException("This option should never be validated.");
            }
        };
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption("opt", null, null, null);
        List<HelpLine> helpLines = option.helpLines(1, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("--opt", helpLine.getOptionSpec().getOption());
        assertEquals("", helpLine.getDescription());
        assertEquals(HelpLine.Type.NORMAL, helpLine.getType());
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption("opt", null, null, null);
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("--opt", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption("opt", null, null, null);
        assertEquals("opt", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption("opt", null, null, null);
        assertEquals("", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new MockOption("opt", null, null, null);
        assertEquals(0, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option1 = new MockOption("opt1", null, null, null);
        Option option2 = new MockOption("opt2", null, null, null);
        Option option3 = new MockOption("opt1", null, null, null);

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        Option result = option1.findOption("opt1");
        assertEquals(option1, result);

        result = option1.findOption("opt2");
        assertNull(result);

        result = option3.findOption("opt1");
        assertEquals(option1, result);
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption("opt", null, null, null);
        assertFalse(option.isRequired());

        option = new MockOption("opt", null, null, null) {
            @Override
            public boolean isRequired() {
                return true;
            }
        };
        assertTrue(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parent = new MockOption("parent", null, null, null);
        Option option = new MockOption("opt", parent, null, null);
        assertEquals(parent, option.getParent());

        option = new MockOption("opt", null, null, null);
        assertNull(option.getParent());
    }

    @Test
    public void testSetParent() {
        Option option = new MockOption("opt", null, null, null);
        Option parent = new MockOption("parent", null, null, null);
        option.setParent(parent);
        assertEquals(parent, option.getParent());

        option = new MockOption("opt", null, null, null);
        assertNull(option.getParent());
    }

    private static class MockOption implements Option {
        private final String name;
        private final Set<String> triggers;
        private final Set<String> prefixes;
        private final Option parent;

        public MockOption(String name, Set<String> triggers, Set<String> prefixes, Option parent) {
            this.name = name;
            this.triggers = triggers;
            this.prefixes = prefixes;
            this.parent = parent;
        }

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return false;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            return false;
        }

        @Override
        public Set getTriggers() {
            return triggers;
        }

        @Override
        public Set getPrefixes() {
            return prefixes;
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return Collections.emptyList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        }

        @Override
        public String getPreferredName() {
            return name;
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return parent;
        }

        @Override
        public void setParent(Option parent) {
            this.parent = parent;
        }
    }
}