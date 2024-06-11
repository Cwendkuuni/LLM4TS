import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ArgumentImplTest {
    
    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;
    
    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        commandLine = new WriteableCommandLineImpl();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaximumArgument() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 2, 1, '\0', '\0', null, "--", null, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 2, '\0', '\0', null, "--", defaultValues, 0);
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }
    
    @Test
    public void testGetInitialSeparator() {
        assertEquals('\0', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('\0', argument.getSubsequentSeparator());
    }
    
    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }
    
    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }
    
    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }
    
    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "value"));
    }
    
    @Test
    public void testGetPrefixes() {
        assertTrue(argument.getPrefixes().isEmpty());
    }
    
    @Test
    public void testGetTriggers() {
        assertTrue(argument.getTriggers().isEmpty());
    }
    
    @Test
    public void testProcessValues() throws OptionException {
        ListIterator args = new ArrayList().listIterator();
        args.add("value");
        argument.processValues(commandLine, args, argument);
        assertEquals("value", commandLine.getValue(argument));
    }
    
    @Test
    public void testProcessValuesWithMultipleValues() throws OptionException {
        ListIterator args = new ArrayList().listIterator();
        args.add("value1,value2");
        argument.processValues(commandLine, args, argument);
        assertEquals("value1,value2", commandLine.getValue(argument));
    }
    
    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidValue() throws OptionException {
        ListIterator args = new ArrayList().listIterator();
        args.add("@value");
        argument.processValues(commandLine, args, argument);
    }
    
    @Test
    public void testProcessValuesWithConsumeRemaining() throws OptionException {
        ListIterator args = new ArrayList().listIterator();
        args.add("--");
        args.add("value1");
        args.add("value2");
        argument.processValues(commandLine, args, argument);
        assertEquals("value1,value2", commandLine.getValue(argument));
    }
    
    @Test
    public void testProcessValuesWithConsumeRemainingInBetween() throws OptionException {
        ListIterator args = new ArrayList().listIterator();
        args.add("value1");
        args.add("--");
        args.add("value2");
        args.add("value3");
        argument.processValues(commandLine, args, argument);
        assertEquals("value1", commandLine.getValue(argument));
    }
    
    @Test
    public void testValidate() throws OptionException {
        List<String> values = new ArrayList<>();
        values.add("value");
        commandLine.setValues(argument, values);
        argument.validate(commandLine, argument);
    }
    
    @Test(expected = OptionException.class)
    public void testValidateWithMissingValues() throws OptionException {
        argument.validate(commandLine);
    }
    
    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() throws OptionException {
        List<String> values = new ArrayList<>();
        values.add("@value");
        commandLine.setValues(argument, values);
        argument.validate(commandLine, argument);
    }
    
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        assertEquals("arg", buffer.toString());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }
    
    @Test
    public void testHelpLines() {
        ResourceHelper resourceHelper = ResourceHelper.getResourceHelper();
        HelpLine helpLine = new HelpLineImpl(argument, 0);
        assertEquals(helpLine, argument.helpLines(0, null, null).get(0));
    }
    
    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }
    
    @Test
    public void testGetMinimum() {
        assertEquals(0, argument.getMinimum());
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(argument.isRequired());
    }
    
    @Test
    public void testDefaults() {
        argument.defaults(commandLine);
        assertEquals(0, commandLine.getValues(argument).size());
    }
    
    @Test
    public void testDefaultValues() {
        argument.defaultValues(commandLine, argument);
        assertEquals(0, commandLine.getValues(argument).size());
    }
}