package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private static final String ROOT_OPTION_NAME = "rootOption";
    private static final Argument ARGUMENT_A = new Argument("a");
    private static final Argument ARGUMENT_B = new Argument("b");
    private static final Option OPTION_A = new Option("a", "optionA", true, "aValue");
    private static final Option OPTION_B = new Option("b", "optionB");
    private static final Option OPTION_C = new Option("c", "optionC");

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new Option(ROOT_OPTION_NAME, null, false, "root option");
        commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        commandLine.addOption(OPTION_A);
        commandLine.addOption(OPTION_B);
        commandLine.addOption(OPTION_C);

        commandLine.addValue(OPTION_A, ARGUMENT_A);
        commandLine.addValue(OPTION_A, ARGUMENT_B);
        commandLine.addSwitch(OPTION_A, true);

        commandLine.addValue(OPTION_B, ARGUMENT_A);

        commandLine.addProperty(OPTION_A, "propertyA", "propertyValueA");
        commandLine.addProperty(OPTION_B, "propertyB", "propertyValueB");
    }

    @Test
    public void getOption() {
        Option option = commandLine.getOption("a");
        assertSame(OPTION_A, option);
    }

    @Test
    public void hasOption() {
        assertTrue(commandLine.hasOption(OPTION_A));
        assertTrue(commandLine.hasOption(OPTION_B));
        assertFalse(commandLine.hasOption(new Option("d", "optionD")));
    }

    @Test
    public void getValues() {
        List<Object> values = commandLine.getValues(OPTION_A, null);
        assertSame(new ArrayList<>(Arrays.asList(ARGUMENT_A, ARGUMENT_B)), values);

        List<Object> emptyValues = commandLine.getValues(OPTION_C, null);
        assertTrue(emptyValues.isEmpty());
    }

    @Test
    public void getUndefaultedValues() {
        List<Object> values = commandLine.getUndefaultedValues(OPTION_A);
        assertEquals(Collections.singletonList(ARGUMENT_A), values);
    }

    @Test
    public void getSwitch() {
        assertTrue(commandLine.getSwitch(OPTION_A, false));
    }

    @Test
    public void getProperty() {
        assertEquals("propertyValueA", commandLine.getProperty("optionA.propertyA"));
        assertEquals("propertyValueB", commandLine.getProperty("optionB.propertyB", "defaultValue"));
        assertNull(commandLine.getProperty("unknownProperty"));
    }

    @Test
    public void getProperties() {
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("optionA.propertyA", "propertyValueA");
        expectedProperties.put("optionB.propertyB", "propertyValueB");

        Set<String> properties = commandLine.getProperties(OPTION_A).stream()
                                                                   .map(propertyName -> propertyName.substring(2))
                                                                   .collect(Collectors.toSet());
        assertEquals(expectedProperties, commandLine.getProperties());
    }

    @Test
    public void looksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--a"));
        assertTrue(commandLine.looksLikeOption("-a"));
        assertFalse(commandLine.looksLikeOption("-x"));
    }

    @Test
    public void toString() {
        assertEquals("-a aValue --optionB aValue", commandLine.toString());
    }

    @Test
    public void getOptions() {
        assertEquals(Collections.singletonList(OPTION_A), commandLine.getOptions());
    }

    @Test
    public void getOptionTriggers() {
        Set<String> options = commandLine.getOptionTriggers();
        assertEquals(Collections.singleton("a"), options);
        assertEquals(Collections.singleton("b"), commandLine.getOptions().get(0).getOptionTriggers());
    }

    @Test
    public void setDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValueA");

        commandLine.setDefaultValues(OPTION_A, defaultValues);

        List<Object> values = commandLine.getValues(OPTION_A, null);
        assertEquals(new ArrayList<>(Arrays.asList(ARGUMENT_A, ARGUMENT_B, "defaultValueA")), values);
    }

    @Test
    public void setDefaultSwitch() {
        commandLine.setDefaultSwitch(OPTION_A, true);

        assertTrue(commandLine.getSwitch(OPTION_A, false));
    }

    @Test
    public void getNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(Arrays.asList("-a", "aValue", "--optionB", "aValue"), normalised);
    }
}