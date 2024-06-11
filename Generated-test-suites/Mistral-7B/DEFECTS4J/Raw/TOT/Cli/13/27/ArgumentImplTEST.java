package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.StringArrayType;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.ResourceType;
import org.apache.commons.cli2.validator.IntRangeValidator;
import org.apache.commons.cli2.validator.RangeValidator;

public class ArgumentImplTest {

    @org.junit.Test
    public void testSimpleArgument() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument")
                .setMinimumNumber(1)
                .setMaximumNumber(1)
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process argument
        arg.process(cmd, Arrays.asList("arg1").listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testMultipleArguments() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument")
                .setMinimumNumber(2)
                .setMaximumNumber(2)
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList("arg1", "arg2").listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testOptionalArgument() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An optional argument")
                .setMinimumNumber(0)
                .setMaximumNumber(1)
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList().listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertTrue(values.isEmpty());

        // process arguments
        arg.process(cmd, Arrays.asList("arg1").listIterator());

        // check values
        values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testConsumeRemainingArgument() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An optional argument consuming remaining")
                .setMinimumNumber(0)
                .setMaximumNumber(1)
                .setConsumeRemaining(true)
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        cmd.addValue("--arg", "");
        arg.process(cmd, Arrays.asList().listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertTrue(values.isEmpty());

        // process arguments
        cmd.addValue("--arg", "arg1");
        cmd.addValue("--arg", "arg2");
        arg.process(cmd, Arrays.asList().listIterator());

        // check values
        values = cmd.getValues(arg);
        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testInvalidArgument() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument")
                .setMinimumNumber(1)
                .setMaximumNumber(1)
                .setValidator(new RangeValidator(0, 1))
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process invalid arguments
        assertThrows(OptionException.class, () -> arg.process(cmd, Arrays.asList("2").listIterator()));
        assertThrows(OptionException.class, () -> arg.process(cmd, Arrays.asList("foo").listIterator()));

        // check values
        List<String> values = cmd.getValues(arg);
        assertTrue(values.isEmpty());

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testArgumentWithDefaultValue() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument with default value")
                .setMinimumNumber(0)
                .setMaximumNumber(1)
                .setDefaultValues(Collections.singletonList("default"))
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList().listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("default", values.get(0));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());

        // process arguments
        arg.process(cmd, Arrays.asList("arg1").listIterator());

        // check values
        values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        // check option
        opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testArgumentWithMultipleDefaultValues() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument with multiple default values")
                .setMinimumNumber(0)
                .setMaximumNumber(1)
                .setDefaultValues(Arrays.asList("default1", "default2"))
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList().listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(2, values.size());
        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));

        // process arguments
        arg.process(cmd, Arrays.asList("arg1").listIterator());

        // check values
        values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testArgumentWithSeparators() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument with separators")
                .setMinimumNumber(1)
                .setMaximumNumber(1)
                .setInitialSeparator('-')
                .setSubsequentSeparator(',')
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList("-a,arg1,arg2").listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());

        // process arguments
        arg.process(cmd, Arrays.asList("-a,arg1").listIterator());

        // check values
        values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        // check option
        opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testArgumentWithNoDefaultValue() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument with no default value")
                .setMinimumNumber(1)
                .setMaximumNumber(1)
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList("arg1").listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());

        // process arguments
        assertThrows(OptionException.class, () -> arg.process(cmd, Arrays.asList().listIterator()));
    }

    @org.junit.Test
    public void testArgumentWithRangeValidator() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument with validator")
                .setMinimumNumber(1)
                .setMaximumNumber(1)
                .setValidator(new IntRangeValidator(0, 10))
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList("0").listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("0", values.get(0));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());

        // process arguments
        assertThrows(OptionException.class, () -> arg.process(cmd, Arrays.asList("11").listIterator()));

        // check values
        values = cmd.getValues(arg);
        assertTrue(values.isEmpty());

        // check option
        opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testArgumentWithStringArrayType() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("args")
                .setDescription("An argument as string array")
                .setMinimumNumber(1)
                .setMaximumNumber(Integer.MAX_VALUE)
                .setType(StringArrayType.INSTANCE)
                .create();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList("arg1", "arg2").listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());

        // process arguments
        arg.process(cmd, Arrays.asList("arg1", "arg2", "arg3").listIterator());

        // check values
        values = cmd.getValues(arg);
        assertEquals(3, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("arg3", values.get(2));

        // check option
        opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());
    }

    @org.junit.Test
    public void testArgumentWithResourceHelper() {
        // create argument
        Argument arg = new ArgumentBuilder()
                .setName("arg")
                .setDescription("An argument with resource helper")
                .setMinimumNumber(1)
                .setMaximumNumber(1)
                .setHelpName("arg.name")
                .setHelpAlias("a")
                .setHelpMessageKey("arg.message")
                .create();

        // setup resource helper
        ResourceHelper resourceHelper = new ResourceHelper();
        resourceHelper.loadDefaultResources();

        // create command line
        WriteableCommandLine cmd = new DefaultCommandLine();

        // process arguments
        arg.process(cmd, Arrays.asList("arg1").listIterator());

        // check values
        List<String> values = cmd.getValues(arg);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        // check option
        Option opt = (Option) arg;
        assertFalse(opt.isSet());
        assertNull(opt.getValue());

        // check help
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("Test", arg.getHelpLine("arg.name", null), false);
        assertTrue(help.contains("arg.message"));

        // check help with alias
        help = formatter.printHelp("Test", arg.getHelpLine("arg.name", "a"), false);
        assertTrue(help.contains("arg.message"));
    }
}