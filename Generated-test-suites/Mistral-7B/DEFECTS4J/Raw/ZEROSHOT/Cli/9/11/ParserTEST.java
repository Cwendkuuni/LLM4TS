package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParser() throws ParseException {
        // Given
        String[] args = new String[]{"-a", "1", "--optionB", "valueB", "--optionC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", null, "Option C");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, args);

        // Then
        Assert.assertEquals(commandLine.getArgs().size(), 0);
        Assert.assertEquals(commandLine.getOptions().size(), 3);

        Option optionA = commandLine.getOption("a");
        Assert.assertTrue(optionA.isSet());
        Assert.assertEquals(optionA.getValue().toString(), "1");

        Option optionB = commandLine.getOption("b");
        Assert.assertTrue(optionB.isSet());
        Assert.assertNull(optionB.getValue());

        Option optionC = commandLine.getOption("c");
        Assert.assertNull(optionC);

        Assert.assertEquals(commandLine.getOptionValues("b"), new String[]{"valueB"});
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        // Given
        String[] args = new String[]{"--nonExistentOption"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, args);

        // Then
        // ParseException should be thrown
    }

    @Test(expected = ParseException.class)
    public void testMissingArgument() throws ParseException {
        // Given
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "requiredArgument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, args);

        // Then
        // ParseException should be thrown
    }

    @Test
    public void testMissingOptionWithArguments() throws ParseException {
        // Given
        String[] args = new String[]{"--optionA valueA", "--optionB valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, args);

        // Then
        Assert.assertEquals(commandLine.getArgs().size(), 0);
        Assert.assertEquals(commandLine.getOptions().size(), 2);

        Option optionA = commandLine.getOption("a");
        Assert.assertTrue(optionA.isSet());
        Assert.assertEquals(optionA.getValue().toString(), "valueA");

        Option optionB = commandLine.getOption("b");
        Assert.assertTrue(optionB.isSet());
        Assert.assertEquals(optionB.getValue().toString(), "valueB");
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        // Given
        String[] args = new String[]{"arg1", "--optionA", "--optionB", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, args, true);

        // Then
        Assert.assertEquals(commandLine.getArgs().size(), 2);
        Assert.assertEquals(commandLine.getOptions().size(), 0);

        Assert.assertEquals(commandLine.getArgs()[0], "arg1");
        Assert.assertEquals(commandLine.getArgs()[1], "arg2");
    }

    @Test
    public void testProperties() throws ParseException {
        // Given
        String[] args = new String[]{"--optionA=valueA", "--optionB=valueB"};
        Properties properties = new Properties();
        properties.put("optionA", "valueA");
        properties.put("optionB", "valueB");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        CommandLine commandLine = parser.parse(options, args, properties, true);

        // Then
        Assert.assertEquals(commandLine.getArgs().size(), 0);
        Assert.assertEquals(commandLine.getOptions().size(), 2);

        Option optionA = commandLine.getOption("a");
        Assert.assertTrue(optionA.isSet());
        Assert.assertEquals(optionA.getValue().toString(), "valueA");

        Option optionB = commandLine.getOption("b");
        Assert.assertTrue(optionB.isSet());
        Assert.assertEquals(optionB.getValue().toString(), "valueB");
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        // Given
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, args);

        // Then
        // MissingOptionException should be thrown
    }

    @Test(expected = ParseException.class)
    public void testMissingGroupRequiredOption() throws ParseException {
        // Given
        String[] args = new String[]{"--groupOption1"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setExclusive(true);
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // When
        parser.parse(options, args);

        // Then
        // MissingOptionException should be thrown
    }
}

class DefaultParser extends Parser {
    // No need to override abstract method flatten
}