package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("t", "test", false, "Run tests"));
        assertTrue(commandLine.hasOption("t"));
        assertTrue(commandLine.hasOption('t'));
        assertFalse(commandLine.hasOption("f"));
        assertFalse(commandLine.hasOption('f'));
    }
}