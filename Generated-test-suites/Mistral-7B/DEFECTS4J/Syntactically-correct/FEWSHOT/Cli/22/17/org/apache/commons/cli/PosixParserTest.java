package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {
    @Test
    public void testFlattenBasicUsage() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] args = {"--a", "-b", "--c=d", "-e", "-f", "--", "g", "h"};
        String[] expected = {"--a", "-b", "--c", "d", "--", "g", "h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionUsage() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] args = {"--a", "-b", "--c=d", "-e", "-f", "--g", "h"};
        String[] expected = {"--a", "-b", "--g", "h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenHandleLongOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] args = {"--option-a", "--option-b=value", "--c=d", "-e", "-f", "--", "g", "h"};
        String[] expected = {"--option-a", "--option-b", "value", "--c", "d", "--", "g", "h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenHandleMissingOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] args = {"--option-x", "--", "g", "h"};
        String[] expected = {"--option-x", "--", "g", "h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenHandleOptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] args = {"--a", "-b", "value", "--", "g", "h"};
        String[] expected = {"--a", "--b", "value", "--", "g", "h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenHandleNoOptions() {
        Options options = new Options();
        String[] args = {"g", "h"};
        String[] expected = {"--", "g", "h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenHandleLongOptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] args = {"--option-b=value", "--", "g", "h"};
        String[] expected = {"--option-b", "value", "--", "g", "h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "--foo";
        parser.processNonOptionToken(token);
        List expected = Arrays.asList("--", "foo");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessNonOptionTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--foo";
        parser.processNonOptionToken(token);
        parser.eatTheRest = true;
        String nextToken = "bar";
        parser.tokens.add(nextToken);
        List expected = Arrays.asList("--", "foo", "bar");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-a";
        parser.processOptionToken(token, false);
        List expected = Arrays.asList("-a");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-a";
        parser.processOptionToken(token, true);
        assertNull(parser.tokens);
    }

    @Test
    public void testProcessOptionTokenWithHasArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        PosixParser parser = new PosixParser();
        String token = "-a";
        parser.processOptionToken(token, false);
        String nextToken = "value";
        parser.tokens.add(nextToken);
        List expected = Arrays.asList("-a", "value");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessOptionTokenWithHasArgAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        PosixParser parser = new PosixParser();
        String token = "-a";
        parser.processOptionToken(token, true);
        String nextToken = "value";
        parser.tokens.add(nextToken);
        String nextNextToken = "bar";
        parser.tokens.add(nextNextToken);
        List expected = Arrays.asList("-a", "--", "bar");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String token = "-abc";
        parser.burstToken(token, false);
        List expected = Arrays.asList("-a", "-b", "-c");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-abc";
        parser.burstToken(token, true);
        List expected = Arrays.asList("--", "abc");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithOptionExists() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String token = "-ac";
        parser.burstToken(token, false);
        String nextToken = "d";
        parser.tokens.add(nextToken);
        List expected = Arrays.asList("-a", "-c", "d");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithOptionExistsAndArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        PosixParser parser = new PosixParser();
        String token = "-acd";
        parser.burstToken(token, false);
        List expected = Arrays.asList("-a", "-c", "d");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithOptionDoesNotExist() {
        PosixParser parser = new PosixParser();
        String token = "-abc";
        parser.burstToken(token, false);
        List expected = Arrays.asList("--", "-a", "-b", "-c");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithOptionDoesNotExistAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-abc";
        parser.burstToken(token, true);
        List expected = Arrays.asList("--", "-", "abc");
        assertEquals(expected, parser.tokens);
    }
}