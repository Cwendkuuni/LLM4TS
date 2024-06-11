package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    /** OptionBuilder instance */
    private static OptionBuilder optionBuilder;

    @BeforeClass
    public static void setup()
    {
        OptionBuilder.reset();
    }

    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)} .
     */
    @Test
    public void testWithLongOpt()
    {
        optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", optionBuilder.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()} .
     */
    @Test
    public void testHasArg()
    {
        optionBuilder = OptionBuilder.hasArg();
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)} .
     */
    @Test
    public void testHasArg_boolean()
    {
        optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasArg());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)} .
     */
    @Test
    public void testWithArgName()
    {
        optionBuilder = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", optionBuilder.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()} .
     */
    @Test
    public void testIsRequired()
    {
        optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()} .
     */
    @Test
    public void testHasArgs()
    {
        optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasOptionalArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)} .
     */
    @Test
    public void testHasArgs_int()
    {
        optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.getNumberOfArgs());
        assertTrue(optionBuilder.isHasOptionalArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()} .
     */
    @Test
    public void testHasOptionalArg()
    {
        optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.isHasOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)} .
     */
    @Test
    public void testWithType()
    {
        optionBuilder = OptionBuilder.withType(String.class);
        assertNotNull(optionBuilder.getType());
    }

    /**
     * Test method for {@link OptionBuilder#create(char)} .
     */
    @Test
    public void testCreate_char()
    {
        optionBuilder = OptionBuilder.create('T');
        Option opt = optionBuilder.getOption();
        assertNotNull(opt);
        assertEquals("T", opt.getOpt());
    }

    /**
     * Test method for {@link OptionBuilder#create()} .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate()
    {
        optionBuilder = OptionBuilder.create();
    }

    /**
     * Test method for {@link OptionBuilder#create(String)} .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_String()
    {
        optionBuilder = OptionBuilder.create("T");
    }

    /**
     * Test method for {@link OptionBuilder#reset()} .
     */
    @Test
    public void testReset()
    {
        optionBuilder = OptionBuilder.create('T');
        optionBuilder.reset();
        assertEquals(null, optionBuilder.getLongOpt());
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        assertFalse(optionBuilder.isHasArg());
        assertFalse(optionBuilder.isRequired());
        assertFalse(optionBuilder.isHasOptionalArg());
    }
}