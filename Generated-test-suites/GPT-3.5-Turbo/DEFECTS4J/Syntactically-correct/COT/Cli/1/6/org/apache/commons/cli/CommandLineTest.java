import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();

        // Test when option is not set
        assert(cmd.hasOption("opt") == false);

        // Test when option is set
        cmd.addOption(new Option("opt", "option"));
        assert(cmd.hasOption("opt") == true);
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();

        // Test when option is not set
        assert(cmd.getOptionObject("opt") == null);

        // Test when option is set with value
        cmd.addOption(new Option("opt", "option", true, "value"));
        assert(cmd.getOptionObject("opt").equals("value"));

        // Test when option is set without value
        cmd.addOption(new Option("opt2", "option2", false, null));
        assert(cmd.getOptionObject("opt2") == null);
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();

        // Test when option is not set
        assert(cmd.getOptionValue("opt") == null);

        // Test when option is set with value
        cmd.addOption(new Option("opt", "option", true, "value"));
        assert(cmd.getOptionValue("opt").equals("value"));

        // Test when option is set without value
        cmd.addOption(new Option("opt2", "option2", false, null));
        assert(cmd.getOptionValue("opt2") == null);
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();

        // Test when option is not set
        assert(cmd.getOptionValues("opt") == null);

        // Test when option is set with multiple values
        String[] values = {"value1", "value2"};
        cmd.addOption(new Option("opt", "option", true, values));
        assert(cmd.getOptionValues("opt").equals(values));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();

        // Test when no arguments are added
        assert(cmd.getArgs() == null);

        // Test when arguments are added
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = {"arg1", "arg2"};
        assert(cmd.getArgs().equals(args));
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();

        // Test when no arguments are added
        assert(cmd.getArgList() == null);

        // Test when arguments are added
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List<String> argList = new ArrayList<>();
        argList.add("arg1");
        argList.add("arg2");
        assert(cmd.getArgList().equals(argList));
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();

        // Test when argument is added
        cmd.addArg("arg1");
        String[] args = {"arg1"};
        assert(cmd.getArgs().equals(args));
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();

        // Test when option is added
        Option opt = new Option("opt", "option");
        cmd.addOption(opt);
        Option[] options = {opt};
        assert(Arrays.equals(cmd.getOptions(), options));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();

        // Test when no options are added
        Iterator<Option> iterator = cmd.iterator();
        assert(iterator.hasNext() == false);

        // Test when options are added
        Option opt1 = new Option("opt1", "option1");
        Option opt2 = new Option("opt2", "option2");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        Option[] options = {opt1, opt2};
        assert(Arrays.equals(cmd.getOptions(), options));
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();

        // Test when no options are added
        assert(cmd.getOptions() == null);

        // Test when options are added
        Option opt1 = new Option("opt1", "option1");
        Option opt2 = new Option("opt2", "option2");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        Option[] options = {opt1, opt2};
        assert(Arrays.equals(cmd.getOptions(), options));
    }
}