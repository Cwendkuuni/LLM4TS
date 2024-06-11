package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"", ""});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        PosixParser parser = new PosixParser();
        String[] args = {"-o"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"-o"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    public void testFlattenSingleValueOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"-o", "value"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        PosixParser parser = new PosixParser();
        String[] args = {"-o1", "-o2"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"-o1", "-o2"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption1", true, "l1");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"--longOption1"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption1", true, "l1");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1=value"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"--longOption1=value"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void testFlattenWithArg() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", 2, "opt2");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value1", "-opt2", "value2"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"-o", "value1", "--", "-opt2", "value2"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
        assertEquals(expected.get(3), actual.get(3));
    }

    @Test
    public void testFlattenWithArgAndLongOpt() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("longOption1", true, "l");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value1", "--longOption1", "value2"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"-o", "value1", "--longOption1", "value2"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
        assertEquals(expected.get(3), actual.get(3));
    }

    @Test
    public void testFlattenWithArgAndLongOptValue() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("longOption1", true, "l");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value1", "--longOption1", "value2"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"-o", "value1", "--longOption1=value2"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    public void testFlattenWithUnrecognizedToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--unknown"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"--unknown"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "--unknown"};

        parser.flatten(options, args, true);

        List expected = Arrays.asList(new String[]{"-o", "--unknown"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndArg() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "--unknown", "value"};

        parser.flatten(options, args, true);

        List expected = Arrays.asList(new String[]{"-o", "--unknown", "value"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOpt() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "--longOption1"};

        parser.flatten(options, args, true);

        List expected = Arrays.asList(new String[]{"-o", "--longOption1"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLongOptValue() {
        Options options = new Options();
        options.addOption("option1", true, "o");
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "--longOption1=value"};

        parser.flatten(options, args, true);

        List expected = Arrays.asList(new String[]{"-o", "--longOption1=value"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    public void testFlattenWithMultipleUnrecognizedTokens() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--unknown1", "--unknown2"};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"--unknown1", "--unknown2"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    public void testFlattenWithMultipleUnrecognizedTokensAndStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--unknown1", "--unknown2", "value"};

        parser.flatten(options, args, true);

        List expected = Arrays.asList(new String[]{"--unknown1", "--unknown2", "value"});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    public void testFlattenWithEmptyToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"", ""};

        parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"", ""});
        List actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }
}