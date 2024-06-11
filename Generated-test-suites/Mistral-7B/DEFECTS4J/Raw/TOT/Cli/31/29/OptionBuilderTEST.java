package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testBasicOption() {
        Options options = new Options();
        OptionBuilder.withLongOpt("myOption")
                .isRequired()
                .create(options);

        assertFalse("Long option 'myOption' should be required", options.isRequired("myOption"));
        try {
            options.parse("-t", new String[]{"test"});
            fail("Expected ParseException for required option 'myOption'");
        } catch (ParseException e) {
            // expected
        }
        try {
            options.parse("-t --myOption", new String[]{"test", "value"});
            Option myOption = options.getOption("myOption");
            assertEquals("value", myOption.getValue());
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testOptionalArgument() {
        Options options = new Options();
        OptionBuilder.withLongOpt("myOption")
                .hasArg()
                .create(options);

        try {
            options.parse("-t test", new String[]{"test"});
            fail("Expected ParseException for missing argument for optional option 'myOption'");
        } catch (ParseException e) {
            // expected
        }

        try {
            options.parse("-t test value", new String[]{"test", "value"});
            Option myOption = options.getOption("myOption");
            assertEquals("value", myOption.getValue());
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try {
            options.parse("-t test", new String[]{"test", "value2"});
            Option myOption = options.getOption("myOption");
            String[] values = myOption.getArgValues();
            assertArrayEquals(new String[]{"value2"}, values);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleArguments() {
        Options options = new Options();
        OptionBuilder.withLongOpt("myOption")
                .hasArgs()
                .create(options);

        try {
            options.parse("-t test arg1 arg2", new String[]{"test", "arg1", "arg2"});
            Option myOption = options.getOption("myOption");
            String[] values = myOption.getArgValues();
            assertArrayEquals(new String[]{"arg1", "arg2"}, values);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try {
            options.parse("-t test arg1", new String[]{"test", "arg1"});
            Option myOption = options.getOption("myOption");
            fail("Expected ParseException for missing argument after optional argument 'myOption'");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testValueSeparator() {
        Options options = new Options();
        OptionBuilder.withLongOpt("myOption")
                .hasArgs()
                .withValueSeparator('=')
                .create(options);

        try {
            options.parse("-t test arg1=value1 arg2=value2", new String[]{"test", "arg1", "value1", "arg2", "value2"});
            Option myOption = options.getOption("myOption");
            Map<String, String> values = myOption.getValuesMap();
            assertEquals("value1", values.get("arg1"));
            assertEquals("value2", values.get("arg2"));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try {
            options.parse("-t test arg1=value1 arg2", new String[]{"test", "arg1", "value1", "arg2"});
            Option myOption = options.getOption("myOption");
            Map<String, String> values = myOption.getValuesMap();
            assertEquals("value1", values.get("arg1"));
            assertNull(values.get("arg2"));
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidOption() {
        Options options = new Options();
        OptionBuilder.withLongOpt("myOption")
                .hasArg()
                .create(options);

        try {
            options.parse("-t --myInvalidOption", new String[]{"test"});
            fail("Expected ParseException for invalid option '--myInvalidOption'");
        } catch (ParseException e) {
            // expected
        }
    }
}