import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test");
        Assert.assertEquals("test", OptionBuilder.getLongopt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithoutValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        Assert.assertEquals(0, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("test");
        Assert.assertEquals("test", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.getRequired());
    }
    
    @Test
    public void testWithRequiredValue() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        Assert.assertTrue(OptionBuilder.getRequired());
    }
    
    @Test
    public void testWithOptionalValue() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.getRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgsWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.getOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.getOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Assert.assertEquals(2, OptionBuilder.getNumberOfArgs());
        Assert.assertTrue(OptionBuilder.getOptionalArg());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Object.class);
        Assert.assertEquals(Object.class, OptionBuilder.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test");
        Assert.assertEquals("test", OptionBuilder.getDescription());
    }

}