import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for WriteableCommandLine interface
 */
public class WriteableCommandLineTest {

    /**
     * Test method for {@link WriteableCommandLine#addOption(Option)}
     */
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    /**
     * Test method for {@link WriteableCommandLine#addValue(Option, Object)}
     */
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addValue(option, "value");
        List values = commandLine.getOptionValues("option");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#getValues(Option)}
     */
    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultValues(Option, List)}
     */
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        List defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertArrayEquals(new String[]{"default1", "default2"}, values.toArray());
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)}
     */
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.getValue().isPresent());
        Assert.assertEquals(true, option.getValue().get());
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultSwitch(Option, Boolean)}
     */
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.getValue().isPresent());
        Assert.assertEquals(true, option.getValue().get());
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(String, String)}
     */
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createMockCommandLine();
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getPropertyValues("property").get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#looksLikeOption(String)}
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    /**
     * Test method for edge case: {@link WriteableCommandLine#addOption(Option)}
     * when multiple options with the same name are added
     */
    @Test(expected = IllegalStateException.class)
    public void testAddOptionDuplicateName() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    /**
     * Test method for edge case: {@link WriteableCommandLine#addValue(Option, Object)}
     * when an empty option is passed
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueEmptyOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("").build();
        commandLine.addValue(option, null);
    }

    /**
     * Test method for edge case: {@link WriteableCommandLine#getValues(Option)}
     * when no values are set for an option
     */
    @Test
    public void testGetValuesNoValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        List values = commandLine.getOptionValues("option");
        Assert.assertEquals(0, values.size());
    }

    /**
     * Test method for edge case: {@link WriteableCommandLine#setDefaultValues(Option, List)}
     * when an empty list is passed
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesEmptyList() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.setDefaultValues(option, new ArrayList<>());
    }

    /**
     * Test method for edge case: {@link WriteableCommandLine#addSwitch(Option, boolean)}
     * when the switch is already set
     */
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    /**
     * Test method for edge case: {@link WriteableCommandLine#setDefaultSwitch(Option, Boolean)}
     * when the switch is already set
     */
    @Test(expected = IllegalStateException.class)
    public void testSetDefaultSwitchAlreadySet() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.setDefaultSwitch(option, true);
        commandLine.setDefaultSwitch(option, false);
    }

    /**
     * Test method for edge case: {@link WriteableCommandLine#addProperty(String, String)}
     * when a property with the same name already exists
     */
    @Test(expected = IllegalStateException.class)
    public void testAddPropertyDuplicateName() {
        WriteableCommandLine commandLine = createMockCommandLine();
        commandLine.addProperty("property", "value1");
        commandLine.addProperty("property", "value2");
    }

    /**
     * Helper method to create a mock WriteableCommandLine object
     * @return a mock WriteableCommandLine instance
     */
    private WriteableCommandLine createMockCommandLine() {
        return new CliOptions();
    }

}