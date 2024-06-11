import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.test.TestHelper;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupWithBasicOptions() throws OptionException {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("o", "option1", true, "Option 1"));
        options.add(new Option("o", "option2", true, "Option 2"));
        Group grp = new GroupImpl(options, "testGroup", "Testing group", 1, 2, false);

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();

        grp.parse(cmd, new String[]{"testGroup", "--option1", "value1", "--option2", "value2"});
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));

        formatter.printHelp("TestGroup", grp, true);
        System.out.println(formatter.getHelpText());
    }

    @Test
    public void testGroupWithMaximumOptions() throws OptionException {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("o", "option", true, "Option"));

        Group grp = new GroupImpl(options, "testGroup", "Testing group", 1, 100, false);

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();

        for (int i = 0; i < 11; i++) {
            cmd.parseOption("o", "option" + i, "value" + i);
            grp.parse(cmd, new String[]{"testGroup"});
        }

        try {
            grp.parse(cmd, new String[]{"testGroup", "--option12"});
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        formatter.printHelp("TestGroup", grp, true);
        System.out.println(formatter.getHelpText());
    }

    @Test
    public void testGroupWithMinimumOptions() throws OptionException {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("o", "option", true, "Option"));

        Group grp = new GroupImpl(options, "testGroup", "Testing group", 2, 10, false);

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();

        try {
            grp.parse(cmd, new String[]{"testGroup"});
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        formatter.printHelp("TestGroup", grp, true);
        System.out.println(formatter.getHelpText());

        cmd.parseOption("o", "option1", "value1");
        cmd.parseOption("o", "option2", "value2");

        grp.parse(cmd, new String[]{"testGroup"});
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));

        formatter.printHelp("TestGroup", grp, true);
        System.out.println(formatter.getHelpText());
    }

    @Test
    public void testGroupWithAnonymousArguments() throws OptionException {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("g", "group", false, "Test group", "group"));
        options.add(new Option("a", "arg", true, "Anonymous argument", 1, 1, "value"));

        Group grp = new GroupImpl(options, "testGroup", "Testing group", 0, 0, false);

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();

        cmd.parseOption("g", "group");
        cmd.parseArgument("arg0", "value0");
        cmd.parseArgument("arg1", "value1");

        grp.parse(cmd, new String[]{"testGroup"});
        assertEquals("value0", cmd.getArgument(0).getValue());
        assertEquals("value1", cmd.getArgument(1).getValue());

        formatter.printHelp("TestGroup", grp, true);
        System.out.println(formatter.getHelpText());
    }

    @Test
    public void testGroupWithMixedOptionsAndArguments() throws OptionException {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("g", "group", false, "Test group", "group"));
        options.add(new Option("o", "option", true, "Option", "option"));
        options.add(new Option("a", "arg", true, "Anonymous argument", 1, 1, "value"));

        Group grp = new GroupImpl(options, "testGroup", "Testing group", 0, 0, false);

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();

        cmd.parseOption("g", "group");
        cmd.parseOption("o", "option", "value");
        cmd.parseArgument("arg0", "value0");
        cmd.parseArgument("arg1", "value1");

        grp.parse(cmd, new String[]{"testGroup"});
        assertEquals("value", cmd.getOptionValue("option"));
        assertEquals("value0", cmd.getArgument(0).getValue());
        assertEquals("value1", cmd.getArgument(1).getValue());

        formatter.printHelp("TestGroup", grp, true);
        System.out.println(formatter.getHelpText());
    }

    @Test
    public void testGroupWithInvalidTrigger() throws OptionException {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("g", "group", false, "Test group", "group"));

        Group grp = new GroupImpl(options, "testGroup", "Testing group", 0, 0, false);

        CommandLine cmd = new CommandLine();
        HelpFormatter formatter = new HelpFormatter();

        try {
            grp.parse(cmd, new String[]{"testGroup", "--invalidOption"});
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        formatter.printHelp("TestGroup", grp, true);
        System.out.println(formatter.getHelpText());
    }
}