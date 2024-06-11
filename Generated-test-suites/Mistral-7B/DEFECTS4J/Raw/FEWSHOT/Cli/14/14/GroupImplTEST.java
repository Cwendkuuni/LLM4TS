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
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        List options = new ArrayList();
        options.add(createOption("opt1", false));
        options.add(createOption("opt2", false));
        Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertNull(group.findOption("opt3"));
        assertEquals(Collections.emptySet(), group.getTriggers());
        assertSame(group, group.validate(null));
    }

    @Test
    public void testGroupWithArgument() {
        List options = new ArrayList();
        options.add(createOption("opt1", false));
        options.add(createArgument("arg1", "argValue"));
        Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.singleton("arg1"), group.getTriggers());
        assertNull(group.findOption("opt3"));
    }

    @Test
    public void testGroupWithNestedOption() {
        List options = new ArrayList();
        Option parentOption = createOption("parent", false);
        options.add(parentOption);
        Option childOption = createOption("child", false);
        parentOption.addOption(childOption);
        Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.singleton("child"), group.findOption("child").getTriggers());
    }

    @Test(expected = OptionException.class)
    public void testGroupMinimumNotMet() throws OptionException {
        List options = new ArrayList();
        options.add(createOption("opt1", false));
        Group group = new GroupImpl(options, null, null, 0, 1);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        group.process(commandLine, commandLine.getArguments().listIterator());
    }

    @Test(expected = OptionException.class)
    public void testGroupMaximumExceeded() throws OptionException {
        List options = new ArrayList();
        options.add(createOption("opt1", false));
        Group group = new GroupImpl(options, null, null, 1, 2);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse(new String[]{"--opt1", "--opt1"});
        group.process(commandLine, commandLine.getArguments().listIterator());
    }

    @Test(expected = OptionException.class)
    public void testGroupUnexpectedOption() throws OptionException {
        List options = new ArrayList();
        options.add(createOption("opt1", false));
        Group group = new GroupImpl(options, null, null, 1, 1);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse(new String[]{"--opt2"});
        group.process(commandLine, commandLine.getArguments().listIterator());
    }

    @Test
    public void testGroupWithAnonymousArgument() {
        List options = new ArrayList();
        options.add(createOption("opt1", false, null, null, true));
        Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.emptySet(), group.getTriggers());
        assertNull(group.findOption("opt3"));
        assertTrue(group.getAnonymous().isEmpty());
    }

    @Test
    public void testGroupWithAnonymousArguments() {
        List options = new ArrayList();
        options.add(createArgument("arg1", "argValue1"));
        options.add(createArgument("arg2", "argValue2"));
        Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.emptySet(), group.getTriggers());
        assertTrue(group.getAnonymous().size() == 2);
    }

    private Option createOption(String longName, boolean hasArg) {
        return createOption(longName, longName, null, null, null, hasArg);
    }

    private Option createOption(String longName, String shortName, String description, int min, int max) {
        return createOption(longName, longName, description, min, max, null);
    }

    private Option createOption(String longName, String shortName, String description, int min, int max, Set<String> triggers) {
        return new OptionBuilder()
                .withLongOpt(longName)
                .withShortOpt(shortName)
                .withDescription(description)
                .withMinOccurs(min)
                .withMaxOccurs(max)
                .withOptionalArg(triggers != null)
                .withTriggers(triggers)
                .create();
    }

    private Argument createArgument(String longName, String description) {
        return new ArgumentBuilder()
                .withLongOpt(longName)
                .withDescription(description)
                .create();
    }
}