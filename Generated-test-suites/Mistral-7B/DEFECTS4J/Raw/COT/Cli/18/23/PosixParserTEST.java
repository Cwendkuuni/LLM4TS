package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    

    @Test
    public void testFlattenEmptyArguments() {
        String[] args = {};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArgument() {
        String[] args = {"arg1"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlattenMultipleArguments() {
        String[] args = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    @Test
    public void testFlattenWithOption() {
        options.addOption("option1", false, "o1");
        String[] args = {"--option1"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        options.addOption("longOption", false, "lo");
        String[] args = {"--longOption"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenWithOptionAndArgument() {
        options.addOption("option1", true, "o1");
        String[] args = {"--option1=value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndArgument() {
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption=value"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        options.addOption("option1", false, "o1");
        options.addOption("option2", false, "o2");
        String[] args = {"--option1", "--option2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);
    }

    @Test
    public void testFlattenWithOptionValueAndArgument() {
        options.addOption("option1", true, "o1");
        String[] args = {"--option1=value1", "arg1"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "value1", "arg1"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        options.addOption("option1", false, "o1");
        String[] args = {"--invalidOption"};
        parser.flatten(options, args, false); // Throws ParseException
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        options.addOption("option1", false, "o1");
        String[] args = {"--option1", "arg1", "nonOption"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--option1", "nonOption"}, result);
    }

    @Test
    public void testBurstTokenEmptyToken() {
        String token = "";
        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(0, tokens.size());
    }

    @Test
    public void testBurstTokenSingleCharacterOption() {
        String token = "-o1";
        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(1, tokens.size());
        Assert.assertEquals("-o1", tokens.get(0));
    }

    @Test
    public void testBurstTokenSingleCharacterOptionWithArgument() {
        String token = "-o1=value";
        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(2, tokens.size());
        Assert.assertEquals("-o1", tokens.get(0));
        Assert.assertEquals("value", tokens.get(1));
    }

    @Test
    public void testBurstTokenLongOption() {
        String token = "--lo";
        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(1, tokens.size());
        Assert.assertEquals("--lo", tokens.get(0));
    }

    @Test
    public void testBurstTokenLongOptionWithArgument() {
        String token = "--lo=value";
        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(2, tokens.size());
        Assert.assertEquals("--lo", tokens.get(0));
        Assert.assertEquals("value", tokens.get(1));
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        String token = "-o1 -o2";
        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(2, tokens.size());
        Assert.assertEquals("-o1", tokens.get(0));
        Assert.assertEquals("-o2", tokens.get(1));
    }

    @Test
    public void testBurstTokenMultipleOptionsWithArguments() {
        String token = "-o1=value1 -o2=value2";
        List<String> tokens = parser.burstToken(token, false);
        Assert.assertEquals(4, tokens.size());
        Assert.assertEquals("-o1", tokens.get(0));
        Assert.assertEquals("value1", tokens.get(1));
        Assert.assertEquals("-o2", tokens.get(2));
        Assert.assertEquals("value2", tokens.get(3));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "-o1 nonOption";
        List<String> tokens = parser.burstToken(token, true);
        Assert.assertEquals(2, tokens.size());
        Assert.assertEquals("-o1", tokens.get(0));
        Assert.assertEquals("nonOption", tokens.get(1));
    }

    
}