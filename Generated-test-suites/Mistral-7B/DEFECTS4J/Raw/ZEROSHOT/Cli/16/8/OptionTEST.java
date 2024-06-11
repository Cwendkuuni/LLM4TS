import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(Parameterized.class)
public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLine();
        option = new MockOption();
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Option> getOptions() {
        Options options = new Options();
        Option option1 = new MockOption("option1");
        Option option2 = new MockOption("option2");
        Option option3 = new MockOption("option3");

        option1.setRequired(true);
        option2.setRequired(false);
        option3.setRequired(false);

        option1.setValueSeparator(':');
        option2.setValueSeparator('=');

        option1.addOption("subOption1", false, null, "subOption1Value");
        option1.addOption("subOption2", false, null, "subOption2Value");

        option2.addOption("subOption3", false, null, "subOption3Value");
        option2.addOption("subOption4", false, null, "subOption4Value");

        option3.addOption("subOption5", false, null, "subOption5Value");
        option3.addOption("subOption6", false, null, "subOption6Value");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        return Arrays.asList(option1, option2, option3);
    }

    @Test
    public void testProcess() throws OptionException {
        List<String> args = new ArrayList<>();
        args.add("--option1");
        args.add("--option1:value1");
        args.add("--option2=value2");
        args.add("--option3=value3");
        args.add("--option3:subOption5=subValue5");
        args.add("--subOption1");
        args.add("--subOption2");
        args.add("--subOption3");
        args.add("--subOption4");
        args.add("--subOption5");
        args.add("--subOption6");

        option.process(commandLine, args.listIterator());

        Assert.assertEquals("--option1:value1", commandLine.getOptionValue("option1"));
        Assert.assertEquals("value2", commandLine.getOptionValue("option2"));
        Assert.assertEquals("value3", commandLine.getOptionValue("option3"));
        Assert.assertEquals("subValue5", commandLine.getOptionValue("option3.subOption5"));
        Assert.assertNull(commandLine.getOptionValue("nonexistentOption"));
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);

        Assert.assertNull(commandLine.getOptionValue("option1"));
        Assert.assertNull(commandLine.getOptionValue("option2"));
        Assert.assertNull(commandLine.getOptionValue("option3"));

        Assert.assertNull(commandLine.getOptionValue("option1.subOption1"));
        Assert.assertNull(commandLine.getOptionValue("option1.subOption2"));
        Assert.assertNull(commandLine.getOptionValue("option2.subOption3"));
        Assert.assertNull(commandLine.getOptionValue("option2.subOption4"));
        Assert.assertNull(commandLine.getOptionValue("option3.subOption5"));
        Assert.assertNull(commandLine.getOptionValue("option3.subOption6"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessInvalidArgument() throws OptionException {
        option.canProcess(commandLine, Arrays.asList("invalidArgument").listIterator());
    }

    @Test
    public void testCanProcessValidArgument() {
        List<String> args = new ArrayList<>();
        args.add("--option1");

        Assert.assertTrue(option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        Assert.assertNotNull(triggers);
        Assert.assertEquals(Arrays.asList("--", "-"), triggers);
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        Assert.assertNotNull(prefixes);
        Assert.assertEquals(Arrays.asList("--", "-"), prefixes);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalidCommandLine() throws OptionException {
        commandLine.addOption("invalidOption", true);
        option.validate(commandLine);
    }

    @Test
    public void testValidateValidCommandLine() {
        commandLine.addOption("option1", true);
        commandLine.addOption("option2", false);
        commandLine.addOption("option3", false);

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List<String> helpLines = option.helpLines(1, null, null);
        Assert.assertNotNull(helpLines);
        Assert.assertEquals(3, helpLines.size());
        Assert.assertTrue(helpLines.toString().contains("option1"));
        Assert.assertTrue(helpLines.toString().contains("option2"));
        Assert.assertTrue(helpLines.toString().contains("option3"));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, null, null);

        Assert.assertEquals("option1,option2,option3", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("option1", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("This is an option test", option.getDescription());
    }

    @Test
    public void testgetId() {
        Assert.assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option foundOption = option.findOption("option1");
        Assert.assertNotNull(foundOption);
        Assert.assertEquals("option1", foundOption.getPreferredName());
    }

    @Test
    public void testIsRequired() {
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testHasParent() {
        Option parentOption = new MockOption("parentOption");
        option.setParent(parentOption);

        Assert.assertEquals(parentOption, option.getParent());
    }

    private static class MockOption extends AbstractOption {
        @Override
        public String getPreferredName() {
            return "optionTest";
        }

        @Override
        public String getDescription() {
            return "This is an option test";
        }

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
        public Set<String> getTriggers() {
            return new HashSet<>(Arrays.asList("--", "-"));
        }

        @Override
        public Set<String> getPrefixes() {
            return new HashSet<>(Arrays.asList("--", "-"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList<>();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        }

        @Override
        public String getPreferredName() {
            return "optionTest";
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
}