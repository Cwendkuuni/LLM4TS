import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class OptionTest {
    
    private Option option;

    @Before
    public void setUp() {
        option = new Option("-o", true, "option for testing");
    }

    @Test
    public void testGetOpt() {
        Assert.assertEquals("-o", option.getOpt());
    }

    @Test
    public void testGetType() {
        Assert.assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        option.setType(String.class);
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Assert.assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("--option");
        Assert.assertEquals("--option", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        option.setLongOpt("--option");
        Assert.assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("option for testing", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("new description");
        Assert.assertEquals("new description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        option.setRequired(true);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        option.setArgName("argument");
        Assert.assertEquals("argument", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        option.setArgName("argument");
        Assert.assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Assert.assertFalse(option.hasArgs());
        option.setArgs(3);
        Assert.assertTrue(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        option.setValueSeparator(',');
        Assert.assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Assert.assertEquals(-1, option.getArgs());
        option.setArgs(2);
        Assert.assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing() {
        option.addValueForProcessing("value");
    }

    @Test
    public void testGetValue() {
        Assert.assertNull(option.getValue());
        option.addValueForProcessing("value");
        Assert.assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueAtIndex() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        Assert.assertEquals("default", option.getValue("default"));
        option.addValueForProcessing("value");
        Assert.assertEquals("value", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        Assert.assertNull(option.getValues());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] values = option.getValues();
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);
    }

    @Test
    public void testGetValuesList() {
        Assert.assertEquals(new ArrayList<String>(), option.getValuesList());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List<String> valuesList = option.getValuesList();
        Assert.assertEquals("value1", valuesList.get(0));
        Assert.assertEquals("value2", valuesList.get(1));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[ option: -o [ARG] :: option for testing ]", option.toString());
        option.setLongOpt("--option");
        option.setArgs(3);
        option.setDescription("extended description");
        option.setType(Integer.class);
        Assert.assertEquals("[ option: -o --option [ARG...] :: extended description :: class java.lang.Integer ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("-o", true, "option for testing");
        Option option2 = new Option("-o", true, "option for testing");
        Assert.assertTrue(option1.equals(option2));
        option2.setLongOpt("--option");
        Assert.assertFalse(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("-o", true, "option for testing");
        Option option2 = new Option("-o", true, "option for testing");
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
        option2.setLongOpt("--option");
        Assert.assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option optionClone = (Option) option.clone();
        Assert.assertNotSame(option, optionClone);
        Assert.assertEquals(option.getOpt(), optionClone.getOpt());
        Assert.assertEquals(option.getType(), optionClone.getType());
        Assert.assertEquals(option.getLongOpt(), optionClone.getLongOpt());
        Assert.assertEquals(option.hasOptionalArg(), optionClone.hasOptionalArg());
        Assert.assertEquals(option.hasLongOpt(), optionClone.hasLongOpt());
        Assert.assertEquals(option.hasArg(), optionClone.hasArg());
        Assert.assertEquals(option.getDescription(), optionClone.getDescription());
        Assert.assertEquals(option.isRequired(), optionClone.isRequired());
        Assert.assertEquals(option.getArgName(), optionClone.getArgName());
        Assert.assertEquals(option.hasArgName(), optionClone.hasArgName());
        Assert.assertEquals(option.hasArgs(), optionClone.hasArgs());
        Assert.assertEquals(option.getArgs(), optionClone.getArgs());
        Assert.assertEquals(option.getValueSeparator(), optionClone.getValueSeparator());
        Assert.assertEquals(option.hasValueSeparator(), optionClone.hasValueSeparator());
        Assert.assertEquals(option.getValuesList(), optionClone.getValuesList());
        Assert.assertEquals(option.toString(), optionClone.toString());
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        option.addValueForProcessing("value");
        option.clearValues();
        Assert.assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}