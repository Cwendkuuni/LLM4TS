import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_OptionsWithNoArgs_ReturnsFlattenedArgs() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        String[] arguments = {"-a", "-b"};
        String[] expected = {"-a", "-b"};

        // Act
        String[] actual = posixParser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionsWithArgs_ReturnsFlattenedArgs() {
        // Arrange
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        String[] arguments = {"-a", "value1", "-b", "value2"};
        String[] expected = {"-a", "value1", "-b", "value2"};

        // Act
        String[] actual = posixParser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SpecialToken_ReturnsFlattenedArgs() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        String[] arguments = {"--special"};
        String[] expected = {"--special"};

        // Act
        String[] actual = posixParser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleHyphen_ReturnsFlattenedArgs() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        String[] arguments = {"-"};
        String[] expected = {"-"};

        // Act
        String[] actual = posixParser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOptionReturnsRemainingArgs() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        String[] arguments = {"-a", "-b", "-c", "value1", "value2"};
        String[] expected = {"-a", "-b", "--", "-c", "value1", "value2"};

        // Act
        String[] actual = posixParser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOptionReturnsEmptyArray() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        String[] arguments = {"-a", "-b", "-c", "value1", "value2"};
        String[] expected = {};

        // Act
        String[] actual = posixParser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(expected, actual);
    }

}