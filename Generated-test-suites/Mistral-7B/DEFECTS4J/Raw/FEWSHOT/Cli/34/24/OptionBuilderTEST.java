package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        Option expectedOption = new Option("longopt", null);
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder()
                .longOpt("longopt")
                .description("Description")
                .required(false)
                .build();
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        assertEquals(expectedOption.getArgs(), Option.VALUE_ONE_ARG);
    }

    @Test
    public void testOptionBuilderHasArgWithName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option expectedOption = Option.builder()
                .longOpt("longopt")
                .description("Description")
                .required(false)
                .argName("argName")
                .build();
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
        assertEquals(expectedOption.getArgName(), actualOption.getArgName());
        assertEquals(expectedOption.getArgs(), Option.VALUE_ONE_ARG);
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder()
                .longOpt("longopt")
                .description("Description")
                .required(true)
                .build();
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder()
                .longOpt("longopt")
                .description("Description")
                .required(false)
                .args(Option.UNLIMITED_VALUES)
                .build();
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        assertEquals(expectedOption.getArgs(), Option.UNLIMITED_VALUES);
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = Option.builder()
                .longOpt("longopt")
                .description("Description")
                .required(false)
                .args(Option.UNLIMITED_VALUES)
                .optionalArg(true)
                .build();
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        assertEquals(expectedOption.getArgs(), Option.UNLIMITED_VALUES);
        assertEquals(expectedOption.optionalArg, actualOption.optionalArg);
    }

    @Test
    public void testOptionBuilderWithType() {
        Class<Integer> integerType = Integer.class;
        OptionBuilder optionBuilder = OptionBuilder.withType(integerType);
        Option expectedOption = Option.builder()
                .longOpt("longopt")
                .description("Description")
                .required(false)
                .type(integerType)
                .build();
        Option actualOption = optionBuilder.create();

        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        assertEquals(expectedOption.getType(), actualOption.getType());
    }
}