import org.junit.Test;
import org.junit.Assert;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testOption", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder.withArgName("argName");
        Option option = OptionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testDescription", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder.withValueSeparator('=');
        Option option = OptionBuilder.create();
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        Assert.assertEquals(1, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        Assert.assertEquals(2, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgsZero() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs(0);
        Option option = OptionBuilder.create();
        Assert.assertEquals(0, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

}