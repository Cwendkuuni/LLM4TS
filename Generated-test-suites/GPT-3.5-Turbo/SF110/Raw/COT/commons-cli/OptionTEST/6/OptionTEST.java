import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    // Tests for public methods

    @Test
    public void testOptionConstructor1() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testOptionConstructor2() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testOptionConstructor3() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new Option("opt", "description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testGetKeyWithOpt() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getKey());
    }

    @Test
    public void testGetKeyWithLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("longOpt", option.getKey());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("opt", "description");
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("opt", "description");
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", "description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArgWithHasArgTrue() {
        Option option = new Option("opt", true, "description");
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithHasArgFalse() {
        Option option = new Option("opt", false, "description");
        assertFalse(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("opt", "description");
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("opt", "description");
        assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("opt", "description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("opt", "description");
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());
        option.setArgName("argName");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgsWithNumberOfArgs1() {
        Option option = new Option("opt", true, "description");
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testHasArgsWithNumberOfArgsMinus2() {
        Option option = new Option("opt", true, "description");
        option.setArgs(Option.UNLIMITED_VALUES);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("opt", true, "description");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("opt", true, "description");
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option = new Option("opt", true, "description");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("opt", true, "description");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testProcessValue() {
        Option option = new Option("opt", true, "description");
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithListFull() {
        Option option = new Option("opt", true, "description");
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }

    @Test
    public void testGetValueWithNoValues() {
        Option option = new Option("opt", true, "description");
        assertNull(option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        Option option = new Option("opt", true, "description");
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue(0));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("opt", true, "description");
        assertNull(option.getValue("defaultValue"));
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValuesWithNoValues() {
        Option option = new Option("opt", true, "description");
        assertNull(option.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("opt", true, "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt :: description ]", option.toString());
    }

    @Test
    public void testHasNoValuesWithNoValues() {
        Option option = new Option("opt", "description");
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testHasNoValuesWithValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
        assertFalse(option.hasNoValues());
    }

    @Test
    public void testEqualsWhenEqual() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testEqualsWhenNotEqual() {
        Option option1 = new Option("opt1", "description");
        Option option2 = new Option("opt2", "description");
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option = new Option("opt", "description");
        assertEquals(961, option.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("opt", "description");
        option1.addValueForProcessing("value");
        Option option2 = (Option) option1.clone();
        assertEquals(option1.getOpt(), option2.getOpt());
        assertEquals(option1.getDescription(), option2.getDescription());
        assertEquals(option1.getValuesList(), option2.getValuesList());
        assertNotSame(option1.getValuesList(), option2.getValuesList());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
        assertFalse(option.hasNoValues());
        option.clearValues();
        assertTrue(option.hasNoValues());
    }

    // Additional tests for edge cases and exception handling scenarios

    // TODO: Add additional test cases for edge cases and exception handling scenarios

}