package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * OptionBuilderTest class for testing OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test of withLongOpt method, of class OptionBuilder.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        String longOpt = "longOpt";

        optionBuilder = optionBuilder.withLongOpt(longOpt);

        assertEquals(longOpt, OptionBuilder.longopt);
        assertSame(optionBuilder, expectedOptionBuilder);
    }

    /**
     * Test of hasArg method, of class OptionBuilder.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean expectedResult = true;

        optionBuilder = optionBuilder.hasArg();

        assertEquals(Option.UNINITIALIZED + 1, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertTrue(expectedResult);
    }

    /**
     * Test of hasArg method with boolean argument, of class OptionBuilder.
     */
    @Test
    public void testHasArgWithBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean hasArg = true;
        boolean expectedResult = true;

        optionBuilder = optionBuilder.hasArg(hasArg);

        assertEquals(Option.UNINITIALIZED + hasArg, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
        assertTrue(expectedResult);
    }

    /**
     * Test of withArgName method, of class OptionBuilder.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String argName = "argName";

        optionBuilder = optionBuilder.withArgName(argName);

        assertEquals(argName, OptionBuilder.argName);
        assertSame(optionBuilder, OptionBuilder.getInstance());
    }

    /**
     * Test of isRequired method, of class OptionBuilder.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean expectedResult = true;

        optionBuilder = optionBuilder.isRequired();

        assertTrue(expectedResult);
        assertTrue(OptionBuilder.required);
    }

    /**
     * Test of withValueSeparator method, of class OptionBuilder.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        char valueSeparator = ':';

        optionBuilder = optionBuilder.withValueSeparator(valueSeparator);

        assertEquals(valueSeparator, OptionBuilder.valuesep);
        assertSame(optionBuilder, OptionBuilder.getInstance());
    }

    /**
     * Test of withValueSeparator method without argument, of class OptionBuilder.
     */
    @Test
    public void testWithValueSeparatorWithoutArgument()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder = optionBuilder.withValueSeparator();

        assertEquals('=', OptionBuilder.valuesep);
        assertSame(optionBuilder, OptionBuilder.getInstance());
    }

    /**
     * Test of isRequired method with boolean argument, of class OptionBuilder.
     */
    @Test
    public void testIsRequiredWithBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean required = true;
        boolean expectedResult = true;

        optionBuilder = optionBuilder.isRequired(required);

        assertTrue(expectedResult);
        assertTrue(OptionBuilder.required);
    }

    /**
     * Test of hasArgs method, of class OptionBuilder.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numArgs = Option.UNLIMITED_VALUES;

        optionBuilder = optionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    /**
     * Test of hasArgs method with int argument, of class OptionBuilder.
     */
    @Test
    public void testHasArgsWithInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numArgs = 2;
        int expectedNumberOfArgs = 2;

        optionBuilder = optionBuilder.hasArgs(numArgs);

        assertEquals(expectedNumberOfArgs, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    /**
     * Test of hasOptionalArg method, of class OptionBuilder.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean expectedResult = true;

        optionBuilder = optionBuilder.hasOptionalArg();

        assertTrue(expectedResult);
        assertTrue(OptionBuilder.optionalArg);
    }

    /**
     * Test of hasOptionalArgs method, of class OptionBuilder.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numArgs = 2;
        boolean expectedResult = true;

        optionBuilder = optionBuilder.hasOptionalArgs(numArgs);

        assertTrue(expectedResult);
        assertEquals(numArgs, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    /**
     * Test of withType method, of class OptionBuilder.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String className = String.class.getName();
        Object expectedType = new String[1];

        optionBuilder = optionBuilder.withType(className);

        assertEquals(className, OptionBuilder.type.getClass().getName());
        assertArrayEquals(expectedType, (Object[]) OptionBuilder.type);
    }

    /**
     * Test of withDescription method, of class OptionBuilder.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String description = "Test Description";

        optionBuilder = optionBuilder.withDescription(description);

        assertEquals(description, OptionBuilder.description);
        assertSame(optionBuilder, OptionBuilder.getInstance());
    }

    /**
     * Test of create method, of class OptionBuilder, with no arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        Option option = optionBuilder.create();
    }

    /**
     * Test of create method, of class OptionBuilder, with valid arguments.
     */
    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String longOpt = "longOpt";
        String description = "Test Description";
        String argName = "argName";
        boolean required = false;
        int numberOfArgs = 1;
        char valueSeparator = ':';
        boolean optionalArg = false;
        Object type = Integer.class;

        optionBuilder = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .withArgName(argName)
                .isRequired()
                .withValueSeparator(valueSeparator)
                .hasArg()
                .withType(type);

        Option expectedOption = new Option(longOpt, description, required, numberOfArgs, valueSeparator, argName, type, null);

        Option option = optionBuilder.create();

        assertEquals(expectedOption, option);
        assertEquals(longOpt, option.getLongOpt());
        assertEquals(description, option.getDescription());
        assertEquals(required, option.isRequired());
        assertEquals(numberOfArgs, option.getArgs().length);
        assertEquals(valueSeparator, option.getValueSeparator());
        assertEquals(argName, option.getArgName());
        assertEquals(type.getClass().getName(), option.getType().getClass().getName());
        assertNull(option.getValue());
    }

    /**
     * Test of create method, of class OptionBuilder, with valid arguments and values.
     */
    @Test
    public void testCreateWithValues()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String longOpt = "longOpt";
        String description = "Test Description";
        String argName = "argName";
        boolean required = false;
        int numberOfArgs = 2;
        char valueSeparator = ':';
        boolean optionalArg = false;
        Object type = Integer.class;
        int[] values = {1, 2};

        optionBuilder = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .withArgName(argName)
                .isRequired()
                .withValueSeparator(valueSeparator)
                .hasArgs(numberOfArgs)
                .withType(type);

        Option expectedOption = new Option(longOpt, description, required, numberOfArgs, valueSeparator, argName, type, values);

        Option option = optionBuilder.create(values);

        assertEquals(expectedOption, option);
        assertEquals(longOpt, option.getLongOpt());
        assertEquals(description, option.getDescription());
        assertEquals(required, option.isRequired());
        assertArrayEquals(values, option.getValue());
        assertEquals(numberOfArgs, option.getArgs().length);
        assertEquals(valueSeparator, option.getValueSeparator());
        assertEquals(argName, option.getArgName());
        assertEquals(type.getClass().getName(), option.getType().getClass().getName());
    }
}