package org.apache.commons.cli2.option;

import org.apache.commons.cli2.ArgParseException;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.examples.TestGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * JUnit 4 test cases for the GroupImpl class.
 */
public class GroupImplTest {

    private ParseContext context;
    private Group group;

    @Before
    public void setUp() {
        final List options = new ArrayList();
        final Option option1 = OptionBuilder
            .withLongOpt("option1")
            .withDescription("Option 1")
            .withArgName("name")
            .create();
        options.add(option1);

        final Option option2 = OptionBuilder
            .withLongOpt("option2")
            .withDescription("Option 2")
            .withArgName("age")
            .create();
        options.add(option2);

        group = new GroupImpl(options, "MyGroup", "My Group", 0, Integer.MAX_VALUE);
        context = new ParseContext();
    }

    @Test
    public void testCanProcessGroupName() {
        assertTrue(group.canProcess(context, "MyGroup"));
    }

    @Test
    public void testCanProcessOptionName() {
        assertTrue(group.canProcess(context, "option1"));
        assertTrue(group.canProcess(context, "option2"));
    }

    @Test(expected = ArgParseException.class)
    public void testCannotProcessUnexpectedOption() {
        group.canProcess(context, "unexpectedOption");
    }

    @Test
    public void testCanProcessAnonymousArgument() {
        assertFalse(group.canProcess(context, "arg"));

        final List anonymous = group.getAnonymous();
        final Option anonymousOption = (Option) anonymous.get(0);

        assertTrue(anonymousOption.canProcess(context, "arg"));
    }

