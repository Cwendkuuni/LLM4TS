import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption", new String[0]);
        Option option = Option.builder("testOption").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value1 value2");
        Option option = Option.builder("testOption").build();
        commandLine.addValue(option, "value1");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
        Assert.assertEquals(Arrays.asList("value1"), commandLine.getOptionValues("testOption"));

        commandLine.addValue(option, "value2");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getOptionValues("testOption"));
    }

    @Test
    public void testGetOptionValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value1 value2 value3");
        Option option = Option.builder("testOption").build();
        Assert.assertEquals(Arrays.asList("value1", "value2", "value3"), commandLine.getOptionValues("testOption"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption");
        Option option = Option.builder("testOption").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
        Assert.fail();
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption");
        Option option = Option.builder("testOption").build();
        commandLine.addSwitch(option, false);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
        Assert.assertFalse(option.getValue().isPresent());

        commandLine.addSwitch(option, true);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
        Assert.assertTrue(option.getValue().isPresent());
        Assert.assertEquals(Boolean.TRUE, option.getValue().get());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption");
        Option option = Option.builder("testOption").build();
        List<String> defaults = new ArrayList<>();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
        Assert.assertEquals(defaults, commandLine.getOptionValues("testOption"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testProperty");
        commandLine.addProperty("testProperty", "testValue");
        Assert.assertEquals("testValue", commandLine.getPropertyValue("testProperty"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        Assert.assertFalse(WriteableCommandLine.class.getDeclaredMethod("looksLikeOption").invoke(null, "arg1"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        Assert.assertTrue(WriteableCommandLine.class.getDeclaredMethod("looksLikeOption").invoke(null, "--arg1"));
    }
}