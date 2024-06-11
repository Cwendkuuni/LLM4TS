package org.apache.commons.cli;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        assert option.getLongOpt().equals("testLongOpt");
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assert option.getArgs() == 1;
    }

    @Test
    public void testHasArgWithBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        assert option.getArgs() == 1;
        option = OptionBuilder.hasArg(false).create();
        assert option.getArgs() == Option.UNINITIALIZED;
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("testArgName").create();
        assert option.getArgName().equals("testArgName");
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assert option.isRequired();
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create('D');
        assert option.getValueSeparator() == ':';
    }

    @Test
    public void testWithDefaultSeparator() {
        Option option = OptionBuilder.withValueSeparator().create('D');
        assert option.getValueSeparator() == '=';
    }

    @Test
    public void testIsRequiredWithBoolean() {
        Option option = OptionBuilder.isRequired(true).create();
        assert option.isRequired();
        option = OptionBuilder.isRequired(false).create();
        assert !option.isRequired();
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assert option.getArgs() == Option.UNLIMITED_VALUES;
    }

    @Test
    public void testHasArgsWithNum() {
        Option option = OptionBuilder.hasArgs(3).create();
        assert option.getArgs() == 3;
    }

    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assert option.getArgs() == 1;
        assert option.hasOptionalArg();
    }

    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assert option.getArgs() == Option.UNLIMITED_VALUES;
        assert option.hasOptionalArg();
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        Option option = OptionBuilder.hasOptionalArgs(2).create();
        assert option.getArgs() == 2;
        assert option.hasOptionalArg();
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        assert option.getType() == Integer.class;
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("testDescription").create();
        assert option.getDescription().equals("testDescription");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidChar() {
        OptionBuilder.create(' ');
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('D');
        assert option.getOpt().equals("D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOpt");
        assert option.getOpt().equals("testOpt");
    }

}