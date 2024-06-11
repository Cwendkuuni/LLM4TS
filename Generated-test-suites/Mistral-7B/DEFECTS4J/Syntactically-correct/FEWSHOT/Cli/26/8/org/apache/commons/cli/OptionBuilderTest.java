package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testCreateNoArg() {
        OptionBuilder builder = OptionBuilder.create();

        Option option = builder.build();

        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.NO_ARGUMENTS, option.getArgs().length);
    }

    @Test
    public void testCreateRequiredArg() {
        OptionBuilder builder = OptionBuilder.hasArg()
                                             .withArgName("filename")
                                             .isRequired();

        Option option = builder.build();

        assertNull(option.getLongOpt());
        assertEquals("filename", option.getArgName());
        assertFalse(option.isRequired(), "Option should not be required by default");
        assertTrue(option.isRequired());
        assertEquals(Option.REQUIRED_ARGUMENT, option.getArgs().length);
    }

    @Test
    public void testCreateOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg()
                                             .withArgName("filename");

        Option option = builder.build();

        assertNull(option.getLongOpt());
        assertEquals("filename", option.getArgName());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.OPTIONAL_ARGUMENT, option.getArgs().length);

        OptionBuilder builderWithValue = builder.withValueSeparator(':');
        Option optionWithValue = builderWithValue.build();

        assertSame(builderWithValue, optionWithValue.getOptionBuilder());
        assertEquals(":", optionWithValue.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("foo")
                                             .hasArg()
                                             .withArgName("filename")
                                             .isRequired();

        Option option = builder.build();

        assertEquals("foo", option.getLongOpt());
        assertEquals("filename", option.getArgName());
        assertFalse(option.isRequired(), "Option should not be required by default");
        assertTrue(option.isRequired());
        assertEquals(Option.REQUIRED_ARGUMENT, option.getArgs().length);
    }

    @Test
    public void testCreateWithType() {
        OptionBuilder builder = OptionBuilder.hasArgs(2)
                                             .withArgName("color")
                                             .withType(Integer.class);

        Option option = builder.build();

        assertEquals(Integer.class, option.getType());
        assertEquals(2, option.getArgs().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("f")
                                             .build();   
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("")
                                             .build();   
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyArgName() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.hasArg()
                                             .build();
    }

    @Test
    public void testCreateWithDescription() {
        OptionBuilder builder = OptionBuilder.hasArg()
                                             .withArgName("filename")
                                             .isRequired()
                                             .withDescription("Read file from disk");

        Option option = builder.build();

        assertEquals("Read file from disk", option.getDescription());
    }

    @Test
    public void testCreateMultipleOptions() {
        OptionBuilder builder = OptionBuilder.hasArgs(2)
                                             .withArgName("color")
                                             .isRequired()
                                             .withDescription("Color for bar chart")
                                             .withLongOpt("c");

        Option option1 = builder.build();

        OptionBuilder builder2 = OptionBuilder.hasArgs(3)
                                              .withArgName("file")
                                              .isRequired()
                                              .withDescription("File to read from")
                                              .withLongOpt("f");

        Option option2 = builder2.build();

        Option[] options = { option1, option2 };

        Option[] expected = { new Option("c", "Color for bar chart", true, "color"),
                              new Option("f", "File to read from", true, "file") };

        assertArrayEquals(expected, options);
    }
}