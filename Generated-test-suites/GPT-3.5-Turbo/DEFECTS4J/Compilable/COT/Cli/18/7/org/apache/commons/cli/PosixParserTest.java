package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "nonoption"};
        String[] expectedTokens = {"-a", "arg1", "nonoption"};

        String[] actualTokens = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_DontStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "nonoption"};
        String[] expectedTokens = {"-a", "arg1", "nonoption"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = {"--a=value"};
        String[] expectedTokens = {"--a", "value"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-"};
        String[] expectedTokens = {"-"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlatten_ShortOptionWithMultipleCharacters() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("ab", false, "Option ab");

        String[] arguments = {"-ab"};
        String[] expectedTokens = {"-a", "-b"};

        String[] actualTokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, actualTokens);
    }

}