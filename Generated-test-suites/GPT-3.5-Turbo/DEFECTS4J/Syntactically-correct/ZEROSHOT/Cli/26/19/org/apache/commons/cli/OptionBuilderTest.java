import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Assert.assertEquals("test", OptionBuilder.getLongopt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        Assert.assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        Assert.assertEquals("test", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', OptionBuilder.getValuesep());
    }
    
    @Test
    public void testWithDefaultValueSeparator() {
        OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.getValuesep());
    }
    
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        Assert.assertTrue(OptionBuilder.isRequired());
        
        OptionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Assert.assertEquals(-2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(2);
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(-2, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(3, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Assert.assertEquals(Integer.class, OptionBuilder.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        Assert.assertEquals("test", OptionBuilder.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals("t", option.getOpt());
        Assert.assertEquals(null, option.getDescription());
        Assert.assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        Assert.assertEquals(null, option.getOpt());
        Assert.assertEquals("test", option.getLongOpt());
        Assert.assertEquals(null, option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertEquals(null, option.getType());
        Assert.assertEquals((char) 0, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("t");
        Assert.assertEquals("t", option.getOpt());
        Assert.assertEquals(null, option.getDescription());
        Assert.assertEquals("arg", option.getArgName());
    }

}