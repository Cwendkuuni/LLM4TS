package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImplTestImpl(1, true);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        Set prefixes = new HashSet();

        // Test when arguments has next and can process
        commandLine = new WriteableCommandLineImpl();
        commandLine.addArgument("arg");
        assertTrue(option.canProcess(commandLine, commandLine.listIterator()));

        // Test when arguments has next but cannot process
        commandLine = new WriteableCommandLineImpl();
        commandLine.addArgument("arg");
        assertFalse(option.canProcess(commandLine, commandLine.listIterator()));

        // Test when arguments does not have next
        commandLine = new WriteableCommandLineImpl();
        assertFalse(option.canProcess(commandLine, commandLine.listIterator()));
    }

    @Test
    public void testToString() {
        assertNotNull(option.toString());
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testEquals() {
        OptionImpl sameOption = new OptionImplTestImpl(1, true);
        OptionImpl differentIdOption = new OptionImplTestImpl(2, true);
        OptionImpl differentPreferredNameOption = new OptionImplTestImpl(1, true);
        differentPreferredNameOption.setPreferredName("test");
        OptionImpl differentDescriptionOption = new OptionImplTestImpl(1, true);
        differentDescriptionOption.setDescription("test");
        OptionImpl differentPrefixOption = new OptionImplTestImpl(1, true);
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        differentPrefixOption.setPrefixes(prefixes);
        OptionImpl differentTriggersOption = new OptionImplTestImpl(1, true);
        Set<String> triggers = new HashSet<>();
        triggers.add("t");
        differentTriggersOption.setTriggers(triggers);

        assertTrue(option.equals(option));
        assertTrue(option.equals(sameOption));
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertFalse(option.equals(differentIdOption));
        assertFalse(option.equals(differentPreferredNameOption));
        assertFalse(option.equals(differentDescriptionOption));
        assertFalse(option.equals(differentPrefixOption));
        assertFalse(option.equals(differentTriggersOption));
    }

    @Test
    public void testHashCode() {
        assertNotNull(option.hashCode());
    }

    @Test
    public void testFindOption() {
        assertNull(option.findOption("test"));
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        option.defaults(commandLine);
        // Verify that defaults does nothing
        assertTrue(commandLine.getArguments().isEmpty());
        assertTrue(commandLine.getOptions().isEmpty());
    }

    private class OptionImplTestImpl extends OptionImpl {
        public OptionImplTestImpl(int id, boolean required) {
            super(id, required);
        }

        public void setPreferredName(String preferredName) {
            super.setPreferredName(preferredName);
        }

        public void setDescription(String description) {
            super.setDescription(description);
        }

        public void setPrefixes(Set<String> prefixes) {
            super.setPrefixes(prefixes);
        }

        public void setTriggers(Set<String> triggers) {
            super.setTriggers(triggers);
        }
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<String> arguments;
        private List<Option> options;

        public WriteableCommandLineImpl() {
            arguments = new ArrayList<>();
            options = new ArrayList<>();
        }

        @Override
        public List<String> getArguments() {
            return arguments;
        }

        @Override
        public List<Option> getOptions() {
            return options;
        }

        @Override
        public void addArgument(String argument) {
            arguments.add(argument);
        }

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void removeOption(Option option) {
            options.remove(option);
        }

        @Override
        public void setOptions(List<Option> options) {
            this.options = options;
        }
    }
}