package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

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
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String longOpt = "longOpt";
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withLongOpt(longOpt);
        assertEquals(expectedOptionBuilder, optionBuilder.withLongOpt(longOpt));
        assertEquals(longOpt, optionBuilder.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasArg();
        assertEquals(expectedOptionBuilder, optionBuilder.hasArg());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArg_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasArg(true);
        assertEquals(expectedOptionBuilder, optionBuilder.hasArg(true));
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasArg());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String argName = "argName";
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withArgName(argName);
        assertEquals(expectedOptionBuilder, optionBuilder.withArgName(argName));
        assertEquals(argName, optionBuilder.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.isRequired();
        assertEquals(expectedOptionBuilder, optionBuilder.isRequired());
        assertTrue(optionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        char valueSeparator = ':';
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withValueSeparator(valueSeparator);
        assertEquals(expectedOptionBuilder, optionBuilder.withValueSeparator(valueSeparator));
        assertEquals(valueSeparator, optionBuilder.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparator_noArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withValueSeparator();
        assertEquals(expectedOptionBuilder, optionBuilder.withValueSeparator());
        assertEquals('=', optionBuilder.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequired_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean required = true;
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.isRequired(required);
        assertEquals(expectedOptionBuilder, optionBuilder.isRequired(required));
        assertTrue(optionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasArgs();
        assertEquals(expectedOptionBuilder, optionBuilder.hasArgs());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasOptionalArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numArgs = 2;
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasArgs(numArgs);
        assertEquals(expectedOptionBuilder, optionBuilder.hasArgs(numArgs));
        assertEquals(numArgs, optionBuilder.getNumberOfArgs());
        assertFalse(optionBuilder.isHasOptionalArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasOptionalArgs();
        assertEquals(expectedOptionBuilder, optionBuilder.hasOptionalArgs());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasOptionalArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numArgs = 2;
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasOptionalArgs(numArgs);
        assertEquals(expectedOptionBuilder, optionBuilder.hasOptionalArgs(numArgs));
        assertEquals(numArgs, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasOptionalArgs());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Object)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Object type = Integer.class;
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withType(type);
        assertEquals(expectedOptionBuilder, optionBuilder.withType(type));
        assertSame(type, optionBuilder.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String description = "description";
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withDescription(description);
        assertEquals(expectedOptionBuilder, optionBuilder.withDescription(description));
        assertEquals(description, optionBuilder.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOptSet()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder.create();
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreate_char()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        char opt = 'opt';
        Option expectedOption = new Option(opt, "");
        Option actualOption = optionBuilder.create(opt);
        assertSame(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOptSet_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String opt = "opt";
        OptionBuilder.create(opt);
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String)}.
     */
    @Test
    public void testCreate_String_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String longOpt = "longOpt";
        String description = "description";
        Option expectedOption = new Option(longOpt, description);
        Option actualOption = optionBuilder.create(longOpt, description);
        assertSame(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#getLongOpt()}.
     */
    @Test
    public void testGetLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String longOpt = "longOpt";
        optionBuilder.withLongOpt(longOpt);
        assertEquals(longOpt, optionBuilder.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#getDescription()}.
     */
    @Test
    public void testGetDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String description = "description";
        optionBuilder.withDescription(description);
        assertEquals(description, optionBuilder.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#getArgName()}.
     */
    @Test
    public void testGetArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String argName = "argName";
        optionBuilder.withArgName(argName);
        assertEquals(argName, optionBuilder.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#getNumberOfArgs()}.
     */
    @Test
    public void testGetNumberOfArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numberOfArgs = Option.UNINITIALIZED;
        optionBuilder.hasArg();
        assertEquals(numberOfArgs, optionBuilder.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#isHasArg()}.
     */
    @Test
    public void testIsHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean hasArg = true;
        optionBuilder.hasArg();
        assertEquals(hasArg, optionBuilder.isHasArg());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean required = true;
        optionBuilder.isRequired();
        assertEquals(required, optionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#getValueSeparator()}.
     */
    @Test
    public void testgetValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        char valueSeparator = ':';
        optionBuilder.withValueSeparator(valueSeparator);
        assertEquals(valueSeparator, optionBuilder.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#getType()}.
     */
    @Test
    public void testGetType()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Object type = Integer.class;
        optionBuilder.withType(type);
        assertEquals(type, optionBuilder.getType());
    }

    /**
     * Test method for {@link OptionBuilder#reset()}.
     */
    @Test
    public void testReset()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder resetOptionBuilder = OptionBuilder.getInstance();
        optionBuilder.withLongOpt("longOpt");
        optionBuilder.isRequired();
        optionBuilder.hasArg();
        Option expectedOption = new Option("longOpt", "");
        expectedOption.setRequired(true);
        expectedOption.setOptionalArg(true);
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
        optionBuilder.reset();
        assertNull(optionBuilder.getLongOpt());
        assertFalse(optionBuilder.isRequired());
        assertFalse(optionBuilder.isHasArg());
    }
}