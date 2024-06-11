package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "root", true, "root option");
        List arguments = new ArrayList();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(rootOption, writeableCommandLine.getOption(rootOption.getPreferredName()));
        assertEquals(Collections.singletonList(rootOption), writeableCommandLine.getOptions());
        assertEquals(Collections.emptySet(), writeableCommandLine.getOptionTriggers());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "root", true, "root option");
        Option option1 = new Option("o1", "o1", false, "option 1");
        Option option2 = new Option("o2", "o2", false, "option 2");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        assertEquals(3, writeableCommandLine.getOptions().size());
        assertEquals(option1, writeableCommandLine.getOption("o1"));
        assertEquals(option2, writeableCommandLine.getOption("o2"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "o", false, "option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        assertEquals(2, ((List) writeableCommandLine.getValues(option, null)).size());
        assertEquals("value1", ((List) writeableCommandLine.getValues(option, null)).get(0));
        assertEquals("value2", ((List) writeableCommandLine.getValues(option, null)).get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "o", false, "option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("o", "o", false, "option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "o", false, "option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "o", false, "option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "o", false, "option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        List valuesWithDefaults = writeableCommandLine.getValues(option, defaultValues);
        assertEquals(3, valuesWithDefaults.size());
        assertEquals("value1", valuesWithDefaults.get(0));
        assertEquals("value2", valuesWithDefaults.get(1));
        assertEquals("default1", valuesWithDefaults.get(2));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "o", false, "option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List unfaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(2, unfaultedValues.size());
        assertEquals("value1", unfaultedValues.get(0));
        assertEquals("value2", unfaultedValues.get(1));

        List unfaultedValuesWithDefaults = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(2, unfaultedValuesWithDefaults.size());
        assertEquals("value1", unfaultedValuesWithDefaults.get(0));
        assertEquals("value2", unfaultedValuesWithDefaults.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "o", false, "option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option propertyOption = new Option("p", "p", false, "property option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(propertyOption);

        writeableCommandLine.addProperty(propertyOption, "property", "value1");
        assertEquals("value1", writeableCommandLine.getProperty(propertyOption, "value2"));
    }

    @Test
    public void testAddProperty() {
        Option propertyOption = new Option("p", "p", false, "property option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root option"), new ArrayList());
        writeableCommandLine.addOption(propertyOption);

        writeableCommandLine.addProperty(propertyOption, "property1", "value1");
        writeableCommandLine.addProperty(propertyOption, "property2", "value2");

        Map properties = writeableCommandLine.getProperties(propertyOption);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));

        Map allProperties = writeableCommandLine.getProperties();
        assertEquals(1, allProperties.size());
        assertEquals(properties, allProperties.get(propertyOption));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "o1", false, "option 1");
        Option option2 = new Option("o2", "o2", false, "option 2");
        Option rootOption = new Option("root", "root", true, "root option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLine writeableCommandLineWithArguments = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals("arg1 arg2", writeableCommandLineWithArguments.toString());

        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option1, "value2");

        writeableCommandLine.addSwitch(option2, true);

        String expected = "root option " + option1.getLongOpt() + "=value1,value2 "
                + option1.getOpt() + " "
                + option2.getLongOpt() + " "
                + option2.getOpt();
        assertEquals(expected, writeableCommandLine.toString());
    }
}