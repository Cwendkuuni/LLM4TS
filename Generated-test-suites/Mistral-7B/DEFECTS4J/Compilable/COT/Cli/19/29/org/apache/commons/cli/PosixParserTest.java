package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_NoOptions() {
        String[] args = new String[]{"java", "MyClass"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "MyClass");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = new String[]{"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_SingleValueOption() {
        String[] args = new String[]{"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "An option with a value");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "value");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = new String[]{"-a", "--optionB", "-c", "1", "--optionC=value"};
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", true, "An option with a flag");
        options.addOption("c", true, "An option with a flag");
        options.addOption("C", true, "An option with an argument");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "--optionB", "-c", "--optionC=value");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = new String[]{"-x", "--option", "value"};
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.fail("Should have thrown ParseException");
    }

    @Test
    public void testFlatten_LongOptions() {
        String[] args = new String[]{"--longOption", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "A long option");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOption", "value");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = new String[]{"-a", "value", "nonOption"};
        Options options = new Options();
        options.addOption("a", true, "An option with a value");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("-a", "value", "nonOption");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}