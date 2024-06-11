package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultValue;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.MockValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class ArgumentImplTest {
    private Argument arg;
    private ArgumentImpl argumentImpl;
    private CommandLine commandLine;
    private List<String> values;
    private Set<DisplaySetting> helpSettings;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        resourceHelper = new ResourceHelper() {
            @Override
            public String getMessage(String key) {
                return key;
            }
        };

        arg = new DefaultValue("arg", "description", new ArrayList<>());
        argumentImpl = new ArgumentImpl(arg.getName(), arg.getDescription(), 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0) {
            @Override
            public Argument getThis() {
                return this;
            }
        };

        commandLine = new CommandLineImpl();
        values = new ArrayList<>();
        helpSettings = Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NAME);
    }

    @Test
    public void testGetName() {
        assertEquals("arg", argumentImpl.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        String[] args = {"--arg", "foo", "--arg", "bar", "baz"};
        commandLine.parse(args);

        argumentImpl.processValues(commandLine, values.listIterator(), argumentImpl);

        assertEquals(3, values.size());
        assertEquals("foo", values.get(0));
        assertEquals("bar", values.get(1));
        assertEquals("baz", values.get(2));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argumentImpl.canProcess(commandLine, "foo"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argumentImpl.getPrefixes());
    }

    @Test
    public void testProcess() throws OptionException {
        String[] args = {"--arg", "foo", "--arg", "bar", "baz"};
        commandLine.parse(args);

        argumentImpl.process(commandLine, values.listIterator());

        assertEquals(3, values.size());
        assertEquals("foo", values.get(0));
        assertEquals("bar", values.get(1));
        assertEquals("baz", values.get(2));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argumentImpl.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argumentImpl.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argumentImpl.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertNull(argumentImpl.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argumentImpl.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argumentImpl.getValidator());
    }

    @Test
    public void testValidate() throws OptionException {
        List<String> values = new ArrayList<>();
        values.add("foo");
        commandLine.setValues(argumentImpl, values);

        argumentImpl.validate(commandLine, argumentImpl);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() throws OptionException {
        List<String> values = new ArrayList<>();
        values.add("foo");
        values.add("invalid");
        commandLine.setValues(argumentImpl, values);

        argumentImpl.validate(commandLine, argumentImpl);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewValues() throws OptionException {
        List<String> values = new ArrayList<>();
        commandLine.setValues(argumentImpl, values);

        argumentImpl.validate(commandLine, argumentImpl);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyValues() throws OptionException {
        List<String> values = new ArrayList<>();
        values.add("foo");
        values.add("bar");
        values.add("baz");
        values.add("qux");
        commandLine.setValues(argumentImpl, values);

        argumentImpl.validate(commandLine, argumentImpl);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argumentImpl.appendUsage(buffer, helpSettings, null);

        assertEquals("arg", buffer.toString());
    }

    @Test
    public void testAppendUsageWithOptional() {
        Set<DisplaySetting> helpSettingsOptional =
            Collections.singleton(DisplaySetting.DISPLAY_OPTIONAL);
        StringBuffer buffer = new StringBuffer();
        argumentImpl.appendUsage(buffer, helpSettingsOptional, null);

        assertEquals("[arg]", buffer.toString());
    }

    @Test
    public void testAppendUsageWithNumbered() {
        Set<DisplaySetting> helpSettingsNumbered =
            Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        StringBuffer buffer = new StringBuffer();
        argumentImpl.appendUsage(buffer, helpSettingsNumbered, null);

        assertEquals("[1]arg[2]", buffer.toString());
    }

    @Test
    public void testAppendUsageWithBracketed() {
        Set<DisplaySetting> helpSettingsBracketed =
            Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        StringBuffer buffer = new StringBuffer();
        argumentImpl.appendUsage(buffer, helpSettingsBracketed, null);

        assertEquals("<arg>", buffer.toString());
    }

    @Test
    public void testAppendUsageWithOptionalAndNumbered() {
        Set<DisplaySetting> helpSettingsOptionalAndNumbered =
            Collections.singleton(DisplaySetting.DISPLAY_OPTIONAL);
        helpSettingsOptionalAndNumbered.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        StringBuffer buffer = new StringBuffer();
        argumentImpl.appendUsage(buffer, helpSettingsOptionalAndNumbered, null);

        assertEquals("[1]arg[2]", buffer.toString());
    }

    @Test
    public void testAppendUsageWithOptionalAndBracketed() {
        Set<DisplaySetting> helpSettingsOptionalBracketed =
            Collections.singleton(DisplaySetting.DISPLAY_OPTIONAL);
        helpSettingsOptionalBracketed.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        StringBuffer buffer = new StringBuffer();
        argumentImpl.appendUsage(buffer, helpSettingsOptionalBracketed, null);

        assertEquals("[arg]", buffer.toString());
    }

    @Test
    public void testAppendUsageWithOptionalAndNumberedAndBracketed() {
        Set<DisplaySetting> helpSettingsOptionalNumberedBracketed =
            Collections.singleton(DisplaySetting.DISPLAY_OPTIONAL);
        helpSettingsOptionalNumberedBracketed.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        helpSettingsOptionalNumberedBracketed.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        StringBuffer buffer = new StringBuffer();
        argumentImpl.appendUsage(buffer, helpSettingsOptionalNumberedBracketed, null);

        assertEquals("[1]arg[2]", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        HelpLine helpLine = argumentImpl.helpLines(0, helpSettings, null).get(0);

        assertEquals("arg", helpLine.getOptionSpec().getLongOpt());
        assertEquals("description", helpLine.getOptionSpec().getDescription());
        assertEquals(1, helpLine.getValues().size());
        assertEquals("", helpLine.getValues().get(0));
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argumentImpl.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argumentImpl.getMinimum());
    }

    @Test
    public void testValidateWithMockValidator() throws OptionException {
        MockValidator validator = new MockValidator();
        argumentImpl = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, validator, null, null, 0);

        List<String> values = Arrays.asList("foo", "bar");
        commandLine.setValues(argumentImpl, values);

        argumentImpl.validate(commandLine, argumentImpl);

        assertEquals(2, validator.getValidValues().size());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMockValidatorAndInvalidArgument() throws OptionException {
        MockValidator validator = new MockValidator();
        argumentImpl = new ArgumentImpl("arg", "description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, validator, null, null, 0);

        List<String> values = Arrays.asList("foo", "bar", "invalid");
        commandLine.setValues(argumentImpl, values);

        argumentImpl.validate(commandLine, argumentImpl);
    }
}

class MockValidator implements Validator {
    private final List<String> validValues = new ArrayList<>();

    @Override
    public void validate(List<String> values) throws InvalidArgumentException {
        for (String value : values) {
            validValues.add(value);
        }
    }

    public List<String> getValidValues() {
        return validValues;
    }
}

class CommandLineImpl implements WriteableCommandLine {
    private final List<Object> values = new ArrayList<>();

    @Override
    public void addOption(Option option) {
        // not used
    }

    @Override
    public void addOption(Option option, String value) {
        // not used
    }

    @Override
    public void addValue(Option option, String value) {
        values.add(value);
    }

    @Override
    public void setValues(Argument argument, List<String> values) {
        this.values.clear();
        for (String value : values) {
            addValue(argument, value);
        }
    }

    @Override
    public int parse(String[] args) throws OptionException {
        for (String arg : args) {
            if (arg.startsWith("--")) {
                Option option = new OptionImpl(0, false);
                option.setLongOpt(arg.substring(2));
                addOption(option);
            } else {
                Option option = getOption(arg);
                if (option == null) {
                    addValue(new DefaultValue("arg", "description", new ArrayList<>()), arg);
                } else {
                    addValue(option, arg);
                }
            }
        }

        return 0;
    }

    @Override
    public int getArgCount() {
        return values.size();
    }

    @Override
    public String getArgString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getArgCount(); i++) {
            String value = (String) values.get(i);
            if (i > 0) {
                stringBuilder.append(' ');
            }
            stringBuilder.append(value);
        }

        return stringBuilder.toString();
    }

    @Override
    public Object getValueForOption(int index) {
        return values.get(index);
    }

    @Override
    public String getOptionName(int index) {
        Option option = getOption(getValueForOption(index));
        return option == null ? null : option.getLongOpt();
    }

    @Override
    public Option getOption(String name) {
        for (Option option : getOptions()) {
            if (option.getLongOpt().equals(name)) {
                return option;
            }
        }

        return null;
    }

    @Override
    public Option[] getOptions() {
        return new Option[0];
    }

    @Override
    public boolean looksLikeOption(String arg) {
        return arg.startsWith("--");
    }
}