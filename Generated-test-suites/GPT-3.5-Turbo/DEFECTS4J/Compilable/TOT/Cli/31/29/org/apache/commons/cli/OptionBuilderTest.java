package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOption");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArg");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test option");
        assertNotNull(optionBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharThrowsIllegalArgumentException() {
        OptionBuilder.create('t');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOptThrowsIllegalArgumentException() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create("t");
        assertNotNull(option);
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOption");
        assertNotNull(option);
    }
}

