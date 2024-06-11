package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with valid options.
     */
    @Test
    public void testFlattenWithValidOptions() {
        PosixParser parser = new PosixParser();

        String[] args = new String[]{"--option1", "--option2=value1", "--option3=value2", "value3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList(new String[]{"--option1", "--option2", "value1", "--option3", "value2", "value3"});
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid options and stopAtNonOption=true.
     */
    @Test
    public void testFlattenWithInvalidOptionsAndStopAtNonOptionTrue() {
        PosixParser parser = new PosixParser();

        String[] args = new String[]{"--invalidOption", "-a", "--option1", "--option2=value1", "--option3", "value3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        try {
            String[] result = parser.flatten(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        List<String> expected = Arrays.asList(new String[]{"--invalidOption", "-a", "--option1", "--option2=value1", "--option3", "value3"});
        Assert.assertArrayEquals(expected.subList(0, expected.size() - 1).toArray(new String[0]), Arrays.copyOfRange(parser.getTokens().toArray(new Object[0]), 0, parser.getTokens().size() - 1));
        Assert.assertEquals("--invalidOption", parser.getTokens().get(parser.getTokens().size() - 1));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid options and stopAtNonOption=false.
     */
    @Test
    public void testFlattenWithInvalidOptionsAndStopAtNonOptionFalse() {
        PosixParser parser = new PosixParser();

        String[] args = new String[]{"--invalidOption", "-a", "--option1", "--option2=value1", "--option3", "value3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        try {
            String[] result = parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        List<String> expected = Arrays.asList(new String[]{"--invalidOption", "-a", "--option1", "--option2=value1", "--option3", "value3"});
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.getTokens().toArray(new Object[0]));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with a mix of valid and invalid options.
     */
    @Test
    public void testFlattenWithAMixOfValidAndInvalidOptions() {
        PosixParser parser = new PosixParser();

        String[] args = new String[]{"--option1", "--invalidOption", "--option2=value1", "--option3", "-a", "value3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        try {
            String[] result = parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        List<String> expected = Arrays.asList(new String[]{"--option1", "--invalidOption", "--option2", "value1", "--option3", "value3"});
        Assert.assertArrayEquals(expected.subList(0, expected.size() - 1).toArray(new String[0]), Arrays.copyOfRange(parser.getTokens().toArray(new Object[0]), 0, parser.getTokens().size() - 1));
        Assert.assertEquals("--invalidOption", parser.getTokens().get(parser.getTokens().size() - 1));
    }

    
}