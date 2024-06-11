import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionTest {
    
    private Option option;

    @Before
    public void setUp() {
        option = new OptionImpl();
    }

    // Test process method

    @Test
    public void process_shouldAddOptionsToCommandLine() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = Arrays.asList("-f", "file.txt").listIterator();
        
        option.process(commandLine, args);
        
        assertTrue(commandLine.hasOption("-f"));
        assertEquals("file.txt", commandLine.getOptionValue("-f"));
    }

    @Test(expected = OptionException.class)
    public void process_shouldThrowExceptionIfNoArguments() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = Arrays.asList().listIterator();
        
        option.process(commandLine, args);
    }

    // Test defaults method

    @Test
    public void defaults_shouldSetDefaultValueForOption() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        
        option.defaults(commandLine);
        
        assertEquals("default", commandLine.getOptionValue("-f"));
    }

    // Test canProcess method

    @Test
    public void canProcess_shouldReturnTrueIfArgumentMatchesTrigger() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "-f";
        
        boolean result = option.canProcess(commandLine, argument);
        
        assertTrue(result);
    }

    @Test
    public void canProcess_shouldReturnFalseIfArgumentDoesNotMatchTrigger() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "-u";
        
        boolean result = option.canProcess(commandLine, argument);
        
        assertFalse(result);
    }

    // Test getTriggers method

    @Test
    public void getTriggers_shouldReturnSetOfTriggers() {
        Set<String> triggers = option.getTriggers();
        
        assertNotNull(triggers);
        assertTrue(triggers.contains("--file"));
        assertTrue(triggers.contains("-f"));
    }

    // Test getPrefixes method

    @Test
    public void getPrefixes_shouldReturnSetOfPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        
        assertNotNull(prefixes);
        assertTrue(prefixes.contains("--"));
        assertTrue(prefixes.contains("-"));
    }

    // Test validate method

    @Test
    public void validate_shouldNotThrowExceptionForValidCommandLine() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addOption("-f");
        
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void validate_shouldThrowExceptionForInvalidCommandLine() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        
        option.validate(commandLine);
    }

    // Test helpLines method

    @Test
    public void helpLines_shouldReturnListOfHelpLines() {
        int depth = 0;
        Set<HelpSetting> helpSettings = new HashSet<>();
        Comparator<HelpLine> comp = new HelpLineComparator();

        List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);
        
        assertNotNull(helpLines);
        assertEquals(1, helpLines.size());
        assertEquals("Option: -f, --file (default: default)", helpLines.get(0).toString());
    }

    // Test appendUsage method

    @Test
    public void appendUsage_shouldAppendUsageToBuffer() {
        StringBuffer buffer = new StringBuffer();
        Set<HelpSetting> helpSettings = new HashSet<>();
        Comparator<HelpLine> comp = new HelpLineComparator();
        
        option.appendUsage(buffer, helpSettings, comp);
        
        assertEquals("-f, --file", buffer.toString());
    }

    // Test getPreferredName method

    @Test
    public void getPreferredName_shouldReturnPreferredName() {
        String preferredName = option.getPreferredName();
        
        assertEquals("-f, --file", preferredName);
    }

    // Test getDescription method

    @Test
    public void getDescription_shouldReturnDescription() {
        String description = option.getDescription();
        
        assertEquals("The super type of all options representing a particular element of the command line interface.", description);
    }

    // Test getId method

    @Test
    public void getId_shouldReturnId() {
        int id = option.getId();
        
        assertEquals(1, id);
    }

    // Test findOption method

    @Test
    public void findOption_shouldReturnMatchingOption() {
        String trigger = "-f";
        
        Option matchingOption = option.findOption(trigger);
        
        assertNotNull(matchingOption);
        assertEquals("-f, --file", matchingOption.getPreferredName());
    }

    @Test
    public void findOption_shouldReturnNullForNonMatchingTrigger() {
        String trigger = "-u";
        
        Option matchingOption = option.findOption(trigger);
        
        assertNull(matchingOption);
    }

    // Test isRequired method

    @Test
    public void isRequired_shouldReturnTrueIfOptionIsRequired() {
        boolean isRequired = option.isRequired();
        
        assertTrue(isRequired);
    }

    @Test
    public void isRequired_shouldReturnFalseIfOptionIsNotRequired() {
        Option optionalOption = new OptionalOptionImpl();
        
        boolean isRequired = optionalOption.isRequired();
        
        assertFalse(isRequired);
    }
    
    // Test getParent method

    @Test
    public void getParent_shouldReturnParentOption() {
        Option parent = new OptionImpl();
        option.setParent(parent);
        
        Option parentOption = option.getParent();
        
        assertNotNull(parentOption);
        assertEquals(parent, parentOption);
    }

    // Test setParent method

    @Test
    public void setParent_shouldSetParentOption() {
        Option parent = new OptionImpl();
        
        option.setParent(parent);
        
        assertNotNull(option.getParent());
        assertEquals(parent, option.getParent());
    }
}