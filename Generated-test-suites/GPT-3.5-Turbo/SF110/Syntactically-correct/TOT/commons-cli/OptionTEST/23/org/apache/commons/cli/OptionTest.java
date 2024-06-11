import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionTest {

    private Option testOption;

    @Before
    public void setUp() {
        testOption = new Option("t", "test", true, "test option");
    }

    @Test
    public void testGetOpt() {
        assertEquals("t", testOption.getOpt());
    }
    
    @Test
    public void testGetLongOpt() {
        assertEquals("test", testOption.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        assertTrue(testOption.hasArg());
    }
    
    @Test
    public void testSetArgName() {
        testOption.setArgName("arg");
        assertTrue(testOption.hasArgName());
        assertEquals("arg", testOption.getArgName());
    }
    
    @Test
    public void testSetArgs() {
        testOption.setArgs(2);
        assertTrue(testOption.hasArgs());
        assertEquals(2, testOption.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        testOption.setValueSeparator(':');
        assertTrue(testOption.hasValueSeparator());
        assertEquals(':', testOption.getValueSeparator());
    }
    
    @Test
    public void testAddValueForProcessing() {
        testOption.add("value1");
        testOption.add("value2");
        assertEquals("value1", testOption.getValue(0));
        assertEquals("value2", testOption.getValue(1));
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        Option option = new Option("o", false, "option with no args");
        option.addValueForProcessing("value");
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithFullList() {
        Option option = new Option("o", true, "option with one arg");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }
    
    @Test
    public void testGetValue() {
        testOption.addValueForProcessing("value");
        assertEquals("value", testOption.getValue());
    }
    
    @Test
    public void testGetValueWithDefault() {
        assertEquals("default", testOption.getValue("default"));
    }
    
    @Test
    public void testGetValues() {
        testOption.addValueForProcessing("value1");
        testOption.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, testOption.getValues());
    }

    @Test
    public void testGetValuesList() {
        testOption.addValueForProcessing("value1");
        testOption.addValueForProcessing("value2");
        assertEquals(Arrays.asList("value1", "value2"), testOption.getValuesList());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("o", "option1", true, "description1");
        Option option2 = new Option("o", "option2", true, "description2");
        assertEquals(option1, option2);
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("o", "option1", true, "description1");
        Option option2 = new Option("o", "option2", true, "description2");
        assertEquals(option1.hashCode(), option2.hashCode());
    }
    
    @Test
    public void testClone() {
        Option clonedOption = (Option) testOption.clone();
        assertEquals(testOption, clonedOption);
        assertNotSame(testOption, clonedOption);
    }
    
    @Test
    public void testClearValues() {
        testOption.addValueForProcessing("value");
        testOption.clearValues();
        assertNull(testOption.getValue());
        assertArrayEquals(null, testOption.getValues());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        testOption.addValue("value");
    }
}