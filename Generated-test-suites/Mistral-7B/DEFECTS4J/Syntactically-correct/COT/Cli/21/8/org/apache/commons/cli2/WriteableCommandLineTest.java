import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for WriteableCommandLine interface.
 */
public class WriteableCommandLineTest {

    /**
     * Test for addOption method.
     */
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    /**
     * Test for addValue method.
     */
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test");
        Assert.assertEquals("test", commandLine.getUndefaultedValues(option).get(0));
    }

    /**
     * Test for getUndefaultedValues method.
     */
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[]{"--option", "test1", "test2"});
        Option option = new Option("o", "option");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1", values.get(0));
        Assert.assertEquals("test2", values.get(1));
    }

    /**
     * Test for setDefaultValues method.
     */
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        List<String> defaultValues = new ArrayList<>(Arrays.asList("default1", "default2"));
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(2, commandLine.getUndefaultedValues(option).size());
        Assert.assertEquals("default1", commandLine.getUndefaultedValues(option).get(0));
        Assert.assertEquals("default2", commandLine.getUndefaultedValues(option).get(1));
    }

    /**
     * Test for addSwitch method.
     */
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    /**
     * Test for setDefaultSwitch method.
     */
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    /**
     * Test for addProperty method.
     */
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getProperties().get("option.property"));
    }

    /**
     * Test for looksLikeOption method.
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Assert.assertFalse(commandLine.looksLikeOption("test"));
        commandLine.addOption(Option.builder("t").longOpt("test").build());
        Assert.assertTrue(commandLine.looksLikeOption("test"));
    }

    /**
     * Test for getCurrentOption method.
     */
    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }

    /**
     * Test for setCurrentOption method.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetCurrentOptionException() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        commandLine.setCurrentOption(option1);
        commandLine.setCurrentOption(option2);
    }
}