package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.Arrays;

public class PosixParserTest {

    private Options options;
    private String[] arguments;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", "optionA", true, "An option with an argument");
        options.addOption("b", "optionB", false, "An option without an argument");
        options.addOption("c", "optionC", false, "Another option without an argument");
    }

    @Test
    public void testFlattenSimple() {
        String[] args = {"--optionA", "valueA"};
        String[] expected = {"--optionA", "valueA"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOtherOptions() {
        String[] args = {"--optionA", "valueA", "-b", "-c"};
        String[] expected = {"--optionA", "valueA", "-b", "-c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        String[] args = {"--optionA", "longOptionValue"};
        String[] expected = {"--optionA", "longOptionValue"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--optionA", "valueA", "-b", "-c", "--"};
        String[] expected = {"--optionA", "valueA", "--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBursting() {
        String[] args = {"-a--optionB", "valueB"};
        String[] expected = {"-a", "--optionB", "valueB"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }
}