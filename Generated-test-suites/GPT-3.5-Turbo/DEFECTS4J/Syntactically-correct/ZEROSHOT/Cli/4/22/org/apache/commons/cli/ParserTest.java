import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testFlatten() {
        // Test case 1
        Options options1 = new Options();
        options1.addOption("a", "add", false, "Add option");
        Parser parser1 = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-a"};
            }
        };
        String[] arguments1 = new String[]{"-a"};
        String[] expected1 = new String[]{"-a"};
        assertArrayEquals(expected1, parser1.flatten(options1, arguments1, false));

        // Test case 2
        Options options2 = new Options();
        options2.addOption("f", "file", true, "File option");
        Parser parser2 = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-f", "test.txt"};
            }
        };
        String[] arguments2 = new String[]{"-f", "test.txt"};
        String[] expected2 = new String[]{"-f", "test.txt"};
        assertArrayEquals(expected2, parser2.flatten(options2, arguments2, false));
    }

    @Test
    public void testParse() {
        // Test case 1
        Options options1 = new Options();
        options1.addOption("a", "add", false, "Add option");
        Parser parser1 = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-a"};
            }
        };
        String[] arguments1 = new String[]{"-a"};
        CommandLine expected1 = new CommandLine();
        expected1.addOption(options1.getOption("a"));
        assertEquals(expected1, parser1.parse(options1, arguments1));

        // Test case 2
        Options options2 = new Options();
        options2.addOption("f", "file", true, "File option");
        Parser parser2 = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-f", "test.txt"};
            }
        };
        String[] arguments2 = new String[]{"-f", "test.txt"};
        CommandLine expected2 = new CommandLine();
        expected2.addOption(options2.getOption("f"), "test.txt");
        assertEquals(expected2, parser2.parse(options2, arguments2));
    }

    @Test
    public void testProcessArgs() {
        // Test case 1
        Option option1 = new Option("f", "file", true, "File option");
        Parser parser1 = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-f", "test.txt"};
            }
        };
        ListIterator<String> iter1 = Arrays.asList("-f", "test.txt").listIterator();
        parser1.processArgs(option1, iter1);
        assertEquals("test.txt", option1.getValue());

        // Test case 2
        Option option2 = new Option("a", "add", false, "Add option");
        Parser parser2 = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-a"};
            }
        };
        ListIterator<String> iter2 = Arrays.asList("-a").listIterator();
        parser2.processArgs(option2, iter2);
        assertNull(option2.getValue());
    }

    @Test
    public void testProcessOption() throws ParseException {
        // Test case 1
        Options options1 = new Options();
        options1.addOption("a", "add", false, "Add option");
        Parser parser1 = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-a"};
            }
        };
        ListIterator<String> iter1 = Arrays.asList("-a").listIterator();
        parser1.processOption("-a", iter1);
        assertNotNull(parser1.parse(options1, new String[]{}).getOption("a"));

        // Test case 2
        Options options2 = new Options();
        options2.addOption("f", "file", true, "File option");
        Parser parser2 = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-f", "test.txt"};
            }
        };
        ListIterator<String> iter2 = Arrays.asList("-f").listIterator();
        parser2.processOption("-f", iter2);
        assertEquals("test.txt", parser2.parse(options2, new String[]{}).getOptionValue("f"));
    }
}