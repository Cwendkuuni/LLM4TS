import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;
    private String[] args;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();

        // Add options
        options.addOption(Option.builder("a").longOpt("all").required().desc("Select all").build());
        options.addOption(Option.builder("f").longOpt("file").hasArg().desc("Specify file").build());
        options.addOption(Option.builder("l").longOpt("list").desc("List all files").build());
        options.addOption(Option.builder("h").longOpt("help").desc("Show help").build());

        args = new String[] {
            "-a",
            "-f", "test.txt",
            "-l"
        };
    }

    @Test
    public void testFlatten_AllOptionsValid_ReturnProcessedTokens() {
        String[] processedTokens = parser.flatten(options, args, false);

        assertNotNull(processedTokens);
        assertEquals(5, processedTokens.length);
        assertEquals("-a", processedTokens[0]);
        assertEquals("-f", processedTokens[1]);
        assertEquals("test.txt", processedTokens[2]);
        assertEquals("-l", processedTokens[3]);
        assertEquals("--", processedTokens[4]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnProcessedTokens() {
        String[] processedTokens = parser.flatten(options, args, true);

        assertNotNull(processedTokens);
        assertEquals(3, processedTokens.length);
        assertEquals("-a", processedTokens[0]);
        assertEquals("-f", processedTokens[1]);
        assertEquals("test.txt", processedTokens[2]);
    }

    @Test
    public void testFlatten_NoOptions_ReturnProcessedTokens() {
        String[] args = new String[] {
            "test.txt",
            "data.txt"
        };

        String[] processedTokens = parser.flatten(options, args, false);

        assertNotNull(processedTokens);
        assertEquals(2, processedTokens.length);
        assertEquals("test.txt", processedTokens[0]);
        assertEquals("data.txt", processedTokens[1]);
    }

    @Test
    public void testFlatten_InvalidOption_ReturnProcessedTokens() {
        String[] args = new String[] {
            "-a",
            "-x",
            "--",
            "test.txt"
        };

        String[] processedTokens = parser.flatten(options, args, false);

        assertNotNull(processedTokens);
        assertEquals(4, processedTokens.length);
        assertEquals("-a", processedTokens[0]);
        assertEquals("-x", processedTokens[1]);
        assertEquals("--", processedTokens[2]);
        assertEquals("test.txt", processedTokens[3]);
    }

    @Test
    public void testFlatten_OptionWithNoArgValue_ReturnProcessedTokens() {
        String[] args = new String[] {
            "-a",
            "-l",
            "--",
            "test.txt"
        };

        String[] processedTokens = parser.flatten(options, args, false);

        assertNotNull(processedTokens);
        assertEquals(5, processedTokens.length);
        assertEquals("-a", processedTokens[0]);
        assertEquals("-l", processedTokens[1]);
        assertEquals("--", processedTokens[2]);
        assertEquals("test.txt", processedTokens[3]);
    }

    @Test
    public void testFlatten_OptionWithArgValue_ReturnProcessedTokens() {
        String[] args = new String[] {
            "-a",
            "-f",
            "test.txt",
            "-l",
            "--",
            "data.txt"
        };

        String[] processedTokens = parser.flatten(options, args, false);

        assertNotNull(processedTokens);
        assertEquals(7, processedTokens.length);
        assertEquals("-a", processedTokens[0]);
        assertEquals("-f", processedTokens[1]);
        assertEquals("test.txt", processedTokens[2]);
        assertEquals("-l", processedTokens[3]);
        assertEquals("--", processedTokens[4]);
        assertEquals("data.txt", processedTokens[5]);
    }

    @Test
    public void testFlatten_ArgumentWithHyphen_ReturnProcessedTokens() {
        String[] args = new String[] {
            "-a",
            "-f",
            "test.txt",
            "-l",
            "--",
            "-file"
        };

        String[] processedTokens = parser.flatten(options, args, false);

        assertNotNull(processedTokens);
        assertEquals(7, processedTokens.length);
        assertEquals("-a", processedTokens[0]);
        assertEquals("-f", processedTokens[1]);
        assertEquals("test.txt", processedTokens[2]);
        assertEquals("-l", processedTokens[3]);
        assertEquals("--", processedTokens[4]);
        assertEquals("-file", processedTokens[5]);
    }

    @Test
    public void testFlatten_ArgumentWithEqualsSign_ReturnProcessedTokens() {
        String[] args = new String[] {
            "-f=test.txt"
        };

        String[] processedTokens = parser.flatten(options, args, false);

        assertNotNull(processedTokens);
        assertEquals(2, processedTokens.length);
        assertEquals("-f", processedTokens[0]);
        assertEquals("test.txt", processedTokens[1]);
    }

    @Test
    public void testFlatten_NoArguments_ReturnProcessedTokens() {
        String[] args = new String[] {};

        String[] processedTokens = parser.flatten(options, args, false);

        assertNotNull(processedTokens);
        assertEquals(0, processedTokens.length);
    }

    @Test
    public void testFlatten_NullArguments_ReturnProcessedTokens() {
        String[] args = null;

        String[] processedTokens = parser.flatten(options, args, false);

        assertNull(processedTokens);
    }
}