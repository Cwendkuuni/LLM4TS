package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testFlattenSingleArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("arg", result[0]);
    }

    @Test
    public void testFlattenSingleLongOption() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        PosixParser parser = new PosixParser();
        String[] args = {"--a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--a", result[0]);
    }

    @Test
    public void testFlattenSingleShortOption() {
        Options options = new Options();
        options.addOption('a', true, "a help message");
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlattenSingleShortOptionWithArg() {
        Options options = new Options();
        options.addOption('a', true, "a help message");
        options.addOption('a', 1, "an argument");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenMultipleArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlattenMultipleShortOptions() {
        Options options = new Options();
        options.addOption('a', true, "a help message");
        options.addOption('b', true, "another help message");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlattenLongOptionWithArg() {
        Options options = new Options();
        options.addOption("long", true, "a help message");
        options.addOption("long-arg", 1, "an argument");
        PosixParser parser = new PosixParser();
        String[] args = {"--long-arg", "arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--long-arg", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlattenMixedShortAndLongOptions() {
        Options options = new Options();
        options.addOption('a', true, "a help message");
        options.addOption('b', true, "another help message");
        options.addOption("long", true, "a long option help message");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--long", "-b"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--long", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlattenLongOptionWithArgumentAndMultipleValues() {
        Options options = new Options();
        options.addOption("long", 1, "an argument");
        options.addOption("long", 2, "another argument");
        PosixParser parser = new PosixParser();
        String[] args = {"--long=arg1", "--long=arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--long=arg1", result[0]);
        assertEquals("--long=arg2", result[1]);
    }

    @Test
    public void testFlattenLongOptionWithArgumentAndMultipleValuesMixedWithSingleValues() {
        Options options = new Options();
        options.addOption("long", 1, "an argument");
        options.addOption("long", 2, "another argument");
        PosixParser parser = new PosixParser();
        String[] args = {"--long=arg1", "--long", "--long=arg3"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("--long=arg1", result[0]);
        assertEquals("--long", result[1]);
        assertEquals("--long=arg3", result[2]);
    }

    @Test
    public void testFlattenMultipleLongOptionsWithArguments() {
        Options options = new Options();
        options.addOption("long1", 1, "an argument for long1");
        options.addOption("long2", 1, "an argument for long2");
        PosixParser parser = new PosixParser();
        String[] args = {"--long1=arg1", "--long2=arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--long1=arg1", result[0]);
        assertEquals("--long2=arg2", result[1]);
    }

    @Test
    public void testFlattenMixedLongAndShortOptions() {
        Options options = new Options();
        options.addOption('a', true, "a help message");
        options.addOption("long", true, "a long option help message");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--long"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--long", result[1]);
    }

    @Test
    public void testFlattenLongOptionWithNoArgument() {
        Options options = new Options();
        options.addOption("long", true, "a help message");
        PosixParser parser = new PosixParser();
        String[] args = {"--long"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--long", result[0]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption('a', true, "a help message");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "non-option"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testBurstTokenOneCharacter() {
        PosixParser parser = new PosixParser();
        String token = "-a";
        String[] result = parser.burstToken(token, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testBurstTokenMultipleCharactersWithOption() {
        PosixParser parser = new PosixParser();
        String token = "-ab";
        String[] result = parser.burstToken(token, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testBurstTokenMultipleCharactersWithoutOption() {
        PosixParser parser = new PosixParser();
        String token = "-abc";
        String[] result = parser.burstToken(token, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testBurstTokenMultipleCharactersWithOptionAndArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption('a', 1, "an argument");
        String token = "--long-argument-a";
        String[] result = parser.burstToken(token, false);
        assertEquals(2, result.length);
        assertEquals("--long-argument-a", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("an argument", result[2]);
    }

    @Test
    public void testBurstTokenMultipleCharactersWithOptionAndArgAndOtherOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption('a', 1, "an argument");
        options.addOption('b', 1, "another argument");
        String token = "--long-argument-a-b";
        String[] result = parser.burstToken(token, false);
        assertEquals(3, result.length);
        assertEquals("--long-argument-a-b", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("an argument", result[2]);
        assertEquals("-b", result[3]);
    }

    @Test
    public void testBurstTokenMultipleCharactersWithOptionAndArgAndOtherOptionsMixed() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption('a', 1, "an argument");
        options.addOption('b', 1, "another argument");
        String token = "--long-argument-a-b";
        String[] result = parser.burstToken(token, false);
        assertEquals(3, result.length);
        assertEquals("--long-argument-a-b", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("an argument", result[2]);
        assertEquals("-b", result[3]);

        token = "--long-argument-a";
        result = parser.burstToken(token, false);
        assertEquals(2, result.length);
        assertEquals("--long-argument-a", result[0]);
        assertEquals("-a", result[1]);
    }

    @Test
    public void testBurstTokenMultipleCharactersWithOptionAndArgAndOtherOptionsWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption('a', 1, "an argument");
        options.addOption('b', 1, "another argument");
        String token = "--long-argument-a-b";
        String[] result = parser.burstToken(token, true);
        assertEquals(1, result.length);
        assertEquals("--long-argument-a-b", result[0]);
    }

    @Test
    public void testBurstTokenEmptyString() {
        PosixParser parser = new PosixParser();
        String token = "";
        String[] result = parser.burstToken(token, false);
        assertEquals(0, result.length);
    }
}