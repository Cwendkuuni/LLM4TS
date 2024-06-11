
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("h", "help", false, "Display help");
        options.addOption("v", "verbose", false, "Enable verbose mode");
        options.addOption("f", "file", true, "Specify a file");

        String[] args = {"-h", "-v", "-f", "test.txt"};
        CommandLine cmd = null;
        try {
            cmd = new Parser().parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("f"));

        assertEquals("test.txt", cmd.getOptionValue("file"));
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("h", "help", false, "Display help");
        options.addOption("v", "verbose", false, "Enable verbose mode");
        options.addOption("f", "file", true, "Specify a file");

        String[] args = {"-h"};
        Properties properties = new Properties();
        properties.put("verbose", "true");
        properties.put("file", "test.txt");

        CommandLine cmd = null;
        try {
            cmd = new Parser().parse(options, args, properties);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("f"));

        assertEquals("test.txt", cmd.getOptionValue("file"));
    }
}
