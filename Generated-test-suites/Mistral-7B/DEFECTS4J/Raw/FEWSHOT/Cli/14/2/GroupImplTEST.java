package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testCanProcess() throws OptionException {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "group description", 0, 1);

        // test with null commandLine and arg
        assertFalse(group.canProcess(null, null));

        // test with valid input
        final WriteableCommandLine commandLine = new WriteableCommandLine("");
        final String arg = "opt";
        assertTrue(group.canProcess(commandLine, arg));

        // test with invalid input
        assertFalse(group.canProcess(commandLine, "invalid"));

        // test with multiple options
        final Option opt1 = new OptionBuilder()
                .withLongOpt("opt1")
                .withDescription("Option 1")
                .build();
        options.add(opt1);

        final Option opt2 = new OptionBuilder()
                .withLongOpt("opt2")
                .withDescription("Option 2")
                .build();
        options.add(opt2);

        // test with option trigger
        assertTrue(group.canProcess(commandLine, "opt1"));
        assertFalse(group.canProcess(commandLine, "opt2"));
        assertFalse(group.canProcess(commandLine, "opt3")); // should trigger an error

        // test with option prefix
        assertTrue(group.canProcess(commandLine, "--opt1"));
        assertFalse(group.canProcess(commandLine, "--opt2"));
        assertFalse(group.canProcess(commandLine, "--opt3")); // should trigger an error

        // test with option shortcut
        assertTrue(group.canProcess(commandLine, "-opt1"));
        assertFalse(group.canProcess(commandLine, "-opt2"));
        assertFalse(group.canProcess(commandLine, "-opt3")); // should trigger an error

        // test with anonymous argument
        final Option argOpt = new OptionBuilder()
                .withLongOpt("arg")
                .withDescription("Argument")
                .isRequired(false)
                .build();
        options.add(argOpt);

        // test with valid argument
        assertFalse(commandLine.hasOption(argOpt));
        commandLine.parse("-g " + argOpt.getLongOpt() + " value");
        assertTrue(commandLine.hasOption(argOpt));
        assertFalse(group.canProcess(commandLine, argOpt.getLongOpt()));
        assertEquals("value", argOpt.getValue(commandLine));

        // test with invalid argument
        assertFalse(commandLine.hasOption(argOpt));
        commandLine.parse("-g " + argOpt.getLongOpt() + " value value");
        assertTrue(group.canProcess(commandLine, argOpt.getLongOpt()));
        assertNull(argOpt.getValue(commandLine));

        // test with multiple arguments
        final Option argOpt2 = new OptionBuilder()
                .withLongOpt("arg2")
                .withDescription("Argument 2")
                .isRequired(false)
                .build();
        options.add(argOpt2);

        commandLine.parse("-g " + argOpt.getLongOpt() + " value");
        commandLine.parse("-g " + argOpt2.getLongOpt() + " value2");
        assertTrue(group.canProcess(commandLine, argOpt.getLongOpt()));
        assertEquals("value", argOpt.getValue(commandLine));
        assertTrue(group.canProcess(commandLine, argOpt2.getLongOpt()));
        assertEquals("value2", argOpt2.getValue(commandLine));

        // test with required argument
        final Option reqArgOpt = new OptionBuilder()
                .withLongOpt("reqArg")
                .withDescription("Required argument")
                .isRequired(true)
                .build();
        options.add(reqArgOpt);

        try {
            commandLine.parse("-g");
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.MISSING_ARGUMENT_VALUE, e.getMessage());
            assertEquals(reqArgOpt, e.getOption());
        }
    }

    @Test
    public void testValidate() throws OptionException {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "group description", 0, 1);

        final WriteableCommandLine commandLine = new WriteableCommandLine("");

        // test with empty command line
        group.validate(commandLine);

        // test with valid input
        commandLine.parse("--group opt1 value");
        group.validate(commandLine);

        // test with too few options
        commandLine.parse("--group");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.MISSING_OPTION, e.getMessage());
        }

        // test with too many options
        commandLine.parse("--group opt1 value opt1 value opt2 value");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.TOO_MANY_VALUES, e.getMessage());
        }

        // test with invalid option
        commandLine.parse("--group invalid");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.UNRECOGNISED_OPTION, e.getMessage());
        }

        // test with invalid argument
        final Option argOpt = new OptionBuilder()
                .withLongOpt("arg")
                .withDescription("Argument")
                .isRequired(false)
                .build();
        options.add(argOpt);

        commandLine.parse("--group opt1 value");
        commandLine.parse("--group " + argOpt.getLongOpt() + " value value");
        commandLine.parse("--group opt1 value value value");

        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.TOO_MANY_VALUES, e.getMessage());
        }

        // test with required argument
        final Option reqArgOpt = new OptionBuilder()
                .withLongOpt("reqArg")
                .withDescription("Required argument")
                .isRequired(true)
                .build();
        options.add(reqArgOpt);

        commandLine.parse("--group opt1 value");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.MISSING_ARGUMENT_VALUE, e.getMessage());
            assertEquals(reqArgOpt, e.getOption());
        }
    }

    @Test
    public void testProcess() throws OptionException {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "group description", 0, 1);

        final WriteableCommandLine commandLine = new WriteableCommandLine("");

        // test with no arguments
        commandLine.parse("--group opt1");
        group.process(commandLine, commandLine.parser().getPositionalArguments());
        // nothing should have been processed
        assertEquals(0, commandLine.getOptions().size());

        // test with valid input
        commandLine.parse("--group opt1 value1 value2");
        group.process(commandLine, commandLine.parser().getPositionalArguments());
        final Option opt = new OptionBuilder()
                .withLongOpt("opt1")
                .withDescription("Option 1")
                .build();
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(opt, commandLine.getOptionByName("opt1"));
        assertEquals("value1", opt.getValue(commandLine));
        assertEquals("value2", opt.getValues(commandLine).get(1));

        // test with multiple options
        final Option opt1 = new OptionBuilder()
                .withLongOpt("opt1")
                .withDescription("Option 1")
                .build();
        final Option opt2 = new OptionBuilder()
                .withLongOpt("opt2")
                .withDescription("Option 2")
                .build();
        options.add(opt1);
        options.add(opt2);

        commandLine.parse("--group opt1 value1 opt2 value2");
        group.process(commandLine, commandLine.parser().getPositionalArguments());
        assertEquals(2, commandLine.getOptions().size());
        assertEquals(opt1, commandLine.getOptionByName("opt1"));
        assertEquals(opt2, commandLine.getOptionByName("opt2"));
        assertEquals("value1", opt1.getValue(commandLine));
        assertEquals("value2", opt2.getValue(commandLine));

        // test with anonymous argument
        final Option argOpt = new OptionBuilder()
                .withLongOpt("arg")
                .withDescription("Argument")
                .isRequired(false)
                .build();
        options.add(argOpt);

        commandLine.parse("--group opt1 value1");
        commandLine.parse("--group " + argOpt.getLongOpt() + " value");
        group.process(commandLine, commandLine.parser().getPositionalArguments());
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(opt1, commandLine.getOptionByName("opt1"));
        assertEquals("value1", opt1.getValue(commandLine));
        assertEquals("value", argOpt.getValue(commandLine));

        // test with required argument
        final Option reqArgOpt = new OptionBuilder()
                .withLongOpt("reqArg")
                .withDescription("Required argument")
                .isRequired(true)
                .build();
        options.add(reqArgOpt);

        commandLine.parse("--group opt1 value1");
        try {
            group.process(commandLine, commandLine.parser().getPositionalArguments());
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.MISSING_ARGUMENT_VALUE, e.getMessage());
            assertEquals(reqArgOpt, e.getOption());
        }
    }

    @Test
    public void testGetPrefixes() {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "group description", 0, 1);

        final Set prefixes = group.getPrefixes();
        assertEquals(0, prefixes.size());

        final Option opt = new OptionBuilder()
                .withLongOpt("opt")
                .withDescription("Option")
                .build();
        options.add(opt);

        prefixes = group.getPrefixes();
        assertEquals("--", prefixes.toString());

        final Option prefixOpt = new OptionBuilder()
                .withLongOpt("prefix")
                .withDescription("Prefix")
                .isRequired(false)
                .build();
        options.add(prefixOpt);

        prefixes = group.getPrefixes();
        assertEquals("--prefix", prefixes.toString());
    }

    @Test
    public void testGetTriggers() {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", "group description", 0, 1);

        final Set triggers = group.getTriggers();
        assertEquals(0, triggers.size());

        final Option opt = new OptionBuilder()
                .withLongOpt("opt")
                .withDescription("Option")
                .build();
        options.add(opt);

        triggers = group.getTriggers();
        assertEquals(opt.getLongOpt(), triggers.toString());

        final Option opt2 = new OptionBuilder()
                .withLongOpt("opt2")
                .withDescription("Option 2")
                .build();
        options.add(opt2);

        triggers = group.getTriggers();
        final List<String> triggersList = new ArrayList<>(triggers);
        triggersList.remove(opt.getLongOpt());
        assertEquals(triggersList, group.getTriggers());
    }
}