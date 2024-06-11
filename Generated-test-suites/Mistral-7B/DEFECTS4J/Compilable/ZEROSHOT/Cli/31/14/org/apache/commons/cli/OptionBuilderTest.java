package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .create();
        assertEquals("longOption", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        Option option = OptionBuilder.hasArg()
                .create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.hasArg());
    }

    @Test(expected = IllegalStateException.class)
    public void testOptionBuilderHasArgWithoutHasArg()
    {
        Option option = OptionBuilder.create();
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("argName");
        try
        {
            OptionBuilder.create();
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalStateException("Should be an ArgumentException, not an IllegalArgumentException", e);
        }
    }

    @Test
    public void testOptionBuilderWithArgName()
    {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                .create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(',')
                .create();
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                .create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArgsWithNumber()
    {
        Option option = OptionBuilder.hasArgs(2)
                .create();
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs()
                .create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderHasOptionalArgsWithNumber()
    {
        Option option = OptionBuilder.hasOptionalArgs(3)
                .create();
        assertEquals(3, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withType(Integer.class)
                .create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withDescription("Description")
                .create();
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testOptionBuilderCreate()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
    }
}