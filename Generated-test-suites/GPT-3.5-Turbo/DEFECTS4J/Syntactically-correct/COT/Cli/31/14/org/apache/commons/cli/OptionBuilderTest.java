import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
    
    // 
    
    @Test
    public void testWithLongOpt() {
        String longopt = "testLongOpt";
        OptionBuilder.withLongOpt(longopt);
        Assert.assertEquals(longopt, OptionBuilder.create().getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        boolean hasArg = true;
        OptionBuilder.hasArg(hasArg);
        Assert.assertEquals(hasArg ? 1 : -1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testWithArgName() {
        String argName = "testArgName";
        OptionBuilder.withArgName(argName);
        Assert.assertEquals(argName, OptionBuilder.create().getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgsNum() {
        int numArgs = 2;
        OptionBuilder.hasOptionalArgs(numArgs);
        Assert.assertEquals(numArgs, OptionBuilder.create().getArgs());
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder.withType(type);
        Assert.assertEquals(type, OptionBuilder.create().getType());
    }
    
    @Test
    public void testWithDescription() {
        String description = "testDescription";
        OptionBuilder.withDescription(description);
        Assert.assertEquals(description, OptionBuilder.create().getDescription());
    }
    
    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        Assert.assertNotNull(option);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidOptChar() {
        OptionBuilder.create('#');
    }
    
    @Test
    public void testCreateWithValidOptChar() {
        char opt = 'D';
        Option option = OptionBuilder.create(opt);
        Assert.assertEquals(String.valueOf(opt), option.getOpt());
    }
    
    // 
}