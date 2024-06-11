import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_OptionExistsForFirstCharacter() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Define a system property");

        String[] arguments = {"-Dkey=value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result[0], "-D");
        assertEquals(result[1], "key=value");
    }

    @Test
    public void testFlatten_OptionDoesNotExistForWholeArgument() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Define a system property");

        String[] arguments = {"-Xmx512M"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result[0], "-Xmx512M");
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-Dkey=value", "file1", "--", "file2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(result[0], "-Dkey=value");
        assertEquals(result[1], "file1");
        assertEquals(result[2], "--");
        assertEquals(result[3], "file2");
    }

    @Test
    public void testFlatten_OnlyHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-", "-", "test"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result[0], "-");
        assertEquals(result[1], "-");
        assertEquals(result[2], "test");
    }

    @Test
    public void testFlatten_DoubleDashSeparator() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-Dkey=value", "--", "-Xmx512M"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result[0], "-Dkey=value");
        assertEquals(result[1], "--");
        assertEquals(result[2], "-Xmx512M");
    }

}