import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    private static final String ARG_1 = "arg1";
    private static final String ARG_2 = "arg2";
    private static final String ARG_3 = "arg3";
    private static final String OPTION_1 = "--option1";
    private static final String OPTION_2 = "--option2";

    @Test
    public void testProcess() throws Exception {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        List<String> args = Arrays.asList(ARG_1, ARG_2, OPTION_1, ARG_3).listIterator();

        option.process(commandLine, args);

        Assert.assertEquals(ARG_1, commandLine.getOptionValue(OPTION_1));
        Assert.assertNull(commandLine.getOptionValue(OPTION_2));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithError() throws Exception {
        Option option = new MockOptionWithError();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        List<String> args = Arrays.asList(ARG_1, ARG_2, OPTION_1, ARG_3).listIterator();

        option.process(commandLine, args);
    }

    @Test
    public void testCanProcess() {
        Option option = new MockOption();

        Assert.assertTrue(option.canProcess(new DefaultCommandLine(), OPTION_1));
        Assert.assertFalse(option.canProcess(new DefaultCommandLine(), ARG_1));

        List<String> args = Arrays.asList(ARG_1, ARG_2).listIterator();

        Assert.assertTrue(option.canProcess(new DefaultCommandLine(), args));
        Assert.assertFalse(option.canProcess(new DefaultCommandLine(), args));
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption();

        Set<String> triggers = option.getTriggers();
        Assert.assertNotNull(triggers);
        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals(OPTION_1, triggers.iterator().next());
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption();

        Set<String> prefixes = option.getPrefixes();
        Assert.assertNotNull(prefixes);
        Assert.assertEquals(1, prefixes.size());
        Assert.assertEquals("--", prefixes.iterator().next());
    }

    @Test
    public void testValidate() throws Exception {
        Option option = new MockValidOption();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        List<String> args = Arrays.asList(ARG_1, ARG_2, OPTION_1, ARG_3).listIterator();

        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithError() throws Exception {
        Option option = new MockInvalidOption();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        List<String> args = Arrays.asList(ARG_1, ARG_2, OPTION_1, ARG_3).listIterator();

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption();

        int depth = 1;
        Set<String> helpSettings = new HashSet<>();
        Comparator<Option> comp = Collections.reverseOrder();

        List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);

        Assert.assertNotNull(helpLines);
        Assert.assertEquals(1, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        Assert.assertNotNull(helpLine.getShortOption());
        Assert.assertNotNull(helpLine.getLongOption());
        Assert.assertNotNull(helpLine.getDescription());
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption();

        StringBuffer buffer = new StringBuffer();
        Set<String> helpSettings = new HashSet<>();
        Comparator<Option> comp = Collections.reverseOrder();

        option.appendUsage(buffer, helpSettings, comp);

        Assert.assertNotNull(buffer.toString());
    }

    @Test
    public void testFindOption() {
        Option option1 = new MockOption();
        Option option2 = new MockOption();

        Option parentOption = new Options();
        parentOption.addOption(option1);
        parentOption.addOption(option2);

        Option foundOption = parentOption.findOption(OPTION_1);

        Assert.assertNotNull(foundOption);
        Assert.assertEquals(option1, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option1 = new MockOption();
        Option option2 = new MockRequiredOption();

        Assert.assertFalse(option1.isRequired());
        Assert.assertTrue(option2.isRequired());
    }

    private static class MockOption extends AbstractOption {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            super.process(commandLine, args);
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            super.defaults(commandLine);
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return super.canProcess(commandLine, argument);
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            return super.canProcess(commandLine, arguments);
        }

        @Override
        public Set getTriggers() {
            return super.getTriggers();
        }

        @Override
        public Set getPrefixes() {
            return super.getPrefixes();
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            super.validate(commandLine);
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator<Option> comp) {
            return super.helpLines(depth, helpSettings, comp);
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator<Option> comp) {
            super.appendUsage(buffer, helpSettings, comp);
        }

        @Override
        public String getPreferredName() {
            return super.getPreferredName();
        }

        @Override
        public String getDescription() {
            return super.getDescription();
        }

        @Override
        public int getId() {
            return super.getId();
        }

        @Override
        public Option findOption(String trigger) {
            return super.findOption(trigger);
        }

        @Override
        public boolean isRequired() {
            return super.isRequired();
        }

        @Override
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
            // Do nothing
        }
    }

    private static class MockOptionWithError extends MockOption {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            throw new OptionException("Test error message");
        }
    }

    private static class MockValidOption extends MockOption {
        // Empty implementation
    }

    private static class MockInvalidOption extends MockOption {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            super.process(commandLine, args);
            throw new OptionException("Test error message");
        }
    }

    private static class MockRequiredOption extends MockOption {
        @Override
        public boolean isRequired() {
            return true;
        }
    }
}