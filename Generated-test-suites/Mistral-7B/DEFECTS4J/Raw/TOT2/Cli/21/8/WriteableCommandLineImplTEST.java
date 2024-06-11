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
    public void testWriteableCommandLineImpl_SimpleUsage() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=')
                .requiredArg().hasArg().build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");

        Assert.assertEquals("option=testValue", commandLine.toString());
        Assert.assertEquals("testValue", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_MultipleValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=')
                .requiredArg().hasArg().build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");

        Assert.assertEquals("option=testValue1,testValue2", commandLine.toString());
        Assert.assertEquals(2, commandLine.getValues(option, null).size());
        Assert.assertEquals("testValue1", commandLine.getValues(option, null).get(0));
        Assert.assertEquals("testValue2", commandLine.getValues(option, null).get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_EmptyValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=')
                .requiredArg().hasArg().build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        Assert.assertEquals("option", commandLine.toString());
        Assert.assertEquals(Collections.EMPTY_LIST, commandLine.getValues(option, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_SwitchValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);
        commandLine.addValue(option, "testValue");

        Assert.fail("Should not reach this point");
    }

    @Test
    public void testWriteableCommandLineImpl_NoValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Assert.assertEquals("o", commandLine.toString());
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_Property() {
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator('=')
                .requiredArg()
                .hasArg()
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("property", "testValue");
        commandLine.addProperty(option, "property", "testValue");

        Assert.assertEquals("option=testValue", commandLine.toString());
        Assert.assertEquals("testValue", commandLine.getProperty(option, "defaultValue"));
        Assert.assertEquals(properties, commandLine.getProperties(option));
    }

    @Test
    public void testWriteableCommandLineImpl_MultipleProperties() {
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator('=')
                .requiredArg()
                .hasArg()
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "testValue1");
        properties.put("property2", "testValue2");
        commandLine.addProperty(option, "property1", "testValue1");
        commandLine.addProperty(option, "property2", "testValue2");

        Assert.assertEquals("option", commandLine.toString());
        Assert.assertEquals(properties, commandLine.getProperties(option));
    }

    @Test
    public void testWriteableCommandLineImpl_EmptyProperties() {
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator('=')
                .requiredArg()
                .hasArg()
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        Assert.assertEquals(Collections.emptyMap(), commandLine.getProperties(option));
    }

    @Test
    public void testWriteableCommandLineImpl_Switch() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Assert.assertEquals("o", commandLine.toString());
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_MultipleSwitches() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);

        Assert.assertEquals("o", commandLine.toString());
        Assert.assertTrue(commandLine.getSwitch(option, false));
        Assert.assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testWriteableCommandLineImpl_NoSwitch() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        Assert.assertFalse(commandLine.getSwitch(option, false));
        Assert.assertNull(commandLine.getSwitch(option, true));
    }

    @Test
    public void testWriteableCommandLineImpl_EmptyList() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        Assert.assertEquals(Collections.emptyList(), commandLine.getOptions());
        Assert.assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
    }

    @Test
    public void testWriteableCommandLineImpl_SingleOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        Assert.assertEquals(Collections.singletonList(option), commandLine.getOptions());
        Assert.assertEquals(Collections.singleton("o"), commandLine.getOptionTriggers());
    }

    @Test
    public void testWriteableCommandLineImpl_MultipleOptions() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
        Assert.assertEquals(Set.of("o1", "o2"), commandLine.getOptionTriggers());
    }
}