import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "apple", false, "An apple option"));
        assert cmd.hasOption("a") == true;
    }

    @Test
    public void testHasOption_WithChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "apple", false, "An apple option"));
        assert cmd.hasOption('a') == true;
    }

    @Test
    public void testHasOption_NotSet() {
        CommandLine cmd = new CommandLine();
        assert cmd.hasOption("a") == false;
    }

    @Test
    public void testHasOption_NotSet_WithChar() {
        CommandLine cmd = new CommandLine();
        assert cmd.hasOption('a') == false;
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "A file option"));
        cmd.addArg("file.txt");
        assert cmd.getOptionObject("f").equals("file.txt");
    }

    @Test
    public void testGetOptionObject_WithChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "A file option"));
        cmd.addArg("file.txt");
        assert cmd.getOptionObject('f').equals("file.txt");
    }

    @Test
    public void testGetOptionObject_NotSet() {
        CommandLine cmd = new CommandLine();
        assert cmd.getOptionObject("f") == null;
    }

    @Test
    public void testGetOptionObject_NotSet_WithChar() {
        CommandLine cmd = new CommandLine();
        assert cmd.getOptionObject('f') == null;
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "output", true, "An output option"));
        cmd.addArg("output.txt");
        assert cmd.getOptionValue("o").equals("output.txt");
    }

    @Test
    public void testGetOptionValue_WithChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "output", true, "An output option"));
        cmd.addArg("output.txt");
        assert cmd.getOptionValue('o').equals("output.txt");
    }

    @Test
    public void testGetOptionValue_NotSet() {
        CommandLine cmd = new CommandLine();
        assert cmd.getOptionValue("o") == null;
    }

    @Test
    public void testGetOptionValue_NotSet_WithChar() {
        CommandLine cmd = new CommandLine();
        assert cmd.getOptionValue('o') == null;
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "output", true, "An output option"));
        cmd.addArg("file1.txt");
        cmd.addArg("file2.txt");
        String[] expected = {"file1.txt", "file2.txt"};
        assertArrayEquals(cmd.getOptionValues("o"), expected);
    }

    @Test
    public void testGetOptionValues_WithChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "output", true, "An output option"));
        cmd.addArg("file1.txt");
        cmd.addArg("file2.txt");
        String[] expected = {"file1.txt", "file2.txt"};
        assertArrayEquals(cmd.getOptionValues('o'), expected);
    }

    @Test
    public void testGetOptionValues_NotSet() {
        CommandLine cmd = new CommandLine();
        assert cmd.getOptionValues("o") == null;
    }

    @Test
    public void testGetOptionValues_NotSet_WithChar() {
        CommandLine cmd = new CommandLine();
        assert cmd.getOptionValues('o') == null;
    }

    @Test
    public void testGetOptionValue_DefaultValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "output", true, "An output option"));
        assert cmd.getOptionValue("o", "default.txt").equals("default.txt");
    }

    @Test
    public void testGetOptionValue_DefaultValue_WithChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "output", true, "An output option"));
        assert cmd.getOptionValue('o', "default.txt").equals("default.txt");
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(cmd.getArgs(), expected);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");
        assertEquals(cmd.getArgList(), expected);
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        String[] expected = {"arg1"};
        assertArrayEquals(cmd.getArgs(), expected);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "A file option");
        cmd.addOption(option);
        Option[] expected = {option};
        assertArrayEquals(cmd.getOptions(), expected);
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "A file option");
        cmd.addOption(option);
        Iterator<Option> iterator = cmd.iterator();
        assertEquals(iterator.next(), option);
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("f", "file", true, "A file option");
        Option option2 = new Option("a", "apple", false, "An apple option");
        cmd.addOption(option1);
        cmd.addOption(option2);
        Option[] expected = {option1, option2};
        assertArrayEquals(cmd.getOptions(), expected);
    }

}