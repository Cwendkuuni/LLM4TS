import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        Option option = Option.builder("o").required(false)
                .longOpt("option")
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        Option option = Option.builder("o").required(false)
                .longOpt("option")
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value"));
        commandLine.addValue(option, "testValue");

        List values = commandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", commandLine.toString());
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        Option option = Option.builder("s").required(false)
                .longOpt("switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(true, switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_AddDuplicateSwitch() {
        Option option = Option.builder("s").required(false)
                .longOpt("switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        Option option = Option.builder("o").required(false)
                .longOpt("option")
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value"));
        commandLine.addValue(option, "testValue");

        Option retrievedOption = commandLine.getOption(option.getPreferredName());
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        Option option = Option.builder("o").required(false)
                .longOpt("option")
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value"));
        commandLine.addValue(option, "testValue");

        boolean hasOption = commandLine.hasOption(option);
        Assert.assertEquals(true, hasOption);
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        Option option = Option.builder("o").required(false)
                .longOpt("option")
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value1", "value2"));
        commandLine.addValue(option, "testValue");

        List values = commandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        Option option = Option.builder("o").required(false)
                .longOpt("option")
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value1", "value2"));

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        Option option = Option.builder("s").required(false)
                .longOpt("switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--switch"));
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(true, switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperty() {
        Option option = Option.builder("p").required(false)
                .longOpt("property")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addProperty(option, "property", "testProperty");

        String propertyValue = commandLine.getProperty(option, "property", "");
        Assert.assertEquals("testProperty", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_AddProperty() {
        Option option = Option.builder("p").required(false)
                .longOpt("property")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addProperty(option, "property", "testProperty1");
        commandLine.addProperty(option, "property", "testProperty2");

        Map<String, String> properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("testProperty1", properties.get("property"));
        Assert.assertEquals("testProperty2", properties.get("property"));
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        Option option = Option.builder("o").required(false)
                .longOpt("option")
                .hasArg(true)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value1", "value2"));
        commandLine.addValue(option, "testValue");

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "test testValue value1 value2";
        Assert.assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptions() {
        Option option1 = Option.builder("o1").required(false)
                .longOpt("option1")
                .build();

        Option option2 = Option.builder("o2").required(false)
                .longOpt("option2")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test1"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptionTriggers() {
        Option option1 = Option.builder("o1").required(false)
                .longOpt("option1")
                .build();

        Option option2 = Option.builder("o2").required(false)
                .longOpt("option2")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test1"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("o1", "o2")));
    }
}