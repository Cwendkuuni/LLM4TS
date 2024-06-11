
package org.apache.commons.cli.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
    private ArgumentImpl argument;
    
    @Before
    public void setup() {
        argument = new ArgumentImpl("testArg", "Test argument", 1, 3, ':', ',', null, "--", null, 0);
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("testArg", argument.getPreferredName());
    }
    
    @Test
    public void testGetInitialSeparator() {
        assertEquals(':', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(',', argument.getSubsequentSeparator());
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
    public void testIsRequired() {
        assertEquals(true, argument.isRequired());
    }
    
    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }
    
    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }
    
    @Test
    public void testProcessValues() {
        // Test case for valid value
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        ListIterator arguments = Arrays.asList("value").listIterator();
        Option option = new Option("testOption");
        argument.processValues(commandLine, arguments, option);
        
        List values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
        
        // Test case for consumeRemaining
        commandLine = new DefaultWriteableCommandLine();
        arguments = Arrays.asList("value1", "value2", "--").listIterator();
        option = new Option("testOption");
        argument.processValues(commandLine, arguments, option);
        
        values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        
        // Test case for subsequentSplit
        commandLine = new DefaultWriteableCommandLine();
        arguments = Arrays.asList("value1,value2").listIterator();
        option = new Option("testOption");
        argument.processValues(commandLine, arguments, option);
        
        values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        
        // Test case for looksLikeOption
        commandLine = new DefaultWriteableCommandLine();
        arguments = Arrays.asList("--value").listIterator();
        option = new Option("testOption");
        argument.processValues(commandLine, arguments, option);
        
        values = commandLine.getValues(option);
        assertEquals(0, values.size());
    }
    
    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        String arg = "value";
        assertEquals(true, argument.canProcess(commandLine, arg));
    }
    
    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }
    
    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }
    
    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("testOption");
        commandLine.addValue(option, "value");
        argument.validate(commandLine, option);
    }
    
    @Test(expected = OptionException.class)
    public void testValidateWithInvalidNumberOfValues() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("testOption");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        commandLine.addValue(option, "value4");
        argument.validate(commandLine, option);
    }
    
    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() {
        Validator validator = new Validator() {
            public void validate(List values) throws InvalidArgumentException {
                if (values.contains("invalid")) {
                    throw new InvalidArgumentException("Invalid value");
                }
            }
        };
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("testOption");
        commandLine.addValue(option, "valid");
        commandLine.addValue(option, "invalid");
        argument = new ArgumentImpl("testArg", "Test argument", 1, 3, ':', ',', validator, "--", null, 0);
        argument.validate(commandLine, option);
    }
    
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        argument.appendUsage(buffer, helpSettings, comp);
        
        assertEquals("testArg", buffer.toString());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("Test argument", argument.getDescription());
    }
    
    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = argument.helpLines(0, new HashSet<DisplaySetting>(), null);
        assertEquals(1, helpLines.size());
        
        HelpLine helpLine = helpLines.get(0);
        assertEquals(argument, helpLine.getOption());
        assertEquals(0, helpLine.getDepth());
    }
    
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("testOption");
        argument.defaults(commandLine);
        
        List values = commandLine.getValues(option);
        assertNull(values);
    }
    
    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("testOption");
        argument.defaultValues(commandLine, option);
        
        List values = commandLine.getValues(option);
        assertNull(values);
    }
}
