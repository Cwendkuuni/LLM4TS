package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_NoOptions_NoHyphens() {
        String[] arguments = new String[]{"arg1", "arg2"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_NoOptions_SingleHyphen() {
        String[] arguments = new String[]{"-a"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_SingleOption_NoValue() {
        String[] arguments = new String[]{"-a"};
        Options options = new Options().addOption("a", false, "Option A");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_SingleOption_Value() {
        String[] arguments = new String[]{"-a", "value"};
        Options options = new Options().addOption("a", true, "Option A with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] arguments = new String[]{"-a", "-b", "valueB"};
        Options options = new Options()
                .addOption("a", false, "Option A")
                .addOption("b", true, "Option B with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "-b", "valueB"}, result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] arguments = new String[]{"--long-option"};
        Options options = new Options()
                .addOption("longOption", false, "Long option");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] arguments = new String[]{"--long-option=value"};
        Options options = new Options()
                .addOption("longOption", true, "Long option with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = new String[]{"arg1", "arg2", "-n", "arg4"};
        Options options = new Options().addOption("n", false, "Non option");

        String[] result = new GnuParser().flatten(options, arguments, true);

        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_EmptyArgs() {
        String[] arguments = new String[] {};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_DashOnlyArgs() {
        String[] arguments = new String[]{"-", "--"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_DashBeforeLongOption() {
        String[] arguments = new String[]{"--", "-l", "arg"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--", "-l", "arg"}, result);
    }

    @Test
    public void testFlatten_DashAfterLongOption() {
        String[] arguments = new String[]{"--long-option-", "arg"};
        Options options = new Options();

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_DashAfterOption() {
        String[] arguments = new String[]{"-a-", "arg"};
        Options options = new Options().addOption("a", false, "Option A");

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_EmptyOption() {
        String[] arguments = new String[]{"-"};
        Options options = new Options();

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_TwoDashes() {
        String[] arguments = new String[]{"--"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlatten_EmptyStringArray() {
        String[] arguments = new String[] {};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_NullOptions() {
        String[] arguments = new String[]{"arg1", "arg2"};
        Options options = null;

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_NullArguments() {
        String[] arguments = null;
        Options options = new Options();

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_EmptyArgument() {
        String[] arguments = new String[]{"", "arg2"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"", "arg2"}, result);
    }

    @Test
    public void testFlatten_ArgumentContainsNull() {
        String[] arguments = new String[]{"arg1", null, "arg3"};
        Options options = new Options();

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_ArgumentContainsEmptyString() {
        String[] arguments = new String[]{"arg1", "", "arg3"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"arg1", "", "arg3"}, result);
    }

    @Test
    public void testFlatten_ArgumentContainsMultipleTrailingHyphens() {
        String[] arguments = new String[]{"arg1", "--", "--arg"};
        Options options = new Options();

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_ArgumentStartsWithDoubleDash() {
        String[] arguments = new String[]{"--arg1"};
        Options options = new Options();

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_ArgumentStartsWithSingleDash() {
        String[] arguments = new String[]{"-arg1"};
        Options options = new Options();

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_ArgumentStartsWithDoubleDashAndSingleDash() {
        String[] arguments = new String[]{"-- -arg1"};
        Options options = new Options();

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOption() {
        String[] arguments = new String[]{"--long-option"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithShortOptionWithValue() {
        String[] arguments = new String[]{"-a=value"};
        Options options = new Options().addOption("a", true, "Option A with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithShortOptionWithoutValue() {
        String[] arguments = new String[]{"-a"};
        Options options = new Options().addOption("a", false, "Option A");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithShortOptionWithValueAndTrailingHyphen() {
        String[] arguments = new String[]{"-a=value-"};
        Options options = new Options().addOption("a", true, "Option A with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithShortOptionWithValueAndLeadingHyphen() {
        String[] arguments = new String[]{"-a=-value"};
        Options options = new Options().addOption("a", true, "Option A with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValue() {
        String[] arguments = new String[]{"--long-option=value"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValueAndTrailingHyphen() {
        String[] arguments = new String[]{"--long-option=value-"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValueAndLeadingHyphen() {
        String[] arguments = new String[]{"--long-option=-value"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValueAndMultipleHyphens() {
        String[] arguments = new String[]{"--long-option=--value--"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "--value--"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValueAndSingleDashBeforeValue() {
        String[] arguments = new String[]{"--long-option=-value"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValueAndSingleDashAfterValue() {
        String[] arguments = new String[]{"--long-option=value-"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        String[] result = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValueAndDoubleDashBeforeValue() {
        String[] arguments = new String[]{"----long-option=value"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValueAndDoubleDashAfterValue() {
        String[] arguments = new String[]{"--long-option=value--"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_ArgumentStartsWithLongOptionWithValueAndDoubleDashBeforeAndAfterValue() {
        String[] arguments = new String[]{"----long-option=--value--"};
        Options options = new Options().addOption("longOption", true, "Long option with value");

        try {
            new GnuParser().flatten(options, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}