import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "-o2", "--option3=value3", "-", "arg1", "--", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(9, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-o2", result[2]);
        assertEquals("--option3", result[3]);
        assertEquals("value3", result[4]);
        assertEquals("-", result[5]);
        assertEquals("arg1", result[6]);
        assertEquals("--", result[7]);
        assertEquals("arg2", result[8]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"--option1", "value1", "-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-a", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "-o2", "--option3=value3", "-", "arg1", "--", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(8, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-o2", result[2]);
        assertEquals("--option3", result[3]);
        assertEquals("value3", result[4]);
        assertEquals("-", result[5]);
        assertEquals("arg1", result[6]);
        assertEquals("arg2", result[7]);
    }

    @Test
    public void testFlattenOptionsWithArguments() {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");

        String[] arguments = {"--option1=value1", "-option2", "value2", "--option3", "value3"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(6, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-option2", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("--option3", result[4]);
        assertEquals("value3", result[5]);
    }

    @Test
    public void testFlattenOptionsWithoutArguments() {
        Options options = new Options();
        options.addOption("option1", false, "Option 1");
        options.addOption("option2", false, "Option 2");
        options.addOption("option3", false, "Option 3");

        String[] arguments = {"--option1", "-option2", "--option3"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("-option2", result[1]);
        assertEquals("--option3", result[2]);
    }
}