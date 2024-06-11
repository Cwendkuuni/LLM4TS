package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA"));
        assertFalse(commandLine.hasOption("nonexistentOption"));
        assertTrue(commandLine.hasOption("optionA"));
    }
}