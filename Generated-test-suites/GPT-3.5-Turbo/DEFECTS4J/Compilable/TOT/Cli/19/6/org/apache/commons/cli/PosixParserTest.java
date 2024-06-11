package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Test
    public void testFlatten_StopAtNonOption_True() {
        // Arrange
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));
        String[] arguments = new String[]{"-a", "-b", "value1", "-c", "value2"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-b", "value1", "-c", "value2"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        // Arrange
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));
        String[] arguments = new String[]{"-a", "-b", "value1", "-c", "value2"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-b", "value1", "-c", "value2"}, result);
    }

    @Test
    public void testFlatten_SpecialTokens() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"--option1", "--option2=value"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"--option1", "--", "option2", "value"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"-"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_ValidOptionId() {
        // Arrange
        Options options = new Options();
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        String[] arguments = new String[]{"-a", "-b", "value"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-b", "value"}, result);
    }

    @Test
    public void testFlatten_NotValidOptionId() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"-z", "value1", "-y", "value2"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-z", "value1", "-y", "value2"}, result);
    }

    @Test
    public void testFlatten_NotValidOptionId_StopAtNonOption() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"-z", "value1", "-y", "value2"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-z", "value1", "-y", "value2"}, result);
    }

    @Test
    public void testFlatten_ValidOptionIdWithRemainingCharacters() {
        // Arrange
        Options options = new Options();
        options.addOption(new Option("a", true, "Option a"));
        String[] arguments = new String[]{"-a", "value1"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "value1"}, result);
    }

    @Test
    public void testFlatten_NotValidOptionIdWithRemainingCharacters() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"-zvalue1"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-zvalue1"}, result);
    }

    @Test
    public void testFlatten_NotValidOptionIdWithRemainingCharacters_StopAtNonOption() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"-zvalue1"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"-zvalue1"}, result);
    }

    @Test
    public void testFlatten_NotOption() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"value1", "value2"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testFlatten_NotOption_StopAtNonOption() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"value1", "value2"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testFlatten_Gobble() {
        // Arrange
        Options options = new Options();
        String[] arguments = new String[]{"-a", "-b", "value", "remaining"};
        parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        Assert.assertArrayEquals(new String[]{"-a", "-b", "value", "remaining"}, result);
    }
}

