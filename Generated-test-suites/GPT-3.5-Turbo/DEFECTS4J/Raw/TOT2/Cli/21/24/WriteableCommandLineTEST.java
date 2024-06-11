package org.apache.commons.cli2;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        cmd.addOption(option);
        cmd.addValue(option, "value");
        assertTrue(cmd.getUndefaultedValues(option).contains("value"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testSwitch");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertTrue(cmd.isTrue("testSwitch"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testSwitch");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testProperty");
        cmd.addOption(option);
        cmd.addProperty(option, "key", "value");
        assertEquals("value", cmd.getProperty("testProperty", "key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        assertTrue(cmd.looksLikeOption("-option"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options = new ArrayList<>();
        private List<Object> values = new ArrayList<>();
        private List<Boolean> switches = new ArrayList<>();
        private List<String> properties = new ArrayList<>();

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            values.add(value);
        }

        @Override
        public List getUndefaultedValues(Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            //Not implemented for test
        }

        @Override
        public void addSwitch(Option option, boolean value) {
            switches.add(value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            //Not implemented for test
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            properties.add(value);
        }

        @Override
        public void addProperty(String property, String value) {
            properties.add(value);
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        @Override
        public boolean hasOption(String option) {
            for (Option opt : options) {
                if (opt.getName().equals(option)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isTrue(String option) {
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).getName().equals(option)) {
                    return switches.get(i);
                }
            }
            return false;
        }

        @Override
        public String getProperty(String option, String key) {
            return properties.get(0);
        }

        @Override
        public void setCurrentOption(Option option) {
            //Not implemented for test
        }
    }
}