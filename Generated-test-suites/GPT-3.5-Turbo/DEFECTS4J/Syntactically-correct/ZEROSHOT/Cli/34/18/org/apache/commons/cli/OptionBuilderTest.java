import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Assert.assertEquals("testLongOpt", OptionBuilder.class.getDeclaredField("longopt").get(null));
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.class.getDeclaredField("numberOfArgs").getInt(null));
    }
    
    @Test
    public void testHasArgWithParam() {
        OptionBuilder.hasArg(false);
        Assert.assertEquals(-1, OptionBuilder.class.getDeclaredField("numberOfArgs").getInt(null));
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        Assert.assertEquals("testArgName", OptionBuilder.class.getDeclaredField("argName").get(null));
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Assert.assertEquals(true, OptionBuilder.class.getDeclaredField("required").getBoolean(null));
    }
    
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', OptionBuilder.class.getDeclaredField("valuesep").getChar(null));
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.class.getDeclaredField("valuesep").getChar(null));
    }
    
    @Test
    public void testIsRequiredWithParam() {
        OptionBuilder.isRequired(true);
        Assert.assertEquals(true, OptionBuilder.class.getDeclaredField("required").getBoolean(null));
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Assert.assertEquals(-2, OptionBuilder.class.getDeclaredField("numberOfArgs").getInt(null));
    }
    
    @Test
    public void testHasArgsWithParam() {
        OptionBuilder.hasArgs(2);
        Assert.assertEquals(2, OptionBuilder.class.getDeclaredField("numberOfArgs").getInt(null));
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Assert.assertEquals(1, OptionBuilder.class.getDeclaredField("numberOfArgs").getInt(null));
        Assert.assertEquals(true, OptionBuilder.class.getDeclaredField("optionalArg").getBoolean(null));
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(-2, OptionBuilder.class.getDeclaredField("numberOfArgs").getInt(null));
        Assert.assertEquals(true, OptionBuilder.class.getDeclaredField("optionalArg").getBoolean(null));
    }
    
    @Test
    public void testHasOptionalArgsWithParam() {
        OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(3, OptionBuilder.class.getDeclaredField("numberOfArgs").getInt(null));
        Assert.assertEquals(true, OptionBuilder.class.getDeclaredField("optionalArg").getBoolean(null));
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Assert.assertEquals(String.class, OptionBuilder.class.getDeclaredField("type").get(null));
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Assert.assertEquals("testDescription", OptionBuilder.class.getDeclaredField("description").get(null));
    }
    
    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals('t', option.getOpt().charAt(0));
    }
    
    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateWithStringOpt() {
        Option option = OptionBuilder.create("testOpt");
        Assert.assertEquals("testOpt", option.getOpt());
    }
    
    @Test
    public void testCreateWithStringOptAndDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create("testOpt");
        Assert.assertEquals("testOpt", option.getOpt());
        Assert.assertEquals("testDescription", option.getDescription());
    }
    
}