import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
        option1 = new OptionImpl("option1");
        option2 = new OptionImpl("option2");
    }

    @Test
    public void addOption_validOption_success() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption("option1"));
    }

    @Test
    public void addValue_validOptionAndValue_success() {
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        assertEquals(expectedValues, commandLine.getValues(option1));
    }

    @Test
    public void addValue_invalidOptionAndValue_noChange() {
        commandLine.addOption(option1);
        commandLine.addValue(option2, "value1");
        assertEquals(0, commandLine.getValues(option1).size());
    }

    @Test
    public void getValues_optionWithValues_success() {
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, commandLine.getValues(option1));
    }

    @Test
    public void getValues_optionWithoutValues_emptyList() {
        commandLine.addOption(option1);
        assertEquals(0, commandLine.getValues(option1).size());
    }

    @Test
    public void setDefaultValues_validOptionAndValues_success() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option1, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option1));
    }

    @Test
    public void setDefaultValues_invalidOptionAndValues_noChange() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option1, defaultValues);
        assertEquals(0, commandLine.getValues(option2).size());
    }

    @Test(expected = IllegalStateException.class)
    public void addSwitch_alreadyAddedSwitch_exceptionThrown() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, false);
    }

    @Test
    public void addSwitch_validOptionAndValue_success() {
        commandLine.addOption(option1);
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.getSwitch(option1));
    }

    @Test
    public void addSwitch_invalidOptionAndValue_noChange() {
        commandLine.addOption(option1);
        commandLine.addSwitch(option2, true);
        assertFalse(commandLine.hasOption("option2"));
        assertNull(commandLine.getSwitch(option2));
    }

    @Test
    public void setDefaultSwitch_validOptionAndValue_success() {
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1));
    }

    @Test
    public void setDefaultSwitch_invalidOptionAndValue_noChange() {
        commandLine.setDefaultSwitch(option1, true);
        assertNull(commandLine.getSwitch(option2));
    }

    @Test
    public void addProperty_validPropertyAndValue_success() {
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));
    }

    @Test
    public void addProperty_existingProperty_updatedValue() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property1", "value2");
        assertEquals("value2", commandLine.getProperty("property1"));
    }

    @Test
    public void addProperty_invalidPropertyAndValue_noChange() {
        commandLine.addProperty("property1", "value1");
        assertNull(commandLine.getProperty("property2"));
    }

    @Test
    public void looksLikeOption_validOptionTrigger_true() {
        assertTrue(commandLine.looksLikeOption("-"));
    }

    @Test
    public void looksLikeOption_invalidOptionTrigger_false() {
        assertFalse(commandLine.looksLikeOption("+"));
    }

}