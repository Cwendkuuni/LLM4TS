package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WriteableCommandLineImplTest is a JUnit test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    /**
     * A test method for constructor WriteableCommandLineImpl.
     */
    @Test
    public void testConstructor() {
        Option rootOption = new Option("t", "test", false, "Run tests.");
        List arguments = new ArrayList();

        CommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Assert.assertEquals(rootOption, commandLine.getParseInfo().getRootOption());
        Assert.assertEquals(arguments, commandLine.getParseInfo().getArguments());
    }

    /**
     * A test method for method addOption.
     */
    @Test
    public void testAddOption() {
        Option rootOption = new Option("t", "test", false, "Run tests.");
        Option option1 = new Option("i", "input", true, "Input file.");
        Option option2 = new Option("o", "output", true, "Output file.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertTrue(commandLine.hasOption(option2));
        Assert.assertFalse(commandLine.hasOption(new Option("nonexistent", "noEffect", false, "No effect.")));
    }

    /**
     * A test method for method addValue.
     */
    @Test
    public void testAddValue() {
        Option option = new Option("i", "input", true, "Input file.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "input1.txt");
        commandLine.addValue(option, "input2.txt");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("input1.txt", values.get(0));
        Assert.assertEquals("input2.txt", values.get(1));
    }

    /**
     * A test method for method addSwitch.
     */
    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "Be verbose.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    /**
     * A test method for method hasOption.
     */
    @Test
    public void testHasOption() {
        Option option = new Option("i", "input", true, "Input file.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    /**
     * A test method for method getOption.
     */
    @Test
    public void testGetOption() {
        Option option = new Option("i", "input", true, "Input file.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("i");

        Assert.assertEquals(option, retrievedOption);
    }

    /**
     * A test method for method getValues.
     */
    @Test
    public void testGetValues() {
        Option option = new Option("i", "input", true, "Input file.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "input1.txt");
        commandLine.addValue(option, "input2.txt");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("input1.txt", values.get(0));
        Assert.assertEquals("input2.txt", values.get(1));
    }

    /**
     * A test method for method getUndefaultedValues.
     */
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("i", "input", true, "Input file.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    /**
     * A test method for method getSwitch.
     */
    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "Be verbose.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.addSwitch(option, false);

        switchValue = commandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);
    }

    /**
     * A test method for method getProperty.
     */
    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "Property value.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName", "propertyValue1");
        commandLine.addProperty(option, "propertyName", "propertyValue2");

        String propertyValue = commandLine.getProperty(option, "propertyName", "defaultValue");
        Assert.assertEquals("propertyValue2", propertyValue);
    }

    /**
     * A test method for method addProperty.
     */
    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "Property value.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName1", "propertyValue1");
        commandLine.addProperty(option, "propertyName2", "propertyValue2");

        Map properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("propertyName1", properties.keySet().iterator().next());
    }

    /**
     * A test method for method getProperties.
     */
    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property value.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName1", "propertyValue1");
        commandLine.addProperty(option, "propertyName2", "propertyValue2");

        Map commandLineProperties = commandLine.getProperties();
        Assert.assertEquals(2, commandLineProperties.size());
    }

    /**
     * A test method for method looksLikeOption.
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test", false, "Run tests."), new ArrayList());

        Assert.assertTrue(commandLine.looksLikeOption("t"));
        Assert.assertTrue(commandLine.looksLikeOption("test"));
        Assert.assertFalse(commandLine.looksLikeOption("nonexistent"));
    }

    /**
     * A test method for method toString.
     */
    @Test
    public void testToString() {
        Option option1 = new Option("i", "input", true, "Input file.");
        Option option2 = new Option("o", "output", true, "Output file.");
        Option rootOption = new Option("t", "test", false, "Run tests.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addValue(option1, "input1.txt");
        commandLine.addValue(option1, "input2.txt");
        commandLine.addValue(option2, "output1.txt");
        commandLine.addValue(option2, "output2.txt");

        String commandLineString = commandLine.toString();

        Assert.assertEquals("\"input1.txt\" \"input2.txt\" \"output1.txt\" \"output2.txt\"", commandLineString);
    }

    /**
     * A test method for method getOptions.
     */
    @Test
    public void testGetOptions() {
        Option option1 = new Option("i", "input", true, "Input file.");
        Option option2 = new Option("o", "output", true, "Output file.");
        Option rootOption = new Option("t", "test", false, "Run tests.");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addValue(option1, "input1.txt");
        commandLine.addValue(option1, "input2.txt");
        commandLine.addValue(option2, "output1.txt");
        commandLine.addValue(option2, "output2.txt");

        List options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
        Assert.assertEquals(rootOption, options.get(2));
    }

    /**
     * A test method for method parse.
     */
    @Test
    public void testParse() {
        Option option1 = new Option("i", "input", true, "Input file.");
        Option option2 = new Option("o", "output", true, "Output file.");
        Option rootOption = new Option("t", "test", false, "Run tests.");

        WriteableCommandLineImpl expectedCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        expectedCommandLine.addOption(option1);
        expectedCommandLine.addOption(option2);
        expectedCommandLine.addValue(option1, "input1.txt");
        expectedCommandLine.addValue(option1, "input2.txt");
        expectedCommandLine.addValue(option2, "output1.txt");
        expectedCommandLine.addValue(option2, "output2.txt");

        String[] args = new String[]{"-t", "--test", "--input", "input1.txt", "--input", "input2.txt", "--output", "output1.txt", "--output", "output2.txt"};

        try {
            WriteableCommandLineImpl commandLine = (WriteableCommandLineImpl) WriteableCommandLine.parse(args);
            Assert.assertEquals(expectedCommandLine, commandLine);
        } catch (ParseException e) {
            Assert.fail("Exception is thrown.");
        }
    }
}