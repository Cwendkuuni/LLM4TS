package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cli = new CommandLine();
        assertFalse(cli.hasOption("-r"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cli = new CommandLine();
        assertNull(cli.getOptionObject("-r"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cli = new CommandLine();
        assertNull(cli.getOptionValue("-r"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cli = new CommandLine();
        assertNull(cli.getOptionValues("-r"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cli = new CommandLine();
        String[] args = cli.getArgs();
        assertEquals(0, args.length);
    }

    @Test
    public void testAddArg() {
        CommandLine cli = new CommandLine();
        cli.addArg("argument");
        assertEquals(1, cli.getArgs().length);
    }

    @Test
    public void testAddOption() {
        CommandLine cli = new CommandLine();
        Option opt = new Option("-f", "File Option");
        cli.addOption(opt);
        assertEquals(opt, cli.getOptions()[0]);
    }

}