import org.junit.Test;
import org.junit.Assert;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        Assert.assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        Assert.assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("testArgName").create();
        Assert.assertEquals("testArgName", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        Assert.assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("testDescription").create();
        Assert.assertEquals("testDescription", option.getDescription());
    }
    
    @Test
    public void testCreate() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals("t", option.getOpt());
    }
    
    @Test
    public void testCreateWithOptionString() {
        Option option = OptionBuilder.create("test");
        Assert.assertEquals("test", option.getOpt());
    }
    
    @Test
    public void testWithOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgsNumber() {
        Option option = OptionBuilder.hasOptionalArgs(3).create();
        Assert.assertEquals(3, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }
}