package org.apache.commons.cli2;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util. HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        ListIterator<String> args = Arrays.asList("arg1", "arg2", "option1", "val1")
            .listIterator();

        option.process(commandLine, args);

        assertEquals("val1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option1"));
    }

    @Test
    public void testProcess_invalidOption() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        ListIterator<String> args = Arrays.asList("arg1", "arg2", "invalidOption")
            .listIterator();

        try {
            option.process(commandLine, args);
            fail("OptionException expected");
        } catch (OptionException e) {
            // expected
        }

        assertNull(commandLine.getOptionValue("option1"));
        assertFalse(commandLine.hasOption("option1"));
    }

    @Test
    public void testProcess_multipleValues() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        ListIterator<String> args = Arrays.asList("arg1", "arg2", "option1", "val1", "val2")
            .listIterator();

        option.process(commandLine, args);

        List<String> values = commandLine.getOptionValues("option1");
        assertEquals(Arrays.asList("val1", "val2"), values);
    }

    @Test
    public void testProcess_noValue() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        ListIterator<String> args = Arrays.asList("arg1", "arg2", "option1")
            .listIterator();

        option.process(commandLine, args);

        assertNull(commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option1"));
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        option.defaults(commandLine);

        assertEquals("defaultValue1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option1"));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        ListIterator<String> args = Arrays.asList("arg1", "arg2", "option1")
            .listIterator();

        assertTrue(option.canProcess(commandLine, args));
        assertTrue(option.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcess_wrongArgument() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        ListIterator<String> args = Arrays.asList("arg1", "arg2", "arg3")
            .listIterator();

        assertFalse(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, "arg3"));
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption("option1");
        Set triggers = option.getTriggers();

        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("-o"));
        assertTrue(triggers.contains("--option1"));
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption("option1");
        Set prefixes = option.getPrefixes();

        assertTrue(prefixes.contains("-"));
        assertTrue(prefixes.contains("--"));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        List<String> args = Arrays.asList("arg1", "arg2", "--option1", "--otherOption")
            .subList(0, 3).listIterator();

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            // expected
        }

        assertFalse(commandLine.hasOption("option1"));
    }

    @Test
    public void testValidate_validOption() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new MockOption("option1");
        List<String> args = Arrays.asList("arg1", "arg2", "--option1", "val1")
            .subList(0, 3).listIterator();

        option.validate(commandLine);

        assertTrue(commandLine.hasOption("option1"));
        assertEquals("val1", commandLine.getOptionValue("option1"));
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption("option1");
        List helpLines = option.helpLines(1, null, null);

        assertEquals(1, helpLines.size());
        String expectedHelpLine = "  -o, --option1         option1 description";
        assertEquals(expectedHelpLine, helpLines.get(0).toString());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Option option = new MockOption("option1");
        option.appendUsage(buffer, null, null);

        String expectedUsageString = "--option1 option1 description";
        assertEquals(expectedUsageString, buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption("option1");

        assertEquals("option1", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption("option1");

        assertEquals("option1 description", option.getDescription());
    }

    @Test
    public void testgetId() {
        Option option = new MockOption("option1");

        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option1 = new MockOption("option1");
        Option option2 = new MockOption("option2");

        Option foundOption = option1.findOption("option1");
        assertSame(option1, foundOption);

        foundOption = option1.findOption("option2");
        assertNull(foundOption);

        foundOption = option2.findOption("option1");
        assertNull(foundOption);

        foundOption = option2.findOption("option2");
        assertSame(option2, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption("option1");

        assertFalse(option.isRequired());

        Option requiredOption = new MockOption("option2") {
            @Override
            public boolean isRequired() {
                return true;
            }
        };

        assertTrue(requiredOption.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = new MockOption("parentOption");
        Option childOption = new MockOption("childOption") {
            @Override
            public Option getParent() {
                return parentOption;
            }
        };

        assertNull(childOption.getParent());
        assertSame(parentOption, childOption.getParent());
    }

    @Test
    public void testSetParent() {
        Option parentOption = new MockOption("parentOption");
        Option childOption = new MockOption("childOption");

        childOption.setParent(parentOption);

        assertSame(parentOption, childOption.getParent());
    }

    @Test
    public void testFindOption_withParent() {
        Option parentOption = new MockOption("parentOption");
        Option childOption1 = new MockOption("childOption1") {
            @Override
            public Option getParent() {
                return parentOption;
            }
        };
        Option childOption2 = new MockOption("childOption2");

        Option foundOption = parentOption.findOption("childOption1");
        assertSame(childOption1, foundOption);

        foundOption = parentOption.findOption("childOption2");
        assertNull(foundOption);

        foundOption = childOption1.findOption("childOption1");
        assertSame(childOption1, foundOption);

        foundOption = childOption1.findOption("childOption2");
        assertNull(foundOption);

        foundOption = childOption2.findOption("childOption1");
        assertNull(foundOption);

        foundOption = childOption2.findOption("childOption2");
        assertSame(childOption2, foundOption);
    }

    private static class MockOption implements Option {
        private final String name;
        private boolean required = false;
        private Option parent;

        public MockOption(String name) {
            this.name = name;
        }

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
            throws OptionException {
            // empty method
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // empty method
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            return true;
        }

        @Override
        public Set<String> getTriggers() {
            return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(name, "-" + name, "--" + name)));
        }

        @Override
        public Set<String> getPrefixes() {
            return Collections.unmodifiableSet(new HashSet<>(Arrays.asList("-", "--")));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // empty method
        }

        @Override
        public List<HelpLine> helpLines(int depth, Set helpSettings, Comparator comp) {
            return Collections.emptyList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            // empty method
        }

        @Override
        public String getPreferredName() {
            return name;
        }

        @Override
        public String getDescription() {
            return "option description";
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Option findOption(String trigger) {
            return this;
        }

        @Override
        public boolean isRequired() {
            return required;
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