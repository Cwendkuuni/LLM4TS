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
        expectedOptionBuilder.withLongOpt("testLongOpt");

        OptionBuilder actualOptionBuilder = optionBuilder.withLongOpt("testLongOpt");

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
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

        OptionBuilder actualOptionBuilder = optionBuilder.hasArg();

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNINITIALIZED, actualOptionBuilder.numberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArg_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArg(true);

        OptionBuilder actualOptionBuilder = optionBuilder.hasArg(true);

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(1, actualOptionBuilder.numberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withArgName("testArgName");

        OptionBuilder actualOptionBuilder = optionBuilder.withArgName("testArgName");

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals("testArgName", actualOptionBuilder.argName());
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

        OptionBuilder actualOptionBuilder = optionBuilder.isRequired();

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertTrue(actualOptionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator_char()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withValueSeparator(';');

        OptionBuilder actualOptionBuilder = optionBuilder.withValueSeparator(';');

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(';', actualOptionBuilder.valueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withValueSeparator();

        OptionBuilder actualOptionBuilder = optionBuilder.withValueSeparator();

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals('=', actualOptionBuilder.valueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequired_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.isRequired(true);

        OptionBuilder actualOptionBuilder = optionBuilder.isRequired(true);

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertTrue(actualOptionBuilder.isRequired());
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

        OptionBuilder actualOptionBuilder = optionBuilder.hasArgs();

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, actualOptionBuilder.numberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArgs(2);

        OptionBuilder actualOptionBuilder = optionBuilder.hasArgs(2);

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(2, actualOptionBuilder.numberOfArgs());
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

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArg();

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertTrue(actualOptionBuilder.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, actualOptionBuilder.numberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasOptionalArgs();

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArgs();

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, actualOptionBuilder.numberOfArgs());
        assertTrue(actualOptionBuilder.isOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasOptionalArgs(3);

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArgs(3);

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(3, actualOptionBuilder.numberOfArgs());
        assertTrue(actualOptionBuilder.isOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType_Class()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withType(Integer.class);

        OptionBuilder actualOptionBuilder = optionBuilder.withType(Integer.class);

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertSame(Integer.class, actualOptionBuilder.type());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withDescription("Test Description");

        OptionBuilder actualOptionBuilder = optionBuilder.withDescription("Test Description");

        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals("Test Description", actualOptionBuilder.description());
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option", true, "test");

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create();
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreate_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option", true, "test");

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create("testOption");
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_char()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.create('a');
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String)}.
     */
    @Test
    public void testCreate_String_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option 1", false, null);

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create("testOption", "Test Option 2");
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean)}.
     */
    @Test
    public void testCreate_String_String_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option 1", true, null);

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create("testOption", "Test Option 1", true);
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean, int)}.
     */
    @Test
    public void testCreate_String_String_boolean_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option 1", true, 1);

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create("testOption", "Test Option 1", true, 1);
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean, int, char)}.
     */
    @Test
    public void testCreate_String_String_boolean_int_char()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option 1", true, 1, 'a');

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create("testOption", "Test Option 1", true, 1, 'a');
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean, int, char, Class)}.
     */
    @Test
    public void testCreate_String_String_boolean_int_char_Class()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option 1", true, 1, 'a', Integer.class);

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create("testOption", "Test Option 1", true, 1, 'a', Integer.class);
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean, int, Class)}.
     */
    @Test
    public void testCreate_String_String_boolean_int_Class()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option 1", true, 1, null);

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create("testOption", "Test Option 1", true, 1, Integer.class);
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean, Class)}.
     */
    @Test
    public void testCreate_String_String_boolean_Class()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option expectedOption = new Option("testOption", "Test Option 1", true, null);

        Option actualOption = null;
        try
        {
            actualOption = optionBuilder.create("testOption", "Test Option 1", true, Integer.class);
        }
        finally
        {
            OptionBuilder.reset();
        }

        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_String_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.create("testOption", "Test Option 1");
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_String()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.create("testOption");
    }
}