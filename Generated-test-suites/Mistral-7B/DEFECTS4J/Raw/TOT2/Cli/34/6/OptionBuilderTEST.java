import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName");
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder builder = OptionBuilder.hasArg();
        Option result = builder.create();
        assertEquals(Option.VALUE_REQUIRED, result.getArgs());
    }

    @Test
    public void testHasArgWithName()
    {
        OptionBuilder builder = OptionBuilder.hasArg().withArgName("argName");
        Option result = builder.create();
        assertEquals("argName", result.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder builder = OptionBuilder.isRequired();
        Option result = builder.create();
        assertTrue(result.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        Option result = builder.create();
        assertEquals(':', result.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        Option result = builder.create();
        assertEquals('=', result.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder builder = new OptionBuilder();
        builder.create();
    }

    @Test
    public void testCreateWithLongOptAndArg()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName").hasArg();
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
        assertEquals(Option.VALUE_REQUIRED, result.getArgs());
    }

    @Test
    public void testCreateWithLongOptAndOptionalArg()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName").hasOptionalArg();
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
        assertFalse(result.isRequired());
    }

    @Test
    public void testCreateWithLongOptAndType()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName").withType(String.class);
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
        assertEquals(String.class, result.getType());
    }

    @Test
    public void testCreateWithLongOptAndDescription()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName").withDescription("option description");
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
        assertEquals("option description", result.getDescription());
    }

    @Test
    public void testCreateWithLongOptAndArgName()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName").withArgName("argName");
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
        assertEquals("argName", result.getArgName());
    }

    @Test
    public void testCreateWithLongOptAndOptionalArgs()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName").hasOptionalArgs(3);
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
        assertEquals(Option.UNLIMITED_VALUES, result.getArgs());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndDescription()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription("option description")
                .withType(String[].class);
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
        assertEquals("option description", result.getDescription());
        assertEquals(String[].class, result.getType());
    }

    @Test
    public void testCreateWithLongOptAndTypeAndDescriptionAndArgName()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("optionLongName")
                .withDescription("option description")
                .withType(String[].class)
                .withArgName("argName");
        Option result = builder.create();
        assertEquals("optionLongName", result.getLongOpt());
        assertEquals("option description", result.getDescription());
        assertEquals(String[].class, result.getType());
        assertEquals("argName", result.getArgName());
    }
}