package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.cli.*;

/**
 * Test cases for OptionBuilder.
 */
public class OptionBuilderTest
{
    @org.junit.Test
    public void testOptionBuilderWithLongOptHasArgIsRequired()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("myopt")
                                        .hasArg()
                                        .isRequired();
        Option option = ob.create('m');

        // Test with valid arguments
        CommandLine line = new DefaultParser().parse(new String[] {"--myopt", "value"});
        assertEquals("value", option.getValue());

        // Test with no arguments
        try
        {
            CommandLine line2 = new DefaultParser().parse(new String[] {"--myopt"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        // Test with too many arguments
        try
        {
            CommandLine line3 = new DefaultParser().parse(new String[] {"--myopt", "value1", "value2"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @org.junit.Test
    public void testOptionBuilderWithLongOptHasArgIsOptional()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("myopt")
                                        .hasArg()
                                        .isOptional();
        Option option = ob.create('m');

        // Test with no arguments
        CommandLine line = new DefaultParser().parse(new String[] {"--myopt"});
        assertNull(option.getValue());

        // Test with arguments
        CommandLine line2 = new DefaultParser().parse(new String[] {"--myopt", "value"});
        assertEquals("value", option.getValue());

        // Test with multiple arguments
        CommandLine line3 = new DefaultParser().parse(new String[] {"--myopt", "value1", "value2"});
        assertEquals("value1, value2", option.getValues()[0]);
    }

    @org.junit.Test
    public void testOptionBuilderWithLongOptNoArgIsRequired()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("myopt")
                                        .isRequired();
        Option option = ob.create('m');

        // Test with valid argument
        CommandLine line = new DefaultParser().parse(new String[] {"--myopt"});
        assertTrue(option.getValue() == null);

        // Test with no arguments
        try
        {
            CommandLine line2 = new DefaultParser().parse(new String[] {});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @org.junit.Test
    public void testOptionBuilderWithLongOptNoArgIsOptional()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("myopt")
                                        .isOptional();
        Option option = ob.create('m');

        // Test with no arguments
        CommandLine line = new DefaultParser().parse(new String[] {});
        assertNull(option.getValue());

        // Test with arguments
        CommandLine line2 = new DefaultParser().parse(new String[] {"--myopt", "value"});
        assertEquals("value", option.getValue());

        // Test with no argument
        CommandLine line3 = new DefaultParser().parse(new String[] {"--myopt"});
        assertNull(option.getValue());
    }

    @org.junit.Test
    public void testOptionBuilderWithLongOptWithValueSeparator()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("myopt")
                                        .withValueSeparator(':');
        Option option = ob.create('m');

        // Test with one value
        CommandLine line = new DefaultParser().parse(new String[] {"--myopt:value"});
        assertEquals("value", option.getValue());

        // Test with multiple values
        CommandLine line2 = new DefaultParser().parse(new String[] {"--myopt:value1:value2"});
        assertEquals("value1:value2", option.getValue());
    }

    @org.junit.Test
    public void testOptionBuilderWithLongOptWithValueSeparatorAndHasArg()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("myopt")
                                        .withValueSeparator(':')
                                        .hasArg();
        Option option = ob.create('m');
        CommandLine line = new DefaultParser().parse(new String[] {"--myopt:value1:value2"});

        // Test with one value
        try
        {
            CommandLine line2 = new DefaultParser().parse(new String[] {"--myopt:value"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        // Test with multiple values
        assertEquals("value1:value2", option.getValues()[0]);
    }

    @org.junit.Test
    public void testOptionBuilderWithLongOptWithValueSeparatorAndHasOptionalArg()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("myopt")
                                        .withValueSeparator(':')
                                        .hasOptionalArg();
        Option option = ob.create('m');
        CommandLine line = new DefaultParser().parse(new String[] {"--myopt:value1"});

        // Test with no value
        CommandLine line2 = new DefaultParser().parse(new String[] {"--myopt:"});
        assertNull(option.getValue());

        // Test with one value
        assertEquals("value1", option.getValue());

        // Test with multiple values
        CommandLine line3 = new DefaultParser().parse(new String[] {"--myopt:value1:value2"});
        assertEquals("value1, value2", option.getValues()[0]);
    }
}