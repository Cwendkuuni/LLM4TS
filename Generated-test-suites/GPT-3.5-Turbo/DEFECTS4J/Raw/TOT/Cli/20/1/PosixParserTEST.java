import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption_True() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        String[] arguments = {"--", "-a", "file1", "file2"};
        String[] actual = parser.flatten(options, arguments, true);
        String[] expected = {"--", "-a", "file1", "file2"};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        String[] arguments = {"--", "-a", "file1", "file2"};
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--", "-a", "file1", "file2"};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testProcessOptionToken_OptionExists() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");
        Option option = new Option("a", "all", false, "Display all files");

        parser.processOptionToken("-a", true);

        assertEquals(option, parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionToken_OptionDoesNotExist_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.processOptionToken("-x", true);

        assertNull(parser.getCurrentOption());
        assertTrue(parser.getEatTheRest());
    }

    @Test
    public void testProcessOptionToken_OptionDoesNotExist_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.processOptionToken("-x", false);

        assertNull(parser.getCurrentOption());
        assertFalse(parser.getEatTheRest());
    }

    @Test
    public void testProcess_ArgumentIsOption_WithArgumentOption() {
        Options options = new Options();
        options.addOption("a", "all", true, "Display all files");

        parser.setCurrentOption(new Option("a", "all", true, "Display all files"));
        parser.process("file1");

        assertNull(parser.getCurrentOption());
        assertFalse(parser.getEatTheRest());
        assertArrayEquals(new String[]{"file1"}, parser.getTokens().toArray());
    }

    @Test
    public void testProcess_ArgumentIsOption_WithArgsOption() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.setCurrentOption(new Option("a", "all", false, "Display all files"));
        parser.process("file1");

        assertNull(parser.getCurrentOption());
        assertFalse(parser.getEatTheRest());
        assertArrayEquals(new String[]{"file1"}, parser.getTokens().toArray());
    }

    @Test
    public void testProcess_ArgumentIsOption_WithoutOption() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.process("file1");

        assertNull(parser.getCurrentOption());
        assertTrue(parser.getEatTheRest());
        assertArrayEquals(new String[]{"--", "file1"}, parser.getTokens().toArray());
    }

    @Test
    public void testProcess_ArgumentIsNotOption_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.process("file1");

        assertNull(parser.getCurrentOption());
        assertTrue(parser.getEatTheRest());
        assertArrayEquals(new String[]{"--", "file1"}, parser.getTokens().toArray());
    }

    @Test
    public void testProcess_ArgumentIsNotOption_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.process("file1");

        assertNull(parser.getCurrentOption());
        assertTrue(parser.getEatTheRest());
        assertArrayEquals(new String[]{"--", "file1"}, parser.getTokens().toArray());
    }

    @Test
    public void testBurstToken_ValidOptionExists() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.burstToken("-abc", true);

        assertNull(parser.getCurrentOption());
        assertFalse(parser.getEatTheRest());
        assertArrayEquals(new String[]{"-a", "-b", "-c"}, parser.getTokens().toArray());
    }

    @Test
    public void testBurstToken_ValidOptionDoesNotExist_StopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.burstToken("-xyz", true);

        assertNull(parser.getCurrentOption());
        assertTrue(parser.getEatTheRest());
        assertArrayEquals(new String[]{"--", "-xyz"}, parser.getTokens().toArray());
    }

    @Test
    public void testBurstToken_ValidOptionDoesNotExist_StopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", "all", false, "Display all files");

        parser.burstToken("-xyz", false);

        assertNull(parser.getCurrentOption());
        assertFalse(parser.getEatTheRest());
        assertArrayEquals(new String[]{"-xyz"}, parser.getTokens().toArray());
    }

}