import org.junit.Test;
import org.junit.Assert;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create();
        Assert.assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        Assert.assertEquals(true, option.hasArg());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("name").create();
        Assert.assertEquals("name", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        Assert.assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        Assert.assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("desc").create();
        Assert.assertEquals("desc", option.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        Assert.assertEquals("test", option.getOpt());
    }

}