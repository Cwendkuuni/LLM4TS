import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;
    private Option option;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl();
        option = new OptionImpl("test");
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        writeableCommandLine.addValue(option, value);
        assertTrue(writeableCommandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        writeableCommandLine.addValue(option, value1);
        writeableCommandLine.addValue(option, value2);
        List actual = writeableCommandLine.getUndefaultedValues(option);
        List expected = new ArrayList();
        expected.add(value1);
        expected.add(value2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList();
        defaultValues.add(new Object());
        defaultValues.add(new Object());
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        assertTrue(writeableCommandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = false;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "test";
        String value = "value";
        writeableCommandLine.addProperty(option, property, value);
        assertEquals(value, writeableCommandLine.getProperty(option, property));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        String property = "test";
        String value = "value";
        writeableCommandLine.addProperty(property, value);
        assertEquals(value, writeableCommandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-t";
        assertTrue(writeableCommandLine.looksLikeOption(argument));
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = new OptionImpl("current");
        writeableCommandLine.setCurrentOption(currentOption);
        assertEquals(currentOption, writeableCommandLine.getCurrentOption());
    }
}


class WriteableCommandLineImpl implements WriteableCommandLine {

    private List<Option> options = new ArrayList<>();
    private List<Object> values = new ArrayList<>();
    private List<Object> defaultValues = new ArrayList<>();
    private boolean switchValue;
    private Boolean defaultSwitch;
    private String property;

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
        this.defaultValues = defaultValues;
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        switchValue = value;
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        this.defaultSwitch = defaultSwitch;
    }

    @Override
    public void addProperty(Option option, String property, String value) {
        this.property = value;
    }

    @Override
    public void addProperty(String property, String value) {
        this.property = value;
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    @Override
    public Option getCurrentOption() {
        return null;
    }

    @Override
    public void setCurrentOption(Option currentOption) {

    }

    @Override
    public List<Option> getOptions() {
        return options;
    }

    @Override
    public List<Object> getValues(Option option) {
        return values;
    }

    @Override
    public List<Object> getDefaultValues(Option option) {
        return defaultValues;
    }

    @Override
    public Boolean getDefaultSwitch(Option option) {
        return defaultSwitch;
    }

    @Override
    public boolean getSwitchValue(Option option) {
        return switchValue;
    }

    @Override
    public String getProperty(Option option, String property) {
        return this.property;
    }

    @Override
    public String getProperty(String property) {
        return this.property;
    }
}