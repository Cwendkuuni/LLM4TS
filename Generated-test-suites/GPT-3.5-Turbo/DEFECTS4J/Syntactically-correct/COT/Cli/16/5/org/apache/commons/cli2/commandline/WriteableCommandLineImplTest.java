import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        // Act
        commandLine.addOption(option);

        // Assert
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Arrange
        Argument argument = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, null);

        // Act
        commandLine.addValue(argument, "testValue");

        // Assert
        Assert.assertEquals("testValue", commandLine.getValues(argument, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        // Act
        commandLine.addSwitch(option, true);

        // Assert
        Assert.assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        // Act
        boolean hasOption = commandLine.hasOption(option);

        // Assert
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        // Act
        Option result = commandLine.getOption("testOption");

        // Assert
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        // Arrange
        Argument argument = new Argument();
        argument.setDefaultValue("default");
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, null);
        commandLine.addValue(argument, "testValue");

        // Act
        List<String> values = commandLine.getValues(argument, null);

        // Assert
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Arrange
        Argument argument = new Argument();
        argument.setDefaultValue("default");
        
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, null);
        commandLine.addValue(argument, "testValue");

        // Act
        List<String> values = commandLine.getUndefaultedValues(argument);

        // Assert
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);

        // Act
        Boolean result = commandLine.getSwitch(option, null);

        // Assert
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetProperty() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addProperty(option, "testProperty", "testValue");

        // Act
        String result = commandLine.getProperty(option, "testProperty", null);

        // Assert
        Assert.assertEquals("testValue", result);
    }

    @Test
    public void testAddProperty() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        // Act
        commandLine.addProperty(option, "testProperty", "testValue");

        // Assert
        Assert.assertEquals("testValue", commandLine.getProperty(option, "testProperty", null));
    }

    @Test
    public void testGetProperties() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addProperty(option, "testProperty", "testValue");

        // Act
        Set<String> properties = commandLine.getProperties(option);

        // Assert
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains("testProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        // Act
        boolean result = commandLine.looksLikeOption("-t");

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setArguments(Arrays.asList("test", "test2"));

        // Act
        String result = commandLine.toString();

        // Assert
        Assert.assertEquals("test test2", result);
    }

    @Test
    public void testGetOptions() {
        // Arrange
        Option option1 = new Option("testOption1", "Test option 1");
        Option option2 = new Option("testOption2", "Test option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // Act
        List<Option> options = commandLine.getOptions();

        // Assert
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        // Arrange
        Option option1 = new Option("testOption1", "Test option 1");
        Option option2 = new Option("testOption2", "Test option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // Act
        Set<String> optionTriggers = commandLine.getOptionTriggers();

        // Assert
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("testOption1"));
        Assert.assertTrue(optionTriggers.contains("testOption2"));
    }

    @Test
    public void testSetDefaultValues() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        // Act
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        // Assert
        Assert.assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        // Act
        commandLine.setDefaultSwitch(option, true);

        // Assert
        Assert.assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        // Arrange
        Option option = new Option("testOption", "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        // Act
        List<String> normalised = commandLine.getNormalised();

        // Assert
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), normalised);
    }

}