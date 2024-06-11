package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.apache.commons.cli2.CliBuilder.aGroup;
import static org.apache.commons.cli2.CliBuilder.anOption;
import static org.apache.commons.cli2.CliBuilder.anArg;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    @Mock
    private WriteableCommandLine commandLine;

    private GroupImpl group;

    @Before
    public void setUp() {
        final List options = Arrays.asList(
                anOption().withName("option1").withDescription("Desc1"),
                anOption().withName("option2").withDescription("Desc2"),
                anOption().withName("option3").withDescription("Desc3")
        );

        group = new GroupImpl(options, "groupName", "Group description", 1, 2);
    }

    @Test
    public void testCanProcess() {
        when(commandLine.looksLikeOption("option1")).thenReturn(true);
        when(commandLine.looksLikeOption("option2")).thenReturn(false);
        when(commandLine.looksLikeOption("option3")).thenReturn(false);
        when(commandLine.hasOption("option1")).thenReturn(true);
        when(commandLine.hasOption("option2")).thenReturn(false);
        when(commandLine.hasOption("option3")).thenReturn(false);
        when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        assertTrue(group.canProcess(commandLine, "option1"));
        assertFalse(group.canProcess(commandLine, "option2"));
        assertFalse(group.canProcess(commandLine, "option3"));
        assertFalse(group.canProcess(commandLine, "arg"));
    }

    @Test
    public void testGetName() {
        assertEquals("groupName", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Group description", group.getDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(group.getOptions(), Arrays.asList(group.findOption("option1"),
                group.findOption("option2"), group.findOption("option3")));
    }

    @Test
    public void testValidate() throws OptionException {
        final List options = Arrays.asList(
                anOption().withName("option1").isRequired(true),
                anOption().withName("option2")
        );

        group = new GroupImpl(options, null, null, 1, 2);

        when(commandLine.getArguments()).thenReturn(Arrays.asList("arg1"));

        group.validate(commandLine);
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().get(0) instanceof Option);
        assertTrue(group.getOptions().get(1) instanceof Option);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingOption() throws OptionException {
        final List options = Arrays.asList(
                anOption().withName("option1").isRequired(true)
        );

        group = new GroupImpl(options, null, null, 1, 1);

        when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() throws OptionException {
        final List options = Arrays.asList(
                anOption().withName("option1").isRequired(true)
        );

        group = new GroupImpl(options, null, null, 1, 0);

        when(commandLine.getArguments()).thenReturn(Arrays.asList("arg1", "arg2"));

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, new HashSet(), null);

        assertEquals("Group description\n" +
                "[groupName]\n" +
                "option1: Desc1\n" +
                "option2: Desc2\n" +
                "option3: Desc3\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List helpLines = group.helpLines(0, new HashSet(), null);

        assertEquals(3, helpLines.size());
        assertEquals("Group description\n" +
                "[groupName]\n" +
                "  option1: Desc1\n" +
                "  option2: Desc2\n" +
                "  option3: Desc3\n", ((HelpLine) helpLines.get(0)).getHelp());
    }

    @Test
    public void testFindOption() {
        final Option option1 = group.findOption("option1");
        final Option option2 = group.findOption("option2");

        assertNotNull(option1);
        assertNotNull(option2);
        assertEquals("option1", option1.getName());
        assertEquals("option2", option2.getName());
        assertNull(group.findOption("option4"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        for (final Option option : group.getOptions()) {
            option.defaults(commandLine);
        }
    }

    @Test
    public void testGetAnonymous() {
        final List anonymous = Arrays.asList(
                anArg().withName("anonymousArg1").isRequired(true),
                anArg().withName("anonymousArg2")
        );

        final GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 1, 1, anonymous);

        assertEquals(anonymous, group.getAnonymous());
    }
}