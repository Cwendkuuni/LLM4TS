package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.apache.commons.cli.test.OptionBuilderTest;

public class OptionBuilderTest
{
    @org.junit.Test
    public void testOptionBuilder()
    {
        // Test withLongOpt method
        OptionBuilder optBuilder = OptionBuilderTest.withLongOpt("optionLong");
        assertEquals("optionLong", optBuilder.getLongOpt());

        // Test hasArg method
        OptionBuilder hasArgOptBuilder = optBuilder.hasArg();
        assertEquals(1, hasArgOptBuilder.getNumberOfArgs());

        // Test hasOptionalArg method
        OptionBuilder hasOptionalArgOptBuilder = hasArgOptBuilder.hasOptionalArg();
        assertEquals(1, hasOptionalArgOptBuilder.getNumberOfArgs());
        assertTrue(hasOptionalArgOptBuilder.isOptionalArg());

        // Test withValueSeparator method
        OptionBuilder valueSeparatorOptBuilder = hasOptionalArgOptBuilder.withValueSeparator(':');
        assertEquals(':', valueSeparatorOptBuilder.getValueSeparator());

        // Test isRequired method
        OptionBuilder requiredOptBuilder = valueSeparatorOptBuilder.isRequired();
        assertFalse(requiredOptBuilder.isRequired());

        // Test hasArgs method
        OptionBuilder hasArgsOptBuilder = requiredOptBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, hasArgsOptBuilder.getNumberOfArgs());

        // Test hasArgs method with specific number of args
        OptionBuilder hasArgsTwoOptBuilder = requiredOptBuilder.hasArgs(2);
        assertEquals(2, hasArgsTwoOptBuilder.getNumberOfArgs());

        // Test withType method
        OptionBuilder withTypeIntOptBuilder = hasArgsTwoOptBuilder.withType(Integer.class);
        assertEquals(Integer.class, withTypeIntOptBuilder.getType());

        // Test withDescription method
        OptionBuilder withDescriptionOptBuilder = withTypeIntOptBuilder.withDescription("Test Option");
        assertEquals("Test Option", withDescriptionOptBuilder.getDescription());

        // Test create method
        Option option = withDescriptionOptBuilder.create('T');
        assertNull(option.getLongOpt());
        assertEquals('T', option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertFalse(option.isOptionalArg());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
        assertNull(option.getType());

        // Test create method with long opt
        Option longOptOption = withDescriptionOptBuilder.create("testOptionLong");
        assertEquals("testOptionLong", longOptOption.getLongOpt());
        assertEquals('T', longOptOption.getOpt());
        assertEquals("Test Option", longOptOption.getDescription());
        assertEquals(Option.UNINITIALIZED, longOptOption.getNumberOfArgs());
        assertFalse(longOptOption.isRequired());
        assertFalse(longOptOption.isOptionalArg());
        assertNull(longOptOption.getValueSeparator());
        assertNull(longOptOption.getArgName());
        assertNull(longOptOption.getType());

        // Test create method with invalid long opt
        try
        {
            OptionBuilder invalidLongOptBuilder = OptionBuilderTest.withLongOpt(" ");
            Option invalidOption = invalidLongOptBuilder.create(' ');
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            //expected
        }

        // Test create method with uninitialized long opt
        try
        {
            Option uninitializedOption = OptionBuilderTest.create();
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            //expected
        }
    }
}