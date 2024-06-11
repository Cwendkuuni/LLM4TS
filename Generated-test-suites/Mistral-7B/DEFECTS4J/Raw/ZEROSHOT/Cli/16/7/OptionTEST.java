import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.BasicParser;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new DefaultWriteableCommandLine();
        option = OptionBuilder.withLongOpt("test")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
    }

    @Test
    public void testProcess() throws OptionException {
        option.process(commandLine, commandLine.iterator());
        assertEquals("test", commandLine.getOptionValue("test"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArgument() throws OptionException {
        option.process(commandLine, commandLine.iterator());
        commandLine.addOption("test", true);
        option.process(commandLine, commandLine.iterator());
        assertFalse(commandLine.hasOption("test"));
        commandLine.getArgument(0);
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValue("test"));
    }

    @Test
    public void testCanProcess() {
        assertTrue(option.canProcess(commandLine, "test"));
        assertFalse(option.canProcess(commandLine, "arg"));
    }

    @Test
    public void testCanProcessWithArguments() {
        List<String> arguments = Arrays.asList("test", "arg1", "arg2");
        commandLine.setArgs(arguments);
        commandLine.setArgIterator(commandLine.iterator());

        assertTrue(option.canProcess(commandLine, commandLine.iterator()));
        assertEquals("arg1", commandLine.getOptionValue("test"));
        assertEquals("arg2", commandLine.getArgument(1));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        assertNotNull(triggers);
        assertSame(triggers.getClass(), HashSet.class);
        assertTrue(triggers.contains("test"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        assertNotNull(prefixes);
        assertSame(prefixes.getClass(), HashSet.class);
        assertTrue(prefixes.contains("--"));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() throws OptionException {
        commandLine.addOption("test", true);
        option.validate(commandLine);
        commandLine.setArg(0, "invalid");
        option.validate(commandLine);
    }

    @Test
    public void testValidateWithValidCommandLine() throws OptionException {
        commandLine.addOption("test", true);
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List<Option> options = new ArrayList<>();
        options.add(option);
        List<Option> sortedOptions = Collections.singletonList(option);
        Comparator<Option> comparator = (o1, o2) -> o1.getId() - o2.getId();

        List<HelpLine> helpLines = option.helpLines(1, Collections.emptySet(), comparator);
        assertNotNull(helpLines);
        assertSame(helpLines.getClass(), ArrayList.class);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertNotNull(helpLine);
        assertSame(helpLine.getClass(), HelpLineImpl.class);
        assertEquals("-test, --test\n", helpLine.getShortOptionString());
        assertEquals("Test Option\n", helpLine.getLongOptionString());
        assertEquals("arg\n", helpLine.getArgString());
        assertEquals("", helpLine.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("--test ARG\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("test", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test Option", option.getDescription());
    }

    @Test
    public void testGetId() {
        assertEquals(0, option.getId());
    }

    @Test
    public void testFindOption() {
        Option testOption = option;
        Option testOption2 = OptionBuilder.withLongOpt("test2")
                .withDescription("Test Option 2")
                .create();
        commandLine.addOption(testOption);
        commandLine.addOption(testOption2);

        Option foundOption = option.findOption("test");
        assertSame(foundOption, testOption);

        foundOption = option.findOption("test2");
        assertSame(foundOption, testOption2);

        foundOption = option.findOption("nonexistent");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());

        Option requiredOption = OptionBuilder.withLongOpt("required")
                .isRequired(true)
                .create();

        assertTrue(requiredOption.isRequired());
    }

    @Test
    public void testGetParent() {
        Option groupOption = OptionBuilder.withLongOpt("group")
                .isRequired(true)
                .create();

        option.setParent(groupOption);

        Option parentOption = option.getParent();
        assertSame(parentOption, groupOption);
    }
}