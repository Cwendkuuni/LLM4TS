package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "opt", true, "Option description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertEquals(option, writeableCommandLine.getOption(option.getLongOpt()));
        assertEquals(option, writeableCommandLine.getOption(option.getShortOpt()));
        assertNull(writeableCommandLine.getOption("-nonexistent"));

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(Collections.emptyList(), values);

        assertFalse(writeableCommandLine.hasOption(option));

        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List valuesList = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), valuesList);

        writeableCommandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, null));

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty("property1", "value2");
        assertEquals("value2", writeableCommandLine.getProperty("property1", "value1"));

        Set propertiesSet = writeableCommandLine.getProperties();
        assertEquals(1, propertiesSet.size());

        Option option2 = new Option("o2", "opt2", false, "Option description 2");
        writeableCommandLine.addOption(option2);
        List optionList = writeableCommandLine.getOptions();
        assertEquals(2, optionList.size());
        assertEquals(option, optionList.get(0));
        assertEquals(option2, optionList.get(1));

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertEquals(option.getShortOpt(), optionTriggers.toArray()[0]);
        assertEquals(option.getLongOpt(), optionTriggers.toArray()[1]);

        Map defaultValuesMap = new HashMap();
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValuesMap.put(option, defaultValues);
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List defaultValuesList = writeableCommandLine.getValues(option, null);
        assertEquals(defaultValues, defaultValuesList);

        Map defaultSwitchesMap = new HashMap();
        defaultSwitchesMap.put(option, Boolean.TRUE);
        writeableCommandLine.setDefaultSwitch(option, Boolean.TRUE);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, null));
    }
}