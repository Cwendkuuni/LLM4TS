package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithDefaultValues()
    {
        Option option = OptionBuilder.create();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);

        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasArg()
    {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs().numberOfArgs());
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasOptionalArg()
    {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs().numberOfArgs());
        assertTrue(option.getArgs().isUnbounded());
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasArgs()
    {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().numberOfArgs());
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasArgsAndValueSeparator()
    {
        char valueSeparator = ':';
        OptionBuilder.withValueSeparator(valueSeparator);
        Option option = OptionBuilder.create('D');
        assertEquals(valueSeparator, option.getValueSeparator());
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasArgsAndDescription()
    {
        String description = "Description for option D";
        OptionBuilder.withDescription(description);
        Option option = OptionBuilder.create('D');
        assertEquals(description, option.getDescription());
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasArgsAndArgName()
    {
        String argName = "database";
        OptionBuilder.withArgName(argName);
        Option option = OptionBuilder.create('D');
        assertEquals(argName, option.getArgName());
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasArgsAndArgNameAndDescription()
    {
        String argName = "database";
        String description = "Description for option D";
        OptionBuilder.withArgName(argName).withDescription(description);
        Option option = OptionBuilder.create('D');
        assertEquals(argName, option.getArgName());
        assertEquals(description, option.getDescription());
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasArgsAndArgNameAndDescriptionAndIsRequired()
    {
        String argName = "database";
        String description = "Description for option D";
        OptionBuilder.withArgName(argName).withDescription(description).isRequired();
        Option option = OptionBuilder.create('D');
        assertEquals(argName, option.getArgName());
        assertEquals(description, option.getDescription());
        assertTrue(option.isRequired());
        OptionBuilder.reset();
    }

    @Test
    public void testOptionBuilderWithHasArgsAndArgNameAndDescriptionAndIsRequiredAndType()
    {
        String argName = "database";
        String description = "Description for option D";
        Class type = Integer.class;
        OptionBuilder.withArgName(argName).withDescription(description).isRequired().withType(type);
        Option option = OptionBuilder.create('D');
        assertEquals(argName, option.getArgName());
        assertEquals(description, option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(type, option.getType());
        OptionBuilder.reset();
    }
}