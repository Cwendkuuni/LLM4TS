package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * WriteableCommandLineImplTest class containing test cases for the WriteableCommandLineImpl class.
 */
public class WriteableCommandLineImplTest {

    /**
     * Test method for {@link WriteableCommandLineImpl#WriteableCommandLineImpl(Option, List)} constructor.
     */
    @Test
    public void testWriteableCommandLineImplConstructor() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();
        arguments.add("input.txt");

        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Test the resulting CommandLine object
        Assert.assertEquals(option, cli.getOptions().get(0));
        Assert.assertEquals("input.txt", cli.getNormalised().get(0));
        Assert.assertEquals(false, cli.hasOption(new Option("output", "o", true, "Output file")));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addOption(Option)} method.
     */
    @Test
    public void testAddOption() {
        // Create a test option and arguments
        Option optionInput = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();
        arguments.add("input.txt");

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(new Option(), arguments);

        // Add an option to the CommandLine object
        cli.addOption(optionInput);

        // Test the resulting CommandLine object
        Assert.assertEquals(1, cli.getOptions().size());
        Assert.assertEquals(optionInput, cli.getOptions().get(0));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addValue(Option, Object)} method.
     */
    @Test
    public void testAddValue() throws ParseException {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();
        arguments.add("input.txt");

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Add a value to the option
        cli.addValue(option, "output.txt");

        // Test the resulting CommandLine object
        Assert.assertEquals("output.txt", cli.getValues(option, null).get(0));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addSwitch(Option, boolean)} method.
     */
    @Test
    public void testAddSwitch() {
        // Create a test option and arguments
        Option option = new Option("verbose", "v", false, "Verbose output");
        List arguments = new ArrayList();

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Add a switch to the option
        cli.addSwitch(option, true);

        // Test the resulting CommandLine object
        Assert.assertTrue(cli.getSwitch(option, false));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#hasOption(Option)} method.
     */
    @Test
    public void testHasOption() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Test the presence of the option
        Assert.assertTrue(cli.hasOption(option));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getOption(String)} method.
     */
    @Test
    public void testGetOption() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Get the option by its trigger string
        Option retrievedOption = cli.getOption("input");

        // Test the retrieved Option object
        Assert.assertEquals(option, retrievedOption);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getValues(Option, List)} method.
     */
    @Test
    public void testGetValues() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();
        arguments.add("input.txt");
        arguments.add("output.txt");

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Get the values of the option
        List values = cli.getValues(option, null);

        // Test the retrieved values
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("input.txt", values.get(0));
        Assert.assertEquals("output.txt", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getUndefaultedValues(Option)} method.
     */
    @Test
    public void testGetUndefaultedValues() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();
        arguments.add("input.txt");

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Get the unefaulted values of the option
        List values = cli.getUndefaultedValues(option);

        // Test the retrieved values
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("input.txt", values.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getSwitch(Option, Boolean)} method.
     */
    @Test
    public void testGetSwitch() {
        // Create a test option and arguments
        Option option = new Option("verbose", "v", false, "Verbose output");
        List arguments = new ArrayList();

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Set a switch for the option
        cli.addSwitch(option, true);

        // Get the switch value of the option
        Boolean switchValue = cli.getSwitch(option, false);

        // Test the retrieved switch value
        Assert.assertTrue(switchValue);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperty(String)} method.
     */
    @Test
    public void testGetProperty() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Set a property for the option
        cli.addProperty(option, "name", "John Doe");

        // Get the property value of the option
        String propertyValue = cli.getProperty("name");

        // Test the retrieved property value
        Assert.assertEquals("John Doe", propertyValue);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addProperty(Option, String, String)} method.
     */
    @Test
    public void testAddProperty() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Set a property for the option
        cli.addProperty(option, "name", "John Doe");

        // Test the CommandLine object after setting the property
        Map properties = cli.getProperties(option);

        // Test the retrieved properties
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("name", properties.keySet().iterator().next());
        Assert.assertEquals("John Doe", properties.get("name"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperty(Option, String, String)} method.
     */
    @Test
    public void testGetPropertyWithDefaultValue() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Set a property for the option
        cli.addProperty(option, "name", "John Doe");

        // Get the property value of the option with a default value
        String propertyValue = cli.getProperty(option, "defaultValue");

        // Test the retrieved property value
        Assert.assertEquals("John Doe", propertyValue);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperties(Option)} method.
     */
    @Test
    public void testGetProperties() {
        // Create a test option and arguments
        Option option = new Option("input", "i", false, "Input file");
        List arguments = new ArrayList();

        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, arguments);

        // Set some properties for the option
        cli.addProperty(option, "name", "John Doe");
        cli.addProperty(option, "age", "30");

        // Get all properties of the option
        Set properties = cli.getProperties(option);

        // Test the retrieved properties
        Assert.assertEquals(2, properties.size());
    }
}