import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Assert.assertEquals("testLongOpt", OptionBuilder.getOptionLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.getOptionNumberOfArgs());
    }
    
    @Test
    public void testHasArg_boolean() {
        OptionBuilder.hasArg(true);
        Assert.assertEquals(1, OptionBuilder.getOptionNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        Assert.assertEquals(-1, OptionBuilder.getOptionNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        Assert.assertEquals("testArgName", OptionBuilder.getOptionArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.getOptionRequired());
    }
    
    @Test
    public void testWithValueSeparator_char() {
        OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', OptionBuilder.getOptionValueSeparator());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.getOptionValueSeparator());
    }
    
    @Test
    public void testIsRequired_boolean() {
        OptionBuilder.isRequired(true);
        Assert.assertTrue(OptionBuilder.getOptionRequired());
        
        OptionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.getOptionRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Assert.assertEquals(-2, OptionBuilder.getOptionNumberOfArgs());
    }
    
    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(3);
        Assert.assertEquals(3, OptionBuilder.getOptionNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Assert.assertTrue(OptionBuilder.getOptionOptionalArg());
        Assert.assertEquals(1, OptionBuilder.getOptionNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Assert.assertTrue(OptionBuilder.getOptionOptionalArg());
        Assert.assertEquals(-2, OptionBuilder.getOptionNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(4);
        Assert.assertTrue(OptionBuilder.getOptionOptionalArg());
        Assert.assertEquals(4, OptionBuilder.getOptionNumberOfArgs());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Assert.assertEquals(String.class, OptionBuilder.getOptionType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Assert.assertEquals("testDescription", OptionBuilder.getOptionDescription());
    }
    
    @Test
    public void testCreate_char() {
        Option option = OptionBuilder.create('t');
        Assert.assertNotNull(option);
        Assert.assertEquals("t", option.getOpt());
    }
    
    @Test
    public void testCreate() {
        // Reset invalid input.
        try {
            OptionBuilder.create();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("must specify longopt", e.getMessage());
        }
        
        // Reset valid input.
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        Assert.assertNotNull(option);
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testCreate_String() {
        Option option = OptionBuilder.create("t");
        Assert.assertNotNull(option);
        Assert.assertEquals("t", option.getOpt());
    }
}