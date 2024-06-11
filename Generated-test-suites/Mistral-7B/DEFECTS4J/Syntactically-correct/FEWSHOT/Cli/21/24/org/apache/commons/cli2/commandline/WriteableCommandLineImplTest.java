package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testNewInstance() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        assertEquals(rootOption, cmd.getRootOption());
        assertEquals(Collections.emptyList(), cmd.getOptions());
        assertEquals(Collections.emptyMap(), cmd.getProperties());
        assertEquals(Collections.emptyMap(), cmd.getOptionToProperties());
        assertEquals(Collections.emptyMap(), cmd.getDefaultValues());
        assertEquals(Collections.emptyMap(), cmd.getDefaultSwitches());
        assertEquals(Collections.emptyList(), cmd.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("o1", "o1", true, "Option 1");
        Option opt2 = new Option("o2", "o2", true, "Option 2");

        cmd.addOption(opt1);
        cmd.addOption(opt2);

        assertEquals(Arrays.asList(opt1, opt2), cmd.getOptions());
        assertEquals(opt1, cmd.getOption("o1"));
        assertEquals(opt2, cmd.getOption("o2"));

        Option opt3 = new Option("o3", "o3", true, "Option 3");
        cmd.addOption(opt3);

        assertEquals(Arrays.asList(opt1, opt2, opt3), cmd.getOptions());
    }

    @Test
    public void testAddValue() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", true, "Option");

        cmd.addOption(opt);
        cmd.addValue(opt, "value1");
        cmd.addValue(opt, "value2");

        List<Object> values = cmd.getValues(opt, Collections.emptyList());
        assertEquals(Arrays.asList("value1", "value2"), values);

        cmd.addValue(opt, "value3");
        values = cmd.getValues(opt, Collections.emptyList());
        assertEquals(Arrays.asList("value1", "value2", "value3"), values);
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", false, "Option");

        cmd.addOption(opt);
        cmd.addSwitch(opt, true);
        assertEquals(Boolean.TRUE, cmd.getSwitch(opt, Boolean.FALSE));

        cmd.addSwitch(opt, false);
        assertEquals(Boolean.FALSE, cmd.getSwitch(opt, Boolean.TRUE));
    }

    @Test
    public void testHasOption() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("o1", "o1", true, "Option 1");

        cmd.addOption(opt1);
        assertTrue(cmd.hasOption(opt1));

        Option opt2 = new Option("o2", "o2", true, "Option 2");
        cmd.addOption(opt2);
        assertTrue(cmd.hasOption(opt2));

        Option opt3 = new Option("o3", "o3", true, "Option 3");
        assertFalse(cmd.hasOption(opt3));
    }

    @Test
    public void testGetOption() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("o1", "o1", true, "Option 1");
        Option opt2 = new Option("o2", "o2", true, "Option 2");

        cmd.addOption(opt1);
        cmd.addOption(opt2);

        assertSame(opt1, cmd.getOption("o1"));
        assertSame(opt2, cmd.getOption("o2"));

        Option opt3 = new Option("o3", "o3", true, "Option 3");
        assertEquals(null, cmd.getOption("o3"));
    }

    @Test
    public void testGetValues() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", true, "Option");

        cmd.addOption(opt);
        cmd.addValue(opt, "value1");
        cmd.addValue(opt, "value2");

        List<Object> values = cmd.getValues(opt, Collections.emptyList());
        assertEquals(Arrays.asList("value1", "value2"), values);

        cmd.addValue(opt, "value3");
        values = cmd.getValues(opt, Collections.emptyList());
        assertEquals(Arrays.asList("value1", "value2", "value3"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", true, "Option");

        cmd.addOption(opt);
        cmd.addValue(opt, "value1");
        cmd.addValue(opt, "value2");

        List<Object> values = cmd.getUndefaultedValues(opt);
        assertEquals(Collections.emptyList(), values);

        cmd.setDefaultValues(opt, Arrays.asList("defaultValue1", "defaultValue2"));
        values = cmd.getUndefaultedValues(opt);
        assertEquals(Collections.emptyList(), values);

        cmd.addValue(opt, "value3");
        values = cmd.getUndefaultedValues(opt);
        assertEquals(Arrays.asList("value3"), values);
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", false, "Option");

        cmd.addOption(opt);
        cmd.addSwitch(opt, true);
        assertTrue(cmd.getSwitch(opt, Boolean.FALSE));

        cmd.addSwitch(opt, false);
        assertFalse(cmd.getSwitch(opt, Boolean.TRUE));

        cmd.setDefaultSwitch(opt, true);
        assertTrue(cmd.getSwitch(opt, Boolean.FALSE));

        cmd.setDefaultSwitch(opt, false);
        assertFalse(cmd.getSwitch(opt, Boolean.TRUE));
    }

    @Test
    public void testgetProperty() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", false, "Option");
        cmd.addOption(opt);
        cmd.addProperty(opt, "property", "value");

        String propertyValue = cmd.getProperty("property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testaddProperty() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", false, "Option");
        cmd.addOption(opt);

        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");

        cmd.addProperty(opt, properties);

        Map<String, String> propertiesMap = cmd.getProperties(opt);
        assertEquals(properties, propertiesMap);
    }

    @Test
    public void testGetProperties() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", false, "Option");
        cmd.addOption(opt);

        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");

        cmd.addProperty(opt, properties);

        Set<String> propertiesKeys = cmd.getProperties(opt).keySet();
        assertEquals(Arrays.asList("property1", "property2"), Arrays.asList(propertiesKeys.toArray()));
    }

    @Test
    public void testLooksLikeOption() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Arrays.asList("--option1", "--option2"));

        for (String trigger : Arrays.asList("--option1", "--option", "-o", "option1")) {
            assertTrue(cmd.looksLikeOption(trigger));
        }

        for (String trigger : Arrays.asList("--no-option", "-n", "no-option")) {
            assertFalse(cmd.looksLikeOption(trigger));
        }
    }

    @Test
    public void testToString() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        cmd.addOption(new Option("o1", "o1", true, "Option 1"));
        cmd.addOption(new Option("o2", "o2", true, "Option 2"));
        cmd.addValue(new Option("o1", "o1", true, "Option 1"), "value1");
        cmd.addValue(new Option("o2", "o2", true, "Option 2"), "value2");

        String result = cmd.toString();
        assertEquals("--o1 value1 --o2 value2 root", result);
    }

    @Test
    public void testGetOptionsList() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("o1", "o1", true, "Option 1");
        Option opt2 = new Option("o2", "o2", true, "Option 2");

        cmd.addOption(opt1);
        cmd.addOption(opt2);

        List<Option> options = cmd.getOptions();
        assertEquals(Arrays.asList(opt1, opt2), options);
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt1 = new Option("o1", "o1", true, "Option 1");
        Option opt2 = new Option("o2", "o2", true, "Option 2");

        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Set<String> triggers = cmd.getOptionTriggers();
        assertEquals(Arrays.asList("o1", "o2"), Arrays.asList(triggers.toArray()));
    }

    @Test
    public void testSetDefaultValues() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", true, "Option");

        cmd.addOption(opt);
        cmd.setDefaultValues(opt, Arrays.asList("defaultValue1", "defaultValue2"));

        List<Object> defaults = cmd.getDefaultValues(opt, Collections.emptyList());
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), defaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option opt = new Option("o", "o", false, "Option");

        cmd.addOption(opt);
        cmd.setDefaultSwitch(opt, Boolean.TRUE);

        Boolean defaultSwitch = cmd.getSwitch(opt, Boolean.FALSE);
        assertTrue(defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option rootOption = new Option("root", "root", false, "description");
        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Arrays.asList("--option1=value1", "--option2=value2"));

        List<String> normalised = cmd.getNormalised();
        assertEquals(Arrays.asList("--option1=value1", "--option2=value2"), normalised);
    }
}