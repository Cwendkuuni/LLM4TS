package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        // Assert statement
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Assert statement
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // Assert statement
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        // Assert statement
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Assert statement
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // Assert statement
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // Assert statement
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        // Assert statement
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Assert statement
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder.hasArgs(2);
        // Assert statement
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Assert statement
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Assert statement
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder.hasOptionalArgs(3);
        // Assert statement
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // Assert statement
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        // Assert statement
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('o');
        // Assert statement
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        // Assert statement
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("opt");
        // Assert statement
    }
}