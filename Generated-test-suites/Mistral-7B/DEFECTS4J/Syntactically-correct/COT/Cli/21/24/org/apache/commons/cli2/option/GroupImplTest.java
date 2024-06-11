import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImplConstructor() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("opt2", "Option 2", true, "o2"));
        options.add(new Option("opt3", "Option 3", true, "o3"));
        options.add(new Option("arg1", "Argument 1", true));

        GroupImpl group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 2, 3, false);

        assertEquals("MyGroup", group.getName());
        assertEquals("Description of MyGroup", group.getDescription());
        assertEquals(2, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertEquals(3, group.getOptions().size());
        assertEquals(1, group.getAnonymous().size());

        assertFalse(group.isRequired());
    }

    @Test
    public void testCanProcess_NoOptions() {
        List options = new ArrayList();
        GroupImpl group = new GroupImpl(options, null, null, 0, 0, false);

        assertFalse(group.canProcess(null, ""));
    }

    @Test
    public void testCanProcess_ValidOption() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parseOption("o1");
        assertTrue(group.canProcess(commandLine, "o1"));
    }

    @Test
    public void testCanProcess_BurstsToOption() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("opt2", "Option 2", true, "o2"));
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parseOption("o1");
        commandLine.parseOption("o1");
        assertTrue(group.canProcess(commandLine, "o1"));

        assertFalse(group.canProcess(commandLine, "o2"));
    }

    @Test
    public void testCanProcess_ValidAnonymousArgument() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("arg1", "Argument 1", true));
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parseArgument(new ArgumentType(String.class), "arg1value");

        assertTrue(group.canProcess(commandLine, "arg1value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcess_InvalidArgument() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parseOption("invalid");

        assertFalse(group.canProcess(commandLine, "invalid"));
    }

    @Test
    public void testGetPrefixes() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("opt2", "Option 2", true, "o2"));
        options.add(new Option("opt3", "Option 3", true, "o3"));
        options.add(new Option("arg1", "Argument 1", true));
        GroupImpl group = new GroupImpl(options, null, null, 2, 3, false);

        Set prefixes = group.getPrefixes();

        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("opt2", "Option 2", true, "o2"));
        options.add(new Option("opt3", "Option 3", true, "o3"));
        options.add(new Option("arg1", "Argument 1", true));
        GroupImpl group = new GroupImpl(options, null, null, 2, 3, false);

        Set triggers = group.getTriggers();

        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("o1"));
        assertTrue(triggers.contains("o2"));
        assertTrue(triggers.contains("o3"));
    }

    @Test
    public void testProcess_NoOptions() {
        List options = new ArrayList();
        GroupImpl group = new GroupImpl(options, null, null, 0, 0, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = commandLine.getParsed();

        group.process(commandLine, arguments);

        assertEquals(0, arguments.nextIndex());
    }

    @Test
    public void testProcess_ValidOption() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseOption("o1");

        ListIterator arguments = commandLine.getParsed();

        group.process(commandLine, arguments);

        assertEquals(1, arguments.nextIndex());
    }

    @Test
    public void testProcess_BurstsToOption() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("opt2", "Option 2", true, "o2"));
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseOption("o1");
        commandLine.parseOption("o1");

        ListIterator arguments = commandLine.getParsed();

        group.process(commandLine, arguments);

        assertEquals(2, arguments.nextIndex());

        assertFalse(group.canProcess(commandLine, "o2"));
    }

    @Test
    public void testProcess_ValidAnonymousArgument() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("arg1", "Argument 1", true));
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument(new ArgumentType(String.class), "arg1value");

        ListIterator arguments = commandLine.getParsed();

        group.process(commandLine, arguments);

        assertEquals(1, arguments.nextIndex());
    }

    @Test
    public void testValidate_ValidOptions() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("opt2", "Option 2", true, "o2"));
        GroupImpl group = new GroupImpl(options, null, null, 2, 3, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseOption("o1");
        commandLine.parseOption("o1");

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("opt2", "Option 2", true, "o2"));
        GroupImpl group = new GroupImpl(options, null, null, 2, 3, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseOption("o1");
        commandLine.parseOption("o1");
        commandLine.parseOption("o2");

        try {
            group.validate(commandLine);
            fail("Expected OptionException but was none.");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testValidate_ValidAnonymousArgument() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("arg1", "Argument 1", true));
        GroupImpl group = new GroupImpl(options, null, null, 2, 3, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument(new ArgumentType(String.class), "arg1value");
        commandLine.parseArgument(new ArgumentType(String.class), "arg1value2");

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidAnonymousArgument() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "o1"));
        options.add(new Option("arg1", "Argument 1", true));
        GroupImpl group = new GroupImpl(options, null, null, 2, 3, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument(new ArgumentType(String.class), "arg1value");
        commandLine.parseOption("o1");

        try {
            group.validate(commandLine);
            fail("Expected OptionException but was none.");
        } catch (OptionException e) {
            // expected
        }
    }
}