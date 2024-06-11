import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-i", "input.txt", "-o", "output.txt"}, options);

        assertEquals("-i", cmd.getOptions().getOption("i").getLongOpt());
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("-o", cmd.getOptions().getOption("o").getLongOpt());
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals("input.txt output.txt", String.join(" ", cmd.getArgs()));
    }

    @Test
    public void testParseWithMissingArgument() {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        assertThrows(MissingArgumentException.class, () -> {
            parser.parse(new String[]{"-i", "input.txt"}, options);
        });
    }

    @Test
    public void testParseWithMissingOption() {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");

        CommandLineParser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(new String[]{"--output", "output.txt"}, options);
        });
    }

    @Test
    public void testParseWithUnexpectedArgument() {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-i", "input.txt", "--unknown", "output.txt"}, options);

        assertEquals("-i", cmd.getOptions().getOption("i").getLongOpt());
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertNull(cmd.getOptionValue("unknown"));
        assertSame(options.getOption("o"), cmd.getOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithMultipleCommands() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("c", "command", true, "command");
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd1 = parser.parse(new String[]{"-c", "cmd1", "-i", "input1.txt"}, options);
        CommandLine cmd2 = parser.parse(new String[]{"-c", "cmd2", "-i", "input2.txt"}, options);

        assertEquals("cmd1", cmd1.getOptionValue("command"));
        assertEquals("input1.txt", cmd1.getOptionValue("input"));
        assertNull(cmd1.getOptionValue("output"));
        assertNull(cmd1.getOptions().getOption("o"));
        assertTrue(cmd1.getArgs().isEmpty());

        assertEquals("cmd2", cmd2.getOptionValue("command"));
        assertEquals("input2.txt", cmd2.getOptionValue("input"));
        assertSame(options.getOption("o"), cmd2.getOption("o"));
        assertEquals("output.txt", cmd2.getOptionValue("output"));
        assertTrue(cmd2.getArgs().isEmpty());
    }

    @Test
    public void testParseWithRequiredOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.setRequired(true);
        group.addOption("g1", "group1", false, "Group 1");
        group.addOption("g2", "group2", false, "Group 2");
        options.addOptionGroup(group);
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--group1", "group1", "--group1", "group2"}, options);

        assertTrue(group.isSelected());
        assertTrue(cmd.getOptions().getOption("g1").isSet());
        assertTrue(cmd.getOptions().getOption("g2").isSet());
        assertSame(options.getOption("o"), cmd.getOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithOptionalOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.setRequired(false);
        group.addOption("g1", "group1", false, "Group 1");
        group.addOption("g2", "group2", false, "Group 2");
        options.addOptionGroup(group);
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--group1", "group1"}, options);

        assertFalse(group.isSelected());
        assertTrue(cmd.getOptions().getOption("g1").isSet());
        assertFalse(cmd.getOptions().getOption("g2").isSet());
        assertSame(options.getOption("o"), cmd.getOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertTrue(cmd.getArgs().isEmpty());
    }
}