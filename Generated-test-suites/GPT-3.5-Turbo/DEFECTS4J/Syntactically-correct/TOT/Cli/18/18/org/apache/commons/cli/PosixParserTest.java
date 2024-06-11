import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();

        // Adding options for testing
        options.addOption(Option.builder("f")
                .longOpt("file")
                .desc("File option")
                .hasArg()
                .build());

        options.addOption(Option.builder("d")
                .desc("Directory option")
                .build());
    }

    @Test
    public void testFlattenWithValidOptions() {
        String[] args = {"-f", "test.txt", "-d"};

        String[] flattenedArgs = parser.flatten(options, args, true);

        assertEquals(4, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("test.txt", flattenedArgs[1]);
        assertEquals("-d", flattenedArgs[2]);
        assertEquals("--", flattenedArgs[3]);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        String[] args = {"-z"};

        String[] flattenedArgs = parser.flatten(options, args, true);

        assertEquals(1, flattenedArgs.length);
        assertEquals("-z", flattenedArgs[0]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"-f", "test.txt", "arg1", "-d"};

        String[] flattenedArgs = parser.flatten(options, args, true);

        assertEquals(3, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("test.txt", flattenedArgs[1]);
        assertEquals("arg1", flattenedArgs[2]);
    }

    @Test
    public void testFlattenWithRemainingTokens() {
        String[] args = {"-f", "test.txt", "-d", "arg1"};

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(4, flattenedArgs.length);
        assertEquals("-f", flattenedArgs[0]);
        assertEquals("test.txt", flattenedArgs[1]);
        assertEquals("-d", flattenedArgs[2]);
        assertEquals("arg1", flattenedArgs[3]);
    }

    @Test
    public void testFlattenWithSpecialToken() {
        String[] args = {"--file=test.txt"};

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(2, flattenedArgs.length);
        assertEquals("--file", flattenedArgs[0]);
        assertEquals("test.txt", flattenedArgs[1]);
    }
}