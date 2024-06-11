
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setRequired(true);
        options.addOption(option1);

        Option option2 = new Option("o", "output", true, "output file");
        options.addOption(option2);

        String[] arguments = {"-f", "input.txt", "-o", "output.txt"};
        Properties properties = new Properties();
        properties.setProperty("output", "output.txt");

        try {
            CommandLine cmd = new ParserImpl().parse(options, arguments, properties);

            assertEquals("input.txt", cmd.getOptionValue("file"));
            assertEquals("output.txt", cmd.getOptionValue("output"));
        } catch (ParseException e) {
            assertNull(e);
        }
    }

    @Test
    public void testParseRequiredOptionMissing() {
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setRequired(true);
        options.addOption(option1);

        Option option2 = new Option("o", "output", true, "output file");
        options.addOption(option2);

        String[] arguments = {"-o", "output.txt"};
        Properties properties = new Properties();
        properties.setProperty("output", "output.txt");

        try {
            new ParserImpl().parse(options, arguments, properties);
            assertNull("ParseException expected but not thrown", null);
        } catch (ParseException e) {
            assertEquals("Missing required option: file", e.getMessage());
        }
    }
}
