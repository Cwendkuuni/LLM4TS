
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        String longopt = "test";
        Option option = OptionBuilder.withLongOpt(longopt).create();
        Assert.assertEquals(longopt, option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgFalse() {
        Option option = OptionBuilder.hasArg(false).create();
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        String argName = "test";
        Option option = OptionBuilder.withArgName(argName).create();
        Assert.assertEquals(argName, option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testWithOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testWithOptionalArgsNum() {
        int numArgs = 3;
        Option option = OptionBuilder.hasOptionalArgs(numArgs).create();
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(numArgs, option.getArgs());
    }

    @Test
    public void testWithType() {
        Object type = new Object();
        Option option = OptionBuilder.withType(type).create();
        Assert.assertEquals(type, option.getType());
    }

    @Test
    public void testWithDescription() {
        String description = "test";
        Option option = OptionBuilder.withDescription(description).create();
        Assert.assertEquals(description, option.getDescription());
    }

    @Test
    public void testCreateChar() {
        char opt = 'c';
        Option option = OptionBuilder.create(opt);
        Assert.assertEquals(String.valueOf(opt), option.getOpt());
    }

    @Test
    public void testCreate() {
        String longopt = "test";
        Option option = OptionBuilder.create();
        Assert.assertEquals(longopt, option.getLongOpt());
    }

    @Test
    public void testCreateString() {
        String opt = "t";
        Option option = OptionBuilder.create(opt);
        Assert.assertEquals(opt, option.getOpt());
    }

}
