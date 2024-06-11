package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("opt", "option", true, "an option");
        List arguments = new ArrayList() {{
            add("arg1");
            add("arg2");
        }};

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        // Test addOption
        Option option2 = new Option("opt2", "option2");
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());

        // Test getOption
        Option testOption = (Option) writeableCommandline.getOption("opt");
        assertSame(option, testOption);

        // Test hasOption
        assertTrue(writeableCommandLine.hasOption(option));
        assertFalse(writeableCommandLine.hasOption(new Option("opt3", "opt3")));

        // Test getValues
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());

        // Test getSwitch
        assertNull(writeableCommandLine.getSwitch(option, null));

        // Test addProperty
        writeableCommandLine.addProperty("property", "value");
        Properties properties = writeableCommandLine.getProperties();
        assertEquals(1, properties.size());

        // Test getProperty
        assertEquals("value", writeableCommandLine.getProperty("property", "defaultValue"));

        // Test getProperties
        Set propertiesSet = writeableCommandLine.getProperties();
        assertEquals(1, propertiesSet.size());
    }

    // Add more test cases for other methods here
}