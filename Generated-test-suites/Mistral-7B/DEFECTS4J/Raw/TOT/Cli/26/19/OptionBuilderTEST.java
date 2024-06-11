package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

    @Test
    public void testOptionWithSingleRequiredArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1")
                .isRequired()
                .hasArg();
        Option option = optionBuilder.create('o');

        CommandLine args = new DefaultParser().parse(new String[]{"-o", "test"});
        assertEquals("test", option.getValue());

        CommandLine emptyArgs = new DefaultParser().parse(new String[]{"-o"});
        assertNull(option.getValue());
        assertEquals("This option requires an argument.", option.getArgs()[0].getMessage());
    }

    @Test
    public void testOptionWithSingleOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option2")
                .hasArg(true)
                .isRequired();
        Option option = optionBuilder.create('o');

        CommandLine argsWithValue = new DefaultParser().parse(new String[]{"-o", "test"});
        assertEquals("test", option.getValue());

        CommandLine argsWithoutValue = new DefaultParser().parse(new String[]{"-o"});
        assertNull(option.getValue());
    }

    @Test
    public void testOptionWithMultipleOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option3")
                .hasArgs(2)
                .hasOptionalArgs();
        Option option = optionBuilder.create('o');

        CommandLine argsWithValues = new DefaultParser().parse(new String[]{"-o", "test1", "test2"});
        assertEquals("test1", option.getValue(0));
        assertEquals("test2", option.getValue(1));

        CommandLine argsWithoutValues = new DefaultParser().parse(new String[]{"-o"});
        assertNull(option.getValue(0));
        assertNull(option.getValue(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        optionBuilder.create('o');
    }
}