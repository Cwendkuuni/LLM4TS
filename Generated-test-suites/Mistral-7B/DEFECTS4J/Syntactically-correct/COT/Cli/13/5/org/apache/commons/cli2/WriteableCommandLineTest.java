import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals("value1", commandLine.getOptionValue(option.getLongOpt()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getOptionValues(option.getLongOpt());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        List defaultValues = java.util.Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getOptionValues(option.getLongOpt());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption(option.getLongOpt()));
        Assert.assertTrue(commandLine.isSet(option.getLongOpt()));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption(option.getLongOpt()));
        Assert.assertTrue(commandLine.isSet(option.getLongOpt()));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        commandLine.addProperty("propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getProperty("propertyName"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithSet() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Assert.assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.looksLikeOption("optionName"));
    }

    private WriteableCommandLine createWriteableCommandLine() {
        return new DefaultWriteableCommandLine();
    }

    @Test
    public void testGetEmptyValues() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        List values = commandLine.getOptionValues(option.getLongOpt());
        Assert.assertEquals(0, values.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEmptyValuesWithOption() {
        WriteableCommandLine commandLine = createWriteableCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        String value = commandLine.getOptionValue(option.getLongOpt());
    }
}