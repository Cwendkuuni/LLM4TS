package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ArgumentImplTest {

    private Argument argument;
    private ResourceHelper resourceHelper;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        resourceHelper = new ResourceHelper();
        commandLine = new WriteableCommandLine("TestCommand");
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 5, '=', ' ', null, null, null, 1) {
            @Override
            public Set getPrefixes() {
                return Collections.EMPTY_SET;
            }
        };
        argument.setValueParser(new StringParser());
    }

    @Test
    public void testArgumentName() {
        assertEquals("testArg", argument.getPreferredName());
    }

    @Test
    public void testArgumentDescription() {
        assertEquals("Test Argument", argument.getDescription());
    }

    @Test
    public void testArgumentMinMax() {
        assertEquals(1, argument.getMinimum());
        assertEquals(5, argument.getMaximum());
    }

    @Test
    public void testArgumentProcessValues() throws OptionException {
        List args = new ArrayList();
        args.add("--testArg=value1");
        args.add("--testArg=value2");
        args.add("--testArg=value3");
        args.add("--testArg");
        args.add("--testArg");
        args.add("--consumeRemaining");

        argument.process(commandLine, args.listIterator());

        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test(expected = OptionException.class)
    public void testArgumentProcessValuesInvalidOption() throws OptionException {
        List args = new ArrayList();
        args.add("--invalidOption");

        argument.process(commandLine, args.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testArgumentProcessValuesTooFewValues() throws OptionException {
        List args = new ArrayList();
        args.add("--testArg");

        argument.process(commandLine, args.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testArgumentProcessValuesTooManyValues() throws OptionException {
        List args = new ArrayList();
        args.add("--testArg=value1");
        args.add("--testArg=value2");
        args.add("--testArg=value3");
        args.add("--testArg=value4");

        argument.process(commandLine, args.listIterator());
    }

    @Test
    public void testArgumentCanProcess() {
        assertTrue(argument.canProcess(commandLine, "testValue"));
    }

    @Test
    public void testArgumentGetPrefixes() {
        Set prefixes = argument.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testArgumentValidate() throws OptionException {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");

        Validator validator = new Validator() {
            @Override
            public void validate(List values) throws InvalidArgumentException {
            }
        };

        argument.setValidator(validator);
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testArgumentValidateInvalidArgument() throws OptionException {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        values.add("invalidArgument");

        Validator validator = new Validator() {
            @Override
            public void validate(List values) throws InvalidArgumentException {
                fail();
            }
        };

        argument.setValidator(validator);
        argument.validate(commandLine, argument);
    }

    @Test
    public void testArgumentAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals(" testArg", buffer.toString());
    }

    @Test
    public void testArgumentHelpLines() {
        List<HelpLine> helpLines = argument.helpLines(1, null, null);

        HelpLine helpLine = helpLines.get(0);

        assertEquals("testArg", helpLine.getOptionSpec().getName());
        assertEquals("Test Argument", helpLine.getLongOpt());
        assertEquals("Test Argument", helpLine.getShortOpt());
        assertEquals("", helpLine.getDescription());
        assertEquals(1, helpLine.getMinimum());
        assertEquals(5, helpLine.getMaximum());
        assertEquals("", helpLine.getPrefixes().toString());
    }
}