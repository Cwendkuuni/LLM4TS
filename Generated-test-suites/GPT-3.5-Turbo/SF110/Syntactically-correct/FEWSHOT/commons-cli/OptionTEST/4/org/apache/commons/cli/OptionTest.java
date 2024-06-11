import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {
    private Option option;

    @Before
    public void setup() {
        option = new Option("opt", "desc");
    }

    @Test
    public void testConstructor() {
        assertEquals("opt", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(false, option.hasLongOpt());
        assertEquals(false, option.hasArg());
        assertEquals("desc", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals("arg", option.getArgName());
        assertEquals(false, option.hasArgName());
        assertEquals(false, option.hasArgs());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(true, option.hasLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasArg() {
        assertEquals(false, option.hasArg());
        option.setArgs(1);
        assertEquals(true, option.hasArg());
        option.setArgs(Option.UNLIMITED_VALUES);
        assertEquals(true, option.hasArg());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("newDesc");
        assertEquals("newDesc", option.getDescription());
    }

    @Test
    public void testSetRequired() {
        option.setRequired(true);
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testSetArgName() {
        option.setArgName("newArgName");
        assertEquals("newArgName", option.getArgName());
        assertEquals(true, option.hasArgName());
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertEquals(true, option.hasArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator('/');
        assertEquals('/', option.getValueSeparator());
        assertEquals(true, option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testAddValueForProcessing() {
        option.setArgs(3);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_noArgsAllowed() {
        option.setArgs(Option.UNINITIALIZED);
        option.addValueForProcessing("value");
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_listFull() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testGetValue_withDefaultValue() {
        assertEquals("defaultValue", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] expected = {"value1", "value2"};
        assertEquals(expected, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertEquals(new ArrayList(), option.getValuesList());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        assertEquals(expected, option.getValuesList());
    }

    @Test
    public void testToString() {
        String expected = "[ option: opt :: desc ]";
        assertEquals(expected, option.toString());
        option.setArgs(Option.UNLIMITED_VALUES);
        expected = "[ option: opt [ARG...] :: desc ]";
        assertEquals(expected, option.toString());
        option.setArgs(Option.UNINITIALIZED);
        expected = "[ option: opt [ARG] :: desc ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        Option option2 = new Option("opt", "desc");
        assertEquals(true, option.equals(option2));
        option2.setLongOpt("longOpt");
        assertEquals(false, option.equals(option2));
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.isRequired(), clonedOption.isRequired());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.hasArgName(), clonedOption.hasArgName());
        assertEquals(option.hasArgs(), clonedOption.hasArgs());
    }

    @Test
    public void testClearValues() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        assertEquals(new ArrayList(), option.getValuesList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}