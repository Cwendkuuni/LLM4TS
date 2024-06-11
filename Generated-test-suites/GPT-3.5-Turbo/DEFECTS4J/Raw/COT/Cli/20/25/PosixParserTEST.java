
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();

        Option helpOption = Option.builder("h")
                .longOpt("help")
                .desc("display help message")
                .build();

        Option versionOption = Option.builder("v")
                .longOpt("version")
                .desc("display version information")
                .build();

        options.addOption(helpOption);
        options.addOption(versionOption);

        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        String[] args = {"-h", "test"};
        String[] tokens = parser.flatten(options, args, false);
        
        assertNotNull(tokens);
        assertEquals(2, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("test", tokens[1]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"-h", "--", "test"};
        String[] tokens = parser.flatten(options, args, true);
        
        assertNotNull(tokens);
        assertEquals(3, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("--", tokens[1]);
        assertEquals("test", tokens[2]);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        String[] args = {"--help", "test"};
        String[] tokens = parser.flatten(options, args, false);
        
        assertNotNull(tokens);
        assertEquals(2, tokens.length);
        assertEquals("--help", tokens[0]);
        assertEquals("test", tokens[1]);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] args = {"-"};
        String[] tokens = parser.flatten(options, args, false);
        
        assertNotNull(tokens);
        assertEquals(1, tokens.length);
        assertEquals("-", tokens[0]);
    }

    @Test
    public void testFlattenInvalidShortOption() {
        String[] args = {"-x"};
        String[] tokens = parser.flatten(options, args, false);
        
        assertNotNull(tokens);
        assertEquals(1, tokens.length);
        assertEquals("-x", tokens[0]);
    }

    @Test
    public void testFlattenInvalidLongOption() {
        String[] args = {"--invalid"};
        String[] tokens = parser.flatten(options, args, false);
        
        assertNotNull(tokens);
        assertEquals(1, tokens.length);
        assertEquals("--invalid", tokens[0]);
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] args = {"-h", "-v"};
        String[] tokens = parser.flatten(options, args, false);
        
        assertNotNull(tokens);
        assertEquals(2, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("-v", tokens[1]);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        String[] args = {"-h", "test"};
        String[] tokens = parser.flatten(options, args, false);
        
        assertNotNull(tokens);
        assertEquals(2, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("test", tokens[1]);
    }

    @Test
    public void testFlattenOptionWithArguments() {
        String[] args = {"-h", "test1", "test2"};
        String[] tokens = parser.flatten(options, args, false);
        
        assertNotNull(tokens);
        assertEquals(3, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("test1", tokens[1]);
        assertEquals("test2", tokens[2]);
    }
}
