package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test creating an option with no arguments.
     */
    @Test
    public void testCreateNoArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOpt")
                                                    .isRequired();
        Option option = optionBuilder.create();
        assertEquals("Test OPTion", option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a single argument.
     */
    @Test
    public void testCreateSingleArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOpt")
                                                    .isRequired()
                                                    .hasArg();
        Option option = optionBuilder.create();
        assertEquals("Test OPTion", option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());

        // set value and test getValue
        String argValue = "argValue";
        CommandLine cmdLine = new DefaultCommandLine();
        cmdLine.parseOption(option, argValue);
        assertEquals(argValue, option.getValue());
    }

    /**
     * Test creating an option with a single optional argument.
     */
    @Test
    public void testCreateOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOpt")
                                                    .isRequired()
                                                    .hasOptionalArg(true);
        Option option = optionBuilder.create();
        assertEquals("Test OPTion", option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());

        // set value and test getValue
        String argValue = "argValue";
        CommandLine cmdLine = new DefaultCommandLine();
        cmdLine.parseOption(option, argValue);
        assertEquals(argValue, option.getValue());
        
        // test no value
        cmdLine = new DefaultCommandLine();
        cmdLine.parseOption(option);
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with multiple arguments.
     */
    @Test
    public void testCreateMultipleArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOpt")
                                                    .isRequired()
                                                    .hasArgs();
        Option option = optionBuilder.create();
        assertEquals("Test OPTion", option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());

        // set values and test getValues
        String[] argValues = {"argValue1", "argValue2"};
        CommandLine cmdLine = new DefaultCommandLine();
        cmdLine.parseOption(option, argValues);
        assertArrayEquals(argValues, option.getValues());
    }

    /**
     * Test creating an option with a value separator.
     */
    @Test
    public void testCreateValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOpt")
                                                    .isRequired()
                                                    .withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals("Test OPTion", option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());

        // set values and test getValues
        String[] argValues = {"argValue1:argValue2"};
        CommandLine cmdLine = new DefaultCommandLine();
        cmdLine.parseOption(option, argValues);
        assertArrayEquals(new String[]{"argValue1", "argValue2"}, option.getValues());
    }

    /**
     * Test creating an option with a type.
     */
    @Test
    public void testCreateType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOpt")
                                                    .isRequired()
                                                    .withType(Integer.TYPE);
        Option option = optionBuilder.create();
        assertEquals("Test OPTion", option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());

        // set value and test getValue
        int argValue = 42;
        CommandLine cmdLine = new DefaultCommandLine();
        cmdLine.parseOption(option, argValue);
        assertEquals(argValue, option.getValue());
    }
}