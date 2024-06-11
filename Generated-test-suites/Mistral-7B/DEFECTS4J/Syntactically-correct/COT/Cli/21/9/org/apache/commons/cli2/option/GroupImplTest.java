import static org.junit.Assert.*;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.GroupImpl;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testConstructor() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        options.add(new OptionImpl("--option2", "Option 2", false, "o2"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        assertEquals("My Group", group.getPreferredName());
        assertEquals("Description of My Group", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.unmodifiableSet(Collections.singleton("o1")), group.getTriggers());
        assertFalse(group.isRequired());
    }

    @Test
    public void testCanProcessWithValidArgument() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("--option1");

        assertTrue(group.canProcess(commandLine, "--option1"));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("--invalid-argument");

        assertFalse(group.canProcess(commandLine, "--invalid-argument"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArgument() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = commandLine.getArguments().listIterator();
        arguments.next();
        arguments.next();
        arguments.next();

        group.process(commandLine, arguments);
    }

    @Test
    public void testValidateWithValidCommandLine() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        options.add(new OptionImpl("--option2", "Option 2", false, "o2"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("--option1");
        commandLine.parseArgument("--option2");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithFewerOptionsThanMinimum() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 2, 2, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("--option1");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMoreOptionsThanMaximum() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        options.add(new OptionImpl("--option2", "Option 2", false, "o2"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("--option1");
        commandLine.parseArgument("--option1");

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        options.add(new OptionImpl("--option2", "Option 2", false, "o2"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, null, null);

        String expected = "--group My Group\n" +
                "Description of My Group\n" +
                "--option1 <o1>\n" +
                "--option2\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        options.add(new OptionImpl("--option2", "Option 2", false, "o2"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        List helpLines = group.helpLines(0, null, null);

        List expected = Arrays.asList(
                "--group My Group\n" +
                        "Description of My Group\n",
                "--group My Group\n" +
                        "Description of My Group\n" +
                        "OPTIONS:\n" +
                        "--option1 <o1>\n" +
                        "        Option 1\n",
                "--group My Group\n" +
                        "Description of My Group\n" +
                        "OPTIONS:\n" +
                        "--option2\n" +
                        "        Option 2\n");

        assertEquals(expected, helpLines);
    }

    @Test
    public void testFindOption() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        options.add(new OptionImpl("--option2", "Option 2", false, "o2"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        Option found = group.findOption("o1");

        assertNotNull(found);
        assertEquals("--option1", found.getLongOpt());
    }

    @Test
    public void testDefaults() {
        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        GroupImpl group = new GroupImpl(options, "My Group", "Description of My Group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        group.defaults(commandLine);

        assertFalse(commandLine.hasOption("--option1"));
    }

    @Test
    public void testLooksLikeOptionWithValidArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("--option1");

        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        GroupImpl group = new GroupImpl(options, null, null, 1, 1, false);

        assertTrue(group.looksLikeOption(commandLine, "--option1"));
    }

    @Test
    public void testLooksLikeOptionWithInvalidArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("--invalid-argument");

        List options = new ArrayList();
        options.add(new OptionImpl("--option1", "Option 1", true, "o1"));
        GroupImpl group = new GroupImpl(options, null, null, 1, 1, false);

        assertFalse(group.looksLikeOption(commandLine, "--invalid-argument"));
    }

    @Test
    public void testReverseStringComparator() {
        Comparator comparator = ReverseStringComparator.getInstance();

        List list = new ArrayList();
        list.add("B");
        list.add("A");

        int comparisonResult = comparator.compare(list.get(0), list.get(1));

        assertEquals(-1, comparisonResult);
    }
}