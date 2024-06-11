package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * OptionBuilderTest.java - tests for OptionBuilder class
 *
 * @author [Your Name]
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option", false, null);

        Option actualOption = optionBuilder.withLongOpt("testLongOpt").create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option", true, null);

        Option actualOption = optionBuilder.hasArg().create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option", false, "argName");

        Option actualOption = optionBuilder.withArgName("argName").create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option", false, null);
        Option actualOption = optionBuilder.create();

        actualOption = optionBuilder.isRequired().create();

        assertTrue(actualOption.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option", true, null);

        Option actualOption = optionBuilder.hasOptionalArg().create();

        assertTrue(actualOption.hasOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option", false, null);
        Option actualOption = optionBuilder.create();

        actualOption = optionBuilder.hasArgs().create();

        assertEquals(Option.UNLIMITED_VALUES, actualOption.getArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option", false, null);
        Option actualOption = optionBuilder.create();

        actualOption = optionBuilder.hasArgs(2).create();

        assertEquals(2, actualOption.getArgs());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option", false, null);
        String expectedType = "java.lang.String.class";

        Option actualOption = optionBuilder.withType(String.class).create();

        assertEquals(expectedOption, actualOption);
        assertEquals(expectedType, actualOption.getType().getName());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option expectedOption = new Option("testOption", "Test Option Description", false, null);

        Option actualOption = optionBuilder.withDescription("Test Option Description").create();

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.create();
    }
}