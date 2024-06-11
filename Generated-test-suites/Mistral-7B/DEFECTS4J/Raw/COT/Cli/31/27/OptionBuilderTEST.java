package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option(null, "longOpt", false, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.instance.create();
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option(null, null, true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgWithArgName()
    {
        String argName = "argName";
        OptionBuilder optionBuilder = OptionBuilder.withArgName(argName).hasArg();
        Option expectedOption = new Option(null, null, true, argName);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option(null, null, true, null);
        Option actualOption = optionBuilder.create();
        assertTrue(actualOption.isRequired());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option(null, null, true, null);
        Option actualOption = optionBuilder.create();
        assertFalse(actualOption.isRequired());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option(null, null, Option.UNLIMITED_VALUES, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgsWithNumArgs()
    {
        int numArgs = 2;
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(numArgs);
        Option expectedOption = new Option(null, null, numArgs, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = new Option(null, null, Option.UNLIMITED_VALUES, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs()
    {
        int numArgs = 3;
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(numArgs);
        Option expectedOption = new Option(null, null, numArgs, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithType()
    {
        String typeName = "String.class";
        OptionBuilder optionBuilder = OptionBuilder.withType(typeName);
        Option expectedOption = new Option(null, null, 1, null).setType(Class.forName(typeName));
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType()
    {
        String typeName = "InvalidType.class";
        OptionBuilder optionBuilder = OptionBuilder.withType(typeName);
        optionBuilder.create();
    }

    @Test
    public void testWithDescription()
    {
        String description = "Test description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option expectedOption = new Option(null, null, 1, null).setDescription(description);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }
}