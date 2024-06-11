package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenSimpleOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "--optionA=valueA", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionA", true, "Option A with argument");
        options.addOption("b", false, "Option B");

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("optionA");
        expected.add("valueA");
        expected.add("b");

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenLongOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOptionA=valueA", "--longOptionB=valueB"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A with argument");
        options.addOption("longOptionB", true, "Long Option B with argument");

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("longOptionA");
        expected.add("valueA");
        expected.add("longOptionB");
        expected.add("valueB");

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithArg() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "argA"};
        Options options = new Options();
        options.addOption("a", true, "Option A with argument");

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("argA");

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithArgAndValues() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "argA1", "--optionB=valueB1", "--optionB=valueB2"};
        Options options = new Options();
        options.addOption("a", true, "Option A with argument");
        options.addOption("b", true, "Option B with argument");

        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("argA1");
        expected.add("b");
        expected.add("valueB1");
        expected.add("valueB2");

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-x", "--unknownOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionFormat() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a="};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a="};
        Options options = new Options();
        options.addOption("a", true, "Option A with argument");

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOptionWithMultipleArgs() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--optionA=valueA=valueB"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A with argument");

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenEmptyOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--optionA"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A with argument");

        parser.flatten(options, arguments, false);
    }
}