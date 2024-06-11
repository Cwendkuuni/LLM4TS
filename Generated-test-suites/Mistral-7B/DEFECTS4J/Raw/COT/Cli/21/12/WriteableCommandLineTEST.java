import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertNotNull(commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List expectedValues = Arrays.asList("value1");
        List actualValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List expectedValues = Arrays.asList("value1", "value2");
        List actualValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        List defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);
        List expectedValues = Arrays.asList("value1", "value2");
        List actualValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true); //should throw an exception
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getOption("o").getValue().equals("true"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getOption("o").getValue().equals("true"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addProperty(option, "property", "value1");
        Assert.assertEquals("value1", commandLine.getOption("o").getPropertyValue("property"));
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        commandLine.addProperty("property", "value1");
        Assert.assertEquals("value1", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testCurrentOption() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.setCurrentOption(option1);
        Assert.assertEquals(option1, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = createDummyCommandLine();
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.setCurrentOption(null);
        commandLine.setCurrentOption(option2);
        Assert.assertEquals(option2, commandLine.getCurrentOption());
    }

    private WriteableCommandLine createDummyCommandLine() {
        return new DefaultWriteableCommandLine();
    }

}