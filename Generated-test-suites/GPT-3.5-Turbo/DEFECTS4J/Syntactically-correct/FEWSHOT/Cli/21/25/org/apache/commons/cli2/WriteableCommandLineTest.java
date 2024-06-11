package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testAddOption_NullOption() {
        assertThrows(IllegalArgumentException.class, () -> commandLine.addOption(null));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option).get(0));
    }

    @Test
    public void testAddValue_NullOption() {
        assertThrows(IllegalArgumentException.class, () -> commandLine.addValue(null, "value"));
    }

    @Test
    public void testAddValue_NullValue() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        assertThrows(IllegalArgumentException.class, () -> commandLine.addValue(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
        assertEquals("value2", commandLine.getUndefaultedValues(option).get(1));
    }

    @Test
    public void testGetUndefaultedValues_NoValues() {
        Option option = new OptionBuilder().withLongName("testOption").create();

        assertEquals(0, commandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testGetUndefaultedValues_NullOption() {
        assertThrows(IllegalArgumentException.class, () -> commandLine.getUndefaultedValues(null));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        commandLine.setDefaultValues(option, List.of("default1", "default2"));

        assertEquals("default1", commandLine.getDefaultValues(option).get(0));
        assertEquals("default2", commandLine.getDefaultValues(option).get(1));
    }

    @Test
    public void testSetDefaultValues_NullOption() {
        assertThrows(IllegalArgumentException.class,
        () -> commandLine.setDefaultValues(null, List.of("default1", "default2")));
    }

    @Test
    public void testSetDefaultValues_NullDefaults() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        assertThrows(IllegalArgumentException.class, () -> commandLine.setDefaultValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        commandLine.addSwitch(option, true);

        assertEquals(true, commandLine.getSwitch(option));
    }

    @Test
    public void testAddSwitch_NullOption() {
        assertThrows(IllegalArgumentException.class, () -> commandLine.addSwitch(null, true));
    }

    @Test
    public void testAddSwitch_DuplicateSwitch() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        commandLine.addSwitch(option, true);
        assertThrows(IllegalStateException.class, () -> commandLine.addSwitch(option, false));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        commandLine.setDefaultSwitch(option, true);

        assertEquals(true, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch_NullOption() {
        assertThrows(IllegalArgumentException.class,
        () -> commandLine.setDefaultSwitch(null, true));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        commandLine.addProperty(option, "property1", "value1");

        assertEquals("value1", commandLine.getProperty(option, "property1"));
    }

    @Test
    public void testAddProperty_NullOption() {
        assertThrows(IllegalArgumentException.class, () -> commandLine.addProperty(null, "property1", "value1"));
    }

    @Test
    public void testAddProperty_NullProperty() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        assertThrows(IllegalArgumentException.class, () -> commandLine.addProperty(option, null, "value1"));
    }

    @Test
    public void testAddProperty_NullValue() {
        Option option = new OptionBuilder().withLongName("testOption").create();
        assertThrows(IllegalArgumentException.class, () -> commandLine.addProperty(option, "property1", null));
    }

    @Test
    public void testAddProperty_DefaultProperty() {
        commandLine.addProperty("property1", "value1");

        assertEquals("value1", commandLine.getProperty(null, "property1"));
    }

    @Test
    public void testAddProperty_NullDefaultProperty() {
        assertThrows(IllegalArgumentException.class, () -> commandLine.addProperty(null, "property1", null));
    }

    @Test
    public void testAddProperty_NullDefaultValue() {
        assertThrows(IllegalArgumentException.class, () -> commandLine.addProperty("property1", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertEquals(true, commandLine.looksLikeOption("-option"));
        assertEquals(true, commandLine.looksLikeOption("--option"));
        assertEquals(false, commandLine.looksLikeOption("value"));
    }

}