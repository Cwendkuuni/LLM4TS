package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option opt = OptionBuilder.withLongOpt("test").create('t');
        assert opt.getLongOpt().equals("test");
    }

    @Test
    public void testHasArg() {
        Option opt = OptionBuilder.hasArg().create('t');
        assert opt.getArgs() == 1;
    }

    @Test
    public void testHasArgWithFalse() {
        Option opt = OptionBuilder.hasArg(false).create('t');
        assert opt.getArgs() == Option.UNINITIALIZED;
    }

    @Test
    public void testWithArgName() {
        Option opt = OptionBuilder.withArgName("test").create('t');
        assert opt.getArgName().equals("test");
    }

    @Test
    public void testIsRequired() {
        Option opt = OptionBuilder.isRequired().create('t');
        assert opt.isRequired();
    }

    @Test
    public void testWithValueSeparator() {
        Option opt = OptionBuilder.withValueSeparator(':').create('t');
        assert opt.getValueSeparator() == ':';
    }

    @Test
    public void testWithDefaultValueSeparator() {
        Option opt = OptionBuilder.withValueSeparator().create('t');
        assert opt.getValueSeparator() == '=';
    }

    @Test
    public void testIsRequiredWithFalse() {
        Option opt = OptionBuilder.isRequired(false).create('t');
        assert !opt.isRequired();
    }

    @Test
    public void testHasArgs() {
        Option opt = OptionBuilder.hasArgs().create('t');
        assert opt.getArgs() == Option.UNLIMITED_VALUES;
    }

    @Test
    public void testHasNumArgs() {
        Option opt = OptionBuilder.hasArgs(2).create('t');
        assert opt.getArgs() == 2;
    }

    @Test
    public void testHasOptionalArg() {
        Option opt = OptionBuilder.hasOptionalArg().create('t');
        assert opt.getArgs() == 1 && opt.hasOptionalArg();
    }

    @Test
    public void testHasOptionalArgs() {
        Option opt = OptionBuilder.hasOptionalArgs().create('t');
        assert opt.getArgs() == Option.UNLIMITED_VALUES && opt.hasOptionalArg();
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        Option opt = OptionBuilder.hasOptionalArgs(3).create('t');
        assert opt.getArgs() == 3 && opt.hasOptionalArg();
    }

    @Test
    public void testWithType() {
        Option opt = OptionBuilder.withType(String.class).create('t');
        assert opt.getType().equals(String.class);
    }

    @Test
    public void testWithDescription() {
        Option opt = OptionBuilder.withDescription("test option").create('t');
        assert opt.getDescription().equals("test option");
    }

    @Test
    public void testCreateWithChar() {
        Option opt = OptionBuilder.create('t');
        assert opt.getOpt().equals("t");
    }

    @Test
    public void testCreate() {
        Option opt = OptionBuilder.create();
        assert opt.getOpt() == null;
    }

    @Test
    public void testCreateWithString() {
        Option opt = OptionBuilder.create("test");
        assert opt.getOpt().equals("test");
    }

}