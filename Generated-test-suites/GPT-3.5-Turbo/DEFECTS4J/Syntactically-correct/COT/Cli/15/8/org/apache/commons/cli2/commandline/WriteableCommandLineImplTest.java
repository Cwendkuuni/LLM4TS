import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WriteableCommandLineImplTest {

    // 

    @Test
    public void testAddOption() {
        Option option = new Option("test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        assert commandLine.hasOption(option);
        assert commandLine.getOption("test") == option;
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, Collections.emptyList());

        assert values.size() == 1;
        assert values.get(0).equals("value");
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        assert commandLine.getSwitch(option, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        assert commandLine.hasOption(option);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        assert commandLine.getOption("test") == option;
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, Collections.emptyList());

        assert values.size() == 2;
        assert values.get(0).equals("value1");
        assert values.get(1).equals("value2");
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);

        assert values.size() == 2;
        assert values.get(0).equals("value1");
        assert values.get(1).equals("value2");
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        assert commandLine.getSwitch(option, false);
    }

    @Test
    public void testGetProperty() {
        PropertyOption option = new PropertyOption();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addProperty(option, "property", "value");

        assert commandLine.getProperty(option, "property", "default").equals("value");
    }

    @Test
    public void testGetPropertyWithDefault() {
        PropertyOption option = new PropertyOption();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assert commandLine.getProperty(option, "property", "default").equals("default");
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultValues(option, Collections.singletonList("default"));

        List<String> values = commandLine.getValues(option, Collections.emptyList());

        assert values.size() == 1;
        assert values.get(0).equals("default");
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultSwitch(option, true);

        assert commandLine.getSwitch(option, false);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("arg1 arg2"));

        List<String> normalised = commandLine.getNormalised();

        assert normalised.size() == 1;
        assert normalised.get(0).equals("arg1 arg2");
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assert commandLine.looksLikeOption("-test");
    }

    @Test
    public void testToString() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("arg1 arg2"));

        assert commandLine.toString().equals("arg1 arg2");
    }

    // 
}