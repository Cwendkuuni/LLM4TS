import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", false, "test option");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOption_Char() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("b", "test option");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("c", "test option");
        option.setType(Integer.class);
        commandLine.addOption(option);

        Assert.assertEquals(Integer.class, commandLine.getOptionObject("c").getClass());
    }

    @Test
    public void testGetOptionObject_Char() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("d", "test option");
        option.setType(Double.class);
        commandLine.addOption(option);

        Assert.assertEquals(Double.class, commandLine.getOptionObject('d').getClass());
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("e", true, "test option");
        option.setArgs(1);
        commandLine.addOption(option);

        String value = "test value";
        String[] args = {value};
        commandLine.addArg(value);
        commandLine.addOptionValue("e", args);

        Assert.assertEquals(value, commandLine.getOptionValue("e"));
    }

    @Test
    public void testGetOptionValue_Char() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("f", true, "test option");
        option.setArgs(1);
        commandLine.addOption(option);

        String value = "test value";
        String[] args = {value};
        commandLine.addArg(value);
        commandLine.addOptionValue("f", args);

        Assert.assertEquals(value, commandLine.getOptionValue('f'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("g", true, "test option");
        option.setArgs(2);
        commandLine.addOption(option);

        String[] values = {"value1", "value2"};
        commandLine.addArg(values[0]);
        commandLine.addArg(values[1]);
        commandLine.addOptionValue("g", values);

        Assert.assertArrayEquals(values, commandLine.getOptionValues("g"));
    }

    @Test
    public void testGetOptionValues_Char() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("h", true, "test option");
        option.setArgs(2);
        commandLine.addOption(option);

        String[] values = {"value1", "value2"};
        commandLine.addArg(values[0]);
        commandLine.addArg(values[1]);
        commandLine.addOptionValue("h", values);

        Assert.assertArrayEquals(values, commandLine.getOptionValues('h'));
    }

    @Test
    public void testGetOptionValueWithDefault_String() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("i", true, "test option");
        commandLine.addOption(option);

        String defaultValue = "default value";

        Assert.assertEquals(defaultValue, commandLine.getOptionValue("i", defaultValue));
    }

    @Test
    public void testGetOptionValueWithDefault_Char() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("j", true, "test option");
        commandLine.addOption(option);

        String defaultValue = "default value";

        Assert.assertEquals(defaultValue, commandLine.getOptionValue('j', defaultValue));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        String[] args = {"arg1", "arg2"};
        commandLine.addArg(args[0]);
        commandLine.addArg(args[1]);

        Assert.assertArrayEquals(args, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        String[] args = {"arg1", "arg2"};
        commandLine.addArg(args[0]);
        commandLine.addArg(args[1]);

        Assert.assertArrayEquals(args, commandLine.getArgList().toArray());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        String arg = "test arg";
        commandLine.addArg(arg);

        Assert.assertEquals(arg, commandLine.getArgs()[0]);
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("k", "test option");
        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOptions()[0]);
    }
}