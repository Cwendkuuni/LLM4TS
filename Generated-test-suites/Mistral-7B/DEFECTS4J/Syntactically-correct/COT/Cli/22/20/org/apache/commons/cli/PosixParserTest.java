package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with valid options and arguments.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithValidOptionsAndArguments() throws ParseException
    {
        String[] args = {"--option1", "--option2=value1", "--option3", "-o", "--option4", "-a", "--option5=value5"};
        Options options = new Options();
        options.addOption("option1", true, "h", "help");
        options.addOption("option2", true, "v", "verbose");
        options.addOption("option3", false, "o", "outputFile");
        options.addOption("option4", true, null, "inputFile");
        options.addOption("option5", true, null, "threshold");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"help", "true", "outputFile", "inputFile", "threshold"}, parsedArgs);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with valid options, invalid argument and stopAtNonOption=true.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithInvalidArgumentAndStopAtNonOption() throws ParseException
    {
        String[] args = {"--option1", "--option2=value1", "--invalidArgument", "--option3", "-o", "--option4", "-a", "--option5=value5"};
        Options options = new Options();
        options.addOption("option1", true, "h", "help");
        options.addOption("option2", true, "v", "verbose");
        options.addOption("option3", false, "o", "outputFile");
        options.addOption("option4", true, null, "inputFile");
        options.addOption("option5", true, null, "threshold");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"help", "true", "outputFile", "inputFile", "", "invalidArgument", "", "threshold"}, parsedArgs);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with missing option.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithMissingOption() throws ParseException
    {
        String[] args = {"--unknownOption", "--option1", "--option2=value1", "--option3", "-o", "--option4", "-a", "--option5=value5"};
        Options options = new Options();
        options.addOption("option1", true, "h", "help");
        options.addOption("option2", true, "v", "verbose");
        options.addOption("option3", false, "o", "outputFile");
        options.addOption("option4", true, null, "inputFile");
        options.addOption("option5", true, null, "threshold");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with short option.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithShortOption() throws ParseException
    {
        String[] args = {"-h", "--option2=value1", "--option3", "-o", "--option4", "-a"};
        Options options = new Options();
        options.addOption("h", true, "h", "help");
        options.addOption("option2", true, "v", "verbose");
        options.addOption("option3", false, "o", "outputFile");
        options.addOption("option4", true, null, "inputFile");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"help", "true", "outputFile", "", "verbose"}, parsedArgs);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with empty arguments array.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithEmptyArgumentsArray() throws ParseException
    {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, new String[0], false);

        Assert.assertArrayEquals(new String[0], parsedArgs);
    }
}