import static org.junit.Assert.*;
import org.junit.Test;

public class ArgumentImplTest {
  
    private ArgumentImpl argument;
  
    @Before
    public void setUp() {
        argument = new ArgumentImpl("testArg", "Test argument", 1, 3, '-', ':', null, "--", null, 0);
    }
  
    @Test
    public void testGetPreferredName() {
        assertEquals("testArg", argument.getPreferredName());
    }
  
    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator arguments = Arrays.asList("value1", "value2", "value3").listIterator();
        argument.processValues(commandLine, arguments, argument);
        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }
  
    @Test(expected = OptionException.class)
    public void testProcessValues_MaxExceeded() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator arguments = Arrays.asList("value1", "value2", "value3", "value4").listIterator();
        argument.processValues(commandLine, arguments, argument);
    }
  
    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }
  
    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }
  
    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }
  
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, new HashSet<>(), null);
        assertEquals("<testArg1> <testArg2> <testArg3>", buffer.toString());
    }
  
    @Test
    public void testGetDescription() {
        assertEquals("Test argument", argument.getDescription());
    }
  
    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        argument.defaultValues(commandLine, argument);
        List<String> values = commandLine.getValues(argument);
        assertEquals(0, values.size());
    }
}