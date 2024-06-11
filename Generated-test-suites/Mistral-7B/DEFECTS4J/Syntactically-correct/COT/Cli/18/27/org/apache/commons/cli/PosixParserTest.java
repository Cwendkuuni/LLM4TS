package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_NoOptions() {
        String[] args = new String[]{"java", "MyProgram"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "MyProgram");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = new String[]{"java", "--option"};
        Options options = new Options();
        options.addOption("option", true, "O");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--option");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_OptionWithArgument() {
        String[] args = new String[]{"java", "--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "O");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--option", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = new String[]{"java", "--option1", "val1", "--option2", "val2"};
        Options options = new Options();
        options.addOption("option1", true, "O1");
        options.addOption("option2", true, "O2");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--option1", "val1", "--option2", "val2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOptions() {
        String[] args = new String[]{"java", "--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "L");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--long-option", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        String[] args = new String[]{"java", "--long-option", "value1", "--long-option=value2"};
        Options options = new Options();
        options.addOption("longOption", true, "L");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--long-option", "value1", "--long-option=value2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = new String[]{"java", "--option", "value", "nonOption"};
        Options options = new Options();
        options.addOption("option", true, "O");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("java", "--option", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() {
        String[] args = new String[]{"java", "--invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstToken_InvalidToken() {
        String token = "invalid";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
    }
}