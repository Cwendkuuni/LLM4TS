import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the WriteableCommandLine interface.
 */
public class WriteableCommandLineTest {

    /**
     * Test for addOption method.
     */
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    /**
     * Test for addValue method.
     */
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getOptionValue("option");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    /**
     * Test for getValues method.
     */
    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    /**
     * Test for setDefaultValues method.
     */
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = Option.builder("option").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    /**
     * Test for addSwitch method.
     */
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    /**
     * Test for setDefaultSwitch method.
     */
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Option option = Option.builder("option").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    /**
     * Test for addProperty method.
     */
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property"));
    }

    /**
     * Test for looksLikeOption method.
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = MockWriteableCommandLine.create();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("--no-option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }
}

class MockWriteableCommandLine implements WriteableCommandLine {

    private List<Option> options = new ArrayList<>();
    private List<String> properties = new ArrayList<>();

    public static WriteableCommandLine create() {
        return new MockWriteableCommandLine();
    }

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        // not supported in this mock implementation
    }

    @Override
    public List<Option> getOptions() {
        return options;
    }

    @Override
    public List<String> getOptionValues(String option) {
        Option optionToFind = options.stream()
                .filter(o -> o.getLongOpt().equals(option))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Option not found"));
        return optionToFind.getValues();
    }

    @Override
    public List<String> getOptionValue(String option) {
        Option optionToFind = options.stream()
                .filter(o -> o.getLongOpt().equals(option))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Option not found"));
        return List.of(optionToFind.getValue());
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        // not supported in this mock implementation
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        // not supported in this mock implementation
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        // not supported in this mock implementation
    }

    @Override
    public void addProperty(String property, String value) {
        properties.add(property + "=" + value);
    }

    @Override
    public String getProperty(String property) {
        for (String prop : properties) {
            String[] parts = prop.split("=");
            if (parts[0].equals(property)) {
                return parts[1];
            }
        }
        return null;
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("--") || argument.startsWith("-");
    }
}