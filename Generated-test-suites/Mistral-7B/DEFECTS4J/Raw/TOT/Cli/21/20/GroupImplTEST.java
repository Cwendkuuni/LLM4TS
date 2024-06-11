package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithRequiredOptions() {
        final String[] args = new String[]{"test", "--group1", "--option1", "--option2"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .required(true)
                .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .required(true)
                        .build())
                .addOption(Option.builder("option2")
                        .longOpt("option2")
                        .required(true)
                        .build()));

        final DefaultParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);
        } catch (final ParseException e) {
            Assert.fail("Parsing failed! " + e.getMessage());
        }
    }

    @Test
    public void testGroupWithOptionalOptions() {
        final String[] args = new String[]{"test", "--group1", "--option1", "--option2", "--another"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .required(false)
                        .build())
                .addOption(Option.builder("option2")
                        .longOpt("option2")
                        .required(false)
                        .build()));

        final DefaultParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);
        } catch (final ParseException e) {
            if (e.getOption().isPresent()) {
                Assert.fail("Parsing failed: " + e.getMessage());
            }
        }
    }

    @Test
    public void testGroupWithMinMaxOptions() {
        final String[] args = new String[]{"test", "--group1", "--option1", "--option1", "--option2"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .required(false)
                        .minValues(2)
                        .maxValues(3)
                        .build())
                .addOption(Option.builder("option2")
                        .longOpt("option2")
                        .required(false)
                        .build()));

        final DefaultParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);
        } catch (final ParseException e) {
            if (e.getOption().isPresent()) {
                Assert.fail("Parsing failed: " + e.getMessage());
            } else {
                final HelpFormatter formatter = new HelpFormatter();
                final HelpLine helpLine = formatter.printHelp("Test", options);
                System.out.println(helpLine.getHelpText());
            }
        }
    }

    @Test
    public void testGroupWithAnonymousOptions() {
        final String[] args = new String[]{"test", "--group1", "--option1=value1", "--option2=value2"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .addOption(Option.builder("option1")
                        .argument("value")
                        .required(true)
                        .build())
                .addOption(Option.builder("option2")
                        .argument("value")
                        .required(true)
                        .build()));

        final DefaultParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);

            Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
            Assert.assertEquals("value2", commandLine.getOptionValue("option2"));
        } catch (final ParseException e) {
            Assert.fail("Parsing failed: " + e.getMessage());
        }
    }

    @Test
    public void testGroupWithInvalidOption() {
        final String[] args = new String[]{"test", "--group1", "--invalid"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .build())
                .addOption(Option.builder("option2")
                        .longOpt("option2")
                        .build()));

        final DefaultParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);
            Assert.fail("Parsing succeeded!");
        } catch (final ParseException e) {
            Assert.assertEquals("Unrecognized option: --invalid", e.getMessage());
        }
    }

    @Test
    public void testGroupWithMismatchedOption() {
        final String[] args = new String[]{"test", "--group1", "--option2=value1"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .required(true)
                        .argName("value")
                        .build())
                .addOption(Option.builder("option2")
                        .longOpt("option2")
                        .required(false)
                        .argName("value")
                        .build()));

        final DefaultParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);
            Assert.fail("Parsing succeeded!");
        } catch (final ParseException e) {
            Assert.assertEquals("No option 'option2' defined for --group1", e.getMessage());
        }
    }

    @Test
    public void testGroupWithMixedOptionsAndArguments() {
        final String[] args = new String[]{"test", "--group1", "--option1", "value1", "value2"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .required(true)
                        .argName("value")
                        .build())
                .addOption(Option.builder("option2")
                        .longOpt("option2")
                        .required(false)
                        .argName("value")
                        .build()));

        final DefaultParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);

            Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
            Assert.assertNull(commandLine.getOptionValue("option2"));
            Assert.assertEquals(2, commandLine.getArgs().length);
            Assert.assertEquals("value2", commandLine.getArgs()[0]);
            Assert.assertEquals("value1", commandLine.getArgs()[1]);
        } catch (final ParseException e) {
            Assert.fail("Parsing failed: " + e.getMessage());
        }
    }

    @Test
    public void testGroupWithPrefixes() {
        final String[] args = new String[]{"test", "--g=group1", "--option1=value1"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .required(true)
                        .build())
                .setOptionPrefix("g")
                .build());

        final DefaultParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);

            Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
            Assert.assertEquals("group1", commandLine.getOptionPrefix());
        } catch (final ParseException e) {
            Assert.fail("Parsing failed: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testGroupWithInvalidPrefix() {
        final String[] args = new String[]{"test", "--invalid=group1", "--option1=value1"};

        final Options options = new Options();
        options.addGroup(new OptionGroup("group1")
                .addOption(Option.builder("option1")
                        .longOpt("option1")
                        .required(true)
                        .build())
                .setOptionPrefix("i")
                .build());

        final DefaultParser parser = new DefaultParser();

        parser.parse(options, args);
    }
}