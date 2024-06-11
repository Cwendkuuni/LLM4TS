import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOptAndHasArg() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .create();

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertNull(option.getValue());

        option.setValue("argValue");
        Assert.assertEquals("argValue", option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndOptionalArg() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArg()
                .create();

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertNull(option.getValue());

        option.setValue("argValue");
        Assert.assertEquals("argValue", option.getValue());

        option.setValue((Object[]) new String[]{"argValue1", "argValue2"});
        Assert.assertArrayEquals(new String[]{"argValue1", "argValue2"}, option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndHasArgs() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs()
                .create();

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertNotNull(option.getArgs());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        Assert.assertNull(option.getValue());

        option.setValue("argValue1");
        Assert.assertArrayEquals(new String[]{"argValue1"}, option.getArgs());
        Assert.assertNull(option.getValue());

        option.setValue("argValue1", "argValue2");
        Assert.assertArrayEquals(new String[]{"argValue1", "argValue2"}, option.getArgs());
        Assert.assertNull(option.getValue());

        option.setValue(new String[]{"argValue1", "argValue2", "argValue3"});
        Assert.assertArrayEquals(new String[]{"argValue1", "argValue2", "argValue3"}, option.getArgs());
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndIsRequired() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .create();

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertFalse(option.isRequired());

        try {
            option.setValue(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptionBuilderWithLongOptAndValueSeparator() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withValueSeparator('=')
                .create();

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertNull(option.getValue());

        option.setValue("key=value");
        Assert.assertEquals("key", option.getKey());
        Assert.assertEquals("value", option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndType() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .withType(Integer.class)
                .create();

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertNull(option.getValue());

        try {
            option.setValue("123");
            Assert.assertEquals(123, option.getValue(Integer.class));
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }

        try {
            option.setValue("abc");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}