package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("a", false, "Add");
        options.addOption("b", true, "Branch");
        options.addOption("c", "command", true, "Command");
        options.addOption(Option.builder("d").numberOfArgs(2).valueSeparator(',').desc("Data").build());
        options.addRequiredOption("e", "example", true, "Example");

        String[] args1 = {"-a", "-c", "command", "-e", "example"};
        CommandLine cmd1 = Parser.parse(options, args1);
        assertTrue(cmd1.hasOption("a"));
        assertTrue(cmd1.hasOption("c"));
        assertTrue(cmd1.hasOption("e"));
        assertNull(cmd1.getOptionValue("b"));
        assertNull(cmd1.getOptionValue("d"));

        String[] args2 = {"-b", "branch", "--data", "value1,value2"};
        CommandLine cmd2 = Parser.parse(options, args2);
        assertTrue(cmd2.hasOption("b"));
        assertTrue(cmd2.hasOption("d"));
        assertNull(cmd2.getOptionValue("c"));
        assertNull(cmd2.getOptionValue("e"));
        assertEquals("value1,value2", cmd2.getOptionValue("d"));
    }
}