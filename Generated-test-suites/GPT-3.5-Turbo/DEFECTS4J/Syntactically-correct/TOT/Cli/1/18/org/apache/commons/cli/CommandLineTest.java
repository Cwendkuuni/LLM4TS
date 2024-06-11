
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        cmd.addOption(new Option("f", false));
        assertTrue(cmd.hasOption("f"));

        // Test edge case
        assertFalse(cmd.hasOption("x"));

        // Test error scenario
        assertFalse(cmd.hasOption(null));

    }

    @Test
    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        cmd.addOption(new Option("f", false));
        assertTrue(cmd.hasOption('f'));

        // Test edge case
        assertFalse(cmd.hasOption('x'));

        // Test error scenario
        assertFalse(cmd.hasOption('\0'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option = new Option("v", "verbose", false);
        option.setType(Integer.class);
        option.addValue("10");
        cmd.addOption(option);
        assertEquals(10, cmd.getOptionObject("v"));

        // Test edge case
        assertNull(cmd.getOptionObject("x"));

        // Test error scenario
        assertNull(cmd.getOptionObject(null));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option = new Option("v", "verbose", false);
        option.setType(Integer.class);
        option.addValue("10");
        cmd.addOption(option);
        assertEquals(10, cmd.getOptionObject('v'));

        // Test edge case
        assertNull(cmd.getOptionObject('x'));

        // Test error scenario
        assertNull(cmd.getOptionObject('\0'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option = new Option("o", "output", true);
        option.addValue("file.txt");
        cmd.addOption(option);
        assertEquals("file.txt", cmd.getOptionValue("o"));

        // Test edge case
        assertNull(cmd.getOptionValue("x"));

        // Test error scenario
        assertNull(cmd.getOptionValue(null));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option = new Option("o", "output", true);
        option.addValue("file.txt");
        cmd.addOption(option);
        assertEquals("file.txt", cmd.getOptionValue('o'));

        // Test edge case
        assertNull(cmd.getOptionValue('x'));

        // Test error scenario
        assertNull(cmd.getOptionValue('\0'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option = new Option("i", "input", true);
        option.addValue("file1.txt");
        option.addValue("file2.txt");
        cmd.addOption(option);
        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, cmd.getOptionValues("i"));

        // Test edge case
        assertNull(cmd.getOptionValues("x"));

        // Test error scenario
        assertNull(cmd.getOptionValues(null));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option = new Option("i", "input", true);
        option.addValue("file1.txt");
        option.addValue("file2.txt");
        cmd.addOption(option);
        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, cmd.getOptionValues('i'));

        // Test edge case
        assertNull(cmd.getOptionValues('x'));

        // Test error scenario
        assertNull(cmd.getOptionValues('\0'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option = new Option("o", "output", true);
        option.addValue("file.txt");
        cmd.addOption(option);
        assertEquals("file.txt", cmd.getOptionValue("o", "default.txt"));

        // Test edge case
        assertEquals("default.txt", cmd.getOptionValue("x", "default.txt"));

        // Test error scenario
        assertNull(cmd.getOptionValue(null, "default.txt"));
    }

    @Test
    public void testGetOptionValueWithDefaultChar() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option = new Option("o", "output", true);
        option.addValue("file.txt");
        cmd.addOption(option);
        assertEquals("file.txt", cmd.getOptionValue('o', "default.txt"));

        // Test edge case
        assertEquals("default.txt", cmd.getOptionValue('x', "default.txt"));

        // Test error scenario
        assertNull(cmd.getOptionValue('\0', "default.txt"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());

        // Test edge case
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertEquals(2, cmd.getArgList().size());

        // Test edge case
        assertEquals(0, cmd.getArgList().size());
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option1 = new Option("f", false);
        Option option2 = new Option("v", "verbose", true);
        Option option3 = new Option("o", "output", true);
        cmd.addOption(option1);
        cmd.addOption(option2);
        cmd.addOption(option3);

        int count = 0;
        for (Option option : cmd) {
            count++;
        }
        assertEquals(3, count);

        // Test edge case
        count = 0;
        for (Option option : new CommandLine()) {
            count++;
        }
        assertEquals(0, count);
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();

        // Test typical use case
        Option option1 = new Option("f", false);
        Option option2 = new Option("v", "verbose", true);
        Option option3 = new Option("o", "output", true);
        cmd.addOption(option1);
        cmd.addOption(option2);
        cmd.addOption(option3);

        assertArrayEquals(new Option[]{option1, option2, option3}, cmd.getOptions());

        // Test edge case
        assertArrayEquals(new Option[]{}, new CommandLine().getOptions());
    }
}

