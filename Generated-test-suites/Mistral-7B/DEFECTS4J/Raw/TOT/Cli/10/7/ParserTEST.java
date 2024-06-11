import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
        options.addRequiredOption("i", "input", true, "Input file path");
    }

    @Test
    public void testParseWithValidOptions() throws ParseException {
        String[] args = {"-i", "test.txt"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals("test.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testParseWithMissingRequiredOption() {
        String[] args = {"-i"};
        try {
            parser.parse(options, args);
            fail("Expected ParseException");
        } catch (ParseException e) {
            List<String> messages = Arrays.asList(e.getMessage().split("\n"));
            assertTrue(messages.contains("Missing required option: input"));
        }
    }

    @Test
    public void testParseWithInvalidOption() {
        String[] args = {"--invalid"};
        try {
            parser.parse(options, args);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: invalid", e.getMessage());
        }
    }

    @Test
    public void testParseWithMissingValueForOptionalOption() {
        String[] args = {"-o"};
        try {
            parser.parse(options, args);
            fail("Expected ParseException");
        } catch (ParseException e) {
            List<String> messages = Arrays.asList(e.getMessage().split("\n"));
            assertTrue(messages.contains("Missing value for option: o"));
        }
    }

    @Test
    public void testParseWithValidOptionsAndProperties() throws ParseException {
        options.addPropertyConstants(new Properties() {{
            setProperty("property1", "value1");
            setProperty("property2", "value2");
        }});

        String[] args = {"-i", "test.txt", "--property1=newValue1"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals("newValue1", cmd.getOptionValue("property1"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options group1 = new Options();
        group1.addRequiredOption("g1", "group1");
        Options group2 = new Options();
        group2.addOption("g2", true, "group2");
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);
        String[] args = {"-g1", "group1Value", "--g2"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(group1.isSelected());
        assertFalse(group2.isSelected());
    }
}