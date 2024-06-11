import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {
    
    private ArgumentImpl argumentImpl;
    
    @Before
    public void setUp() {
        argumentImpl = new ArgumentImpl("testArg", "This is a test argument", 1, 3, ' ', ',', null, "--", null, 0);
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("testArg", argumentImpl.getPreferredName());
    }
    
    @Test
    public void testProcessValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1", "value2", "value3").listIterator();
        Option option = new OptionImpl(0, false);
        
        argumentImpl.processValues(commandLine, arguments, option);
        
        List<String> values = commandLine.getValues(option);
        
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }
    
    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        assertTrue(argumentImpl.canProcess(commandLine, "test"));
    }
    
    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argumentImpl.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(',', argumentImpl.getSubsequentSeparator());
    }
    
    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argumentImpl.getTriggers());
    }
    
    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argumentImpl.getConsumeRemaining());
    }
    
    @Test
    public void testGetDefaultValues() {
        assertNull(argumentImpl.getDefaultValues());
    }
    
    @Test
    public void testGetValidator() {
        assertNull(argumentImpl.getValidator());
    }
    
    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        commandLine.addValue(new OptionImpl(0, false), "value1");
        commandLine.addValue(new OptionImpl(0, false), "value2");
        commandLine.addValue(new OptionImpl(0, false), "value3");
        
        argumentImpl.validate(commandLine);
    }
    
    @Test(expected = OptionException.class)
    public void testValidateMissingValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        argumentImpl.validate(commandLine);
    }
    
    @Test(expected = OptionException.class)
    public void testValidateUnexpectedValue() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        commandLine.addValue(new OptionImpl(0, false), "value1");
        commandLine.addValue(new OptionImpl(0, false), "value2");
        commandLine.addValue(new OptionImpl(0, false), "value3");
        commandLine.addValue(new OptionImpl(0, false), "value4");
        
        argumentImpl.validate(commandLine);
    }
    
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<DisplaySetting>();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        Comparator comp = null;
        
        argumentImpl.appendUsage(buffer, helpSettings, comp);
        
        assertEquals("testArg", buffer.toString());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("This is a test argument", argumentImpl.getDescription());
    }
    
    @Test
    public void testHelpLines() {
        int depth = 0;
        Set helpSettings = new HashSet<DisplaySetting>();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        Comparator comp = null;
        
        List helpLines = argumentImpl.helpLines(depth, helpSettings, comp);
        
        assertEquals(1, helpLines.size());
        assertTrue(helpLines.get(0) instanceof HelpLineImpl);
    }
    
    @Test
    public void testGetMaximum() {
        assertEquals(3, argumentImpl.getMaximum());
    }
    
    @Test
    public void testGetMinimum() {
        assertEquals(1, argumentImpl.getMinimum());
    }
    
    @Test
    public void testStripBoundaryQuotes() {
        String token = "\"test\"";
        String result = argumentImpl.stripBoundaryQuotes(token);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        
        argumentImpl.defaultValues(commandLine, option);
        
        assertNull(commandLine.getDefaultValues(option));
    }
    
}