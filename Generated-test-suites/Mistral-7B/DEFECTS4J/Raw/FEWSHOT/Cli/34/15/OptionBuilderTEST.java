package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithSingleCharacterOption() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption");
        Option option = optionBuilder.create('m');
        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("myOption"));
        assertTrue(option.isRequired());
        assertTrue(option.getArgName() == null);
        assertTrue(option.getValueSeparator() == '\u0000');
        assertTrue(option.getArgType() == null);
        assertTrue(option.getArgs() == Option.UNINITIALIZED);
        assertTrue(option.hasOptionalArg() == false);
    }

    @Test
    public void testOptionBuilderWithLongOption() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myLongOption")
                .withDescription("This is a long option");
        Option option = optionBuilder.create('-');
        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("myLongOption"));
        assertTrue(option.getDescription().equals("This is a long option"));
        assertTrue(option.isRequired());
        assertTrue(option.getArgName() == null);
        assertTrue(option.getValueSeparator() == '\u0000');
        assertTrue(option.getArgType() == null);
        assertTrue(option.getArgs() == Option.UNINITIALIZED);
        assertTrue(option.hasOptionalArg() == false);
    }

    @Test
    public void testOptionBuilderWithSingleCharacterRequiredOptionWithArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption")
                .hasArg();
        Option option = optionBuilder.create('m');
        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("myOption"));
        assertTrue(option.isRequired());
        assertTrue(option.getArgName() != null);
        assertTrue(option.getValueSeparator() == '\u0000');
        assertTrue(option.getArgType() == null);
        assertTrue(option.getArgs() == 1);
        assertTrue(option.hasOptionalArg() == false);
    }

    @Test
    public void testOptionBuilderWithSingleCharacterOptionalOptionWithArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption")
                .hasOptionalArg();
        Option option = optionBuilder.create('m');
        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("myOption"));
        assertTrue(option.isRequired() == false);
        assertTrue(option.getArgName() != null);
        assertTrue(option.getValueSeparator() == '\u0000');
        assertTrue(option.getArgType() == null);
        assertTrue(option.getArgs() == 1);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderWithMultipleCharacterOptionalOptionWithArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption")
                .hasOptionalArgs(3)
                .hasArgName("arg");
        Option option = optionBuilder.create("--myOption");
        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("myOption"));
        assertTrue(option.isRequired() == false);
        assertTrue(option.getArgName().equals("arg"));
        assertTrue(option.getValueSeparator() == ' ');
        assertTrue(option.getArgType() == null);
        assertTrue(option.getArgs() == 3);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption")
                .withValueSeparator(':');
        Option option = optionBuilder.create('-m');
        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("myOption"));
        assertTrue(option.isRequired());
        assertTrue(option.getArgName() == null);
        assertTrue(option.getValueSeparator() == ':');
        assertTrue(option.getArgType() == null);
        assertTrue(option.getArgs() == Option.UNINITIALIZED);
        assertTrue(option.hasOptionalArg() == false);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption")
                .withType(Integer.class);
        Option option = optionBuilder.create('-m');
        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("myOption"));
        assertTrue(option.isRequired());
        assertTrue(option.getArgName() == null);
        assertTrue(option.getValueSeparator() == '\u0000');
        assertTrue(option.getArgType().equals(Integer.class));
        assertTrue(option.getArgs() == Option.UNINITIALIZED);
        assertTrue(option.hasOptionalArg() == false);
    }
}