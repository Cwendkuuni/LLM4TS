import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_NoOptions_ReturnsEmptyArray() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{};

        // Act
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_SingleOption_ReturnsOption() {
        // Arrange
        Options options = new Options();
        options.addOption("a");
        String[] arguments = new String[]{"-a"};

        // Act
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsOnlyOptions() {
        // Arrange
        Options options = new Options();
        options.addOption("a");
        options.addOption("b");
        String[] arguments = new String[]{"-a", "value", "-b"};

        // Act
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlatten_FlattenLongOptionWithValue_ReturnsOptionAndValue() {
        // Arrange
        Options options = new Options();
        options.addOption("l", true, "long option");
        String[] arguments = new String[]{"--long=value"};

        // Act
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"--long", "value"}, result);
    }

    @Test
    public void testFlatten_FlattenLongOptionWithNoValue_ReturnsOptionOnly() {
        // Arrange
        Options options = new Options();
        options.addOption("l", false, "long option");
        String[] arguments = new String[]{"--long"};

        // Act
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"--long"}, result);
    }
}