package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenSingleLongOption() {
        Options options = new Options();
        Option option = new Option("longOption", true, "a help message");
        options.addOption(option);
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenSingleArgumentOption() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] arguments = {"-a", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "arg1"}, result);
    }

    @Test
    public void testFlattenMultipleArgumentsOption() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] arguments = {"-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenLongArgumentOption() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] arguments = {"--longOption", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption", "arg1"}, result);
    }

    @Test
    public void testFlattenLongAndShortArguments() {
        Options options = new Options();
        Option option1 = new Option("l", true, "option 1 help message");
        Option option2 = new Option("longOption", true, "option 2 help message");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-l", "--longOption", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-l", "--longOption", "arg1"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidLongOption() {
        Options options = new Options();
        String[] arguments = {"--invalidLongOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", true, "a help message");
        options.addOption(option);
        String[] arguments = {"-a", "--nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a", "--nonOption"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionMultiple() {
        Options options = new Options();
        Option option1 = new Option("a", true, "a help message");
        Option option2 = new Option("b", true, "b help message");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-a", "--nonOption", "-b", "--nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-a", "--nonOption", "-b", "--nonOption"}, result);
    }

    
}