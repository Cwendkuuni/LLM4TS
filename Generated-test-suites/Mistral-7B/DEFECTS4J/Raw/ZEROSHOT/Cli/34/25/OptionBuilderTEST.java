package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        optionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create('T');
        assertEquals("testLongOpt", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg();
        Option option = optionBuilder.create('A');
        assertEquals(1, option.getArgs().length);
        assertFalse(option.isUnbounded());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg(true);
        Option option = optionBuilder.create('B');
        assertEquals(1, option.getArgs().length);
        assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withArgName("testArgName");
        Option option = optionBuilder.create('C');
        assertEquals("testArgName", option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired();
        Option option = optionBuilder.create('D');
        assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('E');
        assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withValueSeparator();
        Option option = optionBuilder.create('F');
        assertEquals('=', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired(false);
        Option option = optionBuilder.create('G');
        assertFalse(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs();
        Option option = optionBuilder.create('H');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertTrue(option.isUnbounded());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs(2);
        Option option = optionBuilder.create('I');
        assertEquals(2, option.getArgs().length);
        assertFalse(option.isUnbounded());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('J');
        assertEquals(1, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create('K');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create('L');
        assertEquals(3, option.getArgs().length);
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withType(String.class);
        Option option = optionBuilder.create('M');
        assertSame(String.class, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withDescription("Test description");
        Option option = optionBuilder.create('N');
        assertNotNull(option.getDescription());
        assertEquals("Test description", option.getDescription());
    }
}