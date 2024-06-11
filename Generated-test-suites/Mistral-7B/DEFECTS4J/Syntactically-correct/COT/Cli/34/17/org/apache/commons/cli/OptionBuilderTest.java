package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptValue");
        OptionexpectedOption = OptionBuilder.create('L');
        Assert.assertEquals("longOptValue", expectedOption.getLongOpt());
        Assert.assertNull(optionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = OptionBuilder.create('A');
        expectedOption.setArgs(Option.ONE_VALUE);
        Assert.assertEquals(expectedOption.getArgs(), Option.ONE_VALUE);
        Assert.assertNull(optionBuilder.getArgs());
    }

    @Test
    public void testHasArgWithValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg("argName");
        Option expectedOption = OptionBuilder.create('A');
        expectedOption.setArgName("argName");
        expectedOption.setArgs(Option.ONE_VALUE);
        Assert.assertEquals(expectedOption.getArgName(), "argName");
        Assert.assertEquals(expectedOption.getArgs(), Option.ONE_VALUE);
        Assert.assertNull(optionBuilder.getArgName());
        Assert.assertNull(optionBuilder.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasArgWithoutReset()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        Option expectedOption = OptionBuilder.create('B');
        expectedOption.setArgs(Option.UNLIMITED_VALUES);
        Option actualOption = OptionBuilder.create('B');
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
        Assert.assertNull(optionBuilder.getArgs());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = OptionBuilder.create('R');
        expectedOption.setRequired(true);
        Assert.assertTrue(expectedOption.isRequired());
        Assert.assertFalse(optionBuilder.isRequired());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = OptionBuilder.create('O');
        expectedOption.setOptionalArg(true);
        expectedOption.setArgs(Option.UNINITIALIZED);
        Assert.assertTrue(expectedOption.isOptionalArg());
        Assert.assertNull(optionBuilder.isOptionalArg());
        Assert.assertNull(optionBuilder.getArgs());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = OptionBuilder.create('H');
        expectedOption.setArgs(Option.UNLIMITED_VALUES);
        Assert.assertEquals(expectedOption.getArgs(), Option.UNLIMITED_VALUES);
        Assert.assertNull(optionBuilder.getArgs());
    }

    @Test
    public void testHasArgsWithValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = OptionBuilder.create('H');
        expectedOption.setArgs(2);
        Assert.assertEquals(expectedOption.getArgs(), 2);
        Assert.assertNull(optionBuilder.getArgs());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = OptionBuilder.create('S');
        expectedOption.setType(String.class);
        Assert.assertEquals(expectedOption.getType(), String.class);
        Assert.assertNull(optionBuilder.getType());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option expectedOption = OptionBuilder.create('D');
        expectedOption.setDescription("description");
        Assert.assertEquals(expectedOption.getDescription(), "description");
        Assert.assertNull(optionBuilder.getDescription());
    }

    @Test
    public void testCreateWithValidOptions() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptValue")
                .hasArg("argName").isRequired()
                .hasOptionalArg().hasArgs(2).withType(Integer.class)
                .withDescription("description");
        Option expectedOption = OptionBuilder.create('C');
        expectedOption.setLongOpt("longOptValue");
        expectedOption.setArgName("argName");
        expectedOption.setRequired(true);
        expectedOption.setOptionalArg(true);
        expectedOption.setArgs(2);
        expectedOption.setType(Integer.class);
        expectedOption.setDescription("description");

        Option actualOption = optionBuilder.create('C');

        Assert.assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        Assert.assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        Assert.assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        Assert.assertEquals(expectedOption.isOptionalArg(), actualOption.isOptionalArg());
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
        Assert.assertEquals(expectedOption.getType(), actualOption.getType());
        Assert.assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMissingLongOpt() throws Exception
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg("argName");
        Option expectedOption = OptionBuilder.create('X');
        Option actualOption = optionBuilder.create('X');
        Assert.assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        Assert.assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        Assert.assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        Assert.assertEquals(expectedOption.isOptionalArg(), actualOption.isOptionalArg());
        Assert.assertEquals(expectedOption.getArgs(), actualOption.getArgs());
        Assert.assertEquals(expectedOption.getType(), actualOption.getType());
        Assert.assertEquals(expectedOption.getDescription(), actualOption.getDescription());

        OptionBuilder.reset();

        Assert.assertThrows(IllegalArgumentException.class, () -> optionBuilder.create('X'));
    }
}