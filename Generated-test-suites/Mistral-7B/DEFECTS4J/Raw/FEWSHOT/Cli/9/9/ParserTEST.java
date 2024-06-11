package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), new String[0]);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseSimple() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"-a", "1"};
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("a")
                .withValueRequired(true)
                .create();
        options.addOption(option);
        CommandLine cmd = parser.parse(options, args);
        assertArrayEquals(new String[]{"1"}, cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertSame(option, cmd.getOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"-a", "1", "--b"};
        Options options = new Options();
        Option optionA = OptionBuilder.withLongOpt("a")
                .withValueOptional(true)
                .create();
        Option optionB = OptionBuilder.withLongOpt("b")
                .withValueOptional(true)
                .create();
        options.addOption(optionA);
        options.addOption(optionB);
        CommandLine cmd = parser.parse(options, args);
        assertArrayEquals(new String[]{"1"}, cmd.getArgs());
        assertTrue(cmd.hasOption("a"));
        assertSame(optionA, cmd.getOption("a"));
        assertNull(cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertSame(optionB, cmd.getOption("b"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"--option1", "--option2", "--option3"};
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withValueOptional(true)
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withValueOptional(true)
                .create();
        Option option3 = OptionBuilder.withLongOpt("option3")
                .withValueOptional(true)
                .create();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        CommandLine cmd = parser.parse(options, args);
        assertArrayEquals(new String[0], cmd.getArgs());
        assertTrue(cmd.hasOption("option1"));
        assertSame(option1, cmd.getOption("option1"));
        assertNull(cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertSame(option2, cmd.getOption("option2"));
        assertNull(cmd.getOptionValue("option2"));
        assertTrue(cmd.hasOption("option3"));
        assertSame(option3, cmd.getOption("option3"));
        assertNull(cmd.getOptionValue("option3"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"--property1=value1", "--property2=value2"};
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("property1")
                .hasArg(true)
                .create();
        Option option2 = OptionBuilder.withLongOpt("property2")
                .hasArg(true)
                .create();
        options.addOption(option1);
        options.addOption(option2);
        Properties props = new Properties();
        props.setProperty("property1", "value1");
        props.setProperty("property2", "value2");
        CommandLine cmd = parser.parse(options, args, props);
        assertArrayEquals(new String[0], cmd.getArgs());
        assertTrue(cmd.hasOption("property1"));
        assertSame(option1, cmd.getOption("property1"));
        assertEquals("value1", cmd.getOptionValue("property1"));
        assertTrue(cmd.hasOption("property2"));
        assertSame(option2, cmd.getOption("property2"));
        assertEquals("value2", cmd.getOptionValue("property2"));
    }

    @Test
    public  void testParseWithRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"--option1"};
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .isRequired(true)
                .create();
        options.addOption(option1);
        try {
            parser.parse(options, args);
            fail();
        } catch (MissingOptionException e) {
            assertSame("Missing required option: option1", e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"--group", "-a", "-b"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        Option optionA = OptionBuilder.withLongOpt("a")
                .create();
        Option optionB = OptionBuilder.withLongOpt("b")
                .create();
        group.addOption(optionA);
        group.addOption(optionB);
        options.addOption(group);
        CommandLine cmd = parser.parse(options, args);
        assertArrayEquals(new String[]{"-a", "-b"}, cmd.getArgs());
        assertTrue(cmd.hasOption("group"));
        assertSame(group, cmd.getOption("group"));
        assertTrue(cmd.isSet("group"));
        assertTrue(cmd.hasOption("a"));
        assertSame(optionA, cmd.getOption("a"));
        assertFalse(cmd.isSet("a"));
        assertTrue(cmd.hasOption("b"));
        assertSame(optionB, cmd.getOption("b"));
        assertFalse(cmd.isSet("b"));
    }

    @Test
    public void testParseWithLongAndShortOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"-a", "--option1", "value1", "--option2"};
        Options options = new Options();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withValueOptional(true)
                .create();
        Option option2 = OptionBuilder.withShortOpt("a")
                .withLongOpt("option2")
                .hasArg(true)
                .create();
        options.addOption(option1);
        options.addOption(option2);
        CommandLine cmd = parser.parse(options, args);
        assertArrayEquals(new String[]{"value1"}, cmd.getArgs());
        assertTrue(cmd.hasOption("option1"));
        assertSame(option1, cmd.getOption("option1"));
        assertNull(cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertSame(option2, cmd.getOption("option2"));
        assertEquals("value1", cmd.getOptionValue("option2"));
    }
}