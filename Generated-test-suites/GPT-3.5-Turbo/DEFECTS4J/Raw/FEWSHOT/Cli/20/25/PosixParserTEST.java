import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", true, "option2 description");
        Option option3 = new Option("c", "option3", false, "option3 description");
        Option option4 = new Option("d", "option4", false, "option4 description");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        
        String[] arguments1 = {"--option1", "value1", "-b", "value2", "--option3"};
        String[] expected1 = {"--option1", "value1", "-b", "value2", "--option3"};
        String[] output1 = PosixParser.flatten(options, arguments1, false);
        assertArrayEquals(expected1, output1);

        String[] arguments2 = {"--option1", "value1", "-b", "value2", "--option3"};
        String[] expected2 = {"--option1", "value1", "-b", "value2"};
        String[] output2 = PosixParser.flatten(options, arguments2, true);
        assertArrayEquals(expected2, output2);

        String[] arguments3 = {"--option1", "value1", "-b", "value2", "--option3"};
        String[] expected3 = {"--option1", "value1", "-b", "value2"};
        String[] output3 = PosixParser.flatten(options, arguments3, true);
        assertArrayEquals(expected3, output3);

        String[] arguments4 = {"--option1", "value1", "-b", "--option3"};
        String[] expected4 = {"--option1", "value1", "-b"};
        String[] output4 = PosixParser.flatten(options, arguments4, true);
        assertArrayEquals(expected4, output4);

        String[] arguments5 = {"--option1", "value1", "-b=value2", "--option3"};
        String[] expected5 = {"--option1", "value1", "-b", "value2", "--option3"};
        String[] output5 = PosixParser.flatten(options, arguments5, false);
        assertArrayEquals(expected5, output5);

        String[] arguments6 = {"--option1", "value1", "-b=value2", "--option3"};
        String[] expected6 = {"--option1", "value1", "-b", "value2"};
        String[] output6 = PosixParser.flatten(options, arguments6, true);
        assertArrayEquals(expected6, output6);

        String[] arguments7 = {"--option1", "value1", "-b"};
        String[] expected7 = {"--option1", "value1", "-b"};
        String[] output7 = PosixParser.flatten(options, arguments7, false);
        assertArrayEquals(expected7, output7);

        String[] arguments8 = {"--option1", "value1", "-b"};
        String[] expected8 = {"--option1", "value1"};
        String[] output8 = PosixParser.flatten(options, arguments8, true);
        assertArrayEquals(expected8, output8);

        String[] arguments9 = {"-a", "-b", "-c", "--option4"};
        String[] expected9 = {"-a", "-b", "-c", "--option4"};
        String[] output9 = PosixParser.flatten(options, arguments9, false);
        assertArrayEquals(expected9, output9);

        String[] arguments10 = {"-a", "-b", "-c", "--option4"};
        String[] expected10 = {"-a", "-b", "-c"};
        String[] output10 = PosixParser.flatten(options, arguments10, true);
        assertArrayEquals(expected10, output10);

        String[] arguments11 = {"--", "-a", "-b", "-c"};
        String[] expected11 = {"--", "-a", "-b", "-c"};
        String[] output11 = PosixParser.flatten(options, arguments11, false);
        assertArrayEquals(expected11, output11);

        String[] arguments12 = {"--", "-a", "-b", "-c"};
        String[] expected12 = {"--", "-a", "-b", "-c"};
        String[] output12 = PosixParser.flatten(options, arguments12, true);
        assertArrayEquals(expected12, output12);
    }
}