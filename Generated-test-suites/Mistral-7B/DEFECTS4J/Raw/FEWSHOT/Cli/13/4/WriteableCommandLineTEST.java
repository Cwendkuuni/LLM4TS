package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private ParseContext parseContext;
    private Option option;

    @Before
    public void setup() {
        parseContext = mock(ParseContext.class);
        option = OptionBuilder.withLongOpt("testOption").withValueSeparator(',').isRequired(false)
                .createOption();
        when(parseContext.getOptions()).thenReturn(Collections.singletonList(option));
    }

    @Test
    public void testAddOption() {
        WriteableCommandLine writeableCommandLine = mock(WriteableCommandLine.class);
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine writeableCommandLine = mock(WriteableCommandLine.class);
        writeableCommandLine.addValue(option, "value1");
        assertEquals("value1", writeableCommandLine.getOptionValues(option).get(0));
    }

    @Test
    public void testGetOptionValues() {
        String[] values = new String[]{"value1", "value2"};
        when(parseContext.parse(new String[0])).thenReturn(new DefaultParsingResult(Arrays.asList(values), option));

        WriteableCommandLine writeableCommandLine = mock(WriteableCommandLine.class);
        when(writeableCommandLine.getParseContext()).thenReturn(parseContext);
        List<Object> valuesList = writeableCommandLine.getOptionValues(option);

        assertEquals(values.length, valuesList.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], valuesList.get(i));
        }
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = Arrays.asList("default1", "default2");

        WriteableCommandLine writeableCommandLine = mock(WriteableCommandLine.class);
        writeableCommandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, writeableCommandLine.getOptionValues(option));
    }

    @Test
    public void testAddSwitch() {
        boolean defaultSwitch = true;
        WriteableCommandLine writeableCommandLine = mock(WriteableCommandLine.class);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getOptionStates().get(option.getLongOpt()).isPresent());
        assertTrue(writeableCommandLine.getOptionStates().get(option.getLongOpt()).get().isPresent());
        assertTrue(writeableCommandLine.getOptionStates().get(option.getLongOpt()).get().booleanValue() == true);

        try {
            writeableCommandLine.addSwitch(option, true);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }

        writeableCommandLine.setDefaultSwitch(option, false);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getOptionStates().get(option.getLongOpt()).get().booleanValue() == true);

        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.getOptionStates().get(option.getLongOpt()).get().booleanValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine writeableCommandLine = mock(WriteableCommandLine.class);
        writeableCommandLine.addProperty("property", "value");
        assertEquals("value", writeableCommandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine writeableCommandLine = mock(WriteableCommandLine.class);
        assertFalse(writeableCommandLine.looksLikeOption(""));
        assertFalse(writeableCommandLine.looksLikeOption("--test"));
        assertFalse(writeableCommandLine.looksLikeOption("--test="));
        assertFalse(writeableCommandLine.looksLikeOption("--test=value"));
        assertTrue(writeableCommandLine.looksLikeOption("--testValue"));
    }
}