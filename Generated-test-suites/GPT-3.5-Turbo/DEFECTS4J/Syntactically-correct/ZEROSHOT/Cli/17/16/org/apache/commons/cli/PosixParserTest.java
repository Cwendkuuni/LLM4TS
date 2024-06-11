import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption_FlattensNonOptions() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = parser.flatten(options, new String[]{"-a", "file1", "file2"}, true);

        assertEquals(args.length, 3);
        assertEquals(args[0], "-a");
        assertEquals(args[1], "file1");
        assertEquals(args[2], "file2");
    }
    
    @Test
    public void testFlatten_StopAtNonOption_DoesNotFlattenNonOptions() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = parser.flatten(options, new String[]{"-a", "file1", "file2"}, false);

        assertEquals(args.length, 1);
        assertEquals(args[0], "-a");
    }

    @Test
    public void testFlatten_SingleHyphen_AddsHyphenToTokens() {
        String[] args = parser.flatten(options, new String[]{"-"}, true);

        assertEquals(args.length, 1);
        assertEquals(args[0], "-");
    }

    @Test
    public void testFlatten_OptionToken_AddsOptionToTokens() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = parser.flatten(options, new String[]{"-a"}, true);

        assertEquals(args.length, 1);
        assertEquals(args[0], "-a");
    }

    @Test
    public void testFlatten_OptionToken_NonOptionExists_StopAtNonOption_IgnoresOption() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = parser.flatten(options, new String[]{"-a", "file"}, true);

        assertEquals(args.length, 2);
        assertEquals(args[0], "-a");
        assertEquals(args[1], "file");
    }

    @Test
    public void testFlatten_OptionToken_NonOptionExists_StopAtNonOption_DoesNotIgnoreOption() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = parser.flatten(options, new String[]{"-a", "file"}, false);

        assertEquals(args.length, 1);
        assertEquals(args[0], "-a");
    }

    @Test
    public void testFlatten_BurstToken_BurstsToken() {
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        String[] args = parser.flatten(options, new String[]{"-abc"}, true);

        assertEquals(args.length, 3);
        assertEquals(args[0], "-a");
        assertEquals(args[1], "-b");
        assertEquals(args[2], "-c");
    }

    @Test
    public void testFlatten_BurstToken_NonOptionExists_StopAtNonOption_IgnoresOption() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = parser.flatten(options, new String[]{"-ab", "file"}, true);

        assertEquals(args.length, 3);
        assertEquals(args[0], "-a");
        assertEquals(args[1], "-b");
        assertEquals(args[2], "file");
    }

    @Test
    public void testFlatten_BurstToken_NonOptionExists_StopAtNonOption_DoesNotIgnoreOption() {
        options.addOption("a", "optionA", false, "Option A");

        String[] args = parser.flatten(options, new String[]{"-ab", "file"}, false);

        assertEquals(args.length, 2);
        assertEquals(args[0], "-ab");
        assertEquals(args[1], "file");
    }
}