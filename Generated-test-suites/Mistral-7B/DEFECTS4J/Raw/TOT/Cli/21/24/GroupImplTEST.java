package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionPrinter;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {
    private Group group;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        List options = Arrays.asList(
                new Option("opt1", "option1", false, "o1"),
                new Option("opt2", "option2", true, "o2"),
                new Option("opt3", "option3", true, "o3"),
                new Option("arg1", "arg1", true, null)
        );
        group = new GroupImpl(options, "group", null, 0, Integer.MAX_VALUE, false);
        formatter = new HelpFormatter();
    }

    @Test
    public void testProcessValidOption() {
        CommandLine cmd = TestUtils.createCommandLine("-o1 --opt2 --opt3 arg1");
        group.process(cmd, null);
        assertEquals("o1", cmd.getOptionValue("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertTrue(cmd.hasOption("opt3"));
        assertTrue(cmd.hasOption("arg1"));
    }

    @Test
    public void testProcessDuplicateOption() {
        CommandLine cmd = TestUtils.createCommandLine("-o1 --opt1 --opt1");
        assertThrows(OptionException.class, () -> group.process(cmd, null));
    }

    @Test
    public void testProcessUnknownOption() {
        CommandLine cmd = TestUtils.createCommandLine("-x");
        assertFalse(group.canProcess(cmd, "x"));
    }

    @Test
    public void testProcessAnonymousArgument() {
        CommandLine cmd = TestUtils.createCommandLine("--arg1 value");
        group.process(cmd, null);
        assertEquals("value", cmd.getOptionValue("arg1"));
    }

    @Test
    public void testProcessMultipleAnonymousArguments() {
        CommandLine cmd = TestUtils.createCommandLine("--arg1 value1 --arg1 value2");
        group.process(cmd, null);
        assertEquals("value1", cmd.getOptionValue("arg1"));
        assertEquals("value2", cmd.getOptionValue("arg1"));
    }

    @Test
    public void testProcessMultipleArguments() {
        CommandLine cmd = TestUtils.createCommandLine("--arg1 value1 --arg1 value2 arg2 arg3");
        group.process(cmd, null);
        assertEquals("value1", cmd.getOptionValue("arg1"));
        assertEquals("value2", cmd.getOptionValue("arg1"));
        assertNull(cmd.getOptionValue("arg2"));
        assertNull(cmd.getOptionValue("arg3"));
    }

    @Test
    public void testValidateMinimumOptions() {
        CommandLine cmd = TestUtils.createCommandLine("");
        assertThrows(OptionException.class, () -> group.validate(cmd));
    }

    @Test
    public void testValidateMaximumOptions() {
        CommandLine cmd = TestUtils.createCommandLine(Arrays.asList("--opt1", "--opt1", "--opt1", "--opt1").toArray(new String[0]));
        assertThrows(OptionException.class, () -> group.validate(cmd));
    }

    @Test
    public void testValidateMinimumAndMaximumOptions() {
        CommandLine cmd = TestUtils.createCommandLine(Arrays.asList("--opt1", "--opt1").toArray(new String[0]));
        group.validate(cmd);
    }

    @Test
    public void testPrintUsage() {
        StringWriter writer = new StringWriter();
        group.appendUsage(new PrintWriter(writer), Collections.emptySet(), null);
        String usage = writer.toString();
        HelpLine helpLine = new HelpLine(usage);
        OptionPrinter printer = formatter.getHelpFormatter().getHelpPrinter();
        String expectedUsage = "group:\n" +
                "opt1 OPTIONAL\n" +
                "option1\n\n" +
                "opt2 REQUIRED\n" +
                "option2\n\n" +
                "opt3 MULTI\n" +
                "option3\n" +
                "arg1 REQUIRED\n" +
                "arg1:ARG\n";
        printer.printHelp(0, null, usage, true);
        assertEquals(expectedUsage, usage);
    }
}