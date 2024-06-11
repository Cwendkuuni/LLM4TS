import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Option.class})
public class OptionTest {

    private WriteableCommandLine commandLine;
    private ListIterator<String> args;

    @Before
    public void setUp() {
        commandLine = PowerMockito.mock(WriteableCommandLine.class);
        args = Collections.listIterator(new ArrayList<>());
    }

    @Test
    public void testProcessOptionWithValue() throws OptionException {
        Option option = new MockOption("option", "o", "v", "Value", "Description");
        option.process(commandLine, args);

        assertEquals("Value", commandLine.getOptionValue("o"));
        assertEquals("Value", commandLine.getOptionValue("o", true));
    }

    @Test
    public void testProcessOptionWithoutValue() throws OptionException {
        Option option = new MockOption("option", "o");
        option.process(commandLine, args);

        assertNull(commandLine.getOptionValue("o"));
        assertNull(commandLine.getOptionValue("o", true));
    }

    @Test(expected = OptionException.class)
    public void testProcessOptionWithInvalidArgument() throws OptionException {
        Option option = new MockOption("option", "o") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        option.process(commandLine, args);
    }

    @Test
    public void testProcessOptionWithLongArgument() throws OptionException {
        Option option = new MockOption("option", "l") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return arguments.hasNext() && "longArgument".equals(arguments.next());
            }
        };
        args.next(); // consume "l"
        option.process(commandLine, args);

        assertNull(commandLine.getOptionValue("l"));
        assertNull(commandLine.getOptionValue("l", true));
        assertEquals("longArgument", commandLine.getOptionValue("l", false));
    }

    @Test
    public void testCanProcessWithValidArgument() {
        Option option = new MockOption("option", "o");
        assertTrue(option.canProcess(commandLine, "value"));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        Option option = new MockOption("option", "o");
        assertFalse(option.canProcess(commandLine, "invalidArgument"));
    }

    @Test
    public void testCanProcessWithListIterator() throws OptionException {
        Option option = new MockOption("option", "l") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return arguments.hasNext() && "longArgument".equals(arguments.next());
            }
        };
        args.next(); // consume "l"

        assertTrue(option.canProcess(commandLine, args));
        assertEquals("longArgument", commandLine.getOptionValue("l", false));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithInvalidListIterator() {
        Option option = new MockOption("option", "l") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return false;
            }
        };
        option.canProcess(commandLine, args);
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption("option", "o");
        Set<String> triggers = option.getTriggers();
        assertEquals(Arrays.asList("o"), triggers);
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption("option", "o");
        Set<String> prefixes = option.getPrefixes();
        assertEquals(Collections.singleton(""), prefixes);
    }

    @Test
    public void testValidate() throws OptionException {
        Option option = new MockOption("option", "o");
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() throws OptionException {
        Option option = new MockOption("option", "o");
        PowerMockito.doThrow(new OptionException("Invalid CommandLine"))
                .when(commandLine).validateOption(option, false);
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption("option", "o");
        List<HelpLine> helpLines = option.helpLines(0, null, null);
        assertEquals(1, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        assertEquals("  -oOPTION             Description", helpLine.getShortOption());
        assertEquals("  --option=VALUE       Description", helpLine.getLongOption());
        assertEquals("", helpLine.getDescription());
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption("option", "o");
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, null, null);

        assertEquals(" OPTION\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption("option", "o");
        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption("option", "o");
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new MockOption("option", "o");
        assertEquals(0, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option1 = new MockOption("option1", "1");
        Option option2 = new MockOption("option2", "2");
        Option option3 = new MockOption("option3", "3");

        Option foundOption = option1.findOption("1");
        assertSame(option1, foundOption);

        foundOption = option2.findOption("2");
        assertSame(option2, foundOption);

        foundOption = option3.findOption("3");
        assertSame(option3, foundOption);

        foundOption = option1.findOption("2");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption("option", "o");
        assertFalse(option.isRequired());
    }

    @Test
    public void testHasParent() {
        Option parent = new MockOption("parent", "p");
        Option option = new MockOption("option", "o");
        option.setParent(parent);

        assertSame(parent, option.getParent());
    }

    private static class MockOption extends AbstractOption {
        public MockOption(String longOpt, String opt, String optArg, String optDescription, String preName) {
            super(longOpt, opt, optArg, optDescription, preName);
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            super.defaults(commandLine);
        }

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> arguments)
                throws OptionException {
            super.process(commandLine, arguments);
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return super.canProcess(commandLine, argument);
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            return super.canProcess(commandLine, arguments);
        }

        @Override
        public Set<String> getTriggers() {
            return super.getTriggers();
        }

        @Override
        public Set<String> getPrefixes() {
            return super.getPrefixes();
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            super.validate(commandLine);
        }

        @Override
        public List<HelpLine> helpLines(int depth, Set<Object> helpSettings, Comparator<Option> comp) {
            return super.helpLines(depth, helpSettings, comp);
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set<Object> helpSettings, Comparator<Option> comp) {
            super.appendUsage(buffer, helpSettings, comp);
        }

        @Override
        public String getPreferredName() {
            return super.getPreferredName();
        }

        @Override
        public String getDescription() {
            return super.getDescription();
        }

        @Override
        public int getId() {
            return super.getId();
        }

        @Override
        public Option findOption(String trigger) {
            return super.findOption(trigger);
        }

        @Override
        public boolean isRequired() {
            return super.isRequired();
        }

        @Override
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
            super.setParent(parent);
        }
    }
}