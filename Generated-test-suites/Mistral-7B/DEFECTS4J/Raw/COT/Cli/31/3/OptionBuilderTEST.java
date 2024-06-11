package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option("longOpt", "option description");
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("arg", "argument description");
        expectedOption.setArgName("arg");
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName").hasArg();
        Option expectedOption = new Option("argName", "argument description");
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option("arg", "argument description");
        expectedOption.setRequired(true);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("arg", "argument description");
        expectedOption.setOptionalArg(true);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.isOptionalArg(), actualOption.isOptionalArg());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("arg", "argument description");
        expectedOption.setArgs(Option.UNLIMITED_VALUES);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option expectedOption = new Option("arg", "argument description");
        expectedOption.setArgs(3);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option expectedOption = new Option("arg", "argument description");
        expectedOption.setType(Integer.class);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getType(), actualOption.getType());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option expectedOption = new Option("arg", "description");
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }
}