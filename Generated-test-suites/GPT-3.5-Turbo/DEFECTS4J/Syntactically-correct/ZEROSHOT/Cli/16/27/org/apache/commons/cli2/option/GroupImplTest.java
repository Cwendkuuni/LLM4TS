import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private OptionImpl option1;
    private OptionImpl option2;
    private OptionImpl option3;
    private ArgumentImpl argument1;
    private ArgumentImpl argument2;
    private GroupImpl group;

    @Before
    public void setUp() {
        option1 = new OptionImpl(1, true);
        option2 = new OptionImpl(2, false);
        option3 = new OptionImpl(3, true);
        argument1 = new ArgumentImpl(4);
        argument2 = new ArgumentImpl(5);

        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument1);
        arguments.add(argument2);

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        group = new GroupImpl(arguments, "MyGroup", "This is a group", 1, 3);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);

        assertFalse(group.canProcess(commandLine, null));

        assertTrue(group.canProcess(commandLine, "input"));
        assertTrue(group.canProcess(commandLine, "-f"));
        assertTrue(group.canProcess(commandLine, "--option"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(0, group.getPrefixes().size());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(0, group.getTriggers().size());
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = new ArrayList<String>().listIterator();
        arguments.add("-f");

        group.process(commandLine, arguments);

        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingOption() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithUnexpectedToken() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("MyGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("This is a group", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option3));
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(2, group.getAnonymous().size());
        assertTrue(group.getAnonymous().contains(argument1));
        assertTrue(group.getAnonymous().contains(argument2));
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("option1");

        assertEquals(option1, foundOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        group.defaults(commandLine);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }
}