import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        String longOpt = "longOpt";
        OptionBuilder withLongOpt = optionBuilder.withLongOpt(longOpt);
        Assert.assertEquals(longOpt, OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder hasArg = optionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder hasArg = optionBuilder.hasArg(false);
        Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = new OptionBuilder();
        String argName = "argName";
        OptionBuilder withArgName = optionBuilder.withArgName(argName);
        Assert.assertEquals(argName, OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder isRequired = optionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = new OptionBuilder();
        char valueSeparator = ';';
        OptionBuilder withValueSeparator = optionBuilder.withValueSeparator(valueSeparator);
        Assert.assertEquals(valueSeparator, OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder withValueSeparator = optionBuilder.withValueSeparator();
        Assert.assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredFalse() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder isRequired = optionBuilder.isRequired(false);
        Assert.assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder hasArgs = optionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder optionBuilder = new OptionBuilder();
        int numArgs = 5;
        OptionBuilder hasArgs = optionBuilder.hasArgs(numArgs);
        Assert.assertEquals(numArgs, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder hasOptionalArg = optionBuilder.hasOptionalArg();
        Assert.assertTrue(OptionBuilder.optionalArg);
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder hasOptionalArgs = optionBuilder.hasOptionalArgs();
        Assert.assertTrue(OptionBuilder.optionalArg);
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder optionBuilder = new OptionBuilder();
        int numArgs = 3;
        OptionBuilder hasOptionalArgs = optionBuilder.hasOptionalArgs(numArgs);
        Assert.assertTrue(OptionBuilder.optionalArg);
        Assert.assertEquals(numArgs, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Class type = String.class;
        OptionBuilder withType = optionBuilder.withType(type);
        Assert.assertEquals(type, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = new OptionBuilder();
        String description = "description";
        OptionBuilder withDescription = optionBuilder.withDescription(description);
        Assert.assertEquals(description, OptionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder create = optionBuilder.create();
    }

    @Test
    public void testCreate() {
        OptionBuilder optionBuilder = new OptionBuilder();
        String longOpt = "longOpt";
        OptionBuilder withLongOpt = optionBuilder.withLongOpt(longOpt);
        Option option = withLongOpt.create('L');
        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals("", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(0, option.getArgs().length);
        Assert.assertNull(option.getType());
        Assert.assertEquals('', option.getValueSeparator());
        Assert.assertEquals("", option.getArgName());
    }
    
}