package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenOptionsOnly() {
        Options options = new Options();
        options.addOption("option1", false, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", "arg1,arg2", "o3");

        String[] arguments = new String[]{"-o1", "--option3=arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o1", "--option3=arg1"}, result);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();
        String[] arguments = new String[]{"--arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--arg1", "arg2", "arg3"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", false, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", "arg1,arg2", "o3");

        String[] arguments = new String[]{"-o1", "--option3=arg1", "arg4"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-o1", "--option3=arg1", "arg4"}, result);
    }

    @Test
    public void testFlattenWithHyphens() {
        Options options = new Options();
        options.addOption("option1", false, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", "arg1,arg2", "o3");

        String[] arguments = new String[]{"--", "-o1", "-o2", "--option3=arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "-o1", "--option3=arg1"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption1", false, "lo1");
        options.addOption("longOption2", true, "lo2");
        options.addOption("longOption3", "arg1,arg2", "lo3");

        String[] arguments = new String[]{"--longOption1", "--longOption2", "--longOption3=arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption1", "--longOption2", "--longOption3=arg1"}, result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        Options options = new Options();
        options.addOption("option", true, "o");

        String token = "-o";
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken(token, false);
        assertArrayEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testBurstTokenSingleLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "lo");

        String token = "--lo";
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken(token, false);
        assertArrayEquals(new String[]{"--lo"}, result);
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        String token = "-o1o2";
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken(token, false);
        assertArrayEquals(new String[]{"-o1", "-o2"}, result);
    }

    @Test
    public void testBurstTokenWithArg() {
        Options options = new Options();
        options.addOption("option", true, "o");

        String token = "-oarg";
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken(token, false);
        assertArrayEquals(new String[]{"-o", "arg"}, result);
    }

    @Test
    public void testBurstTokenWithArgAndLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", true, "lo1");
        options.addOption("longOption2", true, "lo2");

        String token = "--lo1arglo2";
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken(token, false);
        assertArrayEquals(new String[]{"--lo1", "arg", "--lo2"}, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        String token = "-o1o2arg";
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken(token, true);
        assertArrayEquals(new String[]{"-o1o2arg"}, result);

        List tokensList = Arrays.asList(result);
        parser.init();
        tokensList.forEach(token -> parser.process((String) token));

        String[] expectedResult = new String[]{"--", "-o1", "--option2"};
        assertArrayEquals(expectedResult, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
        assertTrue(parser.tokens.isEmpty());

        parser.init();
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = new Options();

        parser.process("--arg1");
        assertArrayEquals(new String[]{"--arg1"}, parser.tokens.toArray(new String[parser.tokens.size()]));

        parser.process("o1");
        Option option1 = new Option("option1", false, "o1");
        parser.options.addOption(option1);
        assertArrayEquals(new String[]{"-o1"}, parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(option1, parser.currentOption);

        parser.process("--option2=arg2");
        assertArrayEquals(new String[]{"--option2=arg2"}, parser.tokens.toArray(new String[parser.tokens.size()]));
        assertNull(parser.currentOption);

        parser.process("--");
        assertArrayEquals(new String[]{"--"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.processSingleHyphen("-");
        assertArrayEquals(new String[]{"-"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.options = new Options();

        parser.processOptionToken("-o1", false);
        assertArrayEquals(new String[]{"-o1"}, parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(parser.options.getOption("o1"), parser.currentOption);

        parser.processOptionToken("--option2", false);
        Option option2 = new Option("option2", false, "o2");
        parser.options.addOption(option2);
        assertArrayEquals(new String[]{"--option2"}, parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(option2, parser.currentOption);
    }
}