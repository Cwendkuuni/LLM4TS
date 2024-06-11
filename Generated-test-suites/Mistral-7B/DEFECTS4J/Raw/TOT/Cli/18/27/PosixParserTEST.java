package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test for flattening basic command line arguments.
     */
    @Test
    public void testFlattenBasic() {
        String[] args = new String[]{"app", "--option1", "--option2=value", "-o", "output.txt", "--option3=value2"};
        Options options = new Options();
        options.addOption("o", "output", true, "Output file name.");
        options.addOption("option1", true, "Option 1.");
        options.addOption("option2", true, "Option 2.");
        options.addOption("option3", true, "Option 3.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"app", "--option1", "true", "--option2", "value", "--o", "output.txt", "--option3", "value2"}, parsedArgs);
    }

    /**
     * Test for flattening command line arguments with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = new String[]{"app", "--option1", "--option2=value", "-o", "output.txt", "-invalid", "--option3=value2"};
        Options options = new Options();
        options.addOption("o", "output", true, "Output file name.");
        options.addOption("option1", true, "Option 1.");
        options.addOption("option2", true, "Option 2.");
        options.addOption("option3", true, "Option 3.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"app", "--option1", "true", "--option2", "value", "--o", "output.txt", "-invalid", "--option3", "value2"}, parsedArgs);
    }

    /**
     * Test for flattening command line arguments with multiple options with the same id.
     */
    @Test
    public void testFlattenWithMultipleOptions() {
        String[] args = new String[]{"app", "--option1=value1", "--option1=value2", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1.");
        options.addOption("option2", true, "Option 2.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"app", "--option1", "value1", "--option1", "value2", "--option2"}, parsedArgs);
    }

    /**
     * Test for flattening command line arguments with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = new String[]{"app", "--invalidOption"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1.");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}