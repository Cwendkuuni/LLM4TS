
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    
    Option option;
    
    @Before
    public void setUp() {
        option = new OptionImpl();
    }
    
    @Test
    public void process_Test() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = Arrays.asList("-a", "value").listIterator();
        
        option.process(commandLine, args);
        
        assertTrue(commandLine.hasOption("-a"));
        assertEquals("value", commandLine.getOptionValue("-a"));
        assertFalse(args.hasNext());
    }
    
    @Test
    public void defaults_Test() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        option.defaults(commandLine);
        
        assertFalse(commandLine.hasOption("-a"));
    }
    
    @Test
    public void canProcess_String_Test() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        assertTrue(option.canProcess(commandLine, "-a"));
        assertFalse(option.canProcess(commandLine, "value"));
    }
    
    @Test
    public void canProcess_ListIterator_Test() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = Arrays.asList("-a", "value").listIterator();
        
        assertTrue(option.canProcess(commandLine, args));
        assertTrue(args.hasNext());
        
        args.next();
        
        assertFalse(option.canProcess(commandLine, args));
        assertFalse(args.hasNext());
    }
    
    @Test
    public void getTriggers_Test() {
        Set<String> triggers = option.getTriggers();
        
        assertTrue(triggers.contains("-a"));
        assertFalse(triggers.contains("-b"));
    }
    
    @Test
    public void getPrefixes_Test() {
        Set<String> prefixes = option.getPrefixes();
        
        assertTrue(prefixes.contains("-"));
        assertFalse(prefixes.contains("+"));
    }
    
    @Test
    public void validate_Test() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        option.validate(commandLine);
    }
    
    @Test
    public void helpLines_Test() {
        Set<HelpSetting> helpSettings = new HashSet<>();
        Comparator<HelpLine> comp = new HelpLineComparator();
        
        List<HelpLine> helpLines = option.helpLines(0, helpSettings, comp);
        
        assertNotNull(helpLines);
    }
    
    @Test
    public void appendUsage_Test() {
        StringBuffer buffer = new StringBuffer();
        Set<HelpSetting> helpSettings = new HashSet<>();
        Comparator<Option> comp = new OptionComparator();
        
        option.appendUsage(buffer, helpSettings, comp);
        
        assertNotNull(buffer.toString());
    }
    
    @Test
    public void getPreferredName_Test() {
        String preferredName = option.getPreferredName();
        
        assertNotNull(preferredName);
    }
    
    @Test
    public void getDescription_Test() {
        String description = option.getDescription();
        
        assertNotNull(description);
    }
    
    @Test
    public void getId_Test() {
        int id = option.getId();
        
        assertNotEquals(0, id);
    }
    
    @Test
    public void findOption_Test() {
        Option foundOption = option.findOption("-a");
        
        assertNotNull(foundOption);
    }
    
    @Test
    public void isRequired_Test() {
        boolean isRequired = option.isRequired();
        
        assertFalse(isRequired);
    }
    
    @Test
    public void getParent_Test() {
        Option parent = option.getParent();
        
        assertNull(parent);
    }
    
    @Test
    public void setParent_Test() {
        Option parent = new OptionImpl();
        option.setParent(parent);
        
        Option updatedParent = option.getParent();
        
        assertEquals(parent, updatedParent);
    }
}
