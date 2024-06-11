package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OptionImplTest {

    @Test
    public void testOptionImpl() {
        Option option = new OptionBuilder("o", "o")
                .withLongOpt("option")
                .withDescription("An example option")
                .isRequired(false)
                .create();

        Options options = new Options();
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        List<Object> arguments = List.of("java", "MyClass.java", "-o", "value");

        try {
            parser.parse(options, commandLine, arguments);
            Assert.assertEquals("value", commandLine.getOptionValue("o"));
            Assert.assertEquals(1, commandLine.getOptions().size());
            Assert.assertEquals(option, commandLine.getOption("o"));
            Assert.assertNull(commandLine.getOptionValue("nonexistentOption"));
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MyClass.java", options);
            System.out.println(e.getMessage());
        }

        try {
            parser.parse(options, commandLine, arguments.subList(0, 2));
            Assert.assertNull(commandLine.getOptionValue("o"));
            Assert.assertEquals(0, commandLine.getOptions().size());
        } catch (ParseException e) {
            // Expected as required option is not present
        }

        try {
            parser.parse(options, commandLine, arguments);
            Assert.assertNull(commandLine.getOptionValue("o"));
            Assert.assertEquals(0, commandLine.getOptions().size());
            commandLine.setValue("o", "newValue");
            Assert.assertEquals("newValue", commandLine.getOptionValue("o"));
            Assert.assertEquals(1, commandLine.getOptions().size());
            Assert.assertEquals(option, commandLine.getOption("o"));
        } catch (ParseException e) {
            // Expected as no arguments provided after option
        }

        try {
            parser.parse(options, commandLine, arguments);
            Assert.assertEquals("value", commandLine.getOptionValue("o"));
            Assert.assertEquals(1, commandLine.getOptions().size());
            Assert.assertEquals(option, commandLine.getOption("o"));
            commandLine.setValue("o", null);
            Assert.assertNull(commandLine.getOptionValue("o"));
            Assert.assertEquals(1, commandLine.getOptions().size());
            Assert.assertEquals(option, commandLine.getOption("o"));
        } catch (ParseException e) {
            // Expected as no arguments provided after option
        }

        options.addOption(new Option("h", "help")
                .withLongOpt("help")
                .withDescription("Print this usage information")
                .isRequired(false)
                .create());

        try {
            parser.parse(options, commandLine, arguments);
            Assert.assertNull(commandLine.getOptionValue("h"));
            Assert.assertEquals(2, commandLine.getOptions().size());
            Assert.assertEquals(option, commandLine.getOption("o"));
            Assert.assertEquals(option, commandLine.getOption("h"));
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MyClass.java", options);
            Assert.assertTrue(e.getMessage().contains("Print this usage information"));
        }

        try {
            parser.parse(options, commandLine, arguments);
            Assert.assertNull(commandLine.getOptionValue("o"));
            Assert.assertEquals(2, commandLine.getOptions().size());
            Assert.assertNull(commandLine.getOption("nonexistentOption"));
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MyClass.java", options);
            Assert.assertTrue(e.getMessage().contains("nonexistentOption"));
        }

        try {
            parser.parse(options, commandLine, arguments);
            Assert.assertNull(commandLine.getOptionValue("o"));
            Assert.assertEquals(2, commandLine.getOptions().size());
            Assert.assertNull(commandLine.getOptionValue("o"));
            Assert.assertEquals(1, commandLine.getOptions().size());
            Assert.assertNull(commandLine.getOption("nonexistentOption"));
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MyClass.java", options);
            Assert.assertTrue(e.getMessage().contains("required option 'o' is missing"));
        }
    }
}