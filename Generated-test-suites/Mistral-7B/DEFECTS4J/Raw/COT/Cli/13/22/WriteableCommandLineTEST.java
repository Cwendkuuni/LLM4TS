import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WRITEABLE_COMMANDLINE;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getOptionValues("optionName");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetValues() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getOptionValues("optionName");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getOptionValues("optionName");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingValue() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        try {
            commandLine.addSwitch(option, false);
        } catch (IllegalStateException e) {
            throw new AssertionError("Unexpected exception", e);
        }
        Assert.fail("Expected IllegalStateException");
    }

    @Test
    public void testAddSwitch() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("optionName", true));
    }

    @Test
    public void testSetDefaultSwitch() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getOptionValue("optionName", false));
    }

    @Test
    public void testAddProperty() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        commandLine.addProperty("propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getProperties().getProperty("propertyName"));
    }

    @Test
    public void testLooksLikeOptionWithLongOptionName() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        String longArgument = "--longOptionName";
        Assert.assertTrue(commandLine.looksLikeOption(longArgument));
    }

    @Test
    public void testLooksLikeOptionWithShortOptionName() {
        WRITEABLE_COMMANDLINE commandLine = createMockCommandLine();
        String shortArgument = "-s";
        Assert.assertFalse(commandLine.looksLikeOption(shortArgument));
    }

    private WRITEABLE_COMMANDLINE createMockCommandLine() {
        return new MockWriteableCommandLine();
    }

}