import org.junit.*;
import java.util.*;

public class OptionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1() {
        Option option = new Option(null, "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2() {
        Option option = new Option("opt", null, false, "description");
    }

    @Test
    public void testConstructor3() {
        Option option = new Option("opt", "description");
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testConstructor4() {
        Option option = new Option("opt", true, "description");
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testConstructor5() {
        Option option = new Option("opt", "longOpt", true, "description");
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testGetId() {
        Option option = new Option("o", "description");
        Assert.assertEquals('o', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("opt", "description");
        Assert.assertEquals("opt", option.getKey());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        Assert.assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("opt", "description");
        option.setType(new Object());
        Assert.assertNotNull(option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("opt", "description");
        option.setLongOpt("longOpt");
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", "description");
        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt", "description");
        Assert.assertFalse(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("opt", "description");
        Assert.assertFalse(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("opt", "description");
        Assert.assertEquals("description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("opt", "description");
        option.setDescription("new description");
        Assert.assertEquals("new description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("opt", "description");
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("opt", "description");
        option.setRequired(true);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("opt", "description");
        option.setArgName("argName");
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("opt", "description");
        Assert.assertFalse(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("opt", "description");
        Assert.assertFalse(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        Assert.assertTrue(option.hasArgs());
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("opt", "description");
        option.setValueSeparator(',');
        Assert.assertTrue(option.hasValueSeparator());
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option = new Option("opt", "description");
        Assert.assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "description");
        Assert.assertEquals("[ option: opt ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Assert.assertTrue(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option = new Option("opt", "description");
        Assert.assertNotNull(option.hashCode());
    }

    @Test
    public void testClone() {
        Option option = new Option("opt", "description");
        Option clonedOption = (Option) option.clone();
        Assert.assertNotSame(option, clonedOption);
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", "description");
        option.addValue("value");
        option.clearValues();
        Assert.assertTrue(option.hasNoValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}