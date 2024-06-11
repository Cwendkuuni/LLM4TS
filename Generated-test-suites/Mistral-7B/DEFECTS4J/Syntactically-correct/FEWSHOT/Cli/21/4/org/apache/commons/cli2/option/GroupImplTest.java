package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.HelpLineImpl;
import org.apache.commons.cli2.util.TestUtils;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testCreateGroup() {
        final List options = new ArrayList();
        options.add(createTestOption("option1", "description1", null, null));
        options.add(createTestOption("option2", "description2", null, null));

        final Group group = new GroupImpl(options, "groupName", "groupDescription", 1, 1, false);

        assertEquals("groupName", group.getPreferredName());
        assertEquals("groupDescription", group.getDescription());
        assertEquals(options, group.getOptions());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.emptySet(), group.getPrefixes());
    }

    @Test
    public void testOptionProcess() {
        final List options = new ArrayList();
        options.add(createTestOption("option1", "description1", null, null));
        options.add(createTestOption("option2", "description2", null, null));

        final Group group = new GroupImpl(options, "groupName", "groupDescription", 1, 1, false);

        final WriteableCommandLine commandLine = TestUtils.createMockCommandLine();
        final ListIterator arguments = TestUtils.createMockListIterator();

        arguments.next(); // consume first argument

        group.process(commandLine, arguments);

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("nonexistent"));
    }

    @Test
    public void testOptionValidation() throws OptionException {
        final List options = new ArrayList();
        options.add(createTestOption("option1", "description1", null, null));
        options.add(createTestOption("option2", "description2", null, null));

        final Group group = new GroupImpl(options, "groupName", "groupDescription", 1, 1, false);

        final WriteableCommandLine commandLine = TestUtils.createMockCommandLine();

        group.validate(commandLine);

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
    }

    @Test(expected = OptionException.class)
    public void testOptionValidationWithTooFewOptions() throws OptionException {
        final List options = new ArrayList();
        options.add(createTestOption("option1", "description1", null, null));

        final Group group = new GroupImpl(options, "groupName", "groupDescription", 2, 1, false);

        final WriteableCommandLine commandLine = TestUtils.createMockCommandLine();

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testOptionValidationWithTooManyOptions() throws OptionException {
        final List options = new ArrayList();
        options.add(createTestOption("option1", "description1", null, null));
        options.add(createTestOption("option2", "description2", null, null));

        final Group group = new GroupImpl(options, "groupName", "groupDescription", 1, 2, false);

        final WriteableCommandLine commandLine = TestUtils.createMockCommandLine();
        commandLine.addOption("option1");
        commandLine.addOption("option2");
        commandLine.addOption("option3");

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        final List options = new ArrayList();
        options.add(createTestOption("option1", "description1", null, null));
        options.add(createTestOption("option2", "description2", null, null));

        final Group group = new GroupImpl(options, "groupName", "groupDescription", 1, 1, false);

        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);

        assertTrue(buffer.toString().startsWith("groupName:"));
        assertTrue(buffer.toString().contains("option1"));
        assertTrue(buffer.toString().contains("option2"));

        final HelpLine helpLine = new HelpLineImpl(group, 0);
        assertEquals(helpLine.getHelpLine(), buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List options = new ArrayList();
        options.add(createTestOption("option1", "description1", null, null));
        options.add(createTestOption("option2", "description2", null, null));

        final Group group = new GroupImpl(options, "groupName", "groupDescription", 1, 1, false);

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(2, helpLines.size());

        final HelpLineImpl helpLine1 = (HelpLineImpl) helpLines.get(0);
        assertEquals("groupName: option1", helpLine1.getHeader());
        assertEquals("description1", helpLine1.getShortDescription());
        assertEquals("option1", helpLine1.getLongDescription());

        final HelpLineImpl helpLine2 = (HelpLineImpl) helpLines.get(1);
        assertEquals("groupName: option2", helpLine2.getHeader());
        assertEquals("description2", helpLine2.getShortDescription());
        assertEquals("option2", helpLine2.getLongDescription());
    }

    @Test
    public void testFindOption() {
        final List options = new ArrayList();
        options.add(createTestOption("option1", "description1", null, null));
        options.add(createTestOption("option2", "description2", null, null));

        final Group group = new GroupImpl(options, "groupName", "groupDescription", 1, 1, false);

        final Option option1 = group.findOption("option1");
        final Option option2 = group.findOption("option2");

        assertSame(createTestOption("option1", "description1", null, null), option1);
        assertSame(createTestOption("option2", "description2", null, null), option2);
        assertNull(group.findOption("nonexistent"));
    }

    private Option createTestOption(final String name, final String description, final Integer value, final String longOpt) {
        final Option option = new TestOptionImpl(name, description, value, longOpt);
        option.setValue("testValue");
        return option;
    }
}

class TestOptionImpl extends OptionImpl implements Option {
    public TestOptionImpl(String name, String description, Integer value, String longOpt) {
        super(name, description, value, longOpt);
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        // no validation for test option
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        super.defaults(commandLine);
        // no defaults for test option
    }
}

class HelpLineImpl implements HelpLine {
    private final Group parent;
    private final int depth;

    public HelpLineImpl(Group parent, int depth) {
        this.parent = parent;
        this.depth = depth;
    }

    public String getHeader() {
        if (depth > 0) {
            return "  " + repeat(" ", 2 * depth) + parent.getPreferredName() + ":";
        } else {
            return parent.getPreferredName() + ":";
        }
    }

    public String getShortDescription() {
        return parent.getDescription();
    }

    public String getLongDescription() {
        return "this is a test long description";
    }
}

class ReverseStringComparator implements Comparator<String> {
    private static final Comparator<String> instance = new ReverseStringComparator();

    private ReverseStringComparator() {
        // just making sure nobody else creates one
    }

    public static Comparator<String> getInstance() {
        return instance;
    }

    @Override
    public int compare(String o1, String o2) {
        return o2.compareTo(o1);
    }
}