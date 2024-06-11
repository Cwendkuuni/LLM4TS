import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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
    public void testFlattenValidOptionToken() {
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").hasArg(true).build());

        String[] arguments = {"-f", "-o", "file.txt"};
        String[] expectedTokens = {"-f", "-o", "file.txt"};

        String[] tokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenValidLongOptionToken() {
        options.addOption(Option.builder("f").longOpt("file").build());
        options.addOption(Option.builder("o").longOpt("output").hasArg(true).build());

        String[] arguments = {"--file", "--output=file.txt"};
        String[] expectedTokens = {"--file", "--output", "file.txt"};

        String[] tokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenHyphenToken() {
        String[] arguments = {"-"};
        String[] expectedTokens = {"-"};

        String[] tokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenUnknownOptionToken() {
        String[] arguments = {"-u"};
        String[] expectedTokens = {"-u"};

        String[] tokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenUnknownOptionTokenWithStopAtNonOption() {
        String[] arguments = {"-u", "file.txt"};
        String[] expectedTokens = {"-u", "file.txt"};

        String[] tokens = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenBurstToken() {
        options.addOption(Option.builder("d").build());
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").hasArg(true).build());

        String[] arguments = {"-dfofile.txt"};
        String[] expectedTokens = {"-d", "-f", "-o", "file.txt"};

        String[] tokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOption() {
        options.addOption(Option.builder("d").build());
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").hasArg(true).build());

        String[] arguments = {"-dfofile.txt", "file2.txt"};
        String[] expectedTokens = {"-d", "-f", "-o", "file.txt", "file2.txt"};

        String[] tokens = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenRemainingTokens() {
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").hasArg(true).build());

        String[] arguments = {"-f", "-o", "file.txt", "file2.txt"};
        String[] expectedTokens = {"-f", "-o", "file.txt", "file2.txt"};

        String[] tokens = parser.flatten(options, arguments, false);

        assertArrayEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenRemainingTokensWithStopAtNonOption() {
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").hasArg(true).build());

        String[] arguments = {"-f", "-o", "file.txt", "file2.txt"};
        String[] expectedTokens = {"-f", "-o", "file.txt", "file2.txt"};

        String[] tokens = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedTokens, tokens);
    }

}