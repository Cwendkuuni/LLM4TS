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
    public void testWriteableCommandLineImpl_addOption() {
        Option option = Option.builder("option")
                .required(false)
                .longOpt("longOption")
                .desc("This is a test option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        Assert.assertEquals(Arrays.asList(option), commandLine.getOptions());
        Assert.assertEquals(option, commandLine.getOption("option"));
        Assert.assertEquals(option, commandLine.getOption("longOption"));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = Option.builder("option")
                .required(false)
                .longOpt("longOption")
                .desc("This is a test option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = Option.builder("switch")
                .required(false)
                .longOpt("longSwitch")
                .desc("This is a test switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Assert.assertEquals(Boolean.TRUE, commandLine.getSwitch(option, Boolean.FALSE));
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = Option.builder("option")
                .required(false)
                .longOpt("longOption")
                .desc("This is a test option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        Option option = Option.builder("switch")
                .required(false)
                .longOpt("longSwitch")
                .desc("This is a test switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = Option.builder("option")
                .required(false)
                .longOpt("longOption")
                .desc("This is a test option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty("option", "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = Option.builder("option")
                .required(false)
                .longOpt("longOption")
                .desc("This is a test option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option);

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "arg1 arg2 --option arg1 --longOption arg2";
        Assert.assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option = Option.builder("option")
                .required(false)
                .longOpt("longOption")
                .desc("This is a test option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option);

        List normalised = commandLine.getNormalised();

        List expectedNormalised = Arrays.asList("arg1", "arg2", "--option", "--longOption", "arg1", "--longOption", "arg2");
        Assert.assertEquals(expectedNormalised, normalised);
    }
}