import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testConstructorWithDescription() {
        Option option = new Option("a", "Test description");
        Assert.assertEquals("a", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertEquals("Test description", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testConstructorWithHasArg() {
        Option option = new Option("b", true, "Test description");
        Assert.assertEquals("b", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertEquals("Test description", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("c", "long", false, "Test description");
        Assert.assertEquals("c", option.getOpt());
        Assert.assertEquals("long", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertEquals("Test description", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertTrue(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testGetId() {
        Option option = new Option("d", "Test description");
        Assert.assertEquals(100, option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("e", "Test description");
        Assert.assertEquals("e", option.getKey());
    }

    @Test
    public void testGetTypeAndSetType() {
        Option option = new Option("f", "Test description");
        Assert.assertNull(option.getType());

        option.setType("type");
        Assert.assertEquals("type", option.getType());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("g", "Test description");
        option.setLongOpt("long");
        Assert.assertEquals("long", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("h", "Test description");
        Assert.assertFalse(option.hasOptionalArg());

        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("i", "Test description");
        Assert.assertFalse(option.hasLongOpt());
        
        option.setLongOpt("long");
        Assert.assertTrue(option.hasLongOpt());
    }
    
    // ... other test cases for each method

    @Test
    public void testAddValue() {
        Option option = new Option("z", "Test description");
        Assert.assertThrows(UnsupportedOperationException.class, () -> {
            option.addValue("value");
        });
    }
}