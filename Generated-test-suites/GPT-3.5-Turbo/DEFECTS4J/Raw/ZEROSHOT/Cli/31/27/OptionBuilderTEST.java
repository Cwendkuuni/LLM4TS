import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        String longOpt = "testLongOpt";
        OptionBuilder.withLongOpt(longOpt);
        Assert.assertEquals(longOpt, OptionBuilder.create().getLongOpt());
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
        Assert.assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        String argName = "testArg";
        OptionBuilder.withArgName(argName);
        Assert.assertEquals(argName, OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        char valueSeparator = ':';
        OptionBuilder.withValueSeparator(valueSeparator);
        Assert.assertEquals(valueSeparator, OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        char valueSeparator = '=';
        OptionBuilder.withValueSeparator();
        Assert.assertEquals(valueSeparator, OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        boolean newRequired = true;
        OptionBuilder.isRequired(newRequired);
        Assert.assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        int numArgs = 3;
        OptionBuilder.hasArgs(numArgs);
        Assert.assertEquals(numArgs, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
        Assert.assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        int numArgs = 2;
        OptionBuilder.hasOptionalArgs(numArgs);
        Assert.assertTrue(OptionBuilder.create().hasOptionalArg());
        Assert.assertEquals(numArgs, OptionBuilder.create().getArgs());
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
    public void testCreateWithChar() {
        char opt = 't';
        OptionBuilder.create(opt);
        Assert.assertEquals(String.valueOf(opt), OptionBuilder.create().getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidChar() {
        char opt = '-';
        OptionBuilder.create(opt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        String opt = "testOpt";
        OptionBuilder.withLongOpt(opt);
        Assert.assertEquals(opt, OptionBuilder.create().getOpt());
    }
}