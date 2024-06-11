package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParsingCompletionException;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    

    @Test
    public void testFlatten_EmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        String[] expectedTokens = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_SingleArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        Options options = new Options();
        String[] expectedTokens = {"arg1"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        Options options = new Options();
        String[] expectedTokens = {"--"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        Options options = new Options();
        String[] expectedTokens = {"-"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_TwoCharOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] expectedTokens = {"-a"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_TwoCharOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] expectedTokens = {"-a", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_MultiCharOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        String[] expectedTokens = {"--longOption"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_MultiCharOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        String[] expectedTokens = {"--longOption", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_TwoOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--longOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOption", true, "Long Option");
        String[] expectedTokens = {"-a", "--longOption"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_OptionWithArgAndRest() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value", "rest"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] expectedTokens = {"-a", "value", "--", "rest"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] expectedTokens = {"arg1", "-a"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithRest() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "arg2", "rest"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] expectedTokens = {"arg1", "-a", "rest"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-x"};
        Options options = new Options();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_InvalidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-x", "arg1"};
        Options options = new Options();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, true));
    }

    @Test
    public void testFlatten_MultipleInvalidOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-x", "-y", "arg1"};
        Options options = new Options();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_MultipleInvalidOptionsWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-x", "-y", "arg1"};
        Options options = new Options();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, true));
    }

    @Test
    public void testFlatten_UnknownLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknownOption"};
        Options options = new Options();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_UnknownLongOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknownOption", "arg1"};
        Options options = new Options();
        assertThrows(ParseException.class, () -> parser.flatten(options, args, true));
    }

    @Test
    public void testFlatten_UnknownLongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknownOption=value"};
        Options options = new Options();
        assertThrows(ParsingCompletionException.class, () -> parser.flatten(options, args, false));
    }

    @Test
    public void testFlatten_UnknownLongOptionWithValueAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--unknownOption=value", "arg1"};
        Options options = new Options();
        assertThrows(ParsingCompletionException.class, () -> parser.flatten(options, args, true));
    }

    @Test
    public void testFlatten_OptionWithArgAndRestWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value", "rest"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] expectedTokens = {"-a", "value"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgAndRest() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "-b", "value2", "-c", "value3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        String[] expectedTokens = {"-a", "value1", "--", "-b", "value2", "--", "-c", "value3"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, result);
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgAndRestWithStopAfterOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "-b", "value2", "-c", "value3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        String[] expectedTokens = {"-a", "value1", "--", "-b", "--", "-c", "--", "value3"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expectedTokens, result);
    }

    
}