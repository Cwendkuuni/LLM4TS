package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for OptionBuilder class
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionA");
        Option expectedOption = new Option("optionA", "Option A description", true, "A");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder("optionB").hasArg(true).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg_hasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Option expectedOption = Option.builder("optionC").hasArg(true, true).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withInvalidLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt"); // Invalid long opt, should throw exception
        try
        {
            Option option = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }

    @Test
    public void testCreate_withValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = Option.builder("optionD").hasArg(true).valueSeparator(':').build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testCreate_withDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description for optionE");
        Option expectedOption = Option.builder("optionE").longOpt("optionE").hasArg(true).description("Description for optionE").build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testCreate_withType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = Option.builder("optionF").longOpt("optionF").hasArg(true).type(String.class).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_withInvalidType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Object.class); // Invalid type, should throw exception
        try
        {
            Option option = optionBuilder.create();
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected exception
        }
    }
}