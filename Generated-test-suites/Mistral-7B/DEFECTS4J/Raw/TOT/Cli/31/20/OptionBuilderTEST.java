package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test for OptionBuilder withLongOpt method.
     */
    @Test
    public void testWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                                      .create();
        assertEquals("testLongOpt", option.getLongOpt());
    }

    /**
     * Test for OptionBuilder hasArg method.
     */
    @Test
    public void testHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                      .create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertEquals(Option.UNINITIALIZED, option.getArgs().length, "Argument length should be uninitialized");

        Option optionWithArg = OptionBuilder.hasArg(true)
                                            .create();
        assertEquals(1, optionWithArg.getArgs().length);
        assertEquals(1, optionWithArg.getArgs().length, "Argument length should be 1");

        Option optionWithArgName = OptionBuilder.hasArg(true)
                                                .withArgName("testArgName")
                                                .create();
        assertEquals("testArgName", optionWithArg.getArgName());
    }

    /**
     * Test for OptionBuilder isRequired method.
     */
    @Test
    public void testIsRequired()
    {
        Option option = OptionBuilder.create();
        assertFalse(option.isRequired());

        Option requiredOption = OptionBuilder.isRequired()
                                             .create();
        assertTrue(requiredOption.isRequired());
    }

    /**
     * Test for OptionBuilder withValueSeparator method.
     */
    @Test
    public void testWithValueSeparator()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getValueSeparator());

        Option valueSepOption = OptionBuilder.withValueSeparator('=')
                                              .create();
        assertEquals('=', valueSepOption.getValueSeparator());

        Option valueSepOptionWithArg = OptionBuilder.withValueSeparator('=')
                                                     .hasArg()
                                                     .create();
        assertEquals('=', valueSepOptionWithArg.getValueSeparator());
    }

    /**
     * Test for OptionBuilder hasArgs method.
     */
    @Test
    public void testHasArgs()
    {
        Option option = OptionBuilder.create();
        assertNotNull(option.getArgs());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);

        Option optionWithThreeArgs = OptionBuilder.hasArgs(3)
                                                   .create();
        assertEquals(3, optionWithThreeArgs.getArgs().length);

        Option optionWithNoArgs = OptionBuilder.create();
        assertEquals(Option.UNINITIALIZED, optionWithNoArgs.getArgs().length);

        Option optionWithUnlimitedArgs = OptionBuilder.hasArgs()
                                                       .create();
        assertNull(optionWithUnlimitedArgs.getArgs());
    }

    /**
     * Test for OptionBuilder hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        Option option = OptionBuilder.create();
        assertFalse(option.hasOptionalArg());

        Option optionalArgOption = OptionBuilder.hasOptionalArg()
                                               .create();
        assertTrue(optionalArgOption.hasOptionalArg());

        Option optionalArgOptionWithArgName = OptionBuilder.hasOptionalArg()
                                                           .withArgName("testArgName")
                                                           .create();
        assertEquals("testArgName", optionalArgOptionWithArgName.getArgName());
    }

    /**
     * Test for OptionBuilder withType method.
     */
    @Test
    public void testWithType()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getType());

        Option stringTypeOption = OptionBuilder.withType(String.class)
                                                .create();
        assertEquals(String.class, stringTypeOption.getType());

        Option intTypeOption = OptionBuilder.withType(Integer.class)
                                             .create();
        assertEquals(Integer.class, intTypeOption.getType());
    }

    /**
     * Test for OptionBuilder withDescription method.
     */
    @Test
    public void testWithDescription()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());

        Option optionWithDescription = OptionBuilder.withDescription("Test option description")
                                                     .create();
        assertEquals("Test option description", optionWithDescription.getDescription());

        Option optionWithLongOpt = OptionBuilder.withDescription("Test option description")
                                                .withLongOpt("testLongOpt")
                                                .create();
        assertEquals("Test option description", optionWithLongOpt.getDescription());
        assertEquals("testLongOpt", optionWithLongOpt.getLongOpt());
    }
}