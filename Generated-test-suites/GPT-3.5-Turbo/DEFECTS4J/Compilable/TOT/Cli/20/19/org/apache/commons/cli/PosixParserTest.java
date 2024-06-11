package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_HasLongOptionWithNoArgument_ValueAddedToTokens() {
        options.addOption(OptionBuilder.withLongOpt("verbose").hasArg(false).create());
        String[] arguments = {"--verbose"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("--verbose", result[0]);
    }

    @Test
    public void testFlatten_HasLongOptionWithArgument_ValueAndArgumentAddedToTokens() {
        options.addOption(OptionBuilder.withLongOpt("host").hasArg().create());
        String[] arguments = {"--host", "localhost"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("--host", result[0]);
        assertEquals("localhost", result[1]);
    }

    @Test
    public void testFlatten_HasLongOptionWithArgumentEqualsSign_ValueAndArgumentAddedToTokens() {
        options.addOption(OptionBuilder.withLongOpt("host").hasArg().create());
        String[] arguments = {"--host=localhost"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("--host", result[0]);
        assertEquals("localhost", result[1]);
    }
  
    @Test
    public void testFlatten_HasSingleHyphen_ValueAddedToTokens() {
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }
  
    @Test
    public void testFlatten_HasShortOptionWithNoArgument_ValueAddedToTokens() {
        options.addOption(OptionBuilder.withArgName("v").hasArg(false).create());
        String[] arguments = {"-v"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-v", result[0]);
    }

    @Test
    public void testFlatten_HasShortOptionWithArgument_ValueAndArgumentAddedToTokens() {
        options.addOption(OptionBuilder.withArgName("h").hasArg().create());
        String[] arguments = {"-h", "localhost"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-h", result[0]);
        assertEquals("localhost", result[1]);
    }

    @Test
    public void testFlatten_HasShortOptionWithArgumentEqualsSign_ValueAndArgumentAddedToTokens() {
        options.addOption(OptionBuilder.withArgName("h").hasArg().create());
        String[] arguments = {"-h=localhost"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-h", result[0]);
        assertEquals("localhost", result[1]);
    }

    @Test
    public void testFlatten_HasOptionWithMultipleArguments_ArgumentsAddedToTokens() {
        options.addOption(OptionBuilder.withArgName("f").hasArgs().create());
        String[] arguments = {"-f", "file1", "file2"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file1", result[1]);
        assertEquals("file2", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFlagSet_FirstNonOptionIsProcessedAndRemainingTokensAddedToTokens() {
        options.addOption(OptionBuilder.withArgName("f").hasArg().create());
        String[] arguments = {"-f", "file1", "-n", "file2"};

        String[] result = parser.flatten(options, arguments, true);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file1", result[1]);
        assertEquals("-n", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFlagNotSet_FirstNonOptionIsIgnoredAndAllTokensAddedToTokens() {
        options.addOption(OptionBuilder.withArgName("f").hasArg().create());
        String[] arguments = {"-f", "file1", "-n", "file2"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file1", result[1]);
        assertEquals("-n", result[2]);
        assertEquals("file2", result[3]);
    }

    @Test
    public void testFlatten_HasNoOption_TokenAddedToTokens() {
        String[] arguments = {"token"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("token", result[0]);
    }

    @Test
    public void testFlatten_HasNoOption_StopAtNonOptionFlagSet_FirstNonOptionIsProcessedAndRemainingTokensAddedToTokens() {
        String[] arguments = {"token1", "-n", "token2"};

        String[] result = parser.flatten(options, arguments, true);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("token1", result[0]);
        assertEquals("-n", result[1]);
        assertEquals("token2", result[2]);
    }

    @Test
    public void testFlatten_HasNoOption_StopAtNonOptionFlagNotSet_AllTokensAddedToTokens() {
        String[] arguments = {"token1", "-n", "token2"};

        String[] result = parser.flatten(options, arguments, false);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("token1", result[0]);
        assertEquals("-n", result[1]);
        assertEquals("token2", result[2]);
    }

}