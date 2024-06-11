import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option2", "o2", false, "Option 2");
        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("value1", commandLine.getValues(option, Collections.emptyList()).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Assert.assertEquals(true, commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        Assert.assertEquals(2, commandLine.getValues(option, Collections.emptyList()).size());
        Assert.assertEquals("value1", commandLine.getValues(option, Collections.emptyList()).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option, Collections.emptyList()).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        Assert.assertEquals(1, commandLine.getUndefaultedValues(option).size());
        Assert.assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");

        Assert.assertEquals(1, commandLine.getProperties(option).size());
        Assert.assertEquals("property1", commandLine.getProperties(option).iterator().next());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "o1", false, "Option 1");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        Assert.assertEquals("-o1 value1", commandLine.toString());
    }
}