
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("f", false, "Option f"));
        options.addOption(new Option("o", true, "Option o"));

        String[] args1 = {"-f"};
        CommandLine cmd1 = new CommandLine();
        cmd1.addOption(new Option("f", false, "Option f"));
        assertEquals(cmd1, new Parser().parse(options, args1));

        String[] args2 = {"-o", "value"};
        CommandLine cmd2 = new CommandLine();
        cmd2.addOption(new Option("o", true, "Option o"));
        cmd2.getOption("o").addValue("value");
        assertEquals(cmd2, new Parser().parse(options, args2));

        String[] args3 = {"-f", "-o", "value"};
        CommandLine cmd3 = new CommandLine();
        cmd3.addOption(new Option("f", false, "Option f"));
        cmd3.addOption(new Option("o", true, "Option o"));
        cmd3.getOption("o").addValue("value");
        assertEquals(cmd3, new Parser().parse(options, args3));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("f", false, "Option f"));
        options.addOption(new Option("o", true, "Option o"));

        Properties properties = new Properties();
        properties.setProperty("o", "value");

        String[] args = {"-f"};
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", false, "Option f"));
        cmd.getOption("o").addValue("value");
        assertEquals(cmd, new Parser().parse(options, args, properties));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("f", false, "Option f"));
        options.addOption(new Option("o", true, "Option o"));

        String[] args = {"-f", "nonOption"};
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", false, "Option f"));
        cmd.addArg("nonOption");
        assertEquals(cmd, new Parser().parse(options, args, true));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());
        options.addOption(Option.builder("o").required().build());

        String[] args = {"-f", "-o", "value"};
        CommandLine cmd = new CommandLine();
        cmd.addOption(Option.builder("f").required().build());
        cmd.addOption(Option.builder("o").required().build());
        cmd.getOption("o").addValue("value");
        assertEquals(cmd, new Parser().parse(options, args));
    }
}
