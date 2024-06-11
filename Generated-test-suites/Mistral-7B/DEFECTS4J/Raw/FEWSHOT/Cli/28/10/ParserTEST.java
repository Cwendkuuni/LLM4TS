package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParser() throws Exception {
        // Test empty arguments
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = new Parser().parse(options, arguments);
        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        assertFalse(cmd.hasArgs());

        // Test simple arguments
        options = new Options();
        arguments = new String[]{"arg1", "arg2"};
        cmd = new Parser().parse(options, arguments);
        assertNotNull(cmd);
        assertFalse(cmd.hasOptions());
        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));

        // Test simple options
        options.addOption("o", "opt", false, "Option test");
        cmd = new Parser().parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt"));

        // Test options with values
        options.addOption("v", "value", true, "Value test");
        cmd = new Parser().parse(options, new String[]{"-v", "test"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOptionValue("value"));
        assertEquals("test", cmd.getOptionValue("value"));

        // Test multiple options with values
        options.addOption("a", "arg", true, "Argument test");
        cmd = new Parser().parse(options, new String[]{"-a", "arg1", "-a", "arg2"});
        assertNotNull(cmd);
        List<String> optionValues = cmd.getOptionValues("arg");
        assertEquals(2, optionValues.size());
        assertEquals("arg1", optionValues.get(0));
        assertEquals("arg2", optionValues.get(1));

        // Test options with long names
        options.addOption("longopt", "longoption", false, "Long option test");
        cmd = new Parser().parse(options, new String[]{"--longoption"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("longoption"));

        // Test options with required arguments
        options.addOption("r", "required", true, "Required argument test");
        try {
            cmd = new Parser().parse(options, new String[]{"-r"});
            assertFalse(true);
        } catch (MissingArgumentException e) {
            // Expected
        }
        cmd = new Parser().parse(options, new String[]{"-r", "arg1", "-r", "arg2"});
        assertNotNull(cmd);
        List<String> requiredArgs = cmd.getArgs();
        assertEquals(2, requiredArgs.size());
        assertEquals("arg1", requiredArgs.get(0));
        assertEquals("arg2", requiredArgs.get(1));

        // Test options in groups
        OptionGroup group = new OptionGroup();
        group.setName("groupname");
        group.addOption(new Option("g", "group", false, "Group test"));
        options.addOptionGroup(group);
        cmd = new Parser().parse(options, new String[]{"-g"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("group"));
        assertTrue(group.isSelected());

        // Test multiple options in groups
        group.addOption(new Option("h", "help", false, "Help test"));
        cmd = new Parser().parse(options, new String[]{"-g", "-h"});
        assertNotNull(cmd);
        assertTrue(group.isSelected());
        assertTrue(cmd.hasOption("help"));

        // Test properties
        Properties properties = new Properties();
        properties.setProperty("opt1", "value1");
        properties.setProperty("opt2.subopt", "value2");
        cmd = new Parser().parse(options, new String[0], properties);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option opt1 = optionsList.get(0);
        Option opt2 = optionsList.get(1);
        assertTrue(opt1.hasArg());
        assertEquals("opt1", opt1.getLongOpt());
        assertEquals("value1", opt1.getValue());
        assertTrue(opt2.hasArg());
        assertEquals("opt2", opt2.getLongOpt());
        assertEquals("subopt", opt2.getOpt());
        assertEquals("value2", opt2.getValue());
    }
}