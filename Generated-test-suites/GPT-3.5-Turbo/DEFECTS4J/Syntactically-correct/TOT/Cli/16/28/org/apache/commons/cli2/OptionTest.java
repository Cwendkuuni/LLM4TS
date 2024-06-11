import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

public class OptionTest {
    
    private Option option;
    
    @Before
    public void setUp() {
        option = new OptionImpl();
    }
    
    @Test
    public void testProcess_TypicalCase() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = Arrays.asList("-f", "file.txt").listIterator();
        try {
            option.process(commandLine, args);
            assertTrue(commandLine.hasOption("-f"));
            assertEquals("file.txt", commandLine.getValue("-f"));
        } catch (OptionException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testProcess_NoArgs() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = Collections.emptyList().listIterator();
        try {
            option.process(commandLine, args);
            assertFalse(commandLine.hasOption("-f"));
        } catch (OptionException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test(expected = OptionException.class)
    public void testProcess_InvalidArgs() throws OptionException {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = Arrays.asList("--").listIterator();
        option.process(commandLine, args);
    }
    
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        option.defaults(commandLine);
        
        assertFalse(commandLine.hasOption("-f"));
    }
    
    @Test
    public void testCanProcess_ValidArgument() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "-f";
        
        assertTrue(option.canProcess(commandLine, argument));
    }
    
    @Test
    public void testCanProcess_InvalidArgument() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "--";
        
        assertFalse(option.canProcess(commandLine, argument));
    }
    
    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        
        assertNotNull(triggers);
        assertTrue(triggers.contains("-f"));
    }
    
    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        
        assertNotNull(prefixes);
        assertTrue(prefixes.contains("--"));
    }
    
    @Test
    public void testValidate_ValidCommandLine() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        
        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test(expected = OptionException.class)
    public void testValidate_InvalidCommandLine() throws OptionException {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addOption("-f", "file.txt");
        commandLine.addOption("-f", "file2.txt");
        commandLine.addOption("-f", "file3.txt");
        
        option.validate(commandLine);
    }
    
    @Test
    public void testHelpLines() {
        int depth = 0;
        Set<HelpSetting> helpSettings = Collections.emptySet();
        Comparator comp = null;
        
        List<HelpLineImpl> helpLines = option.helpLines(depth, helpSettings, comp);
        
        assertNotNull(helpLines);
        assertTrue(helpLines.isEmpty());
    }
    
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<HelpSetting> helpSettings = Collections.emptySet();
        Comparator comp = null;
        
        option.appendUsage(buffer, helpSettings, comp);
        
        assertNotNull(buffer);
        assertEquals("", buffer.toString());
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("-f", option.getPreferredName());
    }
    
    @Test
    public void testGetDescription() {
        String description = option.getDescription();
        
        assertNotNull(description);
        assertTrue(description.contains("option"));
    }
    
    @Test
    public void testFindOption_ExistingOption() {
        String trigger = "-f";
        Option foundOption = option.findOption(trigger);
        
        assertNotNull(foundOption);
        assertEquals(option, foundOption);
    }
    
    @Test
    public void testFindOption_NonExistingOption() {
        String trigger = "-x";
        Option foundOption = option.findOption(trigger);
        
        assertNull(foundOption);
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testGetParent_NoParent() {
        Option parentOption = option.getParent();
        
        assertNull(parentOption);
    }
    
    @Test
    public void testSetParent() {
        Option parentOption = new OptionImpl();
        
        option.setParent(parentOption);
        
        assertEquals(parentOption, option.getParent());
    }
}

class OptionImpl implements Option {
    
    private String preferredName;
    private String description;
    
    public OptionImpl() {
        preferredName = "-f";
        description = "This is an option";
    }
    
    @Override
    public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
        String argument = (String)args.next();
        commandLine.addOption(preferredName, argument);
    }
    
    @Override
    public void defaults(WriteableCommandLine commandLine) {
    }
    
    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return argument.equals(preferredName);
    }
    
    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
        if (arguments.hasNext()) {
            String argument = (String)arguments.next();
            return canProcess(commandLine, argument);
        }
        return false;
    }
    
    @Override
    public Set getTriggers() {
        return Collections.singleton(preferredName);
    }
    
    @Override
    public Set getPrefixes() {
        return Collections.singleton("--");
    }
    
    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        if (commandLine.getValues(preferredName).size() > 1) {
            throw new OptionException("Duplicate option: " + preferredName);
        }
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
        return preferredName;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public int getId() {
        return 0;
    }
    
    @Override
    public Option findOption(String trigger) {
        if (trigger.equals(preferredName)) {
            return this;
        }
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
    public void setParent(Option parent) {
    }
}