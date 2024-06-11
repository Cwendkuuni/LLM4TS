import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class OptionTest {

    @Test
    public void testProcess() throws OptionException {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = new LinkedList<>(Arrays.asList("test", "arg1", "arg2"));
        option.process(commandLine, args.listIterator());

        Assert.assertEquals("test", commandLine.getOptions().get(0).getPreferredName());
        Assert.assertEquals("arg1", commandLine.getArgs().get(0));
        Assert.assertEquals("arg2", commandLine.getArgs().get(1));
    }

    @Test
    public void testDefaults() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        option.defaults(commandLine);

        // Check if defaults have been set
        // Assuming that the MockOption has some default values
        Assert.assertEquals("defaultValue1", commandLine.getString("option1"));
        Assert.assertEquals("defaultValue2", commandLine.getString("option2"));
    }

    @Test
    public void testCanProcess() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test canProcess with single argument
        Assert.assertTrue(option.canProcess(commandLine, Arrays.asList("test").listIterator()));

        // Test canProcess with multiple arguments
        List<String> args = new LinkedList<>(Arrays.asList("test", "arg1", "arg2"));
        Assert.assertTrue(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption();
        Set<String> triggers = option.getTriggers();

        Assert.assertNotNull(triggers);
        Assert.assertEquals(2, triggers.size());
        Assert.assertTrue(triggers.contains("--option"));
        Assert.assertTrue(triggers.contains("-o"));
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption();
        Set<String> prefixes = option.getPrefixes();

        Assert.assertNotNull(prefixes);
        Assert.assertEquals(1, prefixes.size());
        Assert.assertTrue(prefixes.contains("-"));
    }

    @Test(expected = OptionException.class)
    public void testInvalidCommandLine() throws OptionException {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = new LinkedList<>(Arrays.asList("test", "invalid"));

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption();
        List<HelpLine> helpLines = option.helpLines(1, new HashSet(), null);

        Assert.assertNotNull(helpLines);
        Assert.assertEquals(2, helpLines.size());
        // Check the first HelpLine
        Assert.assertEquals("--option <value> ...", helpLines.get(0).getUsage());
        Assert.assertEquals("option description", helpLines.get(0).getShortDescription());
        // Check the second HelpLine
        Assert.assertEquals("-o <value> ...", helpLines.get(1).getUsage());
        Assert.assertEquals("option description", helpLines.get(1).getShortDescription());
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption();
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<Option> comp = null;

        option.appendUsage(buffer, helpSettings, comp);

        // The usage string should be appended to the buffer
        Assert.assertEquals("--option <value> ...\n" +
                "-o <value> ...\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption();
        String preferredName = option.getPreferredName();

        Assert.assertEquals("option", preferredName);
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption();
        String description = option.getDescription();

        Assert.assertEquals("option description", description);
    }

    @Test
    public void testGetId() {
        Option option = new MockOption();
        int id = option.getId();

        Assert.assertEquals(1, id);
    }

    @Test
    public void testFindOption() {
        Option parentOption = new MockOption();
        Option childOption = new MockOption();
        parentOption.setParent(parentOption);
        Option foundOption = parentOption.findOption("childOption");

        Assert.assertEquals(childOption, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption();
        boolean isRequired = option.isRequired();

        Assert.assertFalse(isRequired);
    }

    @Test
    public void testFindParent() {
        Option childOption = new MockOption();
        Option parentOption = new MockOption();
        childOption.setParent(parentOption);

        Option foundParent = childOption.getParent();

        Assert.assertEquals(parentOption, foundParent);
    }

    private static class MockOption implements Option {
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
        public Set<String> getTriggers() {
            return new HashSet<>(Arrays.asList("--option", "-o"));
        }

        @Override
        public Set<String> getPrefixes() {
            return new HashSet<>(Arrays.asList("-"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {

        }

        @Override
        public List<HelpLine> helpLines(int depth, Set<?> helpSettings, Comparator<?> comp) {
            return null;
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set<?> helpSettings, Comparator<?> comp) {

        }

        @Override
        public String getPreferredName() {
            return "option";
        }

        @Override
        public String getDescription() {
            return "option description";
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
            return false;
        }

        @Override
        public Option setParent(Option parent) {
            this.parent = parent;
            return this;
        }

        private Option parent;
    }
}