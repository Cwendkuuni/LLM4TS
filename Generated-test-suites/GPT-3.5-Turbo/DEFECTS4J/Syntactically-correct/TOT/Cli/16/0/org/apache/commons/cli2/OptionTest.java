import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private WriteableCommandLine commandLine;
    private ListIterator args;
    
    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
        args = mock(ListIterator.class);
    }
    
    @Test
    public void testProcess_validArguments_Success() throws OptionException {
        Option option = new OptionImpl();
        option.process(commandLine, args);
        
        verify(commandLine, times(1)).addOption(option);
        verify(args, times(1)).next();
    }
    
    @Test(expected = OptionException.class)
    public void testProcess_emptyArguments_ExceptionThrown() throws OptionException {
        Option option = new OptionImpl();
        when(args.hasNext()).thenReturn(false);
        
        option.process(commandLine, args);
    }
    
    @Test
    public void testDefaults_noDefaultsAdded_Success() {
        Option option = new OptionImpl();
        
        option.defaults(commandLine);
        
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testCanProcess_validArgument_ReturnsTrue() {
        Option option = new OptionImpl();
        String argument = "test";
        
        boolean result = option.canProcess(commandLine, argument);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testCanProcess_invalidArgument_ReturnsFalse() {
        Option option = new OptionImpl();
        String argument = "invalid";
        
        boolean result = option.canProcess(commandLine, argument);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testCanProcess_validArgumentsIterator_ReturnsTrue() {
        Option option = new OptionImpl();
        ListIterator arguments = mock(ListIterator.class);
        
        when(arguments.next()).thenReturn("test");
        boolean result = option.canProcess(commandLine, arguments);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testCanProcess_invalidArgumentsIterator_ReturnsFalse() {
        Option option = new OptionImpl();
        ListIterator arguments = mock(ListIterator.class);
        
        when(arguments.next()).thenReturn("invalid");
        boolean result = option.canProcess(commandLine, arguments);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testGetTriggers_returnSetOfTriggers_Success() {
        Option option = new OptionImpl();
        Set expectedTriggers = new HashSet();
        expectedTriggers.add("--");
        expectedTriggers.add("-");
        
        Set result = option.getTriggers();
        
        assertEquals(expectedTriggers, result);
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testGetPrefixes_returnSetOfPrefixes_Success() {
        Option option = new OptionImpl();
        Set expectedPrefixes = new HashSet();
        expectedPrefixes.add("--");
        expectedPrefixes.add("-");
        
        Set result = option.getPrefixes();
        
        assertEquals(expectedPrefixes, result);
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testValidate_validCommandLine_Success() throws OptionException {
        Option option = new OptionImpl();
        
        option.validate(commandLine);
        
        verifyZeroInteractions(commandLine);
    }
    
    @Test(expected = OptionException.class)
    public void testValidate_invalidCommandLine_ExceptionThrown() throws OptionException {
        Option option = new OptionImpl();
        doThrow(new OptionException("Invalid Command Line")).when(commandLine).validate();
        
        option.validate(commandLine);
    }
    
    @Test
    public void testHelpLines_optionWithoutChildren_returnListOfHelpLines() {
        Option option = new OptionImpl();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        
        List result = option.helpLines(0, helpSettings, comp);
        
        assertEquals(1, result.size());
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testAppendUsage_optionWithoutChildren_AppendsUsageToBuffer() {
        Option option = new OptionImpl();
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        
        option.appendUsage(buffer, helpSettings, comp);
        
        assertEquals("usage", buffer.toString());
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testGetPreferredName_optionWithPreferredName_returnPreferredName() {
        Option option = new OptionImpl();
        
        String result = option.getPreferredName();
        
        assertEquals("test", result);
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testGetDescription_optionWithDescription_returnDescription() {
        Option option = new OptionImpl();
        
        String result = option.getDescription();
        
        assertEquals("description", result);
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testGetId_optionWithId_returnId() {
        Option option = new OptionImpl();
        
        int result = option.getId();
        
        assertEquals(1, result);
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testFindOption_optionExists_returnOption() {
        Option option = new OptionImpl();
        String trigger = "--test";
        
        Option result = option.findOption(trigger);
        
        assertEquals(option, result);
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testFindOption_optionDoesNotExist_returnNull() {
        Option option = new OptionImpl();
        String trigger = "--invalid";
        
        Option result = option.findOption(trigger);
        
        assertEquals(null, result);
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testIsRequired_optionNotRequired_returnFalse() {
        Option option = new OptionImpl();
        
        boolean result = option.isRequired();
        
        assertEquals(false, result);
        verifyZeroInteractions(commandLine);
    }
    
    @Test
    public void testGetSize_optionHasChildren_returnSize() {
        Option option = new OptionImpl();
        Option child1 = mock(Option.class);
        Option child2 = mock(Option.class);
        List<Option> children = new ArrayList();
        children.add(child1);
        children.add(child2);
        when(commandLine.size()).thenReturn(children.size());
        
        int result = option.getSize();
        
        assertEquals(children.size(), result);
        verify(commandLine, times(1)).size();
    }
    
    private class OptionImpl implements Option {

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
            commandLine.addOption(this);
            args.next();
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // no defaults added
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
            arguments.next();
            return true;
        }

        @Override
        public Set getTriggers() {
            Set triggers = new HashSet();
            triggers.add("--");
            triggers.add("-");
            return triggers;
        }

        @Override
        public Set getPrefixes() {
            Set prefixes = new HashSet();
            prefixes.add("--");
            prefixes.add("-");
            return prefixes;
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // valid command line
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            // return a list of help lines
            List<HelpLineImpl> helpLines = new ArrayList<>();
            helpLines.add(new HelpLineImpl("usage"));
            return helpLines;
        }

        @Override
        public void appendUsage(final StringBuffer buffer, final Set helpSettings, final Comparator comp) {
            buffer.append("usage");
        }

        @Override
        public String getPreferredName() {
            return "test";
        }

        @Override
        public String getDescription() {
            return "description";
        }

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public Option findOption(String trigger) {
            if ("--test".equals(trigger)) {
                return this;
            }
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

    }

    private class HelpLineImpl implements Option.HelpLine {
        
        private String line;
        
        public HelpLineImpl(String line) {
            this.line = line;
        }
        
        @Override
        public void accept(final Visitor visitor) {
            
        }

        @Override
        public int compareTo(final Option.HelpLine helpLine) {
            return 0;
        }

        @Override
        public int getMaxWeight() {
            return 0;
        }

        @Override
        public int getDepth() {
            return 0;
        }

        @Override
        public int getDisplayWeight() {
            return 0;
        }

        @Override
        public String getLine() {
            return line;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public boolean isSpecific(final DisplaySetting setting) {
            return false;
        }

    }
}