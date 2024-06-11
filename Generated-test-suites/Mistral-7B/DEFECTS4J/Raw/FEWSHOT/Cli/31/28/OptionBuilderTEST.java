package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myLongOpt");
        Option expectedOption = Option.builder("myLongOpt").build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder().required(false).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getArgs(), Option.UNINITIALIZED);
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder().required(false).args(1).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getArgs(), 1);
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        assertEquals(expectedOption.isOptional(), true);
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option expectedOption = Option.builder().argName("argName").build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getArgName(), actualOption.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder().required(true).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = Option.builder().valueSeparator(':').build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getValueSeparator(), actualOption.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder().args(Option.UNLIMITED_VALUES).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getArgs(), Option.UNLIMITED_VALUES);
    }

    @Test
    public void testOptionBuilderHasArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = Option.builder().args(2).build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getArgs(), 2);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType("java.lang.String.class");
        Option expectedOption = Option.builder().type("java.lang.String").build();
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption.getType(), actualOption.getType());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description for my option");
        Option expectedOption = Option.builder().description("Description for my option").build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }
}