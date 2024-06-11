package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.validator.IntRangeValidator;
import org.apache.commons.cli2.validator.NoArgValidator;
import org.apache.commons.cli2.validator.StringValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertFalse;
import org.junit.Assert.assertNull;
import org.junit.Assert.assertSame;
import org.junit.Assert.assertThrows;
import org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.cli2.resource.ResourceConstants.*;

public class ArgumentImplTest {
    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        argument = new ArgumentImpl(
                "argName",
                "Description",
                1,
                3,
                ';',
                ':',
                new StringValidator(),
                "--",
                Arrays.asList("default1", "default2"),
                1
        );
        commandLine = new WriteableCommandLine();
        option = new Option("testOption", "Test Option", true, argument);
    }

    @Test
    public void testArgumentImplConstructor() throws Exception {
        ArgumentImpl argument = new ArgumentImpl(
                "argName",
                "Description",
                1,
                3,
                ';',
                ':',
                new StringValidator(),
                "--",
                Arrays.asList("default1", "default2"),
                1
        );
        assertEquals("argName", argument.getName());
        assertEquals("Description", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(3, argument.getMaximum());
        assertEquals(';', argument.getInitialSeparator());
        assertEquals(':', argument.getSubsequentSeparator());
        assertEquals(true, argument.isSubsequentSplit());
        assertNull(argument.getValidator());
        assertEquals("--", argument.getConsumeRemaining());
        assertNotNull(argument.getDefaultValues());
        assertEquals(2, argument.getDefaultValues().size());
        assertEquals("default1", argument.getDefaultValues().get(0));
        assertEquals("default2", argument.getDefaultValues().get(1));
    }

    @Test
    public void testGetPreferredName() throws Exception {
        assertEquals("argName", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        List<String> arguments = Arrays.asList("--argName1", "--testOption", "--argName2:value1", "--argName3:value2:value3");
        commandLine.parse(arguments.toArray(new String[0]));

        argument.processValues(commandLine, commandLine.getArguments().listIterator(1), option);

        List<String> values = commandLine.getValues(option);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2:value3", values.get(1));
        assertNull(values.get(2));
    }

    @Test
    public void testCanProcess() throws OptionException {
        assertTrue(argument.canProcess(commandLine, "--argName"));
        assertTrue(argument.canProcess(commandLine, "value1"));
        assertFalse(argument.canProcess(commandLine, "--testOption"));
    }

    @Test
    public void testGetPrefixes() throws Exception {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() throws OptionException {
        List<String> arguments = Arrays.asList("--argName1", "--testOption", "--argName2:value1", "--argName3:value2:value3");
        commandLine.parse(arguments.toArray(new String[0]));

        argument.process(commandLine, commandLine.getArguments().listIterator(1), option);

        List<String> values = commandLine.getValues(option);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2:value3", values.get(1));
        assertNull(values.get(2));
    }

    @Test
    public void testGetInitialSeparator() throws Exception {
        assertEquals(';', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() throws Exception {
        assertEquals(':', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() throws Exception {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() throws Exception {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testgetDefaultValues() throws Exception {
        assertEquals(Arrays.asList("default1", "default2"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() throws Exception {
        assertSame(StringValidator.class, argument.getValidator().getClass());
    }

    @Test
    public void testValidate() throws OptionException {
        List<String> values = Arrays.asList("value1", "value2:value3", "invalidValue");
        commandLine.setValues(option, values);

        argument.validate(commandLine, option);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2:value3", values.get(1));
        assertThrows(OptionException.class, () -> argument.validate(commandLine, option));
    }

    @Test
    public void testAppendUsage() throws Exception {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.EMPTY_SET, null);

        assertEquals("argName [--] [value1 [value2 [value3 ...]]]", buffer.toString());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("Description", argument.getDescription());
    }

    @Test
    public void testHelpLines() throws Exception {
        List<HelpLine> helpLines = argument.helpLines(1, Collections.EMPTY_SET, null);

        HelpLine helpLine = helpLines.get(0);
        assertEquals(" argName [--] [value1 [value2 [value3 ...]]]", helpLine.getDescription());
        assertEquals("Description", helpLine.getLongDescription());
    }

    @Test
    public void testGetMaximum() throws Exception {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() throws Exception {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() throws Exception {
        String token = "\"value\"";
        assertEquals("value", argument.stripBoundaryQuotes(token));
    }

    @Test
    public void testIsRequired() throws Exception {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() throws Exception {
        argument.defaults(commandLine);

        assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() throws Exception {
        argument.setDefaultValues(Arrays.asList("newDefault1", "newDefault2"));

        assertEquals(Arrays.asList("newDefault1", "newDefault2"), argument.getDefaultValues());
        assertEquals(Arrays.asList("newDefault1", "newDefault2"), commandLine.getValues(option));
    }

    @Test
    public void testValidateWithIntRangeValidator() throws Exception {
        argument = new ArgumentImpl(
                "argName",
                "Description",
                1,
                10,
                ';',
                ':',
                new IntRangeValidator(1, 5),
                "--",
                Arrays.asList("default1", "default2"),
                1
        );

        List<String> values = Arrays.asList("value1", "value2:value3:5", "value4:value5:11");
        commandLine.setValues(option, values);

        assertThrows(OptionException.class, () -> argument.validate(commandLine, option));
    }

    @Test
    public void testValidateWithNoArgValidator() throws Exception {
        argument = new ArgumentImpl(
                "argName",
                "Description",
                1,
                1,
                ';',
                ':',
                new NoArgValidator(),
                "--",
                Arrays.asList("default1", "default2"),
                1
        );

        List<String> values = Arrays.asList("value1", "");
        commandLine.setValues(option, values);

        assertThrows(OptionException.class, () -> argument.validate(commandLine, option));
    }
}