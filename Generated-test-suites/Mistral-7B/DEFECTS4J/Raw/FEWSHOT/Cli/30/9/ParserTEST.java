package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseNoArguments() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message.");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);

        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.hasArgs());
        assertTrue(cmd.hasOption("h"));
    }

    @Test
    public void testParseSingleArgument() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message.");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg"});

        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.hasArg("arg"));
        assertTrue(cmd.hasArg(0));
        assertTrue(cmd.getArg(0).equals("arg"));
        assertFalse(cmd.hasOption("h"));
    }

    @Test
    public void testParseMultipleArguments() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message.");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});

        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.hasArg("arg1"));
        assertFalse(cmd.hasArg("arg2"));
        assertTrue(cmd.hasArgs());
        assertTrue(cmd.getArgs().size() == 2);
        assertTrue(cmd.getArgs().get(0).equals("arg1"));
        assertTrue(cmd.getArgs().get(1).equals("arg2"));
        assertFalse(cmd.hasOption("h"));
    }

    @Test
    public void testParseOptionalArgument() throws Exception {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file.");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "out.txt"});

        assertNotNull(cmd);
        assertTrue(cmd.hasOptionValue("o"));
        assertTrue(cmd.getOptionValue("o").equals("out.txt"));
        assertFalse(cmd.hasArg(0));
        assertFalse(cmd.hasArg(1));
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseOptionalArgumentWithValue() throws Exception {
        Options options = new Options();
        options.addOption("o", "output", true, "Output file.");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "out.txt", "arg"});

        assertNotNull(cmd);
        assertTrue(cmd.hasOptionValue("o"));
        assertTrue(cmd.getOptionValue("o").equals("out.txt"));
        assertTrue(cmd.hasArg(0));
        assertTrue(cmd.getArg(0).equals("arg"));
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseRequiredArgument() throws Exception {
        Options options = new Options();
        options.addOption("r", "required", true, "Required argument.");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingArgumentException e) {
            // Expected
        }

        Parser parser2 = new DefaultParser();
        CommandLine cmd = parser2.parse(options, new String[]{"-r"});

        assertNotNull(cmd);
        assertTrue(cmd.hasOptionValue("r"));
        assertNull(cmd.getOptionValue("r"));
        assertFalse(cmd.hasArg(0));
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseRequiredArgumentWithValue() throws Exception {
        Options options = new Options();
        options.addOption("r", "required", true, "Required argument.");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingArgumentException e) {
            // Expected
        }

        Parser parser2 = new DefaultParser();
        CommandLine cmd = parser2.parse(options, new String[]{"-r", "value"});

        assertNotNull(cmd);
        assertTrue(cmd.hasOptionValue("r"));
        assertTrue(cmd.getOptionValue("r").equals("value"));
        assertFalse(cmd.hasArg(0));
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseMultipleRequiredArguments() throws Exception {
        Options options = new Options();
        options.addOption("r1", "required1", true, "Required argument 1.");
        options.addOption("r2", "required2", true, "Required argument 2.");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            // Expected
        }

        Parser parser2 = new DefaultParser();
        String[] args = {"-r1", "arg1", "-r2", "arg2"};
        CommandLine cmd = parser2.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOptionValue("r1"));
        assertTrue(cmd.getOptionValue("r1").equals("arg1"));
        assertTrue(cmd.hasOptionValue("r2"));
        assertTrue(cmd.getOptionValue("r2").equals("arg2"));
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseProperties() throws Exception {
        Options options = new Options();
        options.addOption("p", "property", true, "Property name.");
        options.addOption("v", "value", true, "Property value.");

        Properties props = new Properties();
        props.setProperty("p", "value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], props);

        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.hasArgs());
        assertNull(cmd.getOptionValue("p"));
        assertNull(cmd.getOptionValue("v"));

        options.addOption("p", "property", true, "Property name.");
        options.addOption("v", "value", true, "Property value.");

        props.clear();
        props.setProperty("p", "value1");
        props.setProperty("v", "value2");

        parser = new DefaultParser();
        cmd = parser.parse(options, new String[0], props);

        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.hasArgs());
        assertTrue(cmd.hasOptionValue("p"));
        assertTrue(cmd.getOptionValue("p").equals("p"));
        assertTrue(cmd.hasOptionValue("v"));
        assertTrue(cmd.getOptionValue("v").equals("value2"));
    }

    @Test
    public void testParseMissingRequiredOptions() throws Exception {
        Options options = new Options();
        options.addOption("r1", "required1", true, "Required argument 1.");
        options.addOption("r2", "required2", true, "Required argument 2.");

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add("r1");
        requiredOptions.add("r2");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, new String[0], null, true);
            fail();
        } catch (MissingOptionException e) {
            assertTrue(Arrays.equals(e.getMissingOptions().toArray(), requiredOptions.toArray()));
        }
    }

    @Test
    public void testParseMissingRequiredOptionGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequiredOptions(Arrays.asList("r1", "r2"));
        options.addOptionGroup(group);
        options.addOption("r1", "required1", true, "Required argument 1.");

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add("group");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, new String[0], null, true);
            fail();
        } catch (MissingOptionException e) {
            assertTrue(Arrays.equals(e.getMissingOptions().toArray(), requiredOptions.toArray()));
        }
    }
}