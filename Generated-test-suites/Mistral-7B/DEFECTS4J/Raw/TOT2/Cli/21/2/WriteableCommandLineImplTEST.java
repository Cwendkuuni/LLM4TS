import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        Option option = Option.builder("o")
                .longOpt("option")
                .description("An option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        Option option = Option.builder("o")
                .longOpt("option")
                .description("An option")
                .build();
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List<Object> values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        Option option = Option.builder("s")
                .longOpt("switch")
                .description("A switch")
                .build();
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));

        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getSwitch(option, true));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_AddSwitch_AlreadySet() {
        Option option = Option.builder("s")
                .longOpt("switch")
                .description("A switch")
                .build();
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        Option option = Option.builder("o")
                .longOpt("option")
                .description("An option")
                .build();
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertFalse(commandLine.hasOption(new Option("anotherOption")));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        Option option = Option.builder("o")
                .longOpt("option")
                .description("An option")
                .build();
        commandLine.addOption(option);

        Option returnedOption = commandLine.getOption("option");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        Option option = Option.builder("o")
                .longOpt("option")
                .description("An option")
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<Object> values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        List<Object> emptyValues = commandLine.getValues(option, null);
        Assert.assertEquals(0, emptyValues.size());
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        Option option = Option.builder("o")
                .longOpt("option")
                .description("An option")
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<Object> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        List<Object> emptyValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, emptyValues.size());
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        Option option = Option.builder("s")
                .longOpt("switch")
                .description("A switch")
                .build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, false));
        Assert.assertTrue(commandLine.getSwitch(option, true));

        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testWriteableCommandLineImpl_AddProperty() {
        Option option = Option.builder("o")
                .longOpt("option")
                .description("An option")
                .build();
        commandLine.addOption(option);

        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "value1");
        commandLine.addProperty(option, "property1", "value1");

        Map<String, String> optionProperties = commandLine.getProperties(option);
        Assert.assertEquals(1, optionProperties.size());
        Assert.assertEquals("value1", optionProperties.get("property1"));

        Map<String, String> emptyProperties = commandLine.getProperties(option);
        Assert.assertEquals(0, emptyProperties.size());
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        Option option1 = Option.builder("o1")
                .longOpt("option1")
                .description("Option 1")
                .build();
        Option option2 = Option.builder("o2")
                .longOpt("option2")
                .description("Option 2")
                .build();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option2, "property2", "value2");

        String commandLineString = commandLine.toString();
        List<String> commandLineParts = Arrays.asList(commandLineString.split(" "));

        List<String> expectedParts = new ArrayList<>();
        expectedParts.add("o1:value1 value2");
        expectedParts.add("o2:value3");
        expectedParts.add("--option1=property1:value1");

        for (int i = 0; i < commandLineParts.size(); i++) {
            Assert.assertEquals(expectedParts.get(i), commandLineParts.get(i));
        }
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptions() {
        Option option1 = Option.builder("o1")
                .longOpt("option1")
                .description("Option 1")
                .build();
        Option option2 = Option.builder("o2")
                .longOpt("option2")
                .description("Option 2")
                .build();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptionTriggers() {
        Option option1 = Option.builder("o1")
                .longOpt("option1")
                .description("Option 1")
                .build();
        Option option2 = Option.builder("o2")
                .longOpt("option2")
                .description("Option 2")
                .build();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("o1"));
        Assert.assertTrue(optionTriggers.contains("o2"));
    }
}