package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_EmptyOptions_ReturnsEmptyArray() {
        String[] arguments = new String[] {"-a", "value1", "--option", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_StopAtNonOptionOption_ReturnsRemainingArguments() {
        options.addOption("a", true, "option a");
        options.addOption("b", false, "option b");
        String[] arguments = new String[] {"-a", "value1", "--option", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("--option", result[1]);
    }

    @Test
    public void testFlatten_HasOptionWithArg_ReturnsOptionAndArg() {
        Option optionA = new Option("a", "option a");
        optionA.setArgs(1);
        options.addOption(optionA);
        String[] arguments = new String[] {"-a", "value1", "--option", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_HasOptionWithArgs_ReturnsOptionAndRemainingArgs() {
        Option optionB = new Option("b", "option b");
        optionB.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(optionB);
        String[] arguments = new String[] {"-a", "value1", "--option", "value2", "-b", "value3", "value4"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("-b", result[2]);
        assertEquals("value3", result[3]);
    }

    @Test
    public void testFlatten_NoOptionMatchWithStopAtNonOption_ReturnsAllArguments() {
        Option optionA = new Option("a", "option a");
        optionA.setArgs(1);
        options.addOption(optionA);
        String[] arguments = new String[] {"-c", "value1", "--option", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(4, result.length);
        assertEquals("-c", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testBurstToken_HasOptionWithArg_ReturnsOptionAndArg() {
        Option optionA = new Option("a", "option a");
        optionA.setArgs(1);
        options.addOption(optionA);
        String token = "-avalue1";
        parser.burstToken(token, false);
        String[] result = parser.flatten(options, new String[0], false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testBurstToken_HasOptionWithArgs_ReturnsOptionAndRemainingArgs() {
        Option optionB = new Option("b", "option b");
        optionB.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(optionB);
        String token = "-bvalue1 value2 value3";
        parser.burstToken(token, false);
        String[] result = parser.flatten(options, new String[0], false);
        assertEquals(4, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testBurstToken_NoOptionMatch_ReturnsToken() {
        Option optionA = new Option("a", "option a");
        optionA.setArgs(1);
        options.addOption(optionA);
        String token = "-cvalue1";
        parser.burstToken(token, false);
        String[] result = parser.flatten(options, new String[0], false);
        assertEquals(1, result.length);
        assertEquals("-cvalue1", result[0]);
    }

}