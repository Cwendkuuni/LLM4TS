import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("f", "file", true, "Specify a file");
    }

    @Test
    public void testGetOpt() {
        Assert.assertEquals("f", option.getOpt());
    }

    @Test
    public void testGetLongOpt() {
        Assert.assertEquals("file", option.getLongOpt());
    }

    @Test
    public void testHasLongOpt() {
        Assert.assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testGetArgName() {
        Assert.assertEquals("arg", option.getArgName());
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
    public void testSetRequired() {
        option.setRequired(true);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(';');
        Assert.assertTrue(option.hasValueSeparator());
        Assert.assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testAddValueForProcessing() {
        option.addValueForProcessing("value1");
        Assert.assertEquals("value1", option.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_NoArgsAllowed() {
        Option noArgsOption = new Option("x", "noArgsOption");
        noArgsOption.addValueForProcessing("value");
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_ListFull() {
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }

    @Test
    public void testGetValue() {
        option.addValueForProcessing("value1");
        Assert.assertEquals("value1", option.getValue());
    }

    @Test
    public void testGetValue_Index() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValue_DefaultValue() {
        Assert.assertNull(option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] expectedValues = {"value1", "value2"};
        Assert.assertArrayEquals(expectedValues, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals(option.getValuesList().size(), 2);
    }

    @Test
    public void testEquals() {
        Option option2 = new Option("f", "file2", true, "Specify another file");
        Assert.assertEquals(option, option2);
    }

    @Test
    public void testHashCode() {
        Option option2 = new Option("f", "file2", true, "Specify another file");
        Assert.assertEquals(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        Assert.assertEquals(option, clonedOption);
        Assert.assertNotSame(option, clonedOption);
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value1");
        option.clearValues();
        Assert.assertEquals(option.getValuesList().size(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}