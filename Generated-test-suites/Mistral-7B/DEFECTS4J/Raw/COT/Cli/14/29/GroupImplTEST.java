import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private List<Option> options;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        options = new ArrayList<>();
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcessWithValidOption() throws OptionException {
        Option option = new Option("o", "option", true, "Description");
        Group group = new GroupImpl(options, null, null, 1, 1);
        group.addOption(option);

        assertTrue(group.canProcess(commandLine, "o"));
    }

    @Test
    public void testCanProcessWithInvalidOption() {
        Option option = new Option("o", "option", true, "Description");
        Group group = new GroupImpl(options, null, null, 1, 1);
        group.addOption(option);

        assertFalse(group.canProcess(commandLine, "invalidOption"));
    }

    @Test
    public void testCanProcessWithBurstingOption() throws OptionException {
        Option option1 = new Option("o1", "option1", true, "Description1");
        Option option2 = new Option("o2", "option2", true, "Description2");
        Group group = new GroupImpl(options, null, null, 1, 1);
        group.addOption(option1);
        group.addOption(option2);

        assertTrue(group.canProcess(commandLine, "o1"));
        assertTrue(group.canProcess(commandLine, "o2"));
        assertTrue(group.canProcess(commandLine, "o1 o2"));

        assertFalse(group.canProcess(commandLine, "o1 o2 o1"));
        assertFalse(group.canProcess(commandLine, "o1 o2 o1 o1"));
    }

    @Test
    public void testCanProcessWithArgument() throws OptionException {
        Argument argument = new Argument("arg", true, "Argument");
        Group group = new GroupImpl(options, null, null, 1, 1);
        group.addOption(new Option("o", "option", true, null));
        group.addArgument(argument);

        assertTrue(group.canProcess(commandLine, "o"));
        assertTrue(group.canProcess(commandLine, "o arg"));

        assertFalse(group.canProcess(commandLine, "arg"));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() throws OptionException {
        Option option = new Option("o", "option", true, "Description");
        Group group = new GroupImpl(options, null, null, 1, 1);
        group.addOption(option);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() throws OptionException {
        Option option = new Option("o", "option", true, "Description");
        Group group = new GroupImpl(options, null, null, 1, 2);
        group.addOption(option);
        commandLine.parse(new String[]{"--option", "--option"});

        group.validate(commandLine);
    }

    @Test
    public void testProcessWithValidOptions() throws OptionException {
        Option option1 = new Option("o1", "option1", true, "Description1");
        Option option2 = new Option("o2", "option2", true, "Description2");
        Group group = new GroupImpl(options, null, null, 1, 1);
        group.addOption(option1);
        group.addOption(option2);

        commandLine.parse(new String[]{"--option1", "--option2"});

        group.process(commandLine, commandLine.iterator());

        assertTrue(commandLine.hasOption("o1"));
        assertTrue(commandLine.hasOption("o2"));
    }

    @Test
    public void testProcessWithBurstingOption() throws OptionException {
        Option option1 = new Option("o1", "option1", true, "Description1");
        Option option2 = new Option("o2", "option2", true, "Description2");
        Group group = new GroupImpl(options, null, null, 1, 1);
        group.addOption(option1);
        group.addOption(option2);

        commandLine.parse(new String[]{"--option1", "--option1", "--option2"});

        group.process(commandLine, commandLine.iterator());

        assertTrue(commandLine.hasOption("o1"));
        assertTrue(commandLine.hasOption("o2"));
    }

    @Test
    public void testProcessWithArgument() throws OptionException {
        Argument argument = new Argument("arg", true, "Argument");
        Option option = new Option("o", "option", true, null);
        Group group = new GroupImpl(options, null, null, 1, 1);
        group.addOption(option);
        group.addArgument(argument);

        commandLine.parse(new String[]{"--option", "arg"});

        group.process(commandLine, commandLine.iterator());

        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.hasOption("arg"));
    }
}