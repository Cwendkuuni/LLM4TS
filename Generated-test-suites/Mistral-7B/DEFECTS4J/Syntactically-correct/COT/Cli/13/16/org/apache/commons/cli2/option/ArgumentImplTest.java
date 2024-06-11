package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_MIN_EXCEEDS_MAX;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_MISSING_VALUES;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_TOO_FEW_DEFAULTS;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_TOO_MANY_DEFAULTS;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_UNEXPECTED_VALUE;

public class ArgumentImplTest {
    private Argument argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("argName", "description", 1, 1, ':', ':', null, null, null, 0);
        commandLine = new CommandLine();
    }

    @Test
    public void testArgumentName() {
        Assert.assertEquals("argName", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        Option option = OptionBuilder.withName("option").create();
        List<String> arguments = Arrays.asList("--argName", "value1", "value2");

        argument.process(commandLine, arguments.listIterator(0), option);
        Assert.assertEquals(2, commandLine.getValues(option).size());
        Assert.assertEquals("value1", commandLine.getValues(option).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testCanProcess() {
        Assert.assertTrue(argument.canProcess(commandLine, "argValue"));
    }

    @Test(expected = OptionException.class)
    public void testCannotProcess() {
        Assert.assertFalse(argument.canProcess(commandLine, "option"));
    }

    @Test
    public void testGetPrefixes() {
        Assert.assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() {
        Option option = OptionBuilder.withName("option").create();
        List<String> arguments = Arrays.asList("--argName", "value1", "value2");

        argument.process(commandLine, arguments.listIterator(0), option);
        Assert.assertEquals(2, commandLine.getValues(option).size());
        Assert.assertEquals("value1", commandLine.getValues(option).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testGetInitialSeparator() {
        Assert.assertEquals(':', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Assert.assertEquals(':', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        Assert.assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        Assert.assertNull(argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        Assert.assertNull(argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Assert.assertNull(argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValues() {
        argument.setValidator(new ArgumentValidator());
        List<String> values = Arrays.asList("argValue1", "argValue2", "argValue3");
        commandLine.setValues(new Option("option"), values);
        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithValidValues() {
        argument.setValidator(new ArgumentValidator());
        List<String> values = Arrays.asList("argValue1", "argValue2");
        commandLine.setValues(new Option("option"), values);
        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithMinimumValue() {
        argument.setValidator(new ArgumentValidatorMinimum());
        List<String> values = Arrays.asList("argValue1");
        commandLine.setValues(new Option("option"), values);
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithFewerThanMinimumValues() {
        argument.setValidator(new ArgumentValidatorMinimum());
        List<String> values = Collections.emptyList();
        commandLine.setValues(new Option("option"), values);
        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithMoreThanMaximumValues() {
        argument.setValidator(new ArgumentValidatorMaximum());
        List<String> values = Arrays.asList("argValue1", "argValue2", "argValue3", "argValue4");
        commandLine.setValues(new Option("option"), values);
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMoreThanMaximumValuesAndMaximumIsOne() {
        argument.setValidator(new ArgumentValidatorMaximum());
        List<String> values = Arrays.asList("argValue1", "argValue2");
        commandLine.setValues(new Option("option"), values);
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMoreThanMaximumValuesAndMaximumIsZero() {
        argument.setValidator(new ArgumentValidatorMaximum());
        List<String> values = Arrays.asList("argValue1");
        commandLine.setValues(new Option("option"), values);
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        argument.appendUsage(writer, null, null);
        String usage = writer.toString();
        Assert.assertTrue(usage.contains("--argName"));
    }

    @Test
    public void testGetDescription() {
        Assert.assertNotNull(argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> helpLines = argument.helpLines(0, null, null);
        Assert.assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        Assert.assertEquals("argName", helpLine.getHeader().getShortName());
        Assert.assertTrue(helpLine.getLongText().contains("description"));
    }

    @Test
    public void testGetMinimum() {
        Assert.assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        Assert.assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = "\"argValue\"";
        String strippedToken = argument.stripBoundaryQuotes(token);
        Assert.assertEquals("argValue", strippedToken);
    }

    @Test
    public void testIsRequired() {
        Assert.assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        Option option = OptionBuilder.withName("option").create();
        argument.defaults(commandLine);
        Assert.assertEquals(Collections.emptyList(), commandLine.getValues(option));

        argument = new ArgumentImpl("argName", "description", 1, 1, ':', ':', null, null, Arrays.asList("default1"), 0);
        argument.defaults(commandLine);
        Assert.assertEquals(Arrays.asList("default1"), commandLine.getValues(option));
    }
}

class ArgumentValidator implements Validator {
    @Override
    public void validate(List values) throws InvalidArgumentException {
    }
}

class ArgumentValidatorMinimum implements Validator {
    @Override
    public void validate(List values) throws InvalidArgumentException {
        if (values.size() < 1) {
            throw new InvalidArgumentException(ARGUMENT_MISSING_VALUES);
        }
    }
}

class ArgumentValidatorMaximum implements Validator {
    @Override
    public void validate(List values) throws InvalidArgumentException {
        if (values.size() > 1) {
            throw new InvalidArgumentException(ARGUMENT_UNEXPECTED_VALUE, values.get(1));
        }
    }
}

class StringWriter extends StringBuilder {
}