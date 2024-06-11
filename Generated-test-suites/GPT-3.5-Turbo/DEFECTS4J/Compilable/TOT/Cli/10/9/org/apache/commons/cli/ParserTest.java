package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_NullOptions() {
        try {
            Parser parser = new Parser() {
                @Override
                protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                    return null;
                }
            };

            String[] args = {"-a", "value1", "-b", "value2"};

            parser.parse(null, args);
            fail("Expected exception for null options");
        } catch (ParseException e) {
            assertEquals("Options cannot be null", e.getMessage());
        }
    }

    @Test
    public void testParse_NullArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        CommandLine cmd = parser.parse(options, null);
        assertNotNull("Command line should not be null", cmd);
        assertFalse("Option a should not be present", cmd.hasOption("a"));
        assertFalse("Option b should not be present", cmd.hasOption("b"));
    }

    @Test
    public void testParse_EmptyArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull("Command line should not be null", cmd);
        assertFalse("Option a should not be present", cmd.hasOption("a"));
        assertFalse("Option b should not be present", cmd.hasOption("b"));
    }

    @Test
    public void testParse_AllOptionsPresent() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        String[] args = {"-a", "-b", "value"};

        CommandLine cmd = parser.parse(options, args);
        assertNotNull("Command line should not be null", cmd);
        assertTrue("Option a should be present", cmd.hasOption("a"));
        assertTrue("Option b should be present", cmd.hasOption("b"));

        assertNull("Option a should not have any values", cmd.getOptionValues("a"));
        assertArrayEquals("Option b should have one value", new String[]{"value"}, cmd.getOptionValues("b"));
    }
}