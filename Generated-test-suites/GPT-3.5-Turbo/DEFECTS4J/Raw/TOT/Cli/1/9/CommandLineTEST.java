import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new CommandLine();
    }

    @After
    public void teardown() {
        commandLine = null;
    }

    @Test
    public void testHasOption_String_WhenOptionSet_ShouldReturnTrue() {
        // Arrange
        String opt = "a";
        commandLine.addOption(new Option(opt, "option"));

        // Act
        boolean result = commandLine.hasOption(opt);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasOption_String_WhenOptionNotSet_ShouldReturnFalse() {
        // Arrange
        String opt = "a";

        // Act
        boolean result = commandLine.hasOption(opt);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasOption_Char_WhenOptionSet_ShouldReturnTrue() {
        // Arrange
        char opt = 'a';
        commandLine.addOption(new Option(opt, "option"));

        // Act
        boolean result = commandLine.hasOption(opt);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasOption_Char_WhenOptionNotSet_ShouldReturnFalse() {
        // Arrange
        char opt = 'a';

        // Act
        boolean result = commandLine.hasOption(opt);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetOptionObject_String_WhenOptionSet_ShouldReturnObject() {
        // Arrange
        String opt = "a";
        commandLine.addOption(new Option(opt, "option"));
        commandLine.addArg("value");

        // Act
        Object result = commandLine.getOptionObject(opt);

        // Assert
        assertNotNull(result);
        assertEquals("value", result);
    }

    @Test
    public void testGetOptionObject_String_WhenOptionNotSet_ShouldReturnNull() {
        // Arrange
        String opt = "a";

        // Act
        Object result = commandLine.getOptionObject(opt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetOptionObject_Char_WhenOptionSet_ShouldReturnObject() {
        // Arrange
        char opt = 'a';
        commandLine.addOption(new Option(opt, "option"));
        commandLine.addArg("value");

        // Act
        Object result = commandLine.getOptionObject(opt);

        // Assert
        assertNotNull(result);
        assertEquals("value", result);
    }

    @Test
    public void testGetOptionObject_Char_WhenOptionNotSet_ShouldReturnNull() {
        // Arrange
        char opt = 'a';

        // Act
        Object result = commandLine.getOptionObject(opt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetOptionValue_String_WhenOptionSet_ShouldReturnValue() {
        // Arrange
        String opt = "a";
        commandLine.addOption(new Option(opt, "option"));
        commandLine.addArg("value");

        // Act
        String result = commandLine.getOptionValue(opt);

        // Assert
        assertNotNull(result);
        assertEquals("value", result);
    }

    @Test
    public void testGetOptionValue_String_WhenOptionNotSet_ShouldReturnNull() {
        // Arrange
        String opt = "a";

        // Act
        String result = commandLine.getOptionValue(opt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetOptionValue_Char_WhenOptionSet_ShouldReturnValue() {
        // Arrange
        char opt = 'a';
        commandLine.addOption(new Option(opt, "option"));
        commandLine.addArg("value");

        // Act
        String result = commandLine.getOptionValue(opt);

        // Assert
        assertNotNull(result);
        assertEquals("value", result);
    }

    @Test
    public void testGetOptionValue_Char_WhenOptionNotSet_ShouldReturnNull() {
        // Arrange
        char opt = 'a';

        // Act
        String result = commandLine.getOptionValue(opt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetOptionValues_String_WhenOptionSet_ShouldReturnValues() {
        // Arrange
        String opt = "a";
        commandLine.addOption(new Option(opt, "option"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");

        // Act
        String[] result = commandLine.getOptionValues(opt);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
    }

    @Test
    public void testGetOptionValues_String_WhenOptionNotSet_ShouldReturnNull() {
        // Arrange
        String opt = "a";

        // Act
        String[] result = commandLine.getOptionValues(opt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetOptionValues_Char_WhenOptionSet_ShouldReturnValues() {
        // Arrange
        char opt = 'a';
        commandLine.addOption(new Option(opt, "option"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");

        // Act
        String[] result = commandLine.getOptionValues(opt);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
    }

    @Test
    public void testGetOptionValues_Char_WhenOptionNotSet_ShouldReturnNull() {
        // Arrange
        char opt = 'a';

        // Act
        String[] result = commandLine.getOptionValues(opt);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetOptionValue_String_String_WhenOptionSet_ShouldReturnValue() {
        // Arrange
        String opt = "a";
        commandLine.addOption(new Option(opt, "option"));
        commandLine.addArg("value");

        // Act
        String result = commandLine.getOptionValue(opt, "default");

        // Assert
        assertNotNull(result);
        assertEquals("value", result);
    }

    @Test
    public void testGetOptionValue_String_String_WhenOptionNotSet_ShouldReturnDefault() {
        // Arrange
        String opt = "a";

        // Act
        String result = commandLine.getOptionValue(opt, "default");

        // Assert
        assertNotNull(result);
        assertEquals("default", result);
    }

    @Test
    public void testGetOptionValue_Char_String_WhenOptionSet_ShouldReturnValue() {
        // Arrange
        char opt = 'a';
        commandLine.addOption(new Option(opt, "option"));
        commandLine.addArg("value");

        // Act
        String result = commandLine.getOptionValue(opt, "default");

        // Assert
        assertNotNull(result);
        assertEquals("value", result);
    }

    @Test
    public void testGetOptionValue_Char_String_WhenOptionNotSet_ShouldReturnDefault() {
        // Arrange
        char opt = 'a';

        // Act
        String result = commandLine.getOptionValue(opt, "default");

        // Assert
        assertNotNull(result);
        assertEquals("default", result);
    }

    @Test
    public void testGetArgs_WhenArgumentsSet_ShouldReturnArgsArray() {
        // Arrange
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        // Act
        String[] result = commandLine.getArgs();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testGetArgs_WhenNoArgumentsSet_ShouldReturnEmptyArray() {
        // Act
        String[] result = commandLine.getArgs();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetArgList_WhenArgumentsSet_ShouldReturnArgList() {
        // Arrange
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        // Act
        List result = commandLine.getArgList();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0));
        assertEquals("arg2", result.get(1));
    }

    @Test
    public void testGetArgList_WhenNoArgumentsSet_ShouldReturnEmptyList() {
        // Act
        List result = commandLine.getArgList();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}