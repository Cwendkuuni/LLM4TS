
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_OptionsStringArray() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        String[] arguments = {"-option1", "value1", "-option2", "value2"};

        try {
            CommandLine cmd = parser.parse(options, arguments);

            assertTrue(cmd.getOptions().hasOption("option1"));
            assertEquals("value1", cmd.getOptionValue("option1"));
            assertTrue(cmd.getOptions().hasOption("option2"));
            assertEquals("value2", cmd.getOptionValue("option2"));

        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_OptionsStringArrayProperties() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        String[] arguments = {"-option1", "value1", "-option2", "value2"};
        Properties properties = new Properties();
        properties.setProperty("option3", "value3");

        try {
            CommandLine cmd = parser.parse(options, arguments, properties);

            assertTrue(cmd.getOptions().hasOption("option1"));
            assertEquals("value1", cmd.getOptionValue("option1"));
            assertTrue(cmd.getOptions().hasOption("option2"));
            assertEquals("value2", cmd.getOptionValue("option2"));
            assertTrue(cmd.getOptions().hasOption("option3"));
            assertEquals("value3", cmd.getOptionValue("option3"));

        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_OptionsStringArrayBoolean() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        String[] arguments = {"-option1", "value1", "-option2", "value2"};

        try {
            CommandLine cmd = parser.parse(options, arguments, true);

            assertTrue(cmd.getOptions().hasOption("option1"));
            assertEquals("value1", cmd.getOptionValue("option1"));
            assertTrue(cmd.getOptions().hasOption("option2"));
            assertEquals("value2", cmd.getOptionValue("option2"));

        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_OptionsStringArrayPropertiesBoolean() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        String[] arguments = {"-option1", "value1", "-option2", "value2"};
        Properties properties = new Properties();
        properties.setProperty("option3", "value3");

        try {
            CommandLine cmd = parser.parse(options, arguments, properties, true);

            assertTrue(cmd.getOptions().hasOption("option1"));
            assertEquals("value1", cmd.getOptionValue("option1"));
            assertTrue(cmd.getOptions().hasOption("option2"));
            assertEquals("value2", cmd.getOptionValue("option2"));
            assertTrue(cmd.getOptions().hasOption("option3"));
            assertEquals("value3", cmd.getOptionValue("option3"));

        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

}
