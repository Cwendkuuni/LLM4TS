
import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.ArgumentImpl;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.GroupImpl;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.validation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        // Assert
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        Object value = "testValue";

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, value);

        // Assert
        List<Object> values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList(value), values);
    }

    @Test
    public void testAddSwitch() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        boolean value = true;

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, value);

        // Assert
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        // Assert
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Option result = commandLine.getOption("testOption");

        // Assert
        assertEquals(option, result);
    }

    @Test
    public void testGetValuesWithDefaultValues() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        Object value = "testValue";
        List<Object> defaultValues = Arrays.asList("defaultValue");

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, value);
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> result = commandLine.getValues(option, null);

        // Assert
        assertEquals(Arrays.asList(value), result);
    }

    @Test
    public void testGetUndefaultedValues() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        Object value = "testValue";

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, value);
        List<Object> result = commandLine.getUndefaultedValues(option);

        // Assert
        assertEquals(Arrays.asList(value), result);
    }

    @Test
    public void testGetSwitchWithDefaultValue() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        boolean defaultValue = true;

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Boolean result = commandLine.getSwitch(option, defaultValue);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testGetPropertyWithDefaultOption() {
        // Arrange
        String property = "testProperty";
        String value = "testValue";

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(OptionBuilder.withLongName("testOption").create(), Collections.emptyList());
        commandLine.addProperty(property, value);
        String result = commandLine.getProperty(property);

        // Assert
        assertEquals(value, result);
    }

    @Test
    public void testAddProperty() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        String property = "testProperty";
        String value = "testValue";

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, property, value);

        // Assert
        Set<String> properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertTrue(properties.contains(property));
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddPropertyWithOptionDefaultValues() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        List<String> defaultValues = Arrays.asList("defaultValue");

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultValues(option, defaultValues);

        // Assert
        List<Object> result = commandLine.getValues(option, null);
        assertEquals(defaultValues, result);
    }

    @Test
    public void testGetValueListWithEmptyList() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        List<Object> defaultValues = Arrays.asList("defaultValue");

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultValues(option, defaultValues);

        // Assert
        List<Object> result = commandLine.getValues(option, Collections.emptyList());
        assertEquals(defaultValues, result);
    }

    @Test
    public void testLooksLikeOption() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        String trigger = "-t";

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        boolean result = commandLine.looksLikeOption(trigger);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testToString() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        String argument = "testArgument";

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));
        String result = commandLine.toString();

        // Assert
        assertEquals(argument, result);
    }

    @Test
    public void testGetOptions() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        List<Option> result = commandLine.getOptions();

        // Assert
        assertEquals(Collections.singletonList(option), result);
    }

    @Test
    public void testGetOptionTriggers() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Set<String> result = commandLine.getOptionTriggers();

        // Assert
        assertEquals(Collections.singleton("testOption"), result);
    }

    @Test
    public void testSetDefaultValues() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        List<String> defaultValues = Arrays.asList("defaultValue");

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> result = commandLine.getValues(option, null);

        // Assert
        assertEquals(defaultValues, result);
    }

    @Test
    public void testSetDefaultSwitch() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        Boolean defaultSwitch = true;

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultSwitch(option, defaultSwitch);

        // Assert
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        // Arrange
        Option option = OptionBuilder.withLongName("testOption").create();
        String argument = "testArgument";

        // Act
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));
        List<Object> result = commandLine.getNormalised();

        // Assert
        assertEquals(Collections.singletonList(argument), result);
    }
}

