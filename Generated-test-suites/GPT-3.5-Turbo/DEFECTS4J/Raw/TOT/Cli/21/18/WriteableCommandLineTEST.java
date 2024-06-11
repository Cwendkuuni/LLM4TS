import org.junit.jupiter.api.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

import static org.junit.jupiter.api.Assertions.*;

class WriteableCommandLineTest {

    @Test
    void addOption_shouldAddOptionToCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "Option");
        
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("o"));
    }

    @Test
    void addValue_shouldAddValueToOptionInCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "Option");

        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        assertTrue(commandLine.hasOptionValue("o", "value"));
    }

    @Test
    void getUndefaultedValues_shouldReturnValuesAssociatedWithOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "Option");

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(commandLine.getUndefaultedValues(option).size(), 2);
        assertTrue(commandLine.getUndefaultedValues(option).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("value2"));
    }

    @Test
    void setDefaultValues_shouldSetDefaultValuesForOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "Option");
        List defaultValues = List.of("default1", "default2");

        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);

        assertTrue(commandLine.hasOptionValue("o", "default1"));
        assertTrue(commandLine.hasOptionValue("o", "default2"));
    }

    @Test
    void addSwitch_shouldAddSwitchValueToOptionInCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("s", "switch", false, "Switch");

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasSwitch(option));
    }

    @Test
    void setDefaultSwitch_shouldSetDefaultSwitchValueForOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("s", "switch", false, "Switch");

        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    void addProperty_shouldAddPropertyValueToOptionInCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("p", "property", false, "Property");

        commandLine.addOption(option);
        commandLine.addProperty(option, "name", "value");

        assertTrue(commandLine.hasOptionProperty("p", "name"));
        assertEquals(commandLine.getOptionProperty("p", "name"), "value");
    }

    @Test
    void addProperty_shouldAddPropertyValueToDefaultPropertySet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addProperty("name", "value");

        assertTrue(commandLine.hasOptionProperty("", "name"));
        assertEquals(commandLine.getOptionProperty("", "name"), "value");
    }

    @Test
    void looksLikeOption_shouldReturnTrueForOptionTrigger() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    void setCurrentOption_shouldSetCurrentOptionInCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", false, "Option");

        commandLine.setCurrentOption(option);

        assertEquals(commandLine.getCurrentOption(), option);
    }
}


class WriteableCommandLineImpl implements WriteableCommandLine {

    private Option currentOption;

    @Override
    public void addOption(Option option) {
        // implementation code
    }

    @Override
    public void addValue(Option option, Object value) {
        // implementation code
    }

    @Override
    public List getUndefaultedValues(Option option) {
        // implementation code
        return null;
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        // implementation code
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        // implementation code
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        // implementation code
    }

    @Override
    public void addProperty(Option option, String property, String value) {
        // implementation code
    }

    @Override
    public void addProperty(String property, String value) {
        // implementation code
    }

    @Override
    public boolean looksLikeOption(String argument) {
        // implementation code
        return false;
    }

    @Override
    public Option getCurrentOption() {
        return currentOption;
    }

    @Override
    public void setCurrentOption(Option currentOption) {
        this.currentOption = currentOption;
    }
}