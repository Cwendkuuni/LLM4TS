package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * OptionBuilderTest class.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("input-file")
                                      .create();
        assertEquals("input-file", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                      .create();
        assertEquals(Option.VALUE_OPTIONAL, option.getArgs().numberOfValues());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        Option option = OptionBuilder.hasArg(false)
                                      .create();
        assertEquals(Option.NO_ARGUMENTS, option.getArgs());

        Option optionWithArg = OptionBuilder.hasArg(true)
                                            .create();
        assertEquals(Option.VALUE_REQUIRED, optionWithArg.getArgs());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        Option option = OptionBuilder.withArgName("file")
                                      .create();
        assertEquals("file", option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                                      .create();
        assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                                      .create();
        assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        Option option = OptionBuilder.withValueSeparator()
                                      .create();
        assertEquals('=', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        Option option = OptionBuilder.isRequired(false)
                                      .create();
        assertFalse(option.isRequired());

        Option optionRequired = OptionBuilder.isRequired(true)
                                            .create();
        assertTrue(optionRequired.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                                      .create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        Option option = OptionBuilder.hasArgs(3)
                                      .create();
        assertEquals(3, option.getArgs().numberOfValues());

        try {
            Option optionWithInvalidNumberOfArgs = OptionBuilder.hasArgs(-1)
                                                            .create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                      .create();
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs()
                                      .create();
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        Option option = OptionBuilder.hasOptionalArgs(2)
                                      .create();
        assertTrue(option.isOptional());
        assertEquals(2, option.getArgs().numberOfValues());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        Option option = OptionBuilder.withType(Boolean.TYPE)
                                      .create();
        assertEquals(Boolean.TYPE, option.getType());
    }

    /**
     * Test method for {@link Option#create(char)}.
     */
    @Test
    public void testCreateChar()
    {
        Option option = OptionBuilder.create('i');
        assertEquals('i', option.getOpt());
    }

    /**
     * Test method for {@link Option#create(String)}.
     */
    @Test
    public void testCreateString()
    {
        Option option = OptionBuilder.create("input-file");
        assertEquals("input-file", option.getLongOpt());
        assertEquals("input-file", option.getOpt());
    }

    /**
     * Test method for {@link Option#getDescription()}.
     */
    @Test
    public void testGetDescription()
    {
        Option option = OptionBuilder.withDescription("Read input file")
                                      .create();
        assertEquals("Read input file", option.getDescription());
    }
}