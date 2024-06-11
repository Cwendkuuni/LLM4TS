
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "output file");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("n", "name", true, "name");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "John");

        List<String> values = commandLine.getValues(option, new ArrayList<>());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("John", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "verbose output");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("d", "debug", false, "debug mode");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Assert.assertFalse(commandLine.hasOption(option));

        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option2);

        Assert.assertEquals(option1, commandLine.getOption("i"));
        Assert.assertEquals(option2, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("n", "name", true, "name");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "John");
        commandLine.addValue(option, "Doe");

        List<String> values = commandLine.getValues(option, new ArrayList<>());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("John", values.get(0));
        Assert.assertEquals("Doe", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "verbose output");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        commandLine = new WriteableCommandLineImpl(new Option("n", "name", true, "name"), new ArrayList<>());

        commandLine.addProperty("age", "35");
        commandLine.addProperty("gender", "male");

        Assert.assertEquals("35", commandLine.getProperty("age", null));
        Assert.assertEquals("male", commandLine.getProperty("gender", null));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("o", "output", true, "output file");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Assert.assertTrue(commandLine.looksLikeOption("-o"));
        Assert.assertFalse(commandLine.looksLikeOption("output"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addValue(option1, "input.txt");
        commandLine.addValue(option2, "output.txt");

        Assert.assertEquals("input.txt output.txt", commandLine.toString());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("v", "values", true, "values");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        List<String> defaults = Arrays.asList("1", "2", "3");
        commandLine.setDefaultValues(option, defaults);

        List<String> values = commandLine.getValues(option, new ArrayList<>());
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("1", values.get(0));
        Assert.assertEquals("2", values.get(1));
        Assert.assertEquals("3", values.get(2));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "verbose output");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option1 = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addValue(option1, "input.txt");
        commandLine.addValue(option2, "output.txt");

        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("input.txt", normalised.get(0));
        Assert.assertEquals("output.txt", normalised.get(1));
    }
}
