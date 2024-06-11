import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        String[] arguments = new String[]{"-a", "--b=value"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(3, result.length);
        assertTrue(result[0].equals("-a"));
        assertTrue(result[1].equals("--"));
        assertTrue(result[2].equals("--b=value"));
    }

    @Test
    public void testFlatten_stopAtNonOptionFalse() {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        String[] arguments = new String[]{"-a", "--b=value", "non-option"};

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(4, result.length);
        assertTrue(result[0].equals("-a"));
        assertTrue(result[1].equals("--"));
        assertTrue(result[2].equals("--b=value"));
        assertTrue(result[3].equals("non-option"));
    }

    @Test
    public void testFlatten_optionNoArgument() {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = new String[]{"-a"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(1, result.length);
        assertTrue(result[0].equals("-a"));
    }

    @Test
    public void testFlatten_optionWithArgument() {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("b", true, "Option b");
        String[] arguments = new String[]{"--b=value"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(2, result.length);
        assertTrue(result[0].equals("--"));
        assertTrue(result[1].equals("--b=value"));
    }

    @Test
    public void testFlatten_optionNotFound_stopAtNonOptionFalse() {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = new String[]{"-a", "-b"};

        // Act
      String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(2, result.length);
        assertTrue(result[0].equals("-a"));
        assertTrue(result[1].equals("-b"));
    }

    @Test
    public void testFlatten_optionNotFound_stopAtNonOptionTrue() {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = new String[]{"-a", "-b"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(2, result.length);
        assertTrue(result[0].equals("-a"));
        assertTrue(result[1].equals("-b"));
    }

    @Test
    public void testFlatten_multipleArguments() {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        String[] arguments = new String[]{"-a", "value1", "-b", "value2"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(4, result.length);
        assertTrue(result[0].equals("-a"));
        assertTrue(result[1].equals("value1"));
        assertTrue(result[2].equals("-b"));
        assertTrue(result[3].equals("value2"));
    }
}