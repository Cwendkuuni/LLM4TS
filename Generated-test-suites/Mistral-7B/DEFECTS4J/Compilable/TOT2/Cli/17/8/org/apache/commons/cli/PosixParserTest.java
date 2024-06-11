package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenHappyPath() {
        String[] args = {"--option1", "-o", "value1", "--option2", "-v"};
        Options options = new Options();
        options.addOption("o", true, "option1 option with value");
        options.addOption("v", true, "option2 option with value");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option1", "-o", "value1", "--option2", "-v"}, parsedArgs);
    }

    @Test
    public void testFlattenWithSingleDash() {
        String[] args = {"-", "--option1", "-o", "value1", "--option2", "-v"};
        Options options = new Options();
        options.addOption("o", true, "option1 option with value");
        options.addOption("v", true, "option2 option with value");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--", "-", "--option1", "-o", "value1", "--option2", "-v"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalidOption", "--option1", "-o", "value1", "--option2", "-v"};
        Options options = new Options();
        options.addOption("o", true, "option1 option with value");
        options.addOption("v", true, "option2 option with value");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, true);

        // Should throw ParseException
    }

    @Test
    public void testFlattenWithDoubleDash() {
        String[] args = {"--", "--option1=value1", "--option2=value2"};
        Options options = new Options();
        options.addOption("o", true, "option1 option with value");
        options.addOption("v", true, "option2 option with value");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option1=value1", "--option2=value2"}, parsedArgs);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--", "option1", "--option1", "value1", "--option2", "value2"};
        Options options = new Options();
        options.addOption("o", true, "option1 option with value");
        options.addOption("v", true, "option2 option with value");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--", "option1", "--option1", "value1"}, parsedArgs);
    }
}