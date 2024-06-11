import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testConstructorWithDescription() {
        Option option = new Option("opt", "description");
        
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasLongOpt());
    }

    @Test
    public void testConstructorWithHasArg() {
        Option option = new Option("opt", true, "description");
        
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasLongOpt());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertTrue(option.hasArg());
        Assert.assertTrue(option.hasLongOpt());
    }

    // Test edge cases and exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOpt() {
        new Option(null, "description");
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithNoArgsAllowed() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithListFull() {
        Option option = new Option("opt", true, "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    // Test other methods
    @Test
    public void testToString() {
        Option option = new Option("opt", "description");
        Assert.assertEquals("[ option: opt :: description ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt1", "description2");
        Option option3 = new Option("opt2", "description1");
        
        Assert.assertEquals(option1, option2);
        Assert.assertNotEquals(option1, option3);
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt1", "description2");
        Option option3 = new Option("opt2", "description1");
        
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
        Assert.assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = (Option) option1.clone();
        
        Assert.assertEquals(option1, option2);
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", true, "description");
        option.addValueForProcessing("value");
        option.clearValues();
        
        Assert.assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }

    // 
    // Merge all the individual test cases into a complete JUnit 4 test file
    // ...
    // 
}