package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option1 = new Option("o", "output", true, "Output file path")
                .requiredOpt();
        Option option2 = new Option("i", "input", true, "Input file path")
                .requiredOpt()
                .argName("FILE")
                .argsNumber(1);
        Option option3 = new Option("h", "help", false, "Print help message");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1,
                Collections.singletonList("inputFile"));

        cmd.addOption(option2);
        cmd.addOption(option3);

        cmd.addValue(option1, "/output/file.txt");
        cmd.addValue(option2, "/input/file.txt");

        cmd.addSwitch(option3, true);

        // Test getters
        assertEquals("/output/file.txt", cmd.getProperty("output", ""));
        assertEquals(true, cmd.hasOption(option3));
        assertEquals(Collections.singletonList("/input/file.txt"),
                cmd.getValues(option2, null));
        assertEquals(Boolean.TRUE, cmd.getSwitch(option3, false));

        // Test toString
        assertEquals("-o /output/file.txt -i /input/file.txt", cmd.toString());

        // Test getOptions
        List<Option> options = cmd.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));

        // Test getOptionTriggers
        Set<String> triggers = cmd.getOptionTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("o"));
        assertTrue(triggers.contains("i"));
        assertTrue(triggers.contains("h"));

        // Test setDefaultValues
        cmd.setDefaultValues(option2, Arrays.asList("defaultInput.txt"));
        assertEquals(Arrays.asList("defaultInput.txt"),
                cmd.getValues(option2, null));

        // Test setDefaultSwitch
        cmd.setDefaultSwitch(option3, false);
        assertFalse(cmd.getSwitch(option3, true));

        // Test looksLikeOption
        assertTrue(cmd.looksLikeOption("o"));
        assertFalse(cmd.looksLikeOption("x"));
    }
}