package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withLongOpt("longOpt");

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withLongOpt("longOpt"));
        String actualLongOpt = getLongOpt(optionBuilder);
        Assert.assertEquals("longOpt", actualLongOpt);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArg();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasArg());
        int actualNumberOfArgs = getNumberOfArgs(optionBuilder);
        Assert.assertEquals(Option.UNINITIALIZED != Option.UNINITIALIZED ? 1 : 0, actualNumberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArg(true);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasArg(true));
        int actualNumberOfArgs = getNumberOfArgs(optionBuilder);
        Assert.assertEquals(1, actualNumberOfArgs);

        OptionBuilder optionBuilder2 = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder2 = OptionBuilder.instance();
        expectedOptionBuilder2.hasArg(false);

        Assert.assertEquals(expectedOptionBuilder2, optionBuilder2.hasArg(false));
        int actualNumberOfArgs2 = getNumberOfArgs(optionBuilder2);
        Assert.assertEquals(Option.UNINITIALIZED, actualNumberOfArgs2);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withArgName("argName");

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withArgName("argName"));
        String actualArgName = getArgName(optionBuilder);
        Assert.assertEquals("argName", actualArgName);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.isRequired();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.isRequired());
        boolean actualRequired = getRequired(optionBuilder);
        Assert.assertTrue(actualRequired);
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withValueSeparator(':');

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withValueSeparator(':'));
        char actualValueSeparator = getValueSeparator(optionBuilder);
        Assert.assertEquals(':', actualValueSeparator);
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withValueSeparator();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withValueSeparator());
        char actualValueSeparator = getValueSeparator(optionBuilder);
        Assert.assertEquals('=', actualValueSeparator);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.isRequired(false);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.isRequired(false));
        boolean actualRequired = getRequired(optionBuilder);
        Assert.assertFalse(actualRequired);

        OptionBuilder optionBuilder2 = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder2 = OptionBuilder.instance();
        expectedOptionBuilder2.isRequired(true);

        Assert.assertEquals(expectedOptionBuilder2, optionBuilder2.isRequired(true));
        boolean actualRequired2 = getRequired(optionBuilder2);
        Assert.assertTrue(actualRequired2);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArgs();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasArgs());
        int actualNumberOfArgs = getNumberOfArgs(optionBuilder);
        Assert.assertEquals(Option.UNLIMITED_VALUES, actualNumberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArgs(2);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasArgs(2));
        int actualNumberOfArgs = getNumberOfArgs(optionBuilder);
        Assert.assertEquals(2, actualNumberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasOptionalArg();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasOptionalArg());
        int actualNumberOfArgs = getNumberOfArgs(optionBuilder);
        Assert.assertEquals(1, actualNumberOfArgs);

        OptionBuilder optionBuilder2 = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder2 = OptionBuilder.instance();
        expectedOptionBuilder2.hasOptionalArgs();

        Assert.assertEquals(expectedOptionBuilder2, optionBuilder2.hasOptionalArgs());
        int actualNumberOfArgs2 = getNumberOfArgs(optionBuilder2);
        Assert.assertEquals(Option.UNLIMITED_VALUES, actualNumberOfArgs2);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Class<?> expectedType = String.class;
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withType(expectedType);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withType(expectedType));
        Class<?> actualType = getType(optionBuilder);
        Assert.assertEquals(expectedType, actualType);
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String expectedDescription = "description";
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withDescription(expectedDescription);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withDescription(expectedDescription));
        String actualDescription = getDescription(optionBuilder);
        Assert.assertEquals(expectedDescription, actualDescription);
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreate() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option('opt', "description");

        Option actualOption = optionBuilder.create('opt').create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidSettings() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs();
        optionBuilder.hasOptionalArg();
        optionBuilder.create();
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("opt");
        optionBuilder.create("opt");
    }

    /**
     * Test method for {@link OptionBuilder#create(char, String)}.
     */
    @Test
    public void testCreateWithValidSettings() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String expectedDescription = "description";
        String expectedArgName = "argName";
        boolean expectedRequired = true;
        int expectedNumberOfArgs = 1;
        char expectedLongOpt = 'opt';
        Class<?> expectedType = String.class;
        char expectedValueSeparator = ':';

        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withLongOpt("opt")
                .withDescription(expectedDescription)
                .withArgName(expectedArgName)
                .isRequired()
                .hasArg()
                .withValueSeparator(':')
                .withType(expectedType);

        Option actualOption = optionBuilder.create('opt').create();
        Assert.assertEquals(expectedOption, actualOption);

        String actualDescription = getDescription(optionBuilder);
        Assert.assertEquals(expectedDescription, actualDescription);
        String actualArgName = getArgName(optionBuilder);
        Assert.assertEquals(expectedArgName, actualArgName);
        boolean actualRequired = getRequired(optionBuilder);
        Assert.assertEquals(expectedRequired, actualRequired);
        int actualNumberOfArgs = getNumberOfArgs(optionBuilder);
        Assert.assertEquals(expectedNumberOfArgs, actualNumberOfArgs);
        char actualLongOpt = getLongOpt(optionBuilder);
        Assert.assertEquals(expectedLongOpt, actualLongOpt);
        Class<?> actualType = getType(optionBuilder);
        Assert.assertEquals(expectedType, actualType);
        char actualValueSeparator = getValueSeparator(optionBuilder);
        Assert.assertEquals(expectedValueSeparator, actualValueSeparator);
    }

    private static String getArgName(OptionBuilder optionBuilder)
    {
        try
        {
            Field argNameField = OptionBuilder.class.getDeclaredField("argName");
            argNameField.setAccessible(true);
            return (String) argNameField.get(optionBuilder);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static int getNumberOfArgs(OptionBuilder optionBuilder)
    {
        try
        {
            Field numberOfArgsField = OptionBuilder.class.getDeclaredField("numberOfArgs");
            numberOfArgsField.setAccessible(true);
            return (int) numberOfArgsField.get(optionBuilder);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String getLongOpt(OptionBuilder optionBuilder)
    {
        try
        {
            Field longOptField = OptionBuilder.class.getDeclaredField("longopt");
            longOptField.setAccessible(true);
            return (String) longOptField.get(optionBuilder);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static char getValueSeparator(OptionBuilder optionBuilder)
    {
        try
        {
            Field valuesepField = OptionBuilder.class.getDeclaredField("valuesep");
            valuesepField.setAccessible(true);
            return (char) valuesepField.get(optionBuilder);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static boolean getRequired(OptionBuilder optionBuilder)
    {
        try
        {
            Field requiredField = OptionBuilder.class.getDeclaredField("required");
            requiredField.setAccessible(true);
            return (boolean) requiredField.get(optionBuilder);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getType(OptionBuilder optionBuilder)
    {
        try
        {
            Field typeField = OptionBuilder.class.getDeclaredField("type");
            typeField.setAccessible(true);
            return (Class<?>) typeField.get(optionBuilder);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}