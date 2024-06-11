import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longopt").create();
        assert(option.getLongOpt().equals("longopt"));
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assert(option.getArgs() == 1);
    }

    @Test
    public void testHasArgWithFalse() {
        Option option = OptionBuilder.hasArg(false).create();
        assert(option.getArgs() == Option.UNINITIALIZED);
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("argName").create();
        assert(option.getArgName().equals("argName"));
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assert(option.isRequired());
    }

    @Test
    public void testWithValueSeparatorWithChar() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assert(option.getValueSeparator() == ':');
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator().create();
        assert(option.getValueSeparator() == '=');
    }

    @Test
    public void testIsRequiredWithFalse() {
        Option option = OptionBuilder.isRequired(false).create();
        assert(!option.isRequired());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assert(option.getArgs() == Option.UNLIMITED_VALUES);
    }

    @Test
    public void testHasArgsWithNum() {
        Option option = OptionBuilder.hasArgs(2).create();
        assert(option.getArgs() == 2);
    }

    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assert(option.getArgs() == 1);
        assert(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assert(option.getArgs() == Option.UNLIMITED_VALUES);
        assert(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        Option option = OptionBuilder.hasOptionalArgs(3).create();
        assert(option.getArgs() == 3);
        assert(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Object.class).create();
        assert(option.getType() == Object.class);
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description").create();
        assert(option.getDescription().equals("description"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharWithInvalidOpt() {
        OptionBuilder.create('#');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMissingLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('c');
        assert(option.getOpt().equals("c"));
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("opt");
        assert(option.getOpt().equals("opt"));
    }
}