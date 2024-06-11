package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.commandline.WriteableCommandLineImplTest.TestOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("t", "test", true, "Test option");
        List<String> args = new ArrayList<>();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, args);

        // Test addOption
        Option opt1 = new Option("o", "opt1", false, "Option 1");
        cmd.addOption(opt1);
        assertTrue(cmd.hasOption(opt1));
        assertEquals(opt1, cmd.getOption("o"));

        Option opt2 = new TestOption("t", "opt2", null, "Option 2", new String[]{"val1", "val2"});
        cmd.addOption(opt2);
        assertTrue(cmd.hasOption(opt2));
        assertEquals(opt2, cmd.getOption("t"));

        // Test addValue
        cmd.addValue(opt1, "value1");
        List<String> valueList1 = cmd.getValues(opt1, null);
        assertEquals(1, valueList1.size());
        assertEquals("value1", valueList1.get(0));

        cmd.addValue(opt1, "value2");
        List<String> valueList2 = cmd.getValues(opt1, null);
        assertEquals(2, valueList2.size());
        assertEquals("value1", valueList2.get(0));
        assertEquals("value2", valueList2.get(1));

        // Test addSwitch
        cmd.addSwitch(opt1, true);
        Boolean switch1 = cmd.getSwitch(opt1, false);
        assertTrue(switch1);

        // Test getOption
        Option opt3 = new Option("o2", "opt3", false, "Option 3");
        cmd.addOption(opt3);
        Option getOpt3 = cmd.getOption("o2");
        assertEquals(opt3, getOpt3);

        // Test getValues
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        cmd.setDefaultValues(opt1, defaultValues);
        List<String> defaultValues1 = cmd.getValues(opt1, null);
        assertEquals(1, defaultValues1.size());
        assertEquals("defaultValue1", defaultValues1.get(0));

        List<String> values = new ArrayList<>();
        values.add("value3");
        cmd.addValue(opt1, values);
        List<String> values1 = cmd.getValues(opt1, null);
        assertEquals(1, values1.size());
        assertEquals(values, values1);

        // Test getUndefaultedValues
        List<String> unfaultedValues = cmd.getUndefaultedValues(opt1);
        assertTrue(unfaultedValues.isEmpty());

        // Test getSwitch
        cmd.addSwitch(opt1, false);
        Boolean switch2 = cmd.getSwitch(opt1, true);
        assertFalse(switch2);

        // Test getProperty
        cmd.addProperty(opt1, "property1", "value1");
        String property1 = cmd.getProperty(opt1, "property1");
        assertEquals("value1", property1);

        // Test getProperties
        cmd.addProperty(opt1, "property2", "value2");
        Set<String> properties = cmd.getProperties(opt1);
        assertEquals(2, properties.size());
        assertTrue(properties.containsAll(Arrays.asList("property1", "property2")));

        // Test looksLikeOption
        assertTrue(cmd.looksLikeOption("o"));
        assertFalse(cmd.looksLikeOption("arg"));

        // Test toString
        assertEquals("o value1 value2", cmd.toString());

        // Test getOptions
        List<Option> options = cmd.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(opt1));
        assertTrue(options.contains(opt2));
        assertTrue(options.contains(opt3));

        // Test getOptionTriggers
        Set<String> optionTriggers = cmd.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("o", "t", "o2")));

        // Test setDefaultValues
        defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue2");
        cmd.setDefaultValues(opt1, defaultValues);
        List<String> defaultValues2 = cmd.getValues(opt1, null);
        assertEquals(1, defaultValues2.size());
        assertEquals("defaultValue2", defaultValues2.get(0));

        // Test setDefaultSwitch
        cmd.setDefaultSwitch(opt1, true);
        switch2 = cmd.getSwitch(opt1, false);
        assertTrue(switch2);

        // Test getNormalised
        List<String> normalised = cmd.getNormalised();
        assertEquals(3, normalised.size());
        assertTrue(normalised.containsAll(Arrays.asList("-o", "-t", "--opt2=val1,val2")));
    }

    static class TestOption extends Option {
        private static final long serialVersionUID = 1L;

        public TestOption(String longOpt, String opt, Boolean required, String description, String[] values) {
            super(longOpt, opt, required, description, values);
        }
    }
}