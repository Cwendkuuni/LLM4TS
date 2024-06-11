import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("input", "i", false, "Input file path");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("i"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("output", "o", true, "Output file path");
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        Assert.assertEquals(Arrays.asList("output.txt"), commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("quiet", "q", false, "Suppress warnings");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("quiet", "q", false, "Suppress warnings");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("input", "i", false, "Input file path");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("input", "i", false, "Input file path");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("i"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("output", "o", true, "Output file path");
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("output.txt"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("output", "o", true, "Output file path");
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("quiet", "q", false, "Suppress warnings");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("property", "p", true, "Property name");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getProperty(option, "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("property", "p", true, "Property name");
        commandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("property", "value");
        commandLine.addProperty(option, properties);
        Assert.assertEquals("value", commandLine.getProperty(option, "defaultValue"));
        Assert.assertEquals(Collections.unmodifiableSet(properties.keySet()), commandLine.getProperties(option));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--input", "input.txt", "--output", "output.txt"));
        Assert.assertEquals("--input input.txt --output output.txt", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--input", "input.txt", "--output", "output.txt"));
        Assert.assertEquals(Arrays.asList(new Option("input", "i", false, "Input file path"), new Option("output", "o", true, "Output file path")), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--input", "input.txt", "--output", "output.txt"));
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(Arrays.asList("i", "o"), optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("output", "o", true, "Output file path");
        commandLine.addOption(option);
        List defaultValues = Arrays.asList("output1.txt", "output2.txt");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("quiet", "q", false, "Suppress warnings");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--input", "input.txt", "--output", "output.txt"));
        Assert.assertEquals(Arrays.asList("--input", "input.txt", "--output", "output.txt"), commandLine.getNormalised());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithInvalidOptionName() {
        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        WriteableCommandLine commandLine = parser.parse(new String[]{"--invalid-option"}, new ParseContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNullOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = null;
        commandLine.addValue(option, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSwitchWithNullOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = null;
        commandLine.addSwitch(option, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNullValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("input", "i", false, "Input file path");
        commandLine.addOption(option);
        commandLine.addValue(option, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyWithNullOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = null;
        commandLine.addProperty(option, "property", "value");
    }
}