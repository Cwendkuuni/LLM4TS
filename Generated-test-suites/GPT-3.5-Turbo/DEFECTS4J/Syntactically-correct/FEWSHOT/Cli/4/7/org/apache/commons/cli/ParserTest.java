package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").argName("file").hasArg().build());
        options.addOption(Option.builder("c").longOpt("create").build());

        String[] args = {"-a", "-b", "file.txt", "--create"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("file.txt", cmd.getOptionValue("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").argName("file").hasArg().required().build());

        String[] args = {"-a"};
        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        String[] args = {"-a", "file.txt", "non-option", "-b"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("b"));
        assertNotNull(cmd.getArgs());
        assertEquals(2, cmd.getArgs().length);
        assertEquals("file.txt", cmd.getArgs()[0]);
        assertEquals("non-option", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").argName("file").hasArg().build());

        String[] args = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "file.txt");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("file.txt", cmd.getOptionValue("b"));
    }
}