import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithDefaults() {
        Option option = OptionBuilder.create('A');
        Assert.assertEquals("Option 'A' with no arguments", option.getLongOpt(), null);
        Assert.assertEquals("Option 'A' is not required", option.isRequired(), false);
        Assert.assertEquals("Option 'A' accepts one argument", option.getArgs(), Option.UNINITIALIZED);
        Assert.assertEquals("Option 'A' has no value", option.getType(), null);
        Assert.assertEquals("Option 'A' has no description", option.getDescription(), null);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("myOption")
                .create('A');
        Assert.assertEquals("Option 'A' has long option 'myOption'", option.getLongOpt(), "myOption");
    }

    @Test
    public void testOptionBuilderIsRequired() {
        Option option = OptionBuilder.isRequired().create('A');
        Assert.assertEquals("Option 'A' is required", option.isRequired(), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderMissingLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        OptionBuilder.isRequired();
        OptionBuilder.create();
    }

    @Test
    public void testOptionBuilderHasArg() {
        Option option = OptionBuilder.hasArg().create('A');
        Assert.assertEquals("Option 'A' accepts one argument", option.getArgs(), 1);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        Option option = OptionBuilder.hasArgs().create('A');
        Assert.assertEquals("Option 'A' accepts unlimited arguments", option.getArgs(), Option.UNLIMITED_VALUES);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create('A');
        Assert.assertEquals("Option 'A' can have an optional argument", option.getOptionalArg(), true);
    }

    @Test
    public void testOptionBuilderWithDescription() {
        Option option = OptionBuilder.withDescription("description")
                .create('A');
        Assert.assertEquals("Option 'A' has description 'description'", option.getDescription(), "description");
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':')
                .create('A');
        Assert.assertEquals("Option 'A' uses ':' as value separator", option.getValueSeparator(), ':');
    }

    @Test
    public void testOptionBuilderWithType() {
        Option option = OptionBuilder.withType(String.class)
                .create('A');
        Assert.assertEquals("Option 'A' accepts a String value", option.getType(), String.class);
    }

    @Test
    public void testOptionBuilderHasArgsWithValues() {
        Option option = OptionBuilder.hasArgs(2)
                .create('A');
        Assert.assertEquals("Option 'A' accepts exactly two arguments", option.getArgs(), 2);
    }

    @Test
    public void testParseOptionsWithDefaults() {
        Options options = new Options();
        Option option = OptionBuilder.create('A').create(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"-A", "value1", "value2"});
        Assert.assertEquals("Option 'A' has been parsed correctly", option.getValue(), new String[]{"value1", "value2"});
    }

    @Test
    public void testParseOptionsWithLongOpt() {
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("myOption")
                .create('A')
                .create(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"--myOption", "value1", "value2"});
        Assert.assertEquals("Option 'A' has been parsed correctly", option.getValue(), new String[]{"value1", "value2"});
    }

    @Test
    public void testParseOptionsWithArgName() {
        Options options = new Options();
        Option option = OptionBuilder.withArgName("argName").create('A')
                .create(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"-A", "argName1", "argName2"});
        Assert.assertEquals("Option 'A' has been parsed correctly", option.getValue(), new String[]{"argName1", "argName2"});
    }

    @Test
    public void testParseOptionsWithRequiredArg() {
        Options options = new Options();
        Option option = OptionBuilder.isRequired().create('A')
                .create(options);
        String[] args = new String[]{"-A"};
        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOptionsWithOptionalArg() {
        Options options = new Options();
        Option option = OptionBuilder.hasOptionalArg().create('A')
                .create(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"-A"});
        Assert.assertNull("Option 'A' has no value when not provided", option.getValue());
        CommandLine cmdWithValue = new DefaultParser().parse(options, new String[]{"-A", "value"});
        Assert.assertEquals("Option 'A' has been parsed correctly", option.getValue(), new String[]{"value"});
    }

    @Test
    public void testParseOptionsWithValueSeparator() {
        Options options = new Options();
        Option option = OptionBuilder.withValueSeparator(':')
                .create('A')
                .create(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"-A", "value1:value2"});
        Assert.assertEquals("Option 'A' has been parsed correctly", option.getValue(), new String[]{"value1:value2"});
    }

    @Test
    public void testParseOptionsWithType() {
        Options options = new Options();
        Option option = OptionBuilder.withType(Integer.class)
                .create('A')
                .create(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"-A", "1"});
        Assert.assertEquals("Option 'A' has been parsed correctly", option.getValue(), new Integer(1));
    }

    @Test
    public void testParseOptionsWithHasArgs() {
        Options options = new Options();
        Option option = OptionBuilder.hasArgs(2)
                .create('A')
                .create(options);
        CommandLine cmd = new DefaultParser().parse(options, new String[]{"-A", "value1", "value2"});
        Assert.assertEquals("Option 'A' has been parsed correctly", option.getValue(), new String[]{ "value1", "value2" });
    }
}