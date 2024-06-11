import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Assert.assertEquals("testLongOpt", optionBuilder.longopt);
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Assert.assertEquals(1, optionBuilder.numberOfArgs);
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Assert.assertEquals(1, optionBuilder.numberOfArgs);
        
        optionBuilder = OptionBuilder.hasArg(false);
        Assert.assertEquals(-1, optionBuilder.numberOfArgs);
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        Assert.assertEquals("testArgName", optionBuilder.argName);
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Assert.assertEquals(true, optionBuilder.required);
    }
    
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', optionBuilder.valuesep);
    }
    
    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', optionBuilder.valuesep);
    }
    
    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        Assert.assertEquals(true, optionBuilder.required);
        
        optionBuilder = OptionBuilder.isRequired(false);
        Assert.assertEquals(false, optionBuilder.required);
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Assert.assertEquals(Integer.MAX_VALUE, optionBuilder.numberOfArgs);
    }
    
    @Test
    public void testHasArgsNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        Assert.assertEquals(5, optionBuilder.numberOfArgs);
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Assert.assertEquals(1, optionBuilder.numberOfArgs);
        Assert.assertEquals(true, optionBuilder.optionalArg);
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(Integer.MAX_VALUE, optionBuilder.numberOfArgs);
        Assert.assertEquals(true, optionBuilder.optionalArg);
    }
    
    @Test
    public void testHasOptionalArgsNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(3, optionBuilder.numberOfArgs);
        Assert.assertEquals(true, optionBuilder.optionalArg);
    }
    
    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Assert.assertEquals(Integer.class, optionBuilder.type);
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        Assert.assertEquals("testDescription", optionBuilder.description);
    }
    
    @Test
    public void testCreateChar() {
        try {
            Option option = OptionBuilder.create('t');
            Assert.assertEquals("t", option.getOpt());
        } catch (IllegalArgumentException e) {
            Assert.fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testCreate() {
        try {
            Option option = OptionBuilder.create();
            Assert.assertNotNull(option);
            Assert.assertNull(option.getOpt());
        } catch (IllegalArgumentException e) {
            Assert.fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testCreateString() {
        try {
            Option option = OptionBuilder.create("testOption");
            Assert.assertEquals("testOption", option.getOpt());
        } catch (IllegalArgumentException e) {
            Assert.fail("Exception should not be thrown");
        }
    }
}