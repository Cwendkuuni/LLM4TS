import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testFlatten() {
        // Test case 1
        Options options1 = new Options();
        String[] arguments1 = new String[]{"--option1", "value1", "--option2"};
        boolean stopAtNonOption1 = false;
        try {
            Parser parser1 = new Parser() {
                @Override
                protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                    return arguments;
                }
            };
            String[] result1 = parser1.flatten(options1, arguments1, stopAtNonOption1);
            assertArrayEquals(arguments1, result1);
        } catch (ParseException e) {
            fail(e.getMessage());
        }

        // Test case 2
        Options options2 = new Options();
        String[] arguments2 = new String[]{"--option1", "value1", "--option2"};
        boolean stopAtNonOption2 = true;
        try {
            Parser parser2 = new Parser() {
                @Override
                protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                    return arguments;
                }
            };
            String[] result2 = parser2.flatten(options2, arguments2, stopAtNonOption2);
            assertArrayEquals(arguments2, result2);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParse() {
        // Test case 1
        Options options1 = new Options();
        String[] arguments1 = new String[]{"--option1", "value1", "--option2"};
        try {
            Parser parser1 = new Parser() {
                @Override
                protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                    return new String[]{"--option1", "value1", "--option2"};
                }
            };
            CommandLine result1 = parser1.parse(options1, arguments1);
            assertNotNull(result1);
        } catch (ParseException e) {
            fail(e.getMessage());
        }

        // Test case 2
        Options options2 = new Options();
        options2.addOption("option1", true, "option1 description");
        String[] arguments2 = new String[]{"--option1", "value1", "--option2"};
        try {
            Parser parser2 = new Parser() {
                @Override
                protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                    return new String[]{"--option1", "value1", "--option2"};
                }
            };
            CommandLine result2 = parser2.parse(options2, arguments2);
            assertNotNull(result2);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
}