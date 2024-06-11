import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for WriteableCommandLine interface.
 */
public class WriteableCommandLineTest {

    /**
     * Test method for {@link WriteableCommandLine#addOption(Option)}.
     */
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        Assert.assertNotNull(commandLine.getOption(option.getLongOpt()));
    }

    /**
     * Test method for {@link WriteableCommandLine#addValue(Option, Object)}.
     */
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getValuesForOption(option);
        Assert.assertEquals("value", values.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#getValuesForOption(Option)}.
     */
    @Test
    public void testGetValuesForOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValuesForOption(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultValues(Option, List)}.
     */
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValuesForOption(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)}
     * with existing switch value.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)}
     */
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getValue(option.getLongOpt()));
    }

    /**
     * Test method for {@link WriteableCommandLine#setValue(String, String)}
     */
    @Test
    public void testSetValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getValue("property"));
    }

    /**
     * Test method for {@link WriteableCommandLine#looksLikeOption(String)}
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    /**
     * Create a mock WriteableCommandLine for testing.
     *
     * @return a mock WriteableCommandLine instance
     */
    private WriteableCommandLine createMockCommandLine() {
        return new DefaultWriteableCommandLine();
    }

}