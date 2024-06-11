package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Argument argument1;
    private Argument argument2;

    @Before
    public void setUp() {
        option1 = createOption("-o");
        option2 = createOption("--option");
        argument1 = createArgument("arg1");
        argument2 = createArgument("arg2");

        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
    }

    private Option createOption(String trigger) {
        return new Option() {
            @Override
            public Option setValueSeparator(char separator) {
                return null;
            }

            @Override
            public Option setTrigger(String trigger) {
                return null;
            }

            @Override
            public Option setPreferredName(String preferredName) {
                return null;
            }

            @Override
            public Option setRequired(boolean required) {
                return null;
            }

            @Override
            public Option setDefault(String defaultValue) {
                return null;
            }

            @Override
            public Option setDescription(String description) {
                return null;
            }

            @Override
            public Option setArgs(int args) {
                return null;
            }

            @Override
            public char getValueSeparator() {
                return 0;
            }

            @Override
            public String getTrigger() {
                return trigger;
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public String getDefault() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public int getArgs() {
                return 0;
            }
        };
    }

    private Argument createArgument(String name) {
        return new Argument() {
            @Override
            public Argument setValueSeparator(char separator) {
                return null;
            }

            @Override
            public Argument setTriggers(String[] triggers) {
                return null;
            }

            @Override
            public Argument setDescription(String description) {
                return null;
            }

            @Override
            public Argument setMin(int min) {
                return null;
            }

            @Override
            public Argument setMax(int max) {
                return null;
            }

            @Override
            public char getValueSeparator() {
                return 0;
            }

            @Override
            public String[] getTriggers() {
                return new String[] { name };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public int getMin() {
                return 0;
            }

            @Override
            public int getMax() {
                return 0;
            }
        };
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(argument1, value);
        assertEquals(Collections.singletonList(value), commandLine.getUndefaultedValues(argument1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option2);
        assertEquals(option2, commandLine.getOption("--option"));
    }

    @Test
    public void testGetValues() {
        Object value = new Object();
        commandLine.addValue(argument1, value);
        assertEquals(Collections.singletonList(value), commandLine.getValues(argument1, new ArrayList<>()));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value = new Object();
        commandLine.addValue(argument1, value);
        assertEquals(Collections.singletonList(value), commandLine.getUndefaultedValues(argument1));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetProperty() {
        assertEquals(null, commandLine.getProperty("property"));

        commandLine.addProperty(option1, "property", "value");
        assertEquals("value", commandLine.getProperty(option1, "property", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option1, Collections.singletonList("default1"));
        assertEquals(Collections.singletonList("default1"), commandLine.getValues(option1, new ArrayList<>()));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetNormalised() {
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");
        assertEquals(2, commandLine.getNormalised().size());
    }
}