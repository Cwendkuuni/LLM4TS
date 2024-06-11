package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = Option.builder("root")
            .requiredOption()
            .build();

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
        assertEquals(Collections.emptySet(), writeableCommandLine.getOptionTriggers());
        assertEquals(Collections.emptyMap(), writeableCommandLine.getProperties());
        assertEquals("", writeableCommandLine.toString());

        Option option1 = Option.builder("option1")
            .longOpt("longOption1")
            .hasArg(true)
            .valueSeparator(';')
            .build();

        writeableCommandLine.addOption(option1);
        assertTrue(writeableCommandLine.hasOption(option1));
        assertEquals(1, writeableCommandLine.getOptions().size());
        assertEquals("option1", writeableCommandLine.getOption(option1.getPreferredName()).getValue());
        assertEquals(Collections.singletonList("value1"), writeableCommandLine.getValues(option1, null));

        Option option2 = Option.builder("option2").build();
        writeableCommandLine.addOption(option2);
        assertEquals(2, writeableCommandLine.getOptions().size());
        assertNull(writeableCommandLine.getValues(option2, null));

        Argument arg1 = Argument.builder("arg1")
            .name("argName1")
            .build();

        writeableCommandLine.addOption(arg1);
        assertEquals(3, writeableCommandLine.getOptions().size());
        assertSame(arg1, writeableCommandLine.getOption(arg1.getName()));

        Option option3 = Option.builder("option3")
            .longOpt("longOption3")
            .hasArg(false)
            .build();

        writeableCommandLine.addOption(option3);
        assertTrue(writeableCommandLine.hasOption(option3));
        assertEquals(4, writeableCommandLine.getOptions().size());
        assertFalse(writeableCommandLine.hasOption(option1));

        List values1 = new ArrayList(Arrays.asList("value1", "value2"));
        writeableCommandLine.addValue(option1, values1);
        assertEquals(values1, writeableCommandLine.getValues(option1, null));

        List values2 = new ArrayList(Arrays.asList("value3", "value4"));
        writeableCommandLine.addValue(option1, values2);
        assertEquals(values2, writeableCommandLine.getValues(option1, null));

        List defaultValues1 = new ArrayList(Arrays.asList("defaultValue1", "defaultValue2"));
        writeableCommandLine.setDefaultValues(option1, defaultValues1);
        assertEquals(defaultValues1, writeableCommandLine.getValues(option1, null));

        Boolean defaultSwitch1 = Boolean.TRUE;
        writeableCommandLine.setDefaultSwitch(option3, defaultSwitch1);
        assertEquals(defaultSwitch1, writeableCommandLine.getSwitch(option3, null));

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperty("property1", "value3");
        assertEquals("value3", writeableCommandLine.getProperty("property1", "value1"));

        Map nameToOption = writeableCommandLine.getNameToOption();
        assertEquals(option1, nameToOption.get("option1"));
        assertEquals(option2, nameToOption.get("option2"));
        assertEquals(arg1, nameToOption.get("arg1"));
        assertEquals(option3, nameToOption.get("option3"));

        List normalised = Arrays.asList("root", "option1=value1;value2", "option3");
        assertEquals(normalised, writeableCommandLine.getNormalised());
    }
}