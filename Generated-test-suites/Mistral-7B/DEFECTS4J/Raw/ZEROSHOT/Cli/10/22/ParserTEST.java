import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ParserTest {

    @org.junit.Test
    public void testParse() throws Exception {
        String[] args = new String[]{"-a", "1", "--optionB", "2", "--optionC=3", "--optionD", "--", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");
        options.addOption("c", true, "option C");
        options.addOption("d", Value.NONE, "option D");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(args, true);

        assertFalse(cmd.hasOption("unknown"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertNull(cmd.getOptionValue("d"));
        assertEquals(1, cmd.getOptionValues("a").length);
        assertEquals("1", cmd.getOptionValues("a")[0]);
        assertEquals(2, cmd.getOptionValues("optionB").length);
        assertEquals("2", cmd.getOptionValues("optionB")[0]);
        assertEquals("3", cmd.getOptionValues("optionC")[0]);
        assertNull(cmd.getArgs().length);

        String[] args2 = new String[]{"--optionD"};
        try {
            cmd = parser.parse(args2, true);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: optionD", e.getMessage());
        }

        String[] args3 = new String[]{"--optionD=value"};
        cmd = parser.parse(args3, true);
        assertEquals("value", cmd.getOptionValue("optionD"));

        String[] args4 = new String[]{"--optionD=value1", "--optionD=value2"};
        try {
            cmd = parser.parse(args4, true);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid option value 'value2' for option 'optionD'. Option value can not be null or empty", e.getMessage());
        }
        List<String> optionDValues = Arrays.asList(cmd.getOptionValues("optionD"));
        assertEquals(1, optionDValues.size());
        assertEquals("value1", optionDValues.get(0));

        String[] args5 = new String[]{"--optionD=value1", "--optionD=value2", "--optionD"};
        try {
            cmd = parser.parse(args5, true);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid option value '--optionD' for option 'optionD'. Option value can not be null or empty", e.getMessage());
        }

        String[] args6 = new String[]{"-a", "--optionB"};
        try {
            cmd = parser.parse(args6, true);
            fail();
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: a", e.getMessage());
        }

        String[] args7 = new String[]{"--optionB", "-a"};
        try {
            cmd = parser.parse(args7, true);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Option '--optionB' cannot be used with option 'a'", e.getMessage());
        }

        String[] args8 = new String[]{"--optionA", "--optionB", "--optionC"};
        options.getOptionGroup("mygroup").addOption(new Option("x", true, "option x in group"));
        cmd = parser.parse(args8, options, true);
        assertTrue(cmd.getOptions().hasOption("x"));
        OptionGroup group = cmd.getOptions().getOptionGroup("mygroup");
        assertEquals(3, group.getOptions().size());
        assertEquals(2, group.getSelectedOptions().size());
        assertEquals("--optionB", group.getSelectedOptions().get(0).getOptString());
        assertEquals("--optionC", group.getSelectedOptions().get(1).getOptString());

    }

}