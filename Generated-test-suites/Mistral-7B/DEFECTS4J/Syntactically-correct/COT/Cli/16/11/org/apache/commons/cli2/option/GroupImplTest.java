import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImpl_BasicFunctionality() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("option1", "Option 1", true, "o1"));
        options.add(new Option("option2", "Option 2", false, "o2"));
        Option group = new GroupImpl(options, "Group", "Test Group", 0, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.iterator());

        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
    }

    @Test
    public void testGroupImpl_Minimum_OptionPresent() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("option1", "Option 1", true, "o1"));
        options.add(new Option("option2", "Option 2", false, "o2"));
        Option group = new GroupImpl(options, "Group", "Test Group", 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.iterator());

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_Minimum_LessThanRequired() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("option1", "Option 1", true, "o1"));
        options.add(new Option("option2", "Option 2", false, "o2"));
        Option group = new GroupImpl(options, "Group", "Test Group", 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.iterator());
        group.process(commandLine, commandLine.iterator());

        assertFalse(commandLine.hasOption("option2"));
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_Maximum_MoreThanAllowed() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("option1", "Option 1", true, "o1"));
        options.add(new Option("option2", "Option 2", false, "o2"));
        Option group = new GroupImpl(options, "Group", "Test Group", 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.iterator());
        group.process(commandLine, commandLine.iterator());
        group.process(commandLine, commandLine.iterator());

        assertFalse(commandLine.hasOption("option2"));
    }

    @Test
    public void testGroupImpl_AnonymousArgument_BasicFunctionality() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("option1", "Option 1", true, "o1"));
        options.add(new Argument("argument1", "Argument 1", 1, "arg1"));
        Option group = new GroupImpl(options, "Group", "Test Group", 0, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.iterator());

        String argument = commandLine.getOptionValue("argument1");
        assertEquals("", argument);

        commandLine.parse();
        commandLine.setArgs(new String[]{"arg1"});
        group.process(commandLine, commandLine.iterator());

        argument = commandLine.getOptionValue("argument1");
        assertEquals("arg1", argument);
    }

    @Test(expected = OptionException.class)
    public void testGroupImpl_AnonymousArgument_InvalidArgument() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("option1", "Option 1", true, "o1"));
        options.add(new Argument("argument1", "Argument 1", 1, "arg1"));
        Option group = new GroupImpl(options, "Group", "Test Group", 0, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.iterator());
        group.process(commandLine, commandLine.iterator());

        assertFalse(commandLine.hasOption("argument1"));
    }

    @Test
    public void testGroupImpl_Process_WithDuplicateOption() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("option1", "Option 1", true, "o1"));
        Option opt1 = new Option("option1", "Option 1", true, "o1");
        opt1.setValueDescription("Value Description");
        options.add(opt1);
        Option group = new GroupImpl(options, "Group", "Test Group", 0, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.iterator());

        assertEquals(2, commandLine.getOptionValues("option1").length);
        assertEquals("", commandLine.getOptionValue("option1")[1]); // default value
        assertEquals("Value Description", commandLine.getOptionValue("option1")[0]);
    }

    @Test
    public void testGroupImpl_Process_WithPrefix() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("prefix", "Prefix", false, "p"));
        options.add(new Option("option1", "Option 1", true, "o1"));
        Option group = new GroupImpl(options, "Group", "Test Group", 0, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--prefix --option1 o1");
        group.process(commandLine, commandLine.iterator());

        assertTrue(commandLine.hasOption("prefix"));
        assertTrue(commandLine.hasOption("option1"));
    }

    @Test
    public void testGroupImpl_Process_WithBurstedOption() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("prefix", "Prefix", false, "p"));
        final Option groupOption = new Option("group", "Group", false, null);
        groupOption.setBursted(true);
        options.add(groupOption);
        options.add(new Option("option1", "Option 1", true, "o1"));
        Option group = new GroupImpl(options, "Group", "Test Group", 0, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--prefix --group --option1 o1");
        group.process(commandLine, commandLine.iterator());

        assertTrue(commandLine.hasOption("prefix"));
        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("option1"));
    }
}