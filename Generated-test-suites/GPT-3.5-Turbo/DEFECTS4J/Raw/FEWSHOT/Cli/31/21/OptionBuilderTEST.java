import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        Assert.assertEquals("test", option.getLongOpt());

        OptionBuilder.withLongOpt(null);
        option = OptionBuilder.create();
        Assert.assertNull(option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        Assert.assertEquals(1, option.getArgs());

        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        Option option = OptionBuilder.create();
        Assert.assertEquals("test", option.getArgName());

        OptionBuilder.withArgName(null);
        option = OptionBuilder.create();
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.isRequired());

        OptionBuilder.isRequired(false);
        option = OptionBuilder.create();
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        Option option = OptionBuilder.create();
        Assert.assertEquals("test description", option.getDescription());
    }

    @Test
    public void testReset() {
        OptionBuilder.withDescription("test description");
        Option option = OptionBuilder.create();
        Assert.assertNotNull(option.getDescription());

        OptionBuilder.reset();
        option = OptionBuilder.create();
        Assert.assertNull(option.getDescription());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals("t", option.getOpt());
        Assert.assertEquals("arg", option.getArgName());

        option = OptionBuilder.create("test");
        Assert.assertEquals("test", option.getOpt());
        Assert.assertEquals("arg", option.getArgName());

        option = OptionBuilder.create();
        Assert.assertNull(option.getOpt());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithLongOpt() {
        OptionBuilder.create();
    }
}