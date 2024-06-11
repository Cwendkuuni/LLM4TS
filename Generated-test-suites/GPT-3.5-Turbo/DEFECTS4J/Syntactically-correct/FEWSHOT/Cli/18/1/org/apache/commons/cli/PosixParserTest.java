package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class PosixParserTest {

    private static Options options;
    private static PosixParser parser;

    @BeforeClass
    public static void setUp() {
        options = new Options();
        options.addOption("f", false, "Test option f");
        options.addOption("foo", true, "Test option foo");
        options.addOption("bar", false, "Test option bar");

        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = { "--foo", "value1", "-bar", "value2", "nonOption" };
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-bar", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        String[] arguments = { "--foo", "value1", "-bar", "value2", "nonOption" };
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(6, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-bar", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("--", result[4]);
        assertEquals("nonOption", result[5]);
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        parser.burstToken(token, true);

        String[] result = (String[]) Util.getFieldValue(parser, "tokens");

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("--", result[3]);
    }

    @Test
    public void testBurstTokenWithOptionArg() {
        String token = "-foovalue";
        parser.burstToken(token, true);

        String[] result = (String[]) Util.getFieldValue(parser, "tokens");

        assertEquals(2, result.length);
        assertEquals("-foo", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testBurstTokenWithNonOption() {
        String token = "-abcnonOption";
        parser.burstToken(token, true);

        String[] result = (String[]) Util.getFieldValue(parser, "tokens");

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("--", result[3]);
        assertEquals("nonOption", result[4]);
    }

    @Test
    public void testBurstTokenWithoutStopAtNonOption() {
        String token = "-abcnonOption";
        parser.burstToken(token, false);

        String[] result = (String[]) Util.getFieldValue(parser, "tokens");

        assertEquals(1, result.length);
        assertEquals("-abcnonOption", result[0]);
    }

    @Test
    public void testProcessOptionToken() {
        String token = "-f";
        parser.processOptionToken(token, true);

        Option currentOption = (Option) Util.getFieldValue(parser, "currentOption");

        assertEquals("-f", currentOption.getOpt());
        assertEquals("-f", ((String[]) Util.getFieldValue(parser, "tokens"))[0]);
    }

    @Test
    public void testProcessOptionTokenWithNonOption() {
        String token = "-nonOption";
        parser.processOptionToken(token, true);

        Option currentOption = (Option) Util.getFieldValue(parser, "currentOption");

        assertNull(currentOption);
    }

    @Test
    public void testProcessOptionTokenWithoutStopAtNonOption() {
        String token = "-nonOption";
        parser.processOptionToken(token, false);

        Option currentOption = (Option) Util.getFieldValue(parser, "currentOption");

        assertNull(currentOption);
    }

    @Test
    public void testProcess() {
        String value = "nonOption";
        parser.process(value);

        String[] result = (String[]) Util.getFieldValue(parser, "tokens");

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("nonOption", result[1]);
        assertEquals(true, Util.getFieldValue(parser, "eatTheRest"));
    }

    @Test
    public void testProcessWithOptionArg() {
        String value = "value";
        Option option = new Option("foo", true, "Test option foo");
        option.setArgName("value");
        options.addOption(option);

        Util.setFieldValue(parser, "currentOption", option);

        parser.process(value);

        String[] result = (String[]) Util.getFieldValue(parser, "tokens");

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
        assertNull(Util.getFieldValue(parser, "currentOption"));
    }

    @Test
    public void testProcessWithoutOptionArg() {
        String value = "nonOption";
        Util.setFieldValue(parser, "currentOption", new Option("bar", false, "Test option bar"));

        parser.process(value);

        String[] result = (String[]) Util.getFieldValue(parser, "tokens");

        assertEquals(4, result.length);
        assertEquals("--", result[0]);
        assertEquals("nonOption", result[1]);
        assertEquals(false, Util.getFieldValue(parser, "eatTheRest"));
    }

    @Test
    public void testProcessSingleHyphen() {
        String hyphen = "-";
        parser.processSingleHyphen(hyphen);

        String[] result = (String[]) Util.getFieldValue(parser, "tokens");

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
}