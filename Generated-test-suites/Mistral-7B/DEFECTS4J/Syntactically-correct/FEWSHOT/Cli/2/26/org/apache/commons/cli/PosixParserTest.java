package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {};

        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithValidArguments() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("b", 1, "Description for option b");
        options.addOption("c", 2, "Description for option c");

        String[] arguments = {"-a", "--b=1", "-c", "--", "value"};

        String[] expected = {"-a", "--b", "1", "--c", "--", "value"};

        String[] result = new PosixParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBursting() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("abc", 3, "Description for option abc");

        String[] arguments = {"-a--abc", "value"};

        String[] expected = {"-a", "--abc", "value"};

        String[] result = new PosixParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstingAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("abc", 3, "Description for option abc");

        String[] arguments = {"-a--abc", "value1", "--", "value2"};

        String[] expected = {"-a", "--abc", "value1", "--", "value2"};

        String[] result = new PosixParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");
        options.addOption("abc", 3, "Description for option abc");

        String token = "-a--abc";

        ArrayList<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-a");
        expectedTokens.add("--abc");

        List<String> resultTokens = new PosixParser().burstToken(token, false);

        assertEquals(expectedTokens, resultTokens);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();

        String hyphen = "-";

        ArrayList<String> expectedTokens = new ArrayList<>();
        expectedTokens.add(hyphen);

        List<String> resultTokens = parser.processSingleHyphen(hyphen);

        assertEquals(expectedTokens, resultTokens);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", true, "Description for option a");

        String token = "-a";

        PosixParser parser = new PosixParser();
        parser.options = options;

        Option expectedOption = options.getOption("a");

        parser.currentOption = null;

        ArrayList<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-a");

        List<String> resultTokens = parser.processOptionToken(token, false);

        assertEquals(expectedTokens, resultTokens);
        assertSame(expectedOption, parser.currentOption);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();

        String value = "value";

        ArrayList<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("--");
        expectedTokens.add(value);

        parser.eatTheRest = true;

        List<String> resultTokens = parser.process(value);

        assertEquals(expectedTokens, resultTokens);
    }
}