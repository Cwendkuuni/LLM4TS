package org.apache.commons.cli2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        
        commandLine.addOption(option);

        verify(commandLine, times(1)).addOption(option);
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Testing Option");

        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option).get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Testing Option");
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getUndefaultedValues(option);

        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Testing Option");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals("default", commandLine.getValues(option).get(0));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsIllegalStateException() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Testing Option");

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void testDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Testing Option");

        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option).booleanValue());

        commandLine.setDefaultSwitch(option, false);
        assertEquals(false, commandLine.getSwitch(option).booleanValue());
    }
    
    @Test
    public void testAddPropertyToOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Testing Option");

        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, "property"));
    }
    
    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getDefaultProperty("property"));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertEquals(true, commandLine.looksLikeOption("-option"));
        assertEquals(true, commandLine.looksLikeOption("--option"));
        assertEquals(false, commandLine.looksLikeOption("value"));
    }
    
    //mock class WriteableCommandLineImpl
    private class WriteableCommandLineImpl implements WriteableCommandLine {

        @Override
        public void addOption(Option option) {
            //implementation here
        }

        @Override
        public void addValue(Option option, Object value) {
            //implementation here
        }

        @Override
        public List getUndefaultedValues(Option option) {
            //implementation here
            return null;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            //implementation here
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            //implementation here
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            //implementation here
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            //implementation here
        }

        @Override
        public void addProperty(String property, String value) {
            //implementation here
        }

        @Override
        public boolean looksLikeOption(String argument) {
            //implementation here
            return false;
        }
    }
}