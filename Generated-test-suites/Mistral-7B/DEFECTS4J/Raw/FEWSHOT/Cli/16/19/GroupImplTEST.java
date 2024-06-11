package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.GroupImpl;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("a", "a", false, "A option");
        Option option2 = new Argument(null, "b", true, "B argument");
        Option option3 = new Option("c", "c", false, "C option");
        Option option4 = new Option("d", "d", false, "D option");
        Option option5 = new Option("e", "e", false, "E option");
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);
        options.add(option5);

        GroupImpl group = new GroupImpl(options, null, null, 1, 5);
        assertEquals(5, group.getOptions().size());
        assertEquals(1, group.getMinimum());
        assertEquals(5, group.getMaximum());
        assertNull(group.getName());
        assertNull(group.getDescription());

        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        Set<String> triggers = group.getTriggers();
        assertEquals(5, triggers.size());
        assertTrue(triggers.containsAll(Arrays.asList("a", "b", "c", "d", "e")));

        assertFalse(group.canProcess(null, "a"));
        assertTrue(group.canProcess(null, "b"));

        WriteableCommandLine cmdLine = new WriteableCommandLine("test");
        List<String> arguments = new ArrayList<>();

        try {
            group.process(cmdLine, arguments.listIterator());
            fail();
        } catch (OptionException e) {
            // expected
        }

        // no prefixes
        assertEquals(0, group.validate(cmdLine).size());

        // missing option
        cmdLine.addOption("z");
        List<String> errors = group.validate(cmdLine);
        assertEquals(1, errors.size());
        assertEquals("Missing option: z", errors.get(0));

        // too few options
        cmdLine.removeOption("z");
        errors = group.validate(cmdLine);
        assertEquals(1, errors.size());
        assertEquals("Missing option: a", errors.get(0));

        // too many options
        cmdLine.addOption("f");
        errors = group.validate(cmdLine);
        assertEquals(1, errors.size());
        assertEquals("Too many options: f", errors.get(0));

        // validate anonymous arguments
        List<Argument> anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertEquals("b", anonymous.get(0).getLongOpt());
        assertEquals(true, anonymous.get(0).isRequired());

        cmdLine.clear();
        arguments.clear();

        cmdLine.addOption("a");
        group.process(cmdLine, arguments.listIterator());
        assertEquals("A option", cmdLine.getOptionValue("a"));

        cmdLine.clear();
        arguments.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("b");
        group.process(cmdLine, arguments.listIterator());
        assertEquals("A option", cmdLine.getOptionValue("a"));
        assertEquals("B argument", cmdLine.getArgumentValue(0));

        cmdLine.clear();
        arguments.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("b");
        cmdLine.addOption("b");
        errors = group.validate(cmdLine);
        assertEquals(1, errors.size());
        assertEquals("Too many options: b", errors.get(0));

        cmdLine.clear();
        arguments.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("b");
        cmdLine.addOption("c");
        arguments.add("1");
        group.process(cmdLine, arguments.listIterator());
        assertEquals("A option", cmdLine.getOptionValue("a"));
        assertEquals("B argument", cmdLine.getArgumentValue(0));
        assertEquals("1", cmdLine.getArgumentValue(1));

        cmdLine.clear();
        arguments.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("b");
        cmdLine.addOption("c");
        arguments.add("1");
        arguments.add("2");
        List<String> actual = group.helpLines(1, Collections.emptySet(), null);
        List<String> expected = List.of(
                "  -a                        A option",
                "  -b                        B argument (1)",
                "                            B argument (2)",
                "  -c                        C option"
        );
        assertEquals(expected, actual);

        List<HelpLine> helpLines = group.helpLines(Integer.MAX_VALUE, Collections.emptySet(), null);
        assertEquals(11, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        assertEquals("-a", helpLine.getOptionSpec().getLongOpt());
        assertEquals("A option", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(1);
        assertEquals("--b <ARG>", helpLine.getOptionSpec().getLongOpt());
        assertEquals("B argument", helpLine.getDescription());
        assertEquals(Argument.REQUIRED_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(2);
        assertEquals("--b", helpLine.getOptionSpec().getLongOpt());
        assertEquals("B argument", helpLine.getDescription());
        assertEquals(Argument.OPTIONAL_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(3);
        assertEquals("-c", helpLine.getOptionSpec().getLongOpt());
        assertEquals("C option", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(4);
        assertEquals("--", helpLine.getOptionSpec().getLongOpt());
        assertEquals("all options", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(5);
        assertEquals("--help", helpLine.getOptionSpec().getLongOpt());
        assertEquals("display help for commands, options, and arguments", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(6);
        assertEquals("--version", helpLine.getOptionSpec().getLongOpt());
        assertEquals("print version string", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(7);
        assertEquals("--quiet", helpLine.getOptionSpec().getLongOpt());
        assertEquals("suppress all error messages, including success messages", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(8);
        assertEquals("--verbose", helpLine.getOptionSpec().getLongOpt());
        assertEquals("suppress error messages", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(9);
        assertEquals("--disable-regex", helpLine.getOptionSpec().getLongOpt());
        assertEquals("disable option parsing with regular expressions", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(10);
        assertEquals("--disable-defaults", helpLine.getOptionSpec().getLongOpt());
        assertEquals("disable default parsing of options and arguments", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());

        helpLine = helpLines.get(10);
        assertEquals("-h", helpLine.getOptionSpec().getLongOpt());
        assertEquals("display help for commands, options, and arguments", helpLine.getDescription());
        assertEquals(Option.NO_ARGUMENT, helpLine.getArgType());
    }
}