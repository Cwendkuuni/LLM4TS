import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOpt");
        Assert.assertEquals("testOpt", OptionBuilder.create().getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgWithParam() {
        OptionBuilder.hasArg(true);
        Assert.assertEquals(1, OptionBuilder.create().getArgs());
        
        OptionBuilder.hasArg(false);
        Assert.assertEquals(-1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        Assert.assertEquals("testArg", OptionBuilder.create().getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testIsRequiredWithParam() {
        OptionBuilder.isRequired(true);
        Assert.assertTrue(OptionBuilder.create().isRequired());
        
        OptionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Assert.assertEquals(Integer.MAX_VALUE, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgsWithParam() {
        OptionBuilder.hasArgs(5);
        Assert.assertEquals(5, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Assert.assertEquals(1, OptionBuilder.create().getArgs());
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(Integer.MAX_VALUE, OptionBuilder.create().getArgs());
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithParam() {
        OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(3, OptionBuilder.create().getArgs());
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Assert.assertEquals(String.class, OptionBuilder.create().getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Assert.assertEquals("testDescription", OptionBuilder.create().getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals("t", option.getOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("testOpt");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testOpt", option.getLongOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals((char) 0, option.getValueSeparator());
    }
    
    @Test
    public void testCreateWithLongOptAndArgs() {
        OptionBuilder.withLongOpt("testOpt");
        OptionBuilder.hasArgs();
        OptionBuilder.withArgName("testArg");
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        Assert.assertEquals("testOpt", option.getLongOpt());
        Assert.assertEquals("testArg", option.getArgName());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testCreateWithLongOptAndOptionalArg() {
        OptionBuilder.withLongOpt("testOpt");
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        Assert.assertEquals("testOpt", option.getLongOpt());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals((char) 0, option.getValueSeparator());
    }
}