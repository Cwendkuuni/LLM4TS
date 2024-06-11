import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.system.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = Arrays.asList("--option", "value").listIterator();
        Option option = new DummyOption();
        option.process(commandLine, args);
        Assert.assertEquals("value", commandLine.getOptionValues("option")[0]);
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidArgument() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = Arrays.asList("--option", "invalid").listIterator();
        Option option = new DummyOption();
        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new DummyOption();
        option.defaults(commandLine);
        Assert.assertNull(commandLine.getOptionValues("option"));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new DummyOption();
        List<String> args = Arrays.asList("--option").listIterator();
        Assert.assertTrue(option.canProcess(commandLine, args));
        args.next();
        Assert.assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new DummyOption();
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new DummyInvalidOption();
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new DummyOption();
        List<HelpLine> helpLines = option.helpLines(0, new LinkedHashSet<>(), null);
        Assert.assertEquals(1, helpLines.size());
        String expectedHelp = "  --option\n" +
                "  optional option\n";
        Assert.assertEquals(expectedHelp, helpLines.get(0).getHelp());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Option option = new DummyOption();
        option.appendUsage(buffer, new LinkedHashSet<>(), null);
        String expectedUsage = "  --option\n" +
                "  optional option\n";
        Assert.assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new DummyOption();
        Assert.assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new DummyOption();
        Assert.assertEquals("optional option", option.getDescription());
    }

    @Test
    public void testgetId() {
        Option option = new DummyOption();
        Assert.assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option1 = new DummyOption();
        Option option2 = new DummyOption();
        Option option3 = new DummyOption();
        Option[] options = {option1, option2, option3};
        Option foundOption = option1.findOption("option1");
        Assert.assertEquals(option1, foundOption);
        foundOption = option1.findOption("option2");
        Assert.assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option1 = new DummyOption();
        Option option2 = new DummyRequiredOption();
        Assert.assertFalse(option1.isRequired());
        Assert.assertTrue(option2.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = new DummyParentOption();
        Option childOption = new DummyOption();
        childOption.setParent(parentOption);
        Assert.assertEquals(parentOption, childOption.getParent());
        Assert.assertNull(parentOption.getParent());
    }

    private static class DummyOption implements Option {

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
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
            return new LinkedHashSet(Arrays.asList("option"));
        }

        @Override
        public Set getPrefixes() {
            return new LinkedHashSet(Arrays.asList("--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
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
            return "optional option";
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
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
        }
    }

    private static class DummyInvalidOption implements Option {

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
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
            return new LinkedHashSet(Arrays.asList("invalidOption"));
        }

        @Override
        public Set getPrefixes() {
            return new LinkedHashSet(Arrays.asList("--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            throw new OptionException("Invalid option: invalidOption");
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        }

        @Override
        public String getPreferredName() {
            return "invalidOption";
        }

        @Override
        public String getDescription() {
            return "invalid option";
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
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
        }
    }

    private static class DummyParentOption implements Option {

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
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
            return new LinkedHashSet(Arrays.asList("parentOption"));
        }

        @Override
        public Set getPrefixes() {
            return new LinkedHashSet(Arrays.asList("--parent"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        }

        @Override
        public String getPreferredName() {
            return "parentOption";
        }

        @Override
        public String getDescription() {
            return "parent option";
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
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
        }
    }

    private static class DummyRequiredOption implements Option {

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
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
            return new LinkedHashSet(Arrays.asList("requiredOption"));
        }

        @Override
        public Set getPrefixes() {
            return new LinkedHashSet(Arrays.asList("--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            if (commandLine.getOptionValues("requiredOption").isEmpty()) {
                throw new OptionException("Required option: requiredOption is missing");
            }
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        }

        @Override
        public String getPreferredName() {
            return "requiredOption";
        }

        @Override
        public String getDescription() {
            return "required option";
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
            return true;
        }

        @Override
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
        }
    }

    @Test
    public void testOptionParsing() {
        String[] args = {"--option", "value"};
        WriteableCommandLine commandLine = new DefaultParser().parse(OptionTest.class.getProtectedField("dummyOptionType"), args);
        Option option = OptionTest.class.getProtectedField("dummyOption");
        option.process(commandLine, Arrays.asList(args.clone()).listIterator());
        Assert.assertEquals(1, commandLine.getArgList().size());
        Assert.assertEquals("value", commandLine.getArgList().get(0));
    }

    private static Option dummyOption;
    private static Class dummyOptionType;

    static {
        try {
            dummyOptionType = Class.forName("OptionTest$DummyOptionType");
            dummyOption = dummyOptionType.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}