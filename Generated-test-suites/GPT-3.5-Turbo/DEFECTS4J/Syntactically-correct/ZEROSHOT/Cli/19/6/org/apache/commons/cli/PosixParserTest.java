import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_stopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        options.addOption(Option.builder("t").longOpt("type").hasArgs().build());
        String[] arguments = {"-f", "file.txt", "-t", "1", "2", "--", "arg1", "arg2"};
        String[] expected = {"-f", "file.txt", "-t", "1", "2", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_continueProcessing() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        options.addOption(Option.builder("t").longOpt("type").hasArgs().build());
        String[] arguments = {"-f", "file.txt", "-t", "1", "2", "--", "arg1", "arg2"};
        String[] expected = {"-f", "file.txt", "-t", "1", "2", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        options.addOption(Option.builder("t").longOpt("type").hasArgs().build());
        String[] arguments = {"--file=file.txt", "--type=1", "2"};
        String[] expected = {"--file", "file.txt", "--type", "1", "2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleHyphen() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        options.addOption(Option.builder("t").longOpt("type").hasArgs().build());
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        options.addOption(Option.builder("t").longOpt("type").hasArgs().build());
        String[] arguments = {"-f", "file.txt"};
        String[] expected = {"-f", "file.txt"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_burstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt("file").hasArg().build());
        options.addOption(Option.builder("t").longOpt("type").hasArgs().build());
        String[] arguments = {"-ft1", "2"};
        String[] expected = {"-f", "t1", "2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}