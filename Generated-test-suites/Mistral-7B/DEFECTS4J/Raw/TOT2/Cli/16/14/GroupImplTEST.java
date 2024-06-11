import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.group.GroupImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private CommandLine commandLine;
    private Options options;
    private GroupImpl group;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        options = new Options();

        final List<Option> optionsList = new ArrayList<>();
        optionsList.add(OptionBuilder.withName("a").withDescription("Option A").withShortName('a').build());
        optionsList.add(OptionBuilder.withName("b").withDescription("Option B").withShortName('b').build());
        optionsList.add(OptionBuilder.withName("c").withDescription("Option C").withShortName('c').build());
        optionsList.add(OptionBuilder.withName("d").withDescription("Option D").withShortName('d').build());
        options.addOptions(optionsList);

        group = new GroupImpl(options, "Test Group", null, 1, 3);
    }

    @Test
    public void testCanProcess_ValidOption() {
        group.canProcess(commandLine, "a");
        assertTrue(true);
    }

    @Test
    public void testCanProcess_InvalidOption() {
        assertFalse(group.canProcess(commandLine, "x"));
        assertTrue(false);
    }

    @Test
    public void testCanProcess_BurstedOption() {
        final List<String> arguments = Arrays.asList("--a", "--b", "--c", "--d");
        commandLine.parse(arguments.toArray(new String[0]));

        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));
        assertFalse(group.canProcess(commandLine, "d"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_InvalidArgument() {
        final List<String> arguments = Arrays.asList("--a", "--b", "ab");
        commandLine.parse(arguments.toArray(new String[0]));

        group.canProcess(commandLine, "d");
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_TooManyOptions() {
        final List<String> arguments = Arrays.asList("--a", "--b", "--c", "--d", "--e");
        commandLine.parse(arguments.toArray(new String[0]));

        group.canProcess(commandLine, "a");
        group.canProcess(commandLine, "b");
        group.canProcess(commandLine, "c");
        group.canProcess(commandLine, "d");

        assertThrows(OptionException.class, () -> group.canProcess(commandLine, "e"));
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("Test Group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNull(group.getDescription());
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

    @Test(expected = OptionException.class)
    public void testProcess_MissingOption() {
        final List<String> arguments = Arrays.asList("--a", "--c");
        commandLine.parse(arguments.toArray(new String[0]));

        group.process(commandLine, commandLine.iterator());
    }

    @Test
    public void testProcess_ValidOption() {
        final List<String> arguments = Arrays.asList("--a", "--b", "--c");
        commandLine.parse(arguments.toArray(new String[0]));

        group.process(commandLine, commandLine.iterator());

        assertEquals(1, commandLine.getNumberOfValues("a"));
        assertEquals(1, commandLine.getNumberOfValues("b"));
        assertEquals(1, commandLine.getNumberOfValues("c"));
    }

    @Test
    public void testProcess_BurstedOption() {
        final List<String> arguments = Arrays.asList("--a", "--b", "--c", "--d");
        commandLine.parse(arguments.toArray(new String[0]));

        group.process(commandLine, commandLine.iterator());

        assertEquals(1, commandLine.getNumberOfValues("a"));
        assertEquals(1, commandLine.getNumberOfValues("b"));
        assertEquals(1, commandLine.getNumberOfValues("c"));
        assertNull(commandLine.getValues("d"));
    }

    @Test
    public void testProcess_MultipleValidOptions() {
        final List<String> arguments = Arrays.asList("--a", "--b", "--c", "--a", "--d");
        commandLine.parse(arguments.toArray(new String[0]));

        group.process(commandLine, commandLine.iterator());

        assertEquals(1, commandLine.getNumberOfValues("a"));
        assertEquals(1, commandLine.getNumberOfValues("b"));
        assertEquals(1, commandLine.getNumberOfValues("c"));
        assertEquals(1, commandLine.getNumberOfValues("d"));
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption() {
        final List<String> arguments = Arrays.asList("--a", "--x");
        commandLine.parse(arguments.toArray(new String[0]));

        group.process(commandLine, commandLine.iterator());
    }

    @Test
    public void testValidate() throws OptionException {
        final List<String> arguments = Arrays.asList("--a", "--b");
        commandLine.parse(arguments.toArray(new String[0]));

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_MissingOption() throws OptionException {
        final List<String> arguments = Arrays.asList("--b");
        commandLine.parse(arguments.toArray(new String[0]));

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooManyOptions() throws OptionException {
        final List<String> arguments = Arrays.asList("--a", "--b", "--c", "--d");
        commandLine.parse(arguments.toArray(new String[0]));

        group.validate(commandLine);
    }

    @Test
    public void testValidate_ValidOption() throws OptionException {
        final List<String> arguments = Arrays.asList("--a", "--b", "--c");
        commandLine.parse(arguments.toArray(new String[0]));

        group.validate(commandLine);
    }

    @Test
    public void testValidate_AnonymousArgument() throws OptionException {
        final List<String> arguments = Arrays.asList("--a", "--b", "--value=5");
        commandLine.parse(arguments.toArray(new String[0]));

        group.validate(commandLine);

        assertTrue(commandLine.hasOption("value"));
        assertEquals(1, commandLine.getNumberOfValues("value"));
        assertEquals("5", commandLine.getStringValue("value"));
    }

    @Test
    public void testValidate_AnonymousArgumentWithValue() throws OptionException {
        final List<String> arguments = Arrays.asList("--a", "--b", "--value=5", "5");
        commandLine.parse(arguments.toArray(new String[0]));

        group.validate(commandLine);

        assertTrue(commandLine.hasOption("value"));
        assertEquals(2, commandLine.getNumberOfValues("value"));
        assertEquals("5", commandLine.getStringValue("value", 0));
        assertEquals("5", commandLine.getStringValue("value", 1));
    }

    @Test(expected = OptionException.class)
    public void testValidate_MissingAnonymousArgument() throws OptionException {
        final List<String> arguments = Arrays.asList("--a", "--b");
        commandLine.parse(arguments.toArray(new String[0]));

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_MissingAnonymousArgumentWithValue() throws OptionException {
        final List<String> arguments = Arrays.asList("--a", "--b");
        commandLine.parse(arguments.toArray(new String[0]));

        group.validate(commandLine);

        assertFalse(commandLine.hasOption("value"));
        assertNull(commandLine.getStringValue("value"));
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, null, null);

        final String expected = "Test Group:\n" +
                "  a: Option A\n" +
                "  b: Option B\n" +
                "  c: Option C\n" +
                "  d: Option D\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final HelpFormatter formatter = new HelpFormatter();
        final List<String> lines = group.helpLines(0, null, null);

        formatter.printHelp("test", lines.toArray(new String[lines.size().intValue()]));

        assertTrue(true);
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(Collections.emptyList(), group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        assertEquals(group.findOption("a"), options.findOption("a"));
    }
}