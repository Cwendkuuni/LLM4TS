import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());

        // When
        commandLine.addOption(new Option("b"));

        // Then
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(new Option("a")));
        assertTrue(options.contains(new Option("b")));
    }

    @Test
    public void testAddValue() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());

        // When
        commandLine.addValue(new Option("a"), "value");

        // Then
        List values = commandLine.getValues(new Option("a"), Collections.emptyList());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());

        // When
        commandLine.addSwitch(new Option("a"), true);

        // Then
        Boolean switchValue = commandLine.getSwitch(new Option("a"), false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());

        // When
        commandLine.addSwitch(new Option("a"), true);
        commandLine.addSwitch(new Option("a"), true);

        // Then
        // An exception should be thrown
    }

    @Test
    public void testHasOption() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());

        // When
        commandLine.addOption(new Option("a"));

        // Then
        assertTrue(commandLine.hasOption(new Option("a")));
    }

    @Test
    public void testGetOption() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));

        // When
        Option option = commandLine.getOption("a");

        // Then
        assertEquals(new Option("a"), option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionNull() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());

        // When
        Option option = commandLine.getOption(null);

        // Then
        // An exception should be thrown
    }

    @Test
    public void testGetValues() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addValue(new Option("a"), "value1");
        commandLine.addValue(new Option("a"), "value2");

        // When
        List values = commandLine.getValues(new Option("a"), Collections.emptyList());

        // Then
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addValue(new Option("a"), "value1");

        // When
        List values = commandLine.getUndefaultedValues(new Option("a"));

        // Then
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addSwitch(new Option("a"), true);

        // When
        Boolean switchValue = commandLine.getSwitch(new Option("a"), false);

        // Then
        assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchDefaultValue() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());

        // When
        Boolean switchValue = commandLine.getSwitch(new Option("a"), false);

        // Then
        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addProperty(new PropertyOption(), "property", "value");

        // When
        String propertyValue = commandLine.getProperty("property");

        // Then
        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());

        // When
        String propertyValue = commandLine.getProperty("property");

        // Then
        assertEquals("", propertyValue);
    }

    @Test
    public void testGetProperties() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addProperty(new PropertyOption(), "property1", "value1");
        commandLine.addProperty(new PropertyOption(), "property2", "value2");

        // When
        Set properties = commandLine.getProperties();

        // Then
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addValue(new Option("a"), "value1");
        commandLine.addValue(new Option("a"), "value2");

        // When
        String commandLineString = commandLine.toString();

        // Then
        assertEquals("value1 value2", commandLineString);
    }
}