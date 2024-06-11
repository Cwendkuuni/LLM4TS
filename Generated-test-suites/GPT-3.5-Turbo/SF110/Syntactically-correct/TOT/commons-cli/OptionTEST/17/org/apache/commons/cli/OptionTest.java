
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {
    
    @Test
    public void testConstructor_withOptAndDescription_onlyHasArg() {
        Option option = new Option("a", "test option");
        
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgs());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testConstructor_withOptAndHasArgAndDescription_hasArgAndOptionalArg() {
        Option option = new Option("b", true, "test option");
        
        assertEquals("b", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testConstructor_withOptAndLongOptAndHasArgAndDescription_hasArgAndOptionalArg() {
        Option option = new Option("c", "command", true, "test option");
        
        assertEquals("c", option.getOpt());
        assertEquals("command", option.getLongOpt());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testSetAndGetLongOpt_withValidLongOpt_longOptIsSet() {
        Option option = new Option("a", "test option");
        
        option.setLongOpt("command");
        
        assertEquals("command", option.getLongOpt());
        assertTrue(option.hasLongOpt());
    }
    
    @Test
    public void testSetAndGetLongOpt_withNullLongOpt_longOptIsNull() {
        Option option = new Option("a", "test option");
        
        option.setLongOpt(null);
        
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
    }
    
    @Test
    public void testSetAndGetRequired_withRequiredOption_requiredIsSet() {
        Option option = new Option("a", "test option");
        
        option.setRequired(true);
        
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetAndGetRequired_withNonRequiredOption_requiredIsNotSet() {
        Option option = new Option("a", "test option");
        
        option.setRequired(false);
        
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testSetAndGetArgName_withValidArgName_argNameIsSet() {
        Option option = new Option("a", "test option");
        
        option.setArgName("arg1");
        
        assertEquals("arg1", option.getArgName());
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testSetAndGetArgName_withEmptyArgName_argNameIsNull() {
        Option option = new Option("a", "test option");
        
        option.setArgName("");
        
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
    }
    
    @Test
    public void testSetAndGetArgs_withValidNumOfArgs_numOfArgsIsSet() {
        Option option = new Option("a", "test option");
        
        option.setArgs(2);
        
        assertEquals(2, option.getArgs());
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testSetAndGetArgs_withZeroNumOfArgs_numOfArgsIsZero() {
        Option option = new Option("a", "test option");
        
        option.setArgs(0);
        
        assertEquals(0, option.getArgs());
        assertFalse(option.hasArgs());
    }
    
    @Test
    public void testSetAndGetArgs_withNegativeNumOfArgs_numOfArgsIsNegative() {
        Option option = new Option("a", "test option");
        
        option.setArgs(-1);
        
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasArgs());
    }
    
    @Test
    public void testSetValueSeparator_withValidSeparator_separatorIsSet() {
        Option option = new Option("a", "test option");
        
        option.setValueSeparator(',');
        
        assertEquals(',', option.getValueSeparator());
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testSetValueSeparator_withNullSeparator_separatorIsNull() {
        Option option = new Option("a", "test option");
        
        option.setValueSeparator('\0');
        
        assertEquals('\0', option.getValueSeparator());
        assertFalse(option.hasValueSeparator());
    }
    
    @Test
    public void testAddValueForProcessing_withSingleValue_singleValueIsAdded() {
        Option option = new Option("a", "test option");
        
        option.addValueForProcessing("value1");
        
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        assertEquals(expectedValues, option.getValuesList());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_withNoArgsAllowed_exceptionIsThrown() {
        Option option = new Option("a", "test option");
        option.setArgs(Option.UNINITIALIZED);
        
        option.addValueForProcessing("value1");
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_withExceedingArgs_exceptionIsThrown() {
        Option option = new Option("a", "test option");
        option.setArgs(2);
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }
    
    @Test
    public void testGetValue_withSingleValue_returnsValue() {
        Option option = new Option("a", "test option");
        option.addValueForProcessing("value1");
        
        assertEquals("value1", option.getValue());
    }
    
    @Test
    public void testGetValue_withNoValues_returnsNull() {
        Option option = new Option("a", "test option");
        
        assertNull(option.getValue());
    }
    
    @Test
    public void testGetValues_withMultipleValues_returnsValuesArray() {
        Option option = new Option("a", "test option");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        String[] expectedValues = {"value1", "value2"};
        
        assertArrayEquals(expectedValues, option.getValues());
    }
    
    @Test
    public void testGetValues_withNoValues_returnsNull() {
        Option option = new Option("a", "test option");
        
        assertNull(option.getValues());
    }
    
    @Test
    public void testClone_returnsCopyOfOption() {
        Option option = new Option("a", "test option");
        option.addValueForProcessing("value1");
        Option clonedOption = (Option) option.clone();
        
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.isRequired(), clonedOption.isRequired());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
        assertArrayEquals(option.getValues(), clonedOption.getValues());
        assertNotSame(option, clonedOption);
    }
}
