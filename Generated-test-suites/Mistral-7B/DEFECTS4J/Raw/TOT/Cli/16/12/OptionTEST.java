import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OptionTest {

    @Test
    public void testSimpleOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withValueSeparator('=')
                .withDescription("Test option 1")
                .isRequired(false)
                .create();

        CommandLine commandLine = new CommandLine();

        // process a valid argument
        option.process(commandLine, commandLine.listIterator(0));
        commandLine.addOption(option);
        commandLine.addArgument("option1=value");
        option.process(commandLine, commandLine.listIterator(0));
        assertEquals("value", commandLine.getOptionValue("option1"));

        // process an invalid argument
        try {
            option.process(commandLine, commandLine.listIterator(0));
            commandLine.addArgument("invalidOption");
            option.process(commandLine, commandList.listIterator(0));
            fail("OptionException should have been thrown");
        } catch (OptionException e) {
            // expected
        }

        // test canProcess
        Set triggers = new HashSet(Arrays.asList(new String[]{"option1"}));
        Set prefixes = new HashSet(Arrays.asList(new String[]{"-", "--"}));
        assertTrue(option.canProcess(commandLine, commandLine.listIterator(0), triggers, prefixes));
        assertFalse(option.canProcess(commandLine, commandLine.listIterator(0), triggers, Arrays.asList(new String[]{"invalidPrefix"})));

        // test defaults
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValue("option1"));

        // test getters
        assertEquals("Test option 1", option.getDescription());
        assertEquals("option1", option.getPreferredName());
        assertEquals(1, option.getId());
        assertNull(option.findOption("nonexistentOption"));
        assertSame(option, option.findOption("option1"));
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testNestedOption() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test option 1")
                .isRequired(false)
                .create();

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withValueSeparator('=')
                .withDescription("Test option 2")
                .isRequired(false)
                .create();

        CommandLine commandLine = new CommandLine();

        // process valid arguments
        commandLine.addOption(option1);
        commandLine.addArgument("option1");
        option1.process(commandLine, commandLine.listIterator(0));
        commandLine.addOption(option2);
        commandLine.addArgument("option1");
        commandLine.addArgument("option2=value");
        option2.process(commandLine, commandLine.listIterator(0));
        assertEquals("value", commandLine.getOptionValue("option2"));

        // process an invalid argument
        try {
            commandLine.addArgument("invalidArgument");
            option2.process(commandLine, commandLine.listIterator(0));
            fail("OptionException should have been thrown");
        } catch (OptionException e) {
            // expected
        }

        // test canProcess
        Set triggers = new HashSet(Arrays.asList(new String[]{"option1", "option2"}));
        Set prefixes = new HashSet(Arrays.asList(new String[]{"-", "--"}));
        assertTrue(option1.canProcess(commandLine, commandLine.listIterator(0), triggers, prefixes));
        assertTrue(option2.canProcess(commandLine, commandLine.listIterator(0), triggers, prefixes));
        assertFalse(option2.canProcess(commandLine, commandLine.listIterator(0), triggers, Arrays.asList(new String[]{"--option2"})));

        // test defaults
        option1.defaults(commandLine);
        assertNull(commandLine.getOptionValue("option1"));
        option2.defaults(commandLine);
        assertNull(commandLine.getOptionValue("option2"));

        // test getters
        assertEquals("Test option 1", option1.getDescription());
        assertEquals("Test option 1", option1.getPreferredName());
        assertEquals(1, option1.getId());
        assertEquals("Test option 2", option2.getDescription());
        assertEquals("option2", option2.getPreferredName());
        assertEquals(2, option2.getId());
        assertNull(option1.findOption("nonexistentOption"));
        assertNull(option2.findOption("nonexistentOption"));
        assertSame(option1, option1.findOption("option1"));
        assertSame(option2, option2.findOption("option2"));
        assertFalse(option1.isRequired());
        assertFalse(option2.isRequired());
    }

    @Test
    public void testHelp() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test option 1")
                .isRequired(false)
                .create();

        List<HelpLine> helpLines = option.helpLines(0, new HashSet(), null);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", Arrays.asList(helpLines.toArray(new HelpLine[0])), true);
        String output = new StringBuffer().append(formatter.getHelp()).toString();
        assertTrue(output.contains("--option1"));
        assertTrue(output.contains("Test option 1"));
        assertTrue(output.contains("(value)"));
    }

    @Test
    public void testUsage() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test option 1")
                .isRequired(false)
                .create();

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet(), null);
        String output = buffer.toString();
        assertTrue(output.contains("--option1"));
        assertTrue(output.contains("Test option 1"));
        assertTrue(output.contains("(value)"));
    }
}