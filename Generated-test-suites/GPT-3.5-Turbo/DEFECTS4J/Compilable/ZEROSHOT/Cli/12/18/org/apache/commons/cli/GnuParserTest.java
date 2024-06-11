package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_WithValidOptions_ReturnsFlattenedArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "arg1", "-b", "value1"};
        String[] expected = {"-a", "arg1", "-b", "value1"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithDoubleHyphen_AddsDoubleHyphenToTokenList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"arg1", "--", "arg2"};
        String[] expected = {"arg1", "--", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithSingleHyphen_AddsSingleHyphenToTokenList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"arg1", "-"};
        String[] expected = {"arg1", "-"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithOptionsButMissingHyphen_AddsArgumentToTokenList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"arg1"};
        String[] expected = {"arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithOptionsAndFullArgument_AddsArgumentToTokenList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a=value"};
        String[] expected = {"-a=value"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithOptionsAndPartialArgument_AddsArgumentToTokenList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-avalue"};
        String[] expected = {"-avalue"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithNonOption_StopAtNonOptionTrue_DoesNotAddNonOptionToTokenList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a", "arg1", "-b"};
        String[] expected = {"-a", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithNonOption_StopAtNonOptionFalse_AddsNonOptionToTokenList() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a", "arg1", "-b"};
        String[] expected = {"-a", "arg1", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

}