    @Test
    public void testGetName() {
        assertEquals("MyGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("My Group", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        final List options = group.getOptions();
        assertEquals(2, options.size());

        final Option option1 = (Option) options.get(0);
        assertEquals("option1", option1.getLongOpt());
        assertEquals("Option 1", option1.getDescription());

        final Option option2 = (Option) options.get(1);
        assertEquals("option2", option2.getLongOpt());
        assertEquals("Option 2", option2.getDescription());
    }

    @Test
    public void testGetAnonymous() {
        final List anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());

        final Option anonymousOption = (Option) anonymous.get(0);
        assertNull(anonymousOption.getLongOpt());
        assertNull(anonymousOption.getValueDescription());
        assertNull(anonymousOption.getDefaultValue());
        assertNull(anonymousOption.getArgName());
        assertFalse(anonymousOption.isRequired());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinimum() {
        new GroupImpl(Collections.emptyList(), null, null, -1, Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaximum() {
        new GroupImpl(Collections.emptyList(), null, null, 0, Integer.MIN_VALUE);
    }

    @Test
    public void testValidateGroup() {
        group.validate(context);
    }

    @Test(expected = ArgParseException.class)
    public void testValidateGroupWithMissingOption() {
        final List options = new ArrayList();
        final Option option = OptionBuilder
            .withLongOpt("option")
            .create();
        options.add(option);

        new GroupImpl(options, null, null, 1, Integer.MAX_VALUE).validate(context);
    }

    @Test(expected = ArgParseException.class)
    public void testValidateGroupWithTooManyOptions() {
        final List options = new ArrayList();
        final Option option1 = OptionBuilder
            .withLongOpt("option1")
            .create();
        final Option option2 = OptionBuilder
            .withLongOpt("option2")
            .create();
        options.add(option1);
        options.add(option2);
        options.add(OptionBuilder
            .withLongOpt("option3")
            .create());

        new GroupImpl(options, "MyGroup", null, 2, Integer.MAX_VALUE).validate(context);
    }

    @Test
    public void testProcessGroupWithTwoOptions() {
        final String[] args = new String[]{"--option1", "name", "--option2", "age", "arg"};
        group.process(context, TestUtils.getListIterator(args));

        final Option option1 = (Option) context.getOptions().get(0);
        assertEquals("name", option1.getValue());

        final Option option2 = (Option) context.getOptions().get(1);
        assertEquals("age", option2.getValue());
    }

    @Test
    public void testProcessGroupWithAnonymousArgument() {
        final String[] args = new String[]{"--option1", "name", "--", "arg"};
        group.process(context, TestUtils.getListIterator(args));

        final Option option1 = (Option) context.getOptions().get(0);
        assertEquals("name", option1.getValue());

        final List anonymous = group.getAnonymous();
        final Option anonymousOption = (Option) anonymous.get(0);
        assertEquals("arg", anonymousOption.getValue());
    }

    @Test
    public void testProcessGroupWithAnonymousArgumentAndOption() {
        final String[] args = new String[]{"--option1", "name", "--option2", "age", "--arg", "value"};
        group.process(context, TestUtils.getListIterator(args));

        final Option option1 = (Option) context.getOptions().get(0);
        assertEquals("name", option1.getValue());

        final Option option2 = (Option) context.getOptions().get(1);
        assertEquals("age", option2.getValue());

        final Option argument = (Option) context.getOptions().get(2);
        assertEquals("value", argument.getValue());
    }

    @Test
    public void testHelpLines() {
        group.appendUsage(new StringBuilder(), Collections.emptySet(), new ReverseStringComparator());

        final StringBuilder expectedUsage = new StringBuilder();
        expectedUsage.append("MyGroup:");
        expectedUsage.append(System.lineSeparator());
        expectedUsage.append("  ").append("option1 NAME");
        expectedUsage.append(System.lineSeparator());
        expectedUsage.append("  ").append("option1 NAME=").append("NAME");
        expectedUsage.append(System.lineSeparator());
        expectedUsage.append("  ").append("option2 AGE");
        expectedUsage.append(System.lineSeparator());
        expectedUsage.append("  ").append("option2 AGE=").append("AGE");
        expectedUsage.append(System.lineSeparator());
        expectedUsage.append(System.lineSeparator());

        assertEquals(expectedUsage.toString(), group.getHelpLines(0, Collections.emptySet(), null).get(0).toString());
    }

    @Test
    public void testHelpFormattedOutput() {
        final String[] args = new String[]{"--help"};

        try {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MyClass", group, false, null, false, null);
        } catch (OptionException e) {
            final StringBuilder expectedHelp = new StringBuilder();
            expectedHelp.append("MyClass:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("Options:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  option1");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("    NAME\t\tNAME\tOption 1");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  option2");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("    AGE\t\tAGE\tOption 2");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("ARGUMENTS:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  arg\t\tARG\tAnonymous argument");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("Usage: MyClass [options] [arguments]");
            expectedHelp.append(System.lineSeparator());

            assertEquals(expectedHelp.toString(), TestUtils.getConsoleOutput(new StringBuilder(), formatter, args));
        }
    }

    @Test
    public void testHelpFormattedOutputWithGroupName() {
        final String[] args = new String[]{"--help", "MyGroup"};

        try {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MyClass", group, false, null, false, null);
        } catch (OptionException e) {
            final StringBuilder expectedHelp = new StringBuilder();
            expectedHelp.append("MyGroup:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("Options:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  option1");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("    NAME\t\tNAME\tOption 1");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  option2");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("    AGE\t\tAGE\tOption 2");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append(System.lineSeparator());

            assertEquals(expectedHelp.toString(), TestUtils.getConsoleOutput(new StringBuilder(), formatter, args));
        }
    }

    @Test
    public void testHelpFormattedOutputWithGroupExpanded() {
        final TestGroup testGroup = new TestGroup();
        testGroup.addOption(OptionBuilder
            .withLongOpt("option1")
            .create());
        testGroup.addOption(OptionBuilder
            .withLongOpt("option2")
            .create());

        final String[] args = new String[]{"--help", "MyGroup"};

        try {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MyClass", group, true, null, false, null);
        } catch (OptionException e) {
            final StringBuilder expectedHelp = new StringBuilder();
            expectedHelp.append("MyGroup:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("Options:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  option1");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("    NAME\t\tNAME\tOption 1");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  option2");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("    AGE\t\tAGE\tOption 2");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("ARGUMENTS:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  arg\t\tARG\tAnonymous argument");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("MyGroup:");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  option1");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("    NAME\t\tNAME\tOption 1");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("  option2");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append("    AGE\t\tAGE\tOption 2");
            expectedHelp.append(System.lineSeparator());
            expectedHelp.append(System.lineSeparator());

            assertEquals(expectedHelp.toString(), TestUtils.getConsoleOutput(new StringBuilder(), formatter, args));
        }
    }
}