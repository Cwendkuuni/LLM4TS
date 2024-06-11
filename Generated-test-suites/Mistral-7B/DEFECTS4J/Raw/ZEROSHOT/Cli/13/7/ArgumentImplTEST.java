import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ArgumentImplTest {

    private Argument argument;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        argument = new ArgumentImpl("arg", "test argument", 1, 1, ' ', ' ', null, null, null, 0) {
            @Override
            public void processValues(WriteableCommandLine writeableCommandLine, ListIterator<String> listIterator, Option option)
                    throws OptionException {
                super.processValues(writeableCommandLine, listIterator, option);

                // no-op for test
            }
        };
    }

    @Test
    public void testGetName() {
        assertEquals("arg", argument.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("test argument", argument.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(' ', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetSubsequentSplit() {
        assertFalse(argument.getSubsequentSplit());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertNull(argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testProcessValues() {
        List<String> args = Arrays.asList("--arg", "foo");
        argument.process(commandLine, args.listIterator(), null);
        assertEquals("foo", commandLine.getValue(argument));
    }

    @Test
    public void testProcessValuesWithMultipleValues() {
        List<String> args = Arrays.asList("--arg", "foo", "bar");
        argument.process(commandLine, args.listIterator(), null);
        assertEquals(Arrays.asList("foo", "bar"), commandLine.getValues(argument));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooManyValues() {
        List<String> args = Arrays.asList("--arg", "foo", "bar", "baz");
        argument.process(commandLine, args.listIterator(), null);
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidValue() {
        List<String> args = Arrays.asList("--arg", "foo", "qux");
        argument.setValidator(new MyValidator());
        argument.process(commandLine, args.listIterator(), null);
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "foo"));
    }

    @Test
    public void testCanProcessWithInvalidValue() {
        assertFalse(argument.canProcess(commandLine, "--arg"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.emptySet(), argument.getPrefixes());
    }

    @Test
    public void testProcess() {
        List<String> args = Arrays.asList("--arg", "foo");
        argument.process(commandLine, args.listIterator());

        assertEquals("foo", commandLine.getValue(argument));
    }

    @Test
    public void testProcessWithMultipleValues() {
        List<String> args = Arrays.asList("--arg", "foo", "bar");
        argument.process(commandLine, args.listIterator());

        assertEquals(Arrays.asList("foo", "bar"), commandLine.getValues(argument));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooManyValues() {
        List<String> args = Arrays.asList("--arg", "foo", "bar", "baz");
        argument.process(commandLine, args.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidValue() {
        List<String> args = Arrays.asList("--arg", "foo", "qux");
        argument.setValidator(new MyValidator());
        argument.process(commandLine, args.listIterator());
    }

    @Test
    public void testValidate()
            throws OptionException {
        List<String> args = Arrays.asList("--arg", "foo", "bar");
        argument.validate(commandLine);
        assertEquals(Arrays.asList("foo", "bar"), commandLine.getValues(argument));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyValues() {
        List<String> args = Arrays.asList("--arg", "foo", "bar", "baz");
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() {
        List<String> args = Arrays.asList("--arg", "foo", "qux");
        argument.setValidator(new MyValidator());
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuilder sb = new StringBuilder();
        argument.appendUsage(sb, null, null);
        assertEquals("  --arg <arg>", sb.toString());
    }

    @Test
    public void testAppendUsageWithNumbered() {
        StringBuilder sb = new StringBuilder();
        argument.appendUsage(sb, null, new ArgumentNameComparator());
        assertEquals("  1: --arg <arg>", sb.toString());
    }

    @Test
    public void testAppendUsageWithPrefix() {
        StringBuilder sb = new StringBuilder();
        argument.appendUsage(sb, Arrays.asList("--prefix"), null);
        assertEquals("  --prefix--arg <arg>", sb.toString());
    }

    @Test
    public void testAppendUsageWithBrackets() {
        StringBuilder sb = new StringBuilder();
        argument.appendUsage(sb, null, null, DisplaySetting.DisplayArgumentBracketed.BRACKETED);
        assertEquals("  [--arg] <arg>", sb.toString());
    }

    @Test
    public void testAppendUsageWithOptional() {
        StringBuilder sb = new StringBuilder();
        argument.appendUsage(sb, null, null, DisplaySetting.DisplayOptionality.OPTIONAL);
        assertEquals("  [--arg, --no-arg] <arg>", sb.toString());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> lines = argument.helpLines(0, null, null);
        formatter.printHelp("test", "test", lines.toArray(new HelpLine[0]));

        StringBuilder sb = new StringBuilder();
        TestHelper.printLinesTo(sb, lines.toArray(new HelpLine[0]));

        assertEquals(
                " test (no value)\n" +
                        "  --arg <arg>\n" +
                        "            an argument\n",
                sb.toString());
    }

    @Test
    public void testHelpLinesWithNumbered() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> lines = argument.helpLines(0, null, new ArgumentNameComparator());
        formatter.printHelp("test", "test", lines.toArray(new HelpLine[0]));

        StringBuilder sb = new StringBuilder();
        TestHelper.printLinesTo(sb, lines.toArray(new HelpLine[0]));

        assertEquals(
                " test (no value)\n" +
                        "   1: --arg <arg>\n" +
                        "                   an argument\n",
                sb.toString());
    }

    @Test
    public void testHelpLinesWithPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> lines = argument.helpLines(0, Arrays.asList("--prefix"), null);
        formatter.printHelp("test", "test", lines.toArray(new HelpLine[0]));

        StringBuilder sb = new StringBuilder();
        TestHelper.printLinesTo(sb, lines.toArray(new HelpLine[0]));

        assertEquals(
                " test (no value)\n" +
                        "  --prefix--arg <arg>\n" +
                        "                  an argument\n",
                sb.toString());
    }

    @Test
    public void testHelpLinesWithBrackets() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> lines = argument.helpLines(0, null, null, DisplaySetting.DisplayArgumentBracketed.BRACKETED);
        formatter.printHelp("test", "test", lines.toArray(new HelpLine[0]));

        StringBuilder sb = new StringBuilder();
        TestHelper.printLinesTo(sb, lines.toArray(new HelpLine[0]));

        assertEquals(
                " test (no value)\n" +
                        "  [--arg] <arg>\n" +
                        "            an argument\n",
                sb.toString());
    }

    @Test
    public void testHelpLinesWithOptional() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> lines = argument.helpLines(0, null, null, DisplaySetting.DisplayOptionality.OPTIONAL);
        formatter.printHelp("test", "test", lines.toArray(new HelpLine[0]));

        StringBuilder sb = new StringBuilder();
        TestHelper.printLinesTo(sb, lines.toArray(new HelpLine[0]));

        assertEquals(
                " test (no value)\n" +
                        "  [--arg, --no-arg] <arg>\n" +
                        "            an argument\n",
                sb.toString());
    }

    @Test
    public void testHelpLinesWithNumberedAndPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> lines = argument.helpLines(0, Arrays.asList("--prefix"), null, new ArgumentNameComparator());
        formatter.printHelp("test", "test", lines.toArray(new HelpLine[0]));

        StringBuilder sb = new StringBuilder();
        TestHelper.printLinesTo(sb, lines.toArray(new HelpLine[0]));

        assertEquals(
                " test (no value)\n" +
                        "   1: --prefix--arg <arg>\n" +
                        "                  an argument\n",
                sb.toString());
    }

    @Test
    public void testHelpLinesWithBracketsAndOptional() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> lines = argument.helpLines(0, null, null, DisplaySetting.DisplayArgumentBracketed.BRACKETED, DisplaySetting.DisplayOptionality.OPTIONAL);
        formatter.printHelp("test", "test", lines.toArray(new HelpLine[0]));

        StringBuilder sb = new StringBuilder();
        TestHelper.printLinesTo(sb, lines.toArray(new HelpLine[0]));

        assertEquals(
                " test (no value)\n" +
                        "  [--arg, --no-arg] <arg>\n" +
                        "            an argument\n",
                sb.toString());
    }

    public static class MyValidator implements Validator {
        @Override
        public void validate(List<?> list) throws InvalidArgumentException {
            for (Object o : list) {
                assertTrue(o instanceof String);
                assertEquals("expected", o.toString());
            }
        }
    }

    public static class ArgumentNameComparator implements Comparator<HelpLine> {
        @Override
        public int compare(HelpLine o1, HelpLine o2) {
            return o1.getSpec().length() - o2.getSpec().length();
        }
    }
}