import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, false);
        Assert.assertFalse(writeableCommandLine.getSwitch(option, true));
        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_AddSwitch_AlreadySet() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, false);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("o");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test1");
        writeableCommandLine.addValue(option, "test2");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1", values.get(0));
        Assert.assertEquals("test2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, false);
        Assert.assertFalse(writeableCommandLine.getSwitch(option, true));
        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addProperty(option, "propertyName", "propertyValue");
        String propertyValue = writeableCommandLine.getProperty(option, "propertyName");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_AddProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("propertyName", "propertyValue");
        writeableCommandLine.addProperty(option, properties);
        String propertyValue = writeableCommandLine.getProperty(option, "propertyName");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperties() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("propertyName1", "propertyValue1");
        properties.setProperty("propertyName2", "propertyValue2");
        writeableCommandLine.addProperty(option, properties);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, propertiesSet.size());
        Assert.assertTrue(propertiesSet.containsAll(Arrays.asList("propertyName1", "propertyName2")));
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        String expected = "o option";
        String actual = writeableCommandLine.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptions() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
    }
}