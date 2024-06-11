import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals("value1", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        Assert.assertEquals(2, commandLine.getValues(option, null).size());
        Assert.assertEquals("value1", commandLine.getValues(option, null).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option, null).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals(1, commandLine.getUndefaultedValues(option).size());
        Assert.assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
        Assert.assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");

        Assert.assertEquals("value", commandLine.getProperty(option, "property", null));
        Assert.assertNull(commandLine.getProperty(option, "unknownProperty", null));
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Assert.assertEquals(2, commandLine.getProperties(option).size());
        Assert.assertTrue(commandLine.getProperties(option).contains("property1"));
        Assert.assertTrue(commandLine.getProperties(option).contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Assert.assertFalse(commandLine.looksLikeOption("option"));
        commandLine.addOption(Option.builder("--").longOpt("option").required(false).build());
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }
}