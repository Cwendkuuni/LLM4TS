import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.GroupImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    

    @Test
    public void testGroupImplConstructor() {
        List options = new ArrayList();
        options.add(new Option("o", "option1", false, "This is option1"));
        options.add(new Option("a", "optionAnonymous", ArgType.INTEGER, "Anonymous option with argument"));
        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 1, true);
        assertEquals("groupName", group.getPreferredName());
        assertEquals("Group description", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(options, group.getOptions());
        assertTrue(group.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupImplConstructorWithInvalidMinimum() {
        List options = new ArrayList();
        options.add(new Option("o", "option1", false, "This is option1"));
        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 0, 1, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupImplConstructorWithInvalidMaximum() {
        List options = new ArrayList();
        options.add(new Option("o", "option1", false, "This is option1"));
        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 0, true);
    }

    @Test
    public void testCanProcess() {
        List options = new ArrayList();
        options.add(new Option("o", "option1", false, "This is option1"));
        options.add(new Option("g", "group1", false, "This is group1"));

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 1, true);

        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"arg1", "arg2", "arg3"};

        commandLine.parseArgument(args, 0);
        commandLine.parseArgument(args, 1);

        assertFalse(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "g"));
        assertFalse(group.canProcess(commandLine, "arg3"));

        commandLine.parseArgument("o");
        assertTrue(group.canProcess(commandLine, "o"));

        commandLine.parseArgument("g");
        commandLine.parseArgument("o");
        assertTrue(group.canProcess(commandLine, "g"));

        commandLine.parseArgument("arg3");
        assertFalse(group.canProcess(commandLine, "g"));
    }

    @Test
    public void testCanProcessWithAnonymousArgument() {
        List options = new ArrayList();
        options.add(new Option("a", "anonymousArg", ArgType.INTEGER, "Anonymous argument"));
        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 1, true);

        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"arg1", "arg2", "arg3"};

        commandLine.parseArgument(args, 0);
        commandLine.parseArgument(args, 1, new Integer(1));
        commandLine.parseArgument(args, 2);

        assertFalse(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "arg2"));
        assertTrue(group.canProcess(commandLine, "anonymousArg"));
        assertTrue(group.canProcess(commandLine, "a"));
    }

    @Test
    public void testProcess() {
        List options = new ArrayList();
        options.add(new Option("o", "option1", false, "This is option1"));
        options.add(new Option("g", "group1", false, "This is group1"));
        options.add(new Option("a", "anonymousArg", ArgType.INTEGER, "Anonymous argument"));

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 1, true);

        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"arg1", "g", "arg3", "o"};

        commandLine.parseArgument(args, 0);
        commandLine.parseArgument(args, 1);
        commandLine.parseArgument(args, 2);
        commandLine.parseArgument(args, 3);

        group.process(commandLine, commandLine.getArguments().listIterator());

        assertEquals(2, commandLine.getOptions().size());
        assertEquals("option1", commandLine.getOptionValues("o").get(0).getLongOpt());
        assertEquals("group1", commandLine.getOptionValues("g").get(0).getLongOpt());
    }

    @Test
    public void testProcessWithAnonymousArgument() {
        List options = new ArrayList();
        options.add(new Option("a", "anonymousArg1", ArgType.INTEGER, "Anonymous argument 1"));
        options.add(new Option("a", "anonymousArg2", ArgType.INTEGER, "Anonymous argument 2"));
        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 1, true);

        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"arg1", "arg2", "arg3", "a", "2", "a", "3"};

        commandLine.parseArgument(args, 0);
        commandLine.parseArgument(args, 1);
        commandLine.parseArgument(args, 2);
        commandLine.parseArgument(args, 4, new Integer(2));
        commandLine.parseArgument(args, 5, new Integer(3));

        group.process(commandLine, commandLine.getArguments().listIterator());

        assertEquals(4, commandLine.getOptions().size());
        assertEquals("anonymousArg1", commandLine.getOptionValues("a").get(0).getLongOpt());
        assertEquals("anonymousArg2", commandLine.getOptionValues("a").get(1).getLongOpt());
        assertEquals(2, ((Integer) commandLine.getOptionValues("anonymousArg1").get(0)).intValue());
        assertEquals(3, ((Integer) commandLine.getOptionValues("anonymousArg2").get(0)).intValue());
    }

    @Test
    public void testValidate() {
        List options = new ArrayList();
        options.add(new Option("o", "option1", false, "This is option1"));
        options.add(new Option("a", "anonymousArg", ArgType.INTEGER, "Anonymous argument"));

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 1, true);

        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"arg1", "arg2", "arg3"};

        commandLine.parseArgument(args, 0);
        commandLine.parseArgument(args, 1, new Integer(1));
        commandLine.parseArgument(args, 2);

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with an incorrect number of options.
        String[] args2 = {"arg1", "arg2"};

        commandLine.parseArgument(args2, 0);
        commandLine.parseArgument(args2, 1);

        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("MISSING_OPTION", e.getMessage());
        }

        // Test with an incorrect number of anonymous arguments.
        String[] args3 = {"arg1", "arg2", "arg3", "anonymousArg"};

        commandLine.parseArgument(args3, 0);
        commandLine.parseArgument(args3, 1);
        commandLine.parseArgument(args3, 2);
        commandLine.parseArgument(args3, 3);

        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("TOO_MANY_VALUES", e.getMessage());
        }
    }

    
}