package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * OptionBuilderTest class to test all methods in OptionBuilder class
 */
public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option(null, "longOpt", false, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option(null, null, true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgWithArgumentName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option expectedOption = new Option("argName", null, true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option(null, null, true, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option(null, null, false, null).setValueSeparator(':');
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option(null, null, Option.UNLIMITED_VALUES, null);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option(null, null, 1, null).optionalArg(true);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = new Option(null, null, Option.UNLIMITED_VALUES, null).optionalArg(true);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgsWithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        Option expectedOption = new Option(null, null, 5, null).optionalArg(true);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = new Option(null, null, 0, String.class);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getTypeClass(), actualOption.getTypeClass());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        Option expectedOption = new Option(null, null, 0, null).setDescription("Option description");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder.reset(); // Reset for test purpose
        Option actualOption = optionBuilder.create();
        assertNull(actualOption);
    }
}