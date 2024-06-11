import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option1 = new Option("f", false, "This is option f");
        Option option2 = new Option("v", "verbose", true, "This is option v");
        Option option3 = new Option("r", "recursive", false, "This is option r");
        Option option4 = new Option("h", "help", false, "This is option h");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        PosixParser parser = new PosixParser();
        String[] args1 = {"-f", "--verbose", "5", "-r", "file.txt", "-h"};
        String[] args2 = {"-f", "--verbose=5", "-r", "file.txt", "--help"};
        String[] args3 = {"-f", "-v", "5", "-r", "file.txt", "-h"};

        String[] result1 = parser.flatten(options, args1, false);
        String[] result2 = parser.flatten(options, args2, false);
        String[] result3 = parser.flatten(options, args3, false);

        String[] expected1 = {"-f", "--verbose", "5", "-r", "file.txt", "-h"};
        String[] expected2 = {"-f", "--verbose", "5", "-r", "file.txt", "-h"};
        String[] expected3 = {"-f", "--verbose", "5", "-r", "file.txt", "-h"};

        assertArrayEquals(expected1, result1);
        assertArrayEquals(expected2, result2);
        assertArrayEquals(expected3, result3);
    }
}