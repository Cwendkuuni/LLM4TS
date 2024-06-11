import org.junit.*;

public class OptionTest {
    Option option;

    @Before
    public void setUp() {
        option = new Option("opt", "description");
    }

    @Test
    public void testGetOpt() {
        Assert.assertEquals("opt", option.getOpt());
    }

    @Test
    public void testGetType() {
        option.setType("type");
        Assert.assertEquals("type", option.getType());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("longOpt");
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Assert.assertFalse(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Assert.assertFalse(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("description", option.getDescription());
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
        option.setArgName("argName");
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Assert.assertFalse(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Assert.assertFalse(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        Assert.assertTrue(option.hasArgs());
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        Assert.assertTrue(option.hasValueSeparator());
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Assert.assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Assert.assertEquals(-1, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        option.addValueForProcessing("value");
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithFullList() {
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }

    @Test
    public void testAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals(2, option.getValues().length);
        Assert.assertEquals("value1", option.getValues()[0]);
        Assert.assertEquals("value2", option.getValues()[1]);
    }

    @Test
    public void testGetValue() {
        option.addValueForProcessing("value");
        Assert.assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        option.addValueForProcessing("value");
        Assert.assertEquals("value", option.getValue("default"));
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testGetValues() {
        option.setArgs(3);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
        Assert.assertEquals(3, option.getValues().length);
        Assert.assertEquals("value1", option.getValues()[0]);
        Assert.assertEquals("value2", option.getValues()[1]);
        Assert.assertEquals("value3", option.getValues()[2]);
    }

    @Test
    public void testGetValuesList() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals(2, option.getValuesList().size());
        Assert.assertEquals("value1", option.getValuesList().get(0));
        Assert.assertEquals("value2", option.getValuesList().get(1));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[ option: opt :: description ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option2 = new Option("opt", "description");
        Assert.assertTrue(option.equals(option2));
    }

    @Test
    public void testEqualsWithNullOpt() {
        option.setLongOpt("longOpt");
        Option option2 = new Option(null, "longOpt", false, "description");
        Assert.assertTrue(option.equals(option2));
    }

    @Test
    public void testEqualsWithNullLongOpt() {
        option.setLongOpt(null);
        Option option2 = new Option("opt", null, false, "description");
        Assert.assertTrue(option.equals(option2));
    }

    @Test
    public void testEqualsWithDifferentOpt() {
        Option option2 = new Option("different", "description");
        Assert.assertFalse(option.equals(option2));
    }

    @Test
    public void testEqualsWithDifferentLongOpt() {
        option.setLongOpt("different");
        Option option2 = new Option("opt", "description");
        Assert.assertFalse(option.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option2 = new Option("opt", "description");
        Assert.assertEquals(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        option.addValueForProcessing("value");
        Option clonedOption = (Option) option.clone();
        Assert.assertNotSame(option, clonedOption);
        Assert.assertEquals(option.toString(), clonedOption.toString());
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        option.clearValues();
        Assert.assertTrue(option.hasNoValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }

    @Test
    public void testHasNoValues() {
        Assert.assertTrue(option.hasNoValues());
    }
}