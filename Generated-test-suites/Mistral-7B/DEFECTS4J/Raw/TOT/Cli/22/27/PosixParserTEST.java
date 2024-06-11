package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testSimpleOption() throws ParseException {
        String[] args = {"-a", "--optionA=valueA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        DefaultParser parser = new DefaultParser();
        List<Object> parsedArgs = parser.parse(options, args, false);
        Assert.assertEquals("valueA", parsedArgs.get(0));
        Assert.assertEquals("optionA", parsedArgs.get(1).toString());
    }

    @Test
    public void testLongOption() throws ParseException {
        String[] args = {"--longOption", "--longOptionValue=valueA"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        DefaultParser parser = new DefaultParser();
        List<Object> parsedArgs = parser.parse(options, args, false);
        Assert.assertEquals("valueA", parsedArgs.get(1));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] args = {"--missingOption"};
        Options options = new Options();
        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testMissingValue() throws ParseException {
        String[] args = {"-a", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testMixedOptions() throws ParseException {
        String[] args = {"--optionA=valueA", "-a", "--longOption=valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Long Option");
        options.addOption("c", true, "Option C");
        DefaultParser parser = new DefaultParser();
        List<Object> parsedArgs = parser.parse(options, args, false);
        Assert.assertEquals("valueA", parsedArgs.get(0));
        Assert.assertEquals("valueB", parsedArgs.get(2));
        Assert.assertEquals("valueC", parsedArgs.get(4));
    }

    @Test
    public void testBurstedOption() throws ParseException {
        String[] args = {"-abc", "--optionA", "--optionB=valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        DefaultParser parser = new DefaultParser();
        List<Object> parsedArgs = parser.parse(options, args, false);
        Assert.assertTrue(parsedArgs.get(0) instanceof String);
        Assert.assertEquals("optionA", parsedArgs.get(1).toString());
        Assert.assertEquals("valueB", parsedArgs.get(2));
        Assert.assertEquals("valueC", parsedArgs.get(4));
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"--", "-a", "--optionA=valueA", "--", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        DefaultParser parser = new DefaultParser();
        List<Object> parsedArgs = parser.parse(options, args, true);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", "Test program", options, true);
        System.out.println(formatter.getHelpText());
        Assert.assertEquals(11, parsedArgs.size());
        Assert.assertEquals("--", parsedArgs.get(0));
        Assert.assertEquals("-a", parsedArgs.get(1));
        Assert.assertEquals("--optionA=valueA", parsedArgs.get(2).toString());
        Assert.assertEquals("--", parsedArgs.get(3));
        Assert.assertEquals("-b", parsedArgs.get(4));
    }
}