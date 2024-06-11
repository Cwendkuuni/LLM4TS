package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = Mockito.mock(WriteableCommandLine.class);
    }

    @Test
    public void testAddOption() {
        Option option = Mockito.mock(Option.class);
        writeableCommandLine.addOption(option);
        Mockito.verify(writeableCommandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = Mockito.mock(Option.class);
        Object value = new Object();
        writeableCommandLine.addValue(option, value);
        Mockito.verify(writeableCommandLine).addValue(option, value);
    }

    @Test
    public void testGetArgumentValues() {
        Option option = Mockito.mock(Option.class);
        List<Object> values = new ArrayList<>();
        Mockito.when(writeableCommandLine.getArgumentValues(option)).thenReturn(values);
        assertEquals(values, writeableCommandLine.getArgumentValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Mockito.mock(Option.class);
        List<Object> defaultValues = new ArrayList<>();
        writeableCommandLine.setDefaultValues(option, defaultValues);
        Mockito.verify(writeableCommandLine).setDefaultValues(option, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = Mockito.mock(Option.class);
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        Mockito.verify(writeableCommandLine).addSwitch(option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Mockito.mock(Option.class);
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        Mockito.verify(writeableCommandLine).setDefaultSwitch(option, defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(property, value);
        Mockito.verify(writeableCommandLine).addProperty(property, value);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "--option";
        boolean expectedResult = true;
        assertTrue(writeableCommandLine.looksLikeOption(argument));
    }
}