package org.apache.commons.cli;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // Test functionality
        // ...

    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Test functionality
        // ...
    }

    @Test
    public void testHasArg_boolean() {
        OptionBuilder.hasArg(true);
        // Test functionality
        // ...
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        // Test functionality
        // ...
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Test functionality
        // ...
    }

    @Test
    public void testWithValueSeparator_char() {
        OptionBuilder.withValueSeparator(':');
        // Test functionality
        // ...
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // Test functionality
        // ...
    }

    @Test
    public void testIsRequired_boolean() {
        OptionBuilder.isRequired(true);
        // Test functionality
        // ...
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Test functionality
        // ...
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(3);
        // Test functionality
        // ...
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Test functionality
        // ...
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Test functionality
        // ...
    }

    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(2);
        // Test functionality
        // ...
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // Test functionality
        // ...
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        // Test functionality
        // ...
    }

    @Test
    public void testCreate_char() {
        OptionBuilder.create('t');
        // Test functionality
        // ...
    }

    @Test
    public void testCreate() {
        OptionBuilder.create();
        // Test functionality
        // ...
    }

    @Test
    public void testCreate_String() {
        OptionBuilder.create("test");
        // Test functionality
        // ...
    }
}