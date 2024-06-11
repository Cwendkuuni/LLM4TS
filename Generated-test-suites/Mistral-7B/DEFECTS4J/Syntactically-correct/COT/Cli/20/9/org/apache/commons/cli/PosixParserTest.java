package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser class.
 */
public class PosixParserTest {

    

    @Test
    public void testFlattenEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        String[] result = parser.flatten(new Options(), args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        String[] expected = {"arg1"};

        String[] result = parser.flatten(new Options(), args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", false, null, "op1");
        String[] args = {"-o", "op1Value"};
        String[] expected = {"-o", "option1"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", false, null, "lo1");
        String[] args = {"--longOption1"};
        String[] expected = {"--longOption1"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", true, null, "lo1");
        String[] args = {"--longOption1", "value"};
        String[] expected = {"--longOption1", "value"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", false, null, "op1");
        options.addOption("option2", false, null, "op2");
        String[] args = {"-o1", "--option2"};
        String[] expected = {"-o1", "option1"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValueAndOtherArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", true, null, "lo1");
        String[] args = {"--longOption1", "value1", "arg1"};
        String[] expected = {"--longOption1", "value1", "arg1"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArgs() {
        PosixParser parser = new PosixParser();
        String[] args = null;

        parser.flatten(new Options(), args, false);
    }

    @Test
    public void testFlattenStopAtNonOptionEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        String[] result = parser.flatten(new Options(), args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionSingleArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        String[] expected = {"arg1"};

        String[] result = parser.flatten(new Options(), args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionSingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", false, null, "op1");
        String[] args = {"-o", "op1Value"};
        String[] expected = {"-o", "op1Value"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", false, null, "lo1");
        String[] args = {"--longOption1", "--option2"};
        String[] expected = {"--longOption1", "--option2"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionLongOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", true, null, "lo1");
        String[] args = {"--longOption1", "value", "arg1"};
        String[] expected = {"--longOption1", "value", "arg1"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionMultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", false, null, "op1");
        options.addOption("option2", false, null, "op2");
        String[] args = {"-o1", "-o2", "--option3"};
        String[] expected = {"-o1", "-o2", "--option3"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionLongOptionWithValueAndOtherArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", true, null, "lo1");
        String[] args = {"--longOption1", "value1", "--option2", "arg1"};
        String[] expected = {"--longOption1", "value1", "--option2", "arg1"};

        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenSingleLetterOption() {
        PosixParser parser = new PosixParser();
        String token = "-o";

        List<String> result = parser.burstToken(token, false);

        List<String> expected = Arrays.asList("--", "option", "o");

        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOptionWithoutValue() {
        PosixParser parser = new PosixParser();
        String token = "--option1";

        List<String> result = parser.burstToken(token, false);

        List<String> expected = Arrays.asList("--option1");

        assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String token = "--option1=value";

        List<String> result = parser.burstToken(token, false);

        List<String> expected = Arrays.asList("--option1", "value");

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenEmptyToken() {
        PosixParser parser = new PosixParser();
        String token = "";

        parser.burstToken(token, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenNullToken() {
        PosixParser parser = new PosixParser();
        String token = null;

        parser.burstToken(token, false);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();

        parser.init();

        assertEquals(parser.getTokens().size(), 0);
        assertEquals(parser.isEatTheRest(), false);
        assertEquals(parser.getCurrentOption(), null);
    }

    
}