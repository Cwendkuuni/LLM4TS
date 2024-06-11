import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testOption").create();
        // typical use case
        // test if the option has the correct long option value
        assert(option.getLongOpt().equals("testOption"));

        // edge case
        // test if the option has the correct long option value
        option = OptionBuilder.withLongOpt("").create();
        assert(option.getLongOpt().equals(""));

        // error scenario
        // test if the option has the correct long option value
        option = OptionBuilder.withLongOpt(null).create();
        assert(option.getLongOpt() == null);

    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        // typical use case
        // test if the option requires an argument value
        assert(option.hasArgs());

        // edge case
        // test if the option requires an argument value
        option = OptionBuilder.hasArg(false).create();
        assert(!option.hasArgs());

        // error scenario
        // test if the option requires an argument value
        option = OptionBuilder.hasArg(true).create();
        assert(option.hasArgs());

    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("arg").create();
        // typical use case
        // test if the option has the specified argument value name
        assert(option.getArgName().equals("arg"));

        // edge case
        // test if the option has the specified argument value name
        option = OptionBuilder.withArgName("").create();
        assert(option.getArgName().equals(""));

        // error scenario
        // test if the option has the specified argument value name
        option = OptionBuilder.withArgName(null).create();
        assert(option.getArgName() == null);

    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        // typical use case
        // test if the option is required
        assert(option.isRequired());

        // edge case
        // test if the option is required
        option = OptionBuilder.isRequired(false).create();
        assert(!option.isRequired());

        // error scenario
        // test if the option is required
        option = OptionBuilder.isRequired(true).create();
        assert(option.isRequired());

    }

    @Test
    public void testWithValueSeparatorChar() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        // typical use case
        // test if the option has the specified value separator
        assert(option.getValueSeparator() == ':');

        // edge case
        // test if the option has the specified value separator
        option = OptionBuilder.withValueSeparator((char) 0).create();
        assert(option.getValueSeparator() == (char) 0);

        // error scenario
        // test if the option has the specified value separator
        option = OptionBuilder.withValueSeparator((char) -1).create();
        assert(option.getValueSeparator() == (char) -1);

    }

    @Test
    public void testWithDefaultValueSeparator() {
        Option option = OptionBuilder.withValueSeparator().create();
        // typical use case
        // test if the option has the default value separator
        assert(option.getValueSeparator() == '=');

    }

    @Test
    public void testIsRequiredBoolean() {
        Option option = OptionBuilder.isRequired(true).create();
        // typical use case
        // test if the option is required
        assert(option.isRequired());

        // edge case
        // test if the option is required
        option = OptionBuilder.isRequired(false).create();
        assert(!option.isRequired());

    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        // typical use case
        // test if the option can have unlimited argument values
        assert(option.getArgs() == Option.UNLIMITED_VALUES);

    }

    @Test
    public void testHasArgsInt() {
        Option option = OptionBuilder.hasArgs(3).create();
        // typical use case
        // test if the option can have the specified number of argument values
        assert(option.getArgs() == 3);

    }

    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        // typical use case
        // test if the option can have an optional argument
        assert(option.hasOptionalArg());

    }

    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        // typical use case
        // test if the option can have unlimited optional argument values
        assert(option.getArgs() == Option.UNLIMITED_VALUES);

    }

    @Test
    public void testHasOptionalArgsInt() {
        Option option = OptionBuilder.hasOptionalArgs(2).create();
        // typical use case
        // test if the option can have the specified number of optional argument values
        assert(option.getArgs() == 2);

    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        // typical use case
        // test if the option has the specified type for argument value
        assert(option.getType().equals(Integer.class));

        // edge case
        // test if the option has the specified type for argument value
        option = OptionBuilder.withType(null).create();
        assert(option.getType() == null);

    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("test option").create();
        // typical use case
        // test if the option has the specified description
        assert(option.getDescription().equals("test option"));

        // edge case
        // test if the option has the specified description
        option = OptionBuilder.withDescription("").create();
        assert(option.getDescription().equals(""));

        // error scenario
        // test if the option has the specified description
        option = OptionBuilder.withDescription(null).create();
        assert(option.getDescription() == null);

    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        // typical use case
        // test if the option has the specified option character
        assert(option.getOpt() == 't');

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder.withLongOpt("").create();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
        OptionBuilder.withLongOpt(null).create();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt() {
        OptionBuilder.create();

    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        // typical use case
        // test if the option has the specified option character
        assert(option.getOpt().equals("test"));

    }
}