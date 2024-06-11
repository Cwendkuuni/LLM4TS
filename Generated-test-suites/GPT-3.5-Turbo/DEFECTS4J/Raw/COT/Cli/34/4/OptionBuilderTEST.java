import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longOptTest").create();
        Assert.assertEquals("longOptTest", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        Assert.assertEquals(true, option.hasArg());
    }

    @Test
    public void testHasArgWithValue() {
        Option option = OptionBuilder.hasArg(true).create();
        Assert.assertEquals(true, option.hasArg());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("argNameTest").create();
        Assert.assertEquals("argNameTest", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        Assert.assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        Assert.assertEquals(true, option.hasArg());
        Assert.assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertEquals(true, option.hasArg());
        Assert.assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgsValue() {
        Option option = OptionBuilder.hasOptionalArgs(3).create();
        Assert.assertEquals(3, option.getArgs());
        Assert.assertEquals(true, option.hasArg());
        Assert.assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("descriptionTest").create();
        Assert.assertEquals("descriptionTest", option.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('c');
        Assert.assertEquals("c", option.getOpt());
    }

    @Test
    public void testCreate() {
        try {
            OptionBuilder.reset();
            Option option = OptionBuilder.create();
            Assert.fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("optTest");
        Assert.assertEquals("optTest", option.getOpt());
    }
}