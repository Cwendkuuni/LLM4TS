import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OptionTest {

    private Option testOption;
    private Options testOptions;

    @Before
    public void setup() {
        testOption = new TestOption();
        testOptions = new Options();
        testOptions.addOption(testOption);
    }

    @Test
    public void testProcess() {
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--test", "arg1", "arg2");
        testOption.process(commandLine, args.listIterator());

        Assert.assertEquals("test", commandLine.getOptionValue("test"));
        Assert.assertEquals("arg1", commandLine.getOptionValue("test.arg1"));
        Assert.assertEquals("arg2", commandLine.getOptionValue("test.arg2"));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidArgument() {
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--test", "invalid-argument");

        testOption.process(commandLine, args.listIterator());
    }

    @Test
    public void testDefaults() {
        CommandLine commandLine = new CommandLine();
        testOption.defaults(commandLine);

        Assert.assertNull(commandLine.getOptionValue("test.arg1"));
        Assert.assertNull(commandLine.getOptionValue("test.arg2"));
    }

    @Test
    public void testCanProcess() {
        CommandLine commandLine = new CommandLine();

        Assert.assertTrue(testOption.canProcess(commandLine, "--test"));
        Assert.assertTrue(testOption.canProcess(commandLine, "--test arg1"));
        Assert.assertTrue(testOption.canProcess(commandLine, "--test arg1 arg2"));
        Assert.assertFalse(testOption.canProcess(commandLine, "--test arg1 arg2 arg3"));
    }

    @Test
    public void testValidate() {
        CommandLine commandLine = new CommandLine();

        testOption.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() {
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--test", "invalid-argument");

        testOption.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = testOption.helpLines(0, null, null);

        Assert.assertEquals(3, helpLines.size());
        Assert.assertEquals("--test", helpLines.get(0).getLongOpt());
        Assert.assertEquals("test\n", helpLines.get(0).getSynopsis());
        Assert.assertEquals("Test option\n", helpLines.get(0).getDescription());
        Assert.assertEquals(2, helpLines.get(1).getOptions().size());
        Assert.assertEquals("arg1", helpLines.get(1).getOptions().get(0).getLongOpt());
        Assert.assertEquals("an argument\n", helpLines.get(1).getOptions().get(0).getDescription());
        Assert.assertEquals("arg2", helpLines.get(1).getOptions().get(1).getLongOpt());
        Assert.assertEquals("another argument\n", helpLines.get(1).getOptions().get(1).getDescription());
        Assert.assertNull(helpLines.get(2).getOptions());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        testOption.appendUsage(buffer, null, null);

        Assert.assertEquals("--test [arg1=[an argument]], [arg2=[another argument]]\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("test", testOption.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("Test option", testOption.getDescription());
    }

    @Test
    public void testgetId() {
        Assert.assertEquals(1, testOption.getId());
    }

    @Test
    public void testFindOption() {
        Option testOption2 = new TestOption2();
        testOptions.addOption(testOption2);

        Option foundOption = testOptions.findOption("test2");

        Assert.assertEquals(testOption2, foundOption);
    }

    @Test
    public void testIsRequired() {
        TestRequiredOption testRequiredOption = new TestRequiredOption();
        testOptions.addOption(testRequiredOption);

        Assert.assertTrue(testRequiredOption.isRequired());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithoutArgument() {
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--test");

        testOption.process(commandLine, args.listIterator());

        Assert.fail();
    }

    private static class TestOption extends Option.Builder {
        public TestOption() {
            super("test", "t", "t", "Test option")
                    .withArgName("arg1")
                    .isRequiredWorksWithGroup(false)
                    .hasArg(true)
                    .withLongOpt("test")
                    .valueSeparator('=')
                    .withValuesSeparator(',')
                    .isRequired(false);
        }
    }

    private static class TestOption2 extends Option.Builder {
        public TestOption2() {
            super("test2", "t2", null, "Test option 2")
                    .withArgName("arg1")
                    .isRequiredWorksWithGroup(false)
                    .hasArg(true)
                    .withLongOpt("test2")
                    .valueSeparator('=')
                    .withValuesSeparator(',')
                    .isRequired(false);
        }
    }

    private static class TestRequiredOption extends Option.Builder {
        public TestRequiredOption() {
            super("test-required", "tr", null, "Test required option")
                    .withArgName("arg1")
                    .isRequiredWorksWithGroup(false)
                    .hasArg(true)
                    .withLongOpt("test-required")
                    .valueSeparator('=')
                    .withValuesSeparator(',')
                    .isRequired(true);
        }
    }
}