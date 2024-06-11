package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * OptionBuilderTest.java
 */
public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option expectedOption = new Option("option", "optionLong", false, "Description");
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("arg", true, "Description");
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argumentName");
        Option expectedOption = new Option("argumentName", true, "Description");
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = new Option("arg", false, true, "Description");
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("arg", false, false, "Description", "value");
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("arg", false, Option.UNLIMITED_VALUES, "Description");
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("arg", true, Option.UNINITIALIZED, "Description");
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(5);
        Option expectedOption = new Option("arg", true, 5, "Description");
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option expectedOption = new Option("arg", false, false, "Description", null, Integer.class);
        Option actualOption = optionBuilder.create();
        assertSame(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }

    @Test
    public void testCreateWithNullDescription() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withDescription(null);
        Option actualOption = optionBuilder.create();
        assertNull(actualOption.getDescription());
    }

    @Test
    public void testReset() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("optionLong");
        optionBuilder.hasArg();
        optionBuilder.isRequired();
        Option actualOption = optionBuilder.create();
        Option expectedOption = new Option("arg", false, true, "Description");
        assertSame(expectedOption, actualOption);

        optionBuilder.reset();
        Option actualOptionAfterReset = optionBuilder.create();
        Option expectedOptionAfterReset = new Option("arg", Option.UNINITIALIZED, Option.UNINITIALIZED, null);
        assertSame(expectedOptionAfterReset, actualOptionAfterReset);
    }
}