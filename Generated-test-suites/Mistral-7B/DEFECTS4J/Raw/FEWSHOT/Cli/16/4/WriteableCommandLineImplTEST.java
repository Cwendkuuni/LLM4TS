package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.argument.Argument;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
        assertEquals(Collections.emptySet(), writeableCommandLine.getOptionTriggers());
        assertNull(writeableCommandLine.getProperties());
        assertNull(writeableCommandLine.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option1 = Option.builder("option1")
                .required(false)
                .longOpt("option1")
                .build();

        Option option2 = Option.builder("option2")
                .required(false)
                .longOpt("option2")
                .build();

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        Set optionTriggers = writeableCommandLine.getOptionTriggers();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));

        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testAddValue() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Argument argument = Argument.builder("argument")
                .required(true)
                .index(0)
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .addArgument(argument)
                .build();

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List values = writeableCommandLine.getValues(option, Collections.emptyList());

        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .build();

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testGetOption() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Option option1 = Option.builder("option1")
                .required(false)
                .longOpt("option1")
                .build();

        Option option2 = Option.builder("option2")
                .required(false)
                .longOpt("option2")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Option option = writeableCommandLine.getOption("option1");

        assertEquals(option1, option);
    }

    @Test
    public void testGetValues() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Argument argument = Argument.builder("argument")
                .required(true)
                .index(0)
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .addArgument(argument)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, Collections.emptyList());

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Argument argument = Argument.builder("argument")
                .required(true)
                .index(0)
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .addArgument(argument)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List undefaultedValues = writeableCommandLine.getUndefaultedValues(option);

        assertEquals(0, undefaultedValues.size());
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");

        String propertyValue = writeableCommandLine.getProperty(option, "property", null);

        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", null);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Map optionProperties = writeableCommandLine.getProperties(option);

        assertEquals(2, optionProperties.size());
        assertEquals("value1", optionProperties.get("property1"));
        assertEquals("value2", optionProperties.get("property2"));
    }

    @Test
    public void testGetProperties() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", null);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Map propertiesMap = writeableCommandLine.getProperties();

        assertEquals(2, propertiesMap.size());
        assertTrue(propertiesMap.containsKey(option));
        assertTrue(propertiesMap.get(option) instanceof Map);

        Map optionProperties = (Map) propertiesMap.get(option);

        assertEquals(2, optionProperties.size());
        assertEquals("value1", optionProperties.get("property1"));
        assertEquals("value2", optionProperties.get("property2"));
    }

    @Test
    public void testToString() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Argument argument = Argument.builder("argument")
                .required(true)
                .index(0)
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .addArgument(argument)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        String commandLineString = writeableCommandLine.toString();

        assertEquals("root -option value1", commandLineString);
    }

    @Test
    public void testGetOptionsList() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Option option1 = Option.builder("option1")
                .required(false)
                .longOpt("option1")
                .build();

        Option option2 = Option.builder("option2")
                .required(false)
                .longOpt("option2")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List optionsList = writeableCommandLine.getOptions();

        assertEquals(2, optionsList.size());
        assertTrue(optionsList.contains(option1));
        assertTrue(optionsList.contains(option2));
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Option option1 = Option.builder("option1")
                .required(false)
                .longOpt("option1")
                .build();

        Option option2 = Option.builder("option2")
                .required(false)
                .longOpt("option2")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();

        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Argument argument = Argument.builder("argument")
                .required(true)
                .index(0)
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .addArgument(argument)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, Collections.emptyList());

        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.setDefaultSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option rootOption = Option.builder("rootOption")
                .required(false)
                .longOpt("root")
                .build();

        Argument argument = Argument.builder("argument")
                .required(true)
                .index(0)
                .build();

        Option option = Option.builder("option")
                .required(false)
                .longOpt("option")
                .addArgument(argument)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List normalisedList = writeableCommandLine.getNormalised();

        assertEquals(1, normalisedList.size());
        assertEquals("root -option value1", normalisedList.get(0).toString());
    }
}