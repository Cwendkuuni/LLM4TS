import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());

        // when
        commandLine.addOption(new Option("option2"));

        // then
        Assert.assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        // given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());

        // when
        commandLine.addValue(commandLine.getOption("option"), "value");

        // then
        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        // given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());

        // when
        commandLine.addSwitch(commandLine.getOption("option"), true);

        // then
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        // given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("option"), true);

        // when
        commandLine.addSwitch(commandLine.getOption("option"), false);

        // then AssertionError is thrown
    }

    @Test
    public void testGetOption() {
        // given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option2"));

        // when
        Option option = commandLine.getOption("option");

        // then
        Assert.assertSame(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetProperties() {
        // given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption("property"), new ArrayList<>());
        commandLine.addProperty(commandLine.getOption("property"), "value");

        // when
        Set properties = commandLine.getProperties(commandLine.getOption("property"));

        // then
        Assert.assertEquals("value", properties.iterator().next());
    }

    @Test
    public void testToString() {
        // given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option2"));
        commandLine.addValue(commandLine.getOption("option"), "value");

        // when
        String result = commandLine.toString();

        // then
        String expected = "option option2 value";
        Assert.assertEquals(expected, result);
    }
}