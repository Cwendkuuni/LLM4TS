import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    @Test
    public void testSimpleOption() {
        Option option = new Option.Builder()
                .longOpt("option1")
                .hasArg(false)
                .build();

        Set<String> triggers = new HashSet<>(Arrays.asList("--option1"));
        Set<String> prefixes = new HashSet<>(Collections.singletonList("--"));

        assertEquals("option1", option.getPreferredName());
        assertEquals("An option", option.getDescription());
        assertEquals(1, option.getId());
        assertNull(option.findOption("unknownTrigger"));
        assertEquals(triggers, option.getTriggers());
        assertEquals(prefixes, option.getPrefixes());

        WriteableCommandLine commandLine = new CommandLine();
        ListIterator<String> args = Arrays.asList("--option1", "arg1", "arg2").listIterator();

        option.process(commandLine, args);
        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("unknownOption"));

        option.defaults(commandLine);
        assertNull(commandLine.getOptionValue("option1"));

        CommandLine parsed = new DefaultParser().parse(Arrays.asList("--option1"), commandLine.getArgs());
        assertEquals(parsed.getOptionValues("option1"), new String[]{});

        assertThrows(OptionException.class, () -> option.process(commandLine, args));
        assertThrows(OptionException.class, () -> option.process(commandLine, args));

        option.validate(commandLine);

        List<HelpLine> helpLines = option.helpLines(1, Collections.emptySet(), null);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "1.0", helpLines, true);
    }

    @Test
    public void testOptionWithArgument() {
        Option option = new Option.Builder()
                .longOpt("option2")
                .hasArg(true)
                .build();

        Set<String> triggers = new HashSet<>(Arrays.asList("--option2"));
        Set<String> prefixes = new HashSet<>(Collections.singletonList("--"));

        assertEquals("option2", option.getPreferredName());
        assertEquals("An option with an argument", option.getDescription());
        assertEquals(2, option.getId());
        assertNull(option.findOption("unknownTrigger"));
        assertEquals(triggers, option.getTriggers());
        assertEquals(prefixes, option.getPrefixes());

        WriteableCommandLine commandLine = new CommandLine();
        ListIterator<String> args = Arrays.asList("--option2", "argValue1", "argValue2").listIterator();

        option.process(commandLine, args);
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.getOptionValues("option2").length > 0);

        option.defaults(commandLine);
        assertNull(commandLine.getOptionValue("option2"));

        CommandLine parsed = new DefaultParser().parse(Arrays.asList("--option2", "argValue3"), commandLine.getArgs());
        assertEquals(parsed.getOptionValues("option2"), new String[]{"argValue3"});

        assertThrows(OptionException.class, () -> option.process(commandLine, args));
        assertThrows(OptionException.class, () -> option.process(commandLine, args));

        option.validate(commandLine);

        List<HelpLine> helpLines = option.helpLines(1, Collections.emptySet(), null);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "1.0", helpLines, true);
    }
}