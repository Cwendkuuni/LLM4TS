import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    @Test
    public void testProcess() throws OptionException {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "value").listIterator();

        option.process(commandLine, args);

        assertEquals("value", commandLine.getOptionValue("option"));
    }

    @Test
    public void testDefaults() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.defaults(commandLine);

        assertNull(commandLine.getOptionValue("option"));
    }

    @Test
    public void testCanProcess_String() {
        Option option = new MockOption();

        assertTrue(option.canProcess(new WriteableCommandLine(), "--option value"));
    }

    @Test
    public void testCanProcess_ListIterator() {
        Option option = new MockOption();

        List<String> args = Arrays.asList("--option", "value");
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> iterator = args.listIterator();

        assertTrue(option.canProcess(commandLine, iterator));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_InvalidArgument() {
        Option option = new MockOption();

        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> iterator = Arrays.asList("--invalid").listIterator();

        option.canProcess(commandLine, iterator);
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption();

        Set<String> triggers = option.getTriggers();

        assertEquals(new HashSet<>(Arrays.asList("option")), triggers);
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption();

        Set<String> prefixes = option.getPrefixes();

        assertEquals(new LinkedHashSet<>(Arrays.asList("--")), prefixes);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidCommandLine() throws OptionException {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.validate(commandLine);
    }

    @Test
    public void testValidate_ValidCommandLine() throws OptionException {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.putOptionValue("option", "value");

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption();
        int depth = 0;
        Set helpSettings = new LinkedHashSet<>();
        Comparator comp = null;

        List<Option.HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);

        assertEquals(1, helpLines.size());
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption();
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new LinkedHashSet<>();
        Comparator comp = null;

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals("", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption();

        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption();

        assertEquals("This is a description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new MockOption();

        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption_NullTrigger() {
        Option option = new MockOption();

        assertNull(option.findOption(null));
    }

    @Test
    public void testFindOption_MatchedOption() {
        Option parentOption = new MockOption();
        Option childOption = new MockOption();
        parentOption.setParent(childOption);

        Option option = new MockOption();

        Option matchedOption = option.findOption("childOption");

        assertEquals(childOption, matchedOption);
    }

    @Test
    public void testFindOption_UnmatchedOption() {
        Option option = new MockOption();

        Option unmatchedOption = option.findOption("unmatchedOption");

        assertNull(unmatchedOption);
    }

    @Test
    public void testIsRequired_RequiredOption() {
        Option option = new MockOption();
        option.setRequired(true);

        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequired_NotRequiredOption() {
        Option option = new MockOption();

        assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent_NoParent() {
        Option option = new MockOption();

        assertNull(option.getParent());
    }

    @Test
    public void testGetParent_ParentOption() {
        Option parentOption = new MockOption();
        Option childOption = new MockOption();
        parentOption.setParent(childOption);

        Option option = new MockOption();

        assertEquals(childOption, option.getParent());
    }
}

class MockOption implements Option {
    private boolean required = false;
    private Option parent;

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return true;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
        return true;
    }

    @Override
    public Set getTriggers() {
        return new LinkedHashSet<>();
    }

    @Override
    public Set getPrefixes() {
        return new LinkedHashSet<>();
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
    }

    @Override
    public List helpLines(int depth, Set helpSettings, Comparator comp) {
        return null;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
    }

    @Override
    public String getPreferredName() {
        return "option";
    }

    @Override
    public String getDescription() {
        return "This is a description";
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public Option findOption(String trigger) {
        return null;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public Option getParent() {
        return parent;
    }

    @Override
    public void setParent(Option parent) {
        this.parent = parent;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }
}