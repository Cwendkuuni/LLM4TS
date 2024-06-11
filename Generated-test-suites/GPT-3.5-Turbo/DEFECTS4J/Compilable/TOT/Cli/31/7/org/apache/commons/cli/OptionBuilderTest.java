package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        // Typical use case
        OptionBuilder.withLongOpt("test").create();
        // Edge case
        OptionBuilder.withLongOpt("").create();
        // Error scenario
        try {
            OptionBuilder.withLongOpt(null).create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testHasArg() {
        // Typical use case
        OptionBuilder.hasArg().create();
    }

    @Test
    public void testHasArg_boolean() {
        // Typical use case
        OptionBuilder.hasArg(true).create();
        // Edge case
        OptionBuilder.hasArg(false).create();
    }

    @Test
    public void testWithArgName() {
        // Typical use case
        OptionBuilder.withArgName("test").create();
        // Edge case
        OptionBuilder.withArgName("").create();
        // Error scenario
        try {
            OptionBuilder.withArgName(null).create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testIsRequired() {
        // Typical use case
        OptionBuilder.isRequired().create();
    }

    @Test
    public void testWithValueseparator() {
        // Typical use case
        OptionBuilder.withValueSeparator(':').create();
    }

    @Test
    public void testWithValueseparator_default() {
        // Typical use case
        OptionBuilder.withValueSeparator().create();
    }

    @Test
    public void testIsRequired_boolean() {
        // Typical use case
        OptionBuilder.isRequired(true).create();
        // Edge case
        OptionBuilder.isRequired(false).create();
    }

    @Test
    public void testHasArgs() {
        // Typical use case
        OptionBuilder.hasArgs().create();
    }

    @Test
    public void testHasArgs_int() {
        // Typical use case
        OptionBuilder.hasArgs(2).create();
        // Edge case
        OptionBuilder.hasArgs(0).create();
        // Error scenario
        try {
            OptionBuilder.hasArgs(-1).create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testHasOptionalArg() {
        // Typical use case
        OptionBuilder.hasOptionalArg().create();
    }

    @Test
    public void testHasOptionalArgs() {
        // Typical use case
        OptionBuilder.hasOptionalArgs().create();
    }

    @Test
    public void testHasOptionalArgs_int() {
        // Typical use case
        OptionBuilder.hasOptionalArgs(2).create();
        // Edge case
        OptionBuilder.hasOptionalArgs(0).create();
        // Error scenario
        try {
            OptionBuilder.hasOptionalArgs(-1).create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testWithType() {
        // Typical use case
        OptionBuilder.withType(Object.class).create();
        // Error scenario
        try {
            OptionBuilder.withType(null).create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testWithDescription() {
        // Typical use case
        OptionBuilder.withDescription("test").create();
        // Edge case
        OptionBuilder.withDescription("").create();
        // Error scenario
        try {
            OptionBuilder.withDescription(null).create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    // Additional test cases for create method

    @Test
    public void testCreate_char() {
        // Typical use case
        OptionBuilder.create('t');
    }

    @Test
    public void testCreate() {
        // Typically use case
        OptionBuilder.withLongOpt("test").create();
        // Error scenario
        try {
            OptionBuilder.create();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testCreate_String() {
        // Typical use case
        OptionBuilder.create("test");
        // Error scenario
        try {
            OptionBuilder.create(null);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

}