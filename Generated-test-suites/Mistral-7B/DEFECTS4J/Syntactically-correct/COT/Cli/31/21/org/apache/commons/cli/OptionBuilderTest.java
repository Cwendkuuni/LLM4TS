package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option("longOpt", "description");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder("arg").required(false).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder("arg").required(true).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder("arg").required(false).args(Option.UNLIMITED_VALUES).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.instance.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt()
    {
        OptionBuilder.withLongOpt(" ");
        OptionBuilder.instance.create();
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option expectedOption = new Option("arg", "description");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("name");
        Option expectedOption = new Option("arg", "description").argName("name");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getArgName(), actualOption.getArgName());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');
        Option expectedOption = new Option("arg", "description").valueSeparator(';');
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getValueSeparator(), actualOption.getValueSeparator());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("arg", "description").optionalArg(true);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.isOptional(), actualOption.isOptional());
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option expectedOption = new Option("arg", "description").optionalArg(true).args(2);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getArgs(), actualOption.getArgs());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option expectedOption = new Option("arg", "description").type(Integer.class);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getType(), actualOption.getType());
    }
}