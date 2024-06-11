package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;

/**
 * Test the GroupImpl class
 */
public class GroupImplTest {

    /**
     * Test constructor
     */
    @Test
    public void testGroupImplConstructor() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create(),
                OptionBuilder.withLongOpt("optionB")
                        .withDescription("Option B")
                        .create(),
                OptionBuilder.withLongOpt("optionC")
                        .withDescription("Option C")
                        .create(),
                OptionBuilder.withLongOpt("argumentA")
                        .isRequired(false)
                        .hasArg(true)
                        .create());

        final Group group = new GroupImpl(options, "Group", null, 1, Integer.MAX_VALUE);

        assertNotNull(group);
        assertEquals("Group", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(Arrays.asList(OptionBuilder.withLongOpt("optionA"),
                OptionBuilder.withLongOpt("optionB"),
                OptionBuilder.withLongOpt("optionC")), group.getOptions());
        assertNull(group.getAnonymous());
        assertTrue(group.canProcess(new WriteableCommandLine(), "optionA"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "optionB"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "optionC"));
        assertFalse(group.canProcess(new WriteableCommandLine(), "unknown"));
    }

    /**
     * Test constructor with anonymous argument
     */
    @Test
    public void testGroupImplConstructorWithAnonymousArgument() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create(),
                OptionBuilder.withLongOpt("optionB")
                        .withDescription("Option B")
                        .create(),
                OptionBuilder.withLongOpt("argumentA")
                        .isRequired(false)
                        .hasArg(true)
                        .create());

        final Group group = new GroupImpl(options, "Group", null, 1, Integer.MAX_VALUE);

        assertNotNull(group);
        assertEquals("Group", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(Arrays.asList(OptionBuilder.withLongOpt("optionA"),
                OptionBuilder.withLongOpt("optionB")), group.getOptions());
        assertNotNull(group.getAnonymous());
        assertTrue(group.canProcess(new WriteableCommandLine(), "optionA"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "optionB"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "argumentA"));
    }

    /**
     * Test validation
     */
    @Test(expected = OptionException.class)
    public void testGroupImplValidationMinimum() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create());

        final Group group = new GroupImpl(options, "Group", null, 2, Integer.MAX_VALUE);

        group.validate(new WriteableCommandLine());
    }

    @Test(expected = OptionException.class)
    public void testGroupImplValidationMaximum() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create(),
                OptionBuilder.withLongOpt("optionB")
                        .withDescription("Option B")
                        .create(),
                OptionBuilder.withLongOpt("optionC")
                        .withDescription("Option C")
                        .create());

        final Group group = new GroupImpl(options, "Group", null, 1, 1);

        group.validate(new WriteableCommandLine(Collections.singletonList("--optionA"), null));

        group.validate(new WriteableCommandLine(Arrays.asList("--optionA", "--optionA"), null));
    }

    /**
     * Test process method
     */
    @Test
    public void testGroupImplProcess() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create(),
                OptionBuilder.withLongOpt("optionB")
                        .withDescription("Option B")
                        .create(),
                OptionBuilder.withLongOpt("argumentA")
                        .isRequired(false)
                        .hasArg(true)
                        .create());

        final Group group = new GroupImpl(options, "Group", null, 1, Integer.MAX_VALUE);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse(Arrays.asList("--optionA", "valueA", "--optionB", "valueB"));

        group.process(commandLine, commandLine.iterator());

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("valueA", commandLine.getArgList().get(0));
        assertEquals(1, group.getOptions().size());
        assertTrue(group.getOptions().get(0).isSet());
    }

    /**
     * Test process method with anonymous argument
     */
    @Test
    public void testGroupImplProcessWithAnonymousArgument() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create(),
                OptionBuilder.withLongOpt("optionB")
                        .withDescription("Option B")
                        .create(),
                OptionBuilder.withLongOpt("argumentA")
                        .isRequired(false)
                        .hasArg(true)
                        .create());

        final Group group = new GroupImpl(options, "Group", null, 1, Integer.MAX_VALUE);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse(Arrays.asList("--optionA", "--argumentA", "valueA"));

        group.process(commandLine, commandLine.iterator());

        assertEquals(2, commandLine.getArgList().size());
        assertEquals("valueA", commandLine.getArgList().get(1));
        assertEquals(1, group.getOptions().size());
        assertTrue(group.getOptions().get(0).isSet());
    }

    /**
     * Test process method with non-option token
     */
    @Test(expected = OptionException.class)
    public void testGroupImplProcessWithNonOptionToken() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create());

        final Group group = new GroupImpl(options, "Group", null, 1, Integer.MAX_VALUE);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse(Arrays.asList("--other", "--optionA"));

        group.process(commandLine, commandLine.iterator());
    }

    /**
     * Test appendUsage method
     */
    @Test
    public void testGroupImplAppendUsage() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create(),
                OptionBuilder.withLongOpt("optionB")
                        .withDescription("Option B")
                        .create());

        final Group group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE);

        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.EMPTY_SET, null);

        final String expected = "--group\n" +
                "  This is the group.\n" +
                "\n" +
                "Options:\n" +
                "  --optionA <value>\n" +
                "    Option A\n" +
                "\n" +
                "  --optionB <value>\n" +
                "    Option B\n";

        assertEquals(expected, sb.toString());
    }

    /**
     * Test helpLines method
     */
    @Test
    public void testGroupImplHelpLines() {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("optionA")
                        .withDescription("Option A")
                        .create(),
                OptionBuilder.withLongOpt("optionB")
                        .withDescription("Option B")
                        .create());

        final Group group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE);

        final List helpLines = group.helpLines(1, Collections.EMPTY_SET, null);

        assertEquals(5, helpLines.size());
        assertEquals("--group\n" +
                "  This is the group.\n\n" +
                "Options:\n" +
                "  --optionA <value>\n" +
                "    Option A\n\n" +
                "  --optionB <value>\n" +
                "    Option B\n", HelpLine.formatHelpLines((List) helpLines));
    }
}