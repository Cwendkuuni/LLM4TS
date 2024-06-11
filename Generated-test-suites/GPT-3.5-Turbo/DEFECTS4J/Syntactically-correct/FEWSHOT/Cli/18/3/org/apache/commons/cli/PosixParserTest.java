import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option1 = new Option("f", false, "");
        Option option2 = new Option("p", true, "");
        Option option3 = new Option("t", false, "");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String[] arguments1 = {"-f", "-p", "value", "-t"};
        assertArrayEquals(new String[]{"-f", "-p", "value", "-t"}, new PosixParser().flatten(options, arguments1, false));

        String[] arguments2 = {"--f", "--p", "value", "--t"};
        assertArrayEquals(new String[]{"--f", "--p", "value", "--t"}, new PosixParser().flatten(options, arguments2, false));

        String[] arguments3 = {"-fp", "value", "-t"};
        assertArrayEquals(new String[]{"-f", "-p", "value", "-t"}, new PosixParser().flatten(options, arguments3, false));

        String[] arguments4 = {"-ft", "value"};
        assertArrayEquals(new String[]{"-f", "-t", "value"}, new PosixParser().flatten(options, arguments4, false));

        String[] arguments5 = {"-ftvalue"};
        assertArrayEquals(new String[]{"-f", "-t", "value"}, new PosixParser().flatten(options, arguments5, false));
    }
}