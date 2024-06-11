package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_MISSING_VALUES;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_MIN_EXCEEDS_MAX;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_TOO_FEW_DEFAULTS;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_TOO_MANY_DEFAULTS;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_UNEXPECTED_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentImplTest {

    @Mock
    private ResourceHelper resources;
    @Mock
    private Validator validator;
    @InjectMocks
    private ArgumentImpl argument;
    @InjectMocks
    private HelpLine helpLine;
    @InjectMocks
    private WriteableCommandLine commandLine;
    private List<String> args;

    @Before
    public void setup() {
        args = new ArrayList<>();
    }

    @Test
    public void testArgumentImplConstruction() {
        argument = new ArgumentImpl("name", "description", 1, 1, '.', '.', validator, "--", Collections.emptyList(), 0);
        assertEquals("name", argument.getPreferredName());
        assertEquals("description", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertEquals('.', argument.getInitialSeparator());
        assertEquals('.', argument.getSubsequentSeparator());
        assertFalse(argument.isRequired());
        assertNull(argument.getValidator());
        assertNull(argument.getConsumeRemaining());
        assertTrue(argument.getDefaultValues().isEmpty());

        argument = new ArgumentImpl("name", "description", 1, 1, '.', '.', validator, "--", Arrays.asList("value"), 0);
        assertEquals("name", argument.getPreferredName());
        assertEquals("description", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertEquals('.', argument.getInitialSeparator());
        assertEquals('.', argument.getSubsequentSeparator());
        assertFalse(argument.isRequired());
        assertNotNull(argument.getValidator());
        assertEquals("value", argument.getConsumeRemaining());
        assertEquals(1, argument.getDefaultValues().size());
        assertEquals("value", argument.getDefaultValues().get(0));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testProcessValues() {
        argument.processValues(commandLine, args.listIterator(), new Option("option"));
        assertEquals(0, commandLine.getValues("option").size());

        args.add("--consumeRemaining");
        assertEquals(0, commandLine.getValues("option").size());

        args.add("optionValue1");
        assertEquals(1, commandLine.getValues("option").size());
        assertEquals("optionValue1", commandLine.getValues("option").get(0));

        args.add("optionValue2");
        assertEquals(2, commandLine.getValues("option").size());
        assertEquals("optionValue2", commandLine.getValues("option").get(1));

        args.add("--consumeRemaining");
        assertEquals(2, commandLine.getValues("option").size());

        args.add("optionValue3");
        assertEquals(3, commandLine.getValues("option").size());
        assertEquals("optionValue3", commandLine.getValues("option").get(2));

        args.add("--consumeRemaining");
        assertEquals(3, commandLine.getValues("option").size());

        args.add("optionValue4");
        assertEquals(4, commandLine.getValues("option").size());
        assertEquals("optionValue4", commandLine.getValues("option").get(3));
    }

    @Test
    public void testCanProcess() {
        assertFalse(argument.canProcess(commandLine, "optionValue"));
        assertTrue(argument.canProcess(commandLine, "consumeRemaining"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() {
        argument.process(commandLine, args.listIterator());
        assertEquals(0, commandLine.getValues("option").size());

        args.add("--consumeRemaining");
        argument.process(commandLine, args.listIterator());
        assertEquals(0, commandLine.getValues("option").size());

        args.add("optionValue1");
        argument.process(commandLine, args.listIterator());
        assertEquals(1, commandLine.getValues("option").size());
        assertEquals("optionValue1", commandLine.getValues("option").get(0));

        args.add("optionValue2");
        argument.process(commandLine, args.listIterator());
        assertEquals(2, commandLine.getValues("option").size());
        assertEquals("optionValue2", commandLine.getValues("option").get(1));

        args.add("--consumeRemaining");
        argument.process(commandLine, args.listIterator());
        assertEquals(2, commandLine.getValues("option").size());

        args.add("optionValue3");
        argument.process(commandLine, args.listIterator());
        assertEquals(3, commandLine.getValues("option").size());
        assertEquals("optionValue3", commandLine.getValues("option").get(2));

        args.add("--consumeRemaining");
        argument.process(commandLine, args.listIterator());
        assertEquals(3, commandLine.getValues("option").size());

        args.add("optionValue4");
        argument.process(commandLine, args.listIterator());
        assertEquals(4, commandLine.getValues("option").size());
        assertEquals("optionValue4", commandLine.getValues("option").get(3));
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.emptyList(), argument.getDefaultValues());

        argument = new ArgumentImpl("name", "description", 1, 1, '.', '.', validator, "--", Arrays.asList("value1", "value2"), 0);
        assertEquals(Arrays.asList("value1", "value2"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());

        argument = new ArgumentImpl("name", "description", 1, 1, '.', '.', validator, "--", Collections.emptyList(), 0);
        assertNotNull(argument.getValidator());
    }

    @Test
    public void testValidate() {
        List<String> values = new ArrayList<>();
        commandLine.setValues("option", values);

        assertThrows(OptionException.class, () -> argument.validate(commandLine));
        assertEquals(ARGUMENT_MISSING_VALUES, helpLine.getMessage());

        values.add("value1");
        argument.validate(commandLine);

        values.add("value2");
        argument.validate(commandLine);

        values.add("value3");
        assertThrows(OptionException.class, () -> argument.validate(commandLine));
        assertEquals(ARGUMENT_UNEXPECTED_VALUE, helpLine.getMessage());

        argument = new ArgumentImpl("name", "description", 1, 2, '.', '.', validator, "--", Collections.emptyList(), 0);
        values.clear();
        commandLine.setValues("option", values);
        argument.validate(commandLine);
        assertEquals(ARGUMENT_MISSING_VALUES, helpLine.getMessage());

        values.add("value1");
        argument.validate(commandLine);

        values.add("value2");
        argument.validate(commandLine);

        values.add("value3");
        values.add("value4");
        assertThrows(OptionException.class, () -> argument.validate(commandLine));
        assertEquals(ARGUMENT_UNEXPECTED_VALUE, helpLine.getMessage());
    }

    @Test
    public void testAppendUsage() {
        argument = new ArgumentImpl("name", "description", 1, 1, '.', '.', validator, "--", Collections.emptyList(), 0);
        StringBuilder buffer = new StringBuilder();
        argument.appendUsage(buffer, null, null);
        assertEquals("name [<value>]", buffer.toString());

        argument = new ArgumentImpl("name", "description", 1, 1, '.', '.', validator, "--", Collections.emptyList(), 0);
        buffer.setLength(0);
        argument.appendUsage(buffer, null, null);
        assertEquals("name [<value>]", buffer.toString());

        argument = new ArgumentImpl("name", "description", 1, 1, '.', '.', validator, "--", Arrays.asList("value1", "value2"), 0);
        buffer.setLength(0);
        argument.appendUsage(buffer, null, null);
        assertEquals("name [<value>...]", buffer.toString());

        argument = new ArgumentImpl("name", "description", 1, 2, '.', '.', validator, "--", Collections.emptyList(), 0);
        buffer.setLength(0);
        argument.appendUsage(buffer, null, null);
        assertEquals("name [<value>]", buffer.toString());

        argument = new ArgumentImpl("name", "description", 1, 2, '.', '.', validator, "--", Arrays.asList("value1", "value2"), 0);
        buffer.setLength(0);
        argument.appendUsage(buffer, null, null);
        assertEquals("name [<value>...]", buffer.toString());
    }

    @Test
    public void testIsRequired() {
        assertFalse(argument.isRequired());

        argument = new ArgumentImpl("name", "description", 1, 1, '.', '.', validator, "--", Collections.emptyList(), 0);
        assertFalse(argument.isRequired());

        argument = new ArgumentImpl("name", "description", 0, 1, '.', '.', validator, "--", Collections.emptyList(), 0);
        assertTrue(argument.isRequired());
    }
}