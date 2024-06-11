import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        GnuParser parser = new GnuParser();

        String[] arguments1 = {"-a", "arg1", "arg2"};
        String[] expected1 = {"-a", "arg1", "arg2"};
        assertArrayEquals(expected1, parser.flatten(options, arguments1, false));

        String[] arguments2 = {"-b", "value", "arg1", "arg2"};
        String[] expected2 = {"-b", "value", "arg1", "arg2"};
        assertArrayEquals(expected2, parser.flatten(options, arguments2, false));

        String[] arguments3 = {"-c", "arg1", "arg2"};
        String[] expected3 = {"-c", "arg1", "arg2"};
        assertArrayEquals(expected3, parser.flatten(options, arguments3, false));

        String[] arguments4 = {"--", "-a", "arg1", "arg2"};
        String[] expected4 = {"--", "-a", "arg1", "arg2"};
        assertArrayEquals(expected4, parser.flatten(options, arguments4, false));

        String[] arguments5 = {"-", "arg1", "arg2"};
        String[] expected5 = {"-", "arg1", "arg2"};
        assertArrayEquals(expected5, parser.flatten(options, arguments5, false));

        String[] arguments6 = {"-d", "arg1", "arg2"};
        String[] expected6 = {"-d", "arg1", "arg2"};
        assertArrayEquals(expected6, parser.flatten(options, arguments6, false));

        String[] arguments7 = {};
        String[] expected7 = {};
        assertArrayEquals(expected7, parser.flatten(options, arguments7, false));

        String[] arguments8 = {"-a", "-b", "-c"};
        String[] expected8 = {"-a", "-b", "-c"};
        assertArrayEquals(expected8, parser.flatten(options, arguments8, false));

        String[] arguments9 = {"-a", "-b", "-c", "-d"};
        String[] expected9 = {"-a", "-b", "-c", "-d"};
        assertArrayEquals(expected9, parser.flatten(options, arguments9, false));

        String[] arguments10 = {"-a", "arg1", "--", "-b"};
        String[] expected10 = {"-a", "arg1", "--", "-b"};
        assertArrayEquals(expected10, parser.flatten(options, arguments10, true));
    }
}