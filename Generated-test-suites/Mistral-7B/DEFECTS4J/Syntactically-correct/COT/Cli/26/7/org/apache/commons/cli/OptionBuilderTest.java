package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", newOptionBuilder.getLongOpt());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.hasArg();
        assertEquals(1, newOptionBuilder.getNumberOfArgs());
        assertFalse(newOptionBuilder.isOptionalArg());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.hasArg(true);
        assertEquals(1, newOptionBuilder.getNumberOfArgs());
        assertTrue(newOptionBuilder.isOptionalArg());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.withArgName("testArgName");
        assertEquals("testArgName", newOptionBuilder.getArgName());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.isRequired();
        assertTrue(newOptionBuilder.isRequired());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.withValueSeparator(';');
        assertEquals(';', newOptionBuilder.getValueSeparator());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, newOptionBuilder.getNumberOfArgs());
        assertFalse(newOptionBuilder.isOptionalArg());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.hasArgs(2);
        assertEquals(2, newOptionBuilder.getNumberOfArgs());
        assertFalse(newOptionBuilder.isOptionalArg());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.hasOptionalArg();
        assertEquals(1, newOptionBuilder.getNumberOfArgs());
        assertTrue(newOptionBuilder.isOptionalArg());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, newOptionBuilder.getNumberOfArgs());
        assertTrue(newOptionBuilder.isOptionalArg());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.hasOptionalArgs(3);
        assertEquals(3, newOptionBuilder.getNumberOfArgs());
        assertTrue(newOptionBuilder.isOptionalArg());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.withType(Integer.class);
        assertEquals(Integer.class, newOptionBuilder.getType());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.withDescription("testDescription");
        assertEquals("testDescription", newOptionBuilder.getDescription());
        assertSame(optionBuilder, newOptionBuilder);
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreateChar()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.withLongOpt("testLongOpt")
                                                    .hasArg()
                                                    .withArgName("testArgName")
                                                    .isRequired()
                                                    .withValueSeparator(';')
                                                    .hasArgs(2)
                                                    .hasOptionalArg()
                                                    .withType(Integer.class)
                                                    .withDescription("testDescription");
        Option option = newOptionBuilder.create('t');
        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals("testArgName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(2, option.getNumberOfArgs());
        assertFalse(option.isOptionalArg());
        assertEquals(Integer.class, option.getType());
        assertNull(option.getValue());
        assertEquals(0, option.getArgs().length);
        assertEquals("testDescription", option.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreateString()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.withLongOpt("testLongOpt")
                                                    .hasArg()
                                                    .withArgName("testArgName")
                                                    .isRequired()
                                                    .withValueSeparator(';')
                                                    .hasArgs(2)
                                                    .hasOptionalArg()
                                                    .withType(Integer.class)
                                                    .withDescription("testDescription");
        Option option = newOptionBuilder.create("t");
        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals("testArgName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(2, option.getNumberOfArgs());
        assertFalse(option.isOptionalArg());
        assertEquals(Integer.class, option.getType());
        assertNull(option.getValue());
        assertEquals(0, option.getArgs().length);
        assertEquals("testDescription", option.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create(char, String)}.
     */
    @Test
    public void testCreateCharString()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.withLongOpt("testLongOpt")
                                                    .hasArg()
                                                    .withArgName("testArgName")
                                                    .isRequired()
                                                    .withValueSeparator(';')
                                                    .hasArgs(2)
                                                    .hasOptionalArg()
                                                    .withType(Integer.class)
                                                    .withDescription("testDescription");
        Option option = newOptionBuilder.create('t', "test");
        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals("testArgName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(2, option.getNumberOfArgs());
        assertFalse(option.isOptionalArg());
        assertEquals(Integer.class, option.getType());
        assertNull(option.getValue());
        assertEquals(0, option.getArgs().length);
        assertEquals("testDescription", option.getDescription());
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     */
    @Test
    public void testGetValueInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder newOptionBuilder = optionBuilder.hasArg()
                                                    .isRequired()
                                                    .hasArgs(2);
        Option option = newOptionBuilder.create('t');
        String[] args = {"test1", "test2"};
        CommandLine cmd = new DefaultParser().parse(args);
        option.setValue(cmd);
        String[] values = option.getValue(0);
        assertArrayEquals(new String[]{"test1", "test2"}, values);
    }
}