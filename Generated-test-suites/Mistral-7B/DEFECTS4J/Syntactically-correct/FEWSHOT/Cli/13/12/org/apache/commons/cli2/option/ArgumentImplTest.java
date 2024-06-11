package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.Validator;
import org.apache.commons.cli2.validation.ValidatorTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ArgumentImplTest {
    private Argument argument;
    private ArgumentImpl argumentImpl;
    private CommandLine commandLine;
    private List<String> arguments;
    private Option option;
    private ResourceHelper resources;
    private Validator validator;
    private ValidatorTest validatorTest;

    @Before
    public void setup() {
        resources = ResourceHelper.getResourceHelper();
        validator = new ValidatorTest();
        validatorTest = new ValidatorTest();
        arguments = new LinkedList<>();
        commandLine = new CommandLine();
        option = new OptionImpl(1, false);
        argument = new ArgumentImpl("arg", "", 1, 1, 'a', 'a', validator, "--", Collections.singletonList("default"), 0);
        argumentImpl = (ArgumentImpl) argument;
    }

    @Test
    public void testCreateArgument() {
        assertEquals("arg", argumentImpl.getPreferredName());
        assertEquals("", argumentImpl.getDescription());
        assertEquals(1, argumentImpl.getMinimum());
        assertEquals(1, argumentImpl.getMaximum());
        assertEquals('a', argumentImpl.getInitialSeparator());
        assertEquals('a', argumentImpl.getSubsequentSeparator());
        assertEquals(true, argumentImpl.isSubsequentSplit());
        assertNull(argumentImpl.getValidator());
        assertEquals("--", argumentImpl.getConsumeRemaining());
        assertNull(argumentImpl.getDefaultValues());
        assertFalse(argumentImpl.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimumGreaterThanMaximum() {
        argumentImpl = new ArgumentImpl("arg", "", 2, 1, 'a', 'a', validator, "--", Collections.singletonList("default"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimumGreaterThanMaximumWithDefaultValues() {
        List<String> defaultValues = Collections.singletonList("default");
        argumentImpl = new ArgumentImpl("arg", "", 2, 1, 'a', 'a', validator, "--", defaultValues, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMinimum() {
        argumentImpl = new ArgumentImpl("arg", "", -1, 1, 'a', 'a', validator, "--", Collections.singletonList("default"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaximum() {
        argumentImpl = new ArgumentImpl("arg", "", 1, -1, 'a', 'a', validator, "--", Collections.singletonList("default"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValidator() {
        argumentImpl = new ArgumentImpl("arg", "", 1, 1, 'a', 'a', new Object(), "--", Collections.singletonList("default"), 0);
    }

    @Test
    public void testProcessValues() {
        arguments.add("--arg");
        arguments.add("default");
        arguments.add("value1");
        arguments.add("value2");

        argumentImpl.processValues(commandLine, arguments.listIterator(), option);

        List<String> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argumentImpl.canProcess(commandLine, "value"));
    }

    @Test
    public void testGetPrefixes() {
        Set<Object> prefixes = argumentImpl.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testProcess() throws OptionException {
        arguments.add("--arg");
        arguments.add("value1");
        arguments.add("value2");

        argumentImpl.process(commandLine, arguments.listIterator());

        List<String> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('a', argumentImpl.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('a', argumentImpl.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        Set<Object> triggers = argumentImpl.getTriggers();
        assertTrue(triggers.isEmpty());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argumentImpl.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = Collections.singletonList("default");
        argumentImpl = new ArgumentImpl("arg", "", 1, 1, 'a', 'a', validator, "--", defaultValues, 0);
        assertEquals(defaultValues, argumentImpl.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertEquals(validatorTest, argumentImpl.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValues() throws OptionException {
        argumentImpl.validate(commandLine, option);
        arguments.add("invalid");
        argumentImpl.validate(commandLine, option);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewValues() throws OptionException {
        argumentImpl.validate(commandLine, option);
        arguments.remove(arguments.size() - 1);
        argumentImpl.validate(commandLine, option);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyValues() throws OptionException {
        argumentImpl.validate(commandLine, option);
        argumentImpl.processValues(commandLine, arguments.listIterator(), option);
        arguments.add("extra");
        argumentImpl.validate(commandLine, option);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testValidateWithInvalidArgument() throws OptionException {
        argumentImpl.validate(commandLine, option);
        argumentImpl.setValidator(validatorTest);
        validatorTest.setThrowsException(true);
        arguments.add("invalid");
        argumentImpl.validate(commandLine, option);
    }
}