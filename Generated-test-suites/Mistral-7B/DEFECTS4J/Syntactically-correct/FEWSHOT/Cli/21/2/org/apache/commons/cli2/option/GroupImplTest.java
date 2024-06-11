package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.ArgParser;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        List options = new ArrayList();
        options.add(createTestOption("a"));
        options.add(createTestOption("b"));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);
        assertNotNull(group.getPreferredName());
        assertNotNull(group.getDescription());
        assertEquals(2, group.getOptions().size());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testCanProcessPositive() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption(createTestOptionWithValue("a", "valueA"));
        commandLine.addOption(createTestOptionWithValue("b", "valueB"));

        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertFalse(group.canProcess(commandLine, "c"));
    }

    @Test
    public void testCanProcessNegative() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption(createTestOptionWithValue("a", "valueA"));

        assertFalse(group.canProcess(commandLine, "b"));
        assertFalse(group.canProcess(commandLine, "c"));
    }

    @Test
    public void testProcess() throws OptionException {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValues("b", new String[]{"valueB1", "valueB2"}));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption(createTestOptionWithValue("a", "valueA"));
        commandLine.addOption(createTestOptionWithValue("b", "valueB1"));

        group.process(commandLine, commandLine.listIterator());

        assertEquals(1, commandLine.getNumberOfOptions());
       assertEquals("valueA", commandLine.getOptionValues("a")[0]);
        assertEquals(1, commandLine.getNumberOfArguments());
        assertEquals("valueB1", commandLine.getArgumentValues()[0]);

        // Test empty group
        group = new GroupImpl(Collections.emptyList(), null, null, 0, 0, false);

        commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.listIterator());

        assertEquals(0, commandLine.getNumberOfOptions());
        assertEquals(0, commandLine.getNumberOfArguments());
    }

    @Test
    public void testValidatePositive() throws OptionException {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption(createTestOptionWithValue("a", "valueA"));
        commandLine.addOption(createTestOptionWithValue("b", "valueB"));

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateNegative() throws OptionException {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption(createTestOptionWithValue("a", "valueA"));

        group.validate(commandLine);
    }

    @Test
    public void testGetPrefixes() {
        List options = new ArrayList();
        options.add(createTestOptionWithValues("a", new String[]{"--prefix1", "--prefix2"}));
        options.add(createTestOptionWithValues("b", new String[]{"--prefix3"}));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        Set prefixes = group.getPrefixes();

        assertTrue(prefixes.contains("--prefix1"));
        assertTrue(prefixes.contains("--prefix2"));
        assertTrue(prefixes.contains("--prefix3"));
    }

    @Test
    public void testGetTriggers() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", "aValue"));
        options.add(createTestOptionWithValue("b", "bValue"));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        Set triggers = group.getTriggers();

        assertTrue(triggers.contains("a"));
        assertTrue(triggers.contains("b"));
        assertFalse(triggers.contains("c"));
    }

    @Test
    public void testAppendUsage() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", "aValue"));
        options.add(createTestOptionWithValue("b", "bValue"));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, null, null);

        String expectedUsage = "testGroup:\n" +
                "  Test group\n" +
                "\n" +
                "Options:\n" +
                "  --a <aValue>\n" +
                "  --b <bValue>\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", "aValue"));
        options.add(createTestOptionWithValue("b", "bValue"));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        List helpLines = group.helpLines(0, null, null);

        String expectedHelpLines = "testGroup:\n" +
                "  Test group\n" +
                "\n" +
                "Options:\n" +
                "  --a <aValue>...\n" +
                "  --b <bValue>...\n";

        assertEquals(expectedHelpLines, helpLines.get(0).toString());
    }

    @Test
    public void testFindOption() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", "aValue"));
        options.add(createTestOptionWithValue("b", "bValue"));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        Option option = group.findOption("a");

        assertTrue(option instanceof Option);
        assertEquals("a", option.getLongOpt());
        assertEquals("aValue", option.getValueDescription());

        option = group.findOption("b");

        assertTrue(option instanceof Option);
        assertEquals("b", option.getLongOpt());
        assertEquals("bValue", option.getValueDescription());

        option = group.findOption("c");

        assertNull(option);
    }

    @Test
    public void testGetMinimum() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        assertFalse(group.isRequired());

        options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));
        options.add(createTestOptionWithValue("c", true));

        group = new GroupImpl(options, "testGroup", "Test group", 2, 3, false);

        assertTrue(group.isRequired());

        options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));
        options.add(createTestOptionWithValue("c", true));

        group = new GroupImpl(options, "testGroup", "Test group", 0, 3, false);

        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        List options = new ArrayList();
        options.add(createTestOptionWithValue("a", true));
        options.add(createTestOptionWithValue("b", true));

        Group group = new GroupImpl(options, "testGroup", "Test group", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.defaults(commandLine);

        assertFalse(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    private Option createTestOption(final String longOpt) {
        Option option = new OptionBuilder()
                .withLongOpt(longOpt)
                .withDescription("Test option")
                .createOption();
        return option;
    }

    private Option createTestOptionWithValue(final String longOpt, final boolean hasValue) {
        Option option = createTestOption(longOpt);

        if (hasValue) {
            option.setValueDescription("Test value");
        }

        return option;
    }

    private Option createTestOptionWithValues(final String longOpt, final String[] values) {
        Option option = new OptionBuilder()
                .withLongOpt(longOpt)
                .withDescription("Test value option")
                .isValueSeparatorAllowed(true)
                .createOption();

        option.setValueDescriptions(values);

        return option;
    }
}