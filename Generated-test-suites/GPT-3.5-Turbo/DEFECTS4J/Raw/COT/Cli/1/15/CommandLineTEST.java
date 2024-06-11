import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assert(cmd.hasOption("-a") == false);
        assert(cmd.hasOption('b') == false);
        cmd.addOption(new Option("-a"));
        cmd.addOption(new Option('b'));
        assert(cmd.hasOption("-a") == true);
        assert(cmd.hasOption('b') == true);
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        assert(cmd.getOptionObject("-a") == null);
        assert(cmd.getOptionObject('b') == null);
        cmd.addOption(new Option("-a", "valueA"));
        cmd.addOption(new Option('b', "valueB"));
        assert(cmd.getOptionObject("-a").equals("valueA"));
        assert(cmd.getOptionObject('b').equals("valueB"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assert(cmd.getOptionValue("-a") == null);
        assert(cmd.getOptionValue('b') == null);
        cmd.addOption(new Option("-a", "valueA"));
        cmd.addOption(new Option('b', "valueB"));
        assert(cmd.getOptionValue("-a").equals("valueA"));
        assert(cmd.getOptionValue('b').equals("valueB"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assert(cmd.getOptionValues("-a") == null);
        assert(cmd.getOptionValues('b') == null);
        cmd.addOption(new Option("-a", new String[]{"valueA1", "valueA2"}));
        cmd.addOption(new Option('b', new String[]{"valueB1", "valueB2"}));
        String[] valuesA = cmd.getOptionValues("-a");
        String[] valuesB = cmd.getOptionValues('b');
        assert(valuesA.length == 2 && valuesA[0].equals("valueA1") && valuesA[1].equals("valueA2"));
        assert(valuesB.length == 2 && valuesB[0].equals("valueB1") && valuesB[1].equals("valueB2"));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine cmd = new CommandLine();
        assert(cmd.getOptionValue("-a", "defaultA").equals("defaultA"));
        assert(cmd.getOptionValue('b', "defaultB").equals("defaultB"));
        cmd.addOption(new Option("-a", "valueA"));
        cmd.addOption(new Option('b', "valueB"));
        assert(cmd.getOptionValue("-a", "defaultA").equals("valueA"));
        assert(cmd.getOptionValue('b', "defaultB").equals("valueB"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        assert(cmd.getArgs().length == 0);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assert(args.length == 2 && args[0].equals("arg1") && args[1].equals("arg2"));
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        assert(cmd.getArgList().size() == 0);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List<String> argList = cmd.getArgList();
        assert(argList.size() == 2 && argList.get(0).equals("arg1") && argList.get(1).equals("arg2"));
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assert(args.length == 2 && args[0].equals("arg1") && args[1].equals("arg2"));
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        assert(cmd.getOptions().length == 0);
        cmd.addOption(new Option("-a"));
        cmd.addOption(new Option('b'));
        Option[] options = cmd.getOptions();
        assert(options.length == 2 && options[0].getOpt().equals("-a") && options[1].getOpt().equals("b"));
    }
}