import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        // Arrange
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("add").desc("Add option").build());
        options.addOption(Option.builder("d").longOpt("delete").desc("Delete option").build());
        String[] arguments = {"--add", "-d", "file.txt", "--", "-f"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        String[] expected = {"--add", "-d", "file.txt", "--", "-f"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_FlagTrue() {
        // Arrange
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("add").desc("Add option").build());
        String[] arguments = {"--add", "file.txt", "-d"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        String[] expected = {"--add", "file.txt"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_FlagFalse() {
        // Arrange
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("add").desc("Add option").build());
        String[] arguments = {"--add", "file.txt", "-d"};

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        String[] expected = {"--add", "file.txt", "-d"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_AdditionalOptionsExists() {
        // Arrange
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("add").desc("Add option").build());
        options.addOption(Option.builder("b").longOpt("backup").desc("Backup option").build());
        options.addOption(Option.builder("c").longOpt("compress").desc("Compress option").build());
        String[] arguments = {"--add", "-c", "file.txt", "-d"};

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        String[] expected = {"--add", "-c", "file.txt", "-d"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_MultipleNonOptions() {
        // Arrange
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("add").desc("Add option").build());
        String[] arguments = {"--add", "file.txt", "-d", "nonOption"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        String[] expected = {"--add", "file.txt"};
        Assert.assertArrayEquals(expected, result);
    }
}