import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyOptions_ReturnsEmptyArray() {
        // Arrange
        Options options = new Options();
        String[] arguments = {};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_OptionsWithNoArguments_OnlyAddsOptionsToResult() {
        // Arrange
        Options options = new Options();
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", false, "Option B");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-a", "-b"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlatten_OptionWithArgument_AddsOptionAndArgumentToResult() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", true, "Option A");
        options.addOption(option);

        String[] arguments = {"-a", "value"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    @Test
    public void testFlatten_OptionWithMultipleArguments_AddsOptionAndArgumentsToResult() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", true, "Option A");
        option.setArgs(2);
        options.addOption(option);

        String[] arguments = {"-a", "value1", "value2"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "value1", "value2"}, result);
    }

    @Test
    public void testFlatten_OptionWithMissingArgument_ThrowsMissingArgumentException() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", true, "Option A");
        options.addOption(option);

        String[] arguments = {"-a"};

        // Act and Assert
        try {
            new PosixParser().flatten(options, arguments, true);
            Assert.fail("MissingArgumentException not thrown");
        } catch (MissingArgumentException ex) {
            // Assert
            Assert.assertEquals("Missing argument for option: -a", ex.getMessage());
        }
    }

    @Test
    public void testFlatten_StopAtNonOption_OnlyProcessesOptionsBeforeNonOption() {
        // Arrange
        Options options = new Options();
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", false, "Option B");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-a", "-b", "nonoption"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-b", "nonoption"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionDisabled_ProcessesAllOptionsAndNonOptions() {
        // Arrange
        Options options = new Options();
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", false, "Option B");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-a", "-b", "nonoption"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-b", "nonoption"}, result);
    }

    @Test
    public void testFlatten_HyphenArgument_AddsHyphenToResult() {
        // Arrange
        Options options = new Options();

        String[] arguments = {"-"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_TokenStartingWithHyphen_ProcessesTokenAsNonOption() {
        // Arrange
        Options options = new Options();

        String[] arguments = {"-token"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-token"}, result);
    }

    @Test
    public void testFlatten_HyphenTokenAfterOption_ProcessesHyphenTokenAsNonOption() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", false, "Option A");
        options.addOption(option);

        String[] arguments = {"-a", "-"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-"}, result);
    }

    @Test
    public void testFlatten_TokenStartingWithDoubleHyphen_AddsTokenToResult() {
        // Arrange
        Options options = new Options();

        String[] arguments = {"--token"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"--token"}, result);
    }

    @Test
    public void testFlatten_TokenWithEqualSign_AddsTokenAndValueToResult() {
        // Arrange
        Options options = new Options();

        String[] arguments = {"--token=value"};

        // Act
        String[] result = new PosixParser().flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"--token", "value"}, result);
    }
}