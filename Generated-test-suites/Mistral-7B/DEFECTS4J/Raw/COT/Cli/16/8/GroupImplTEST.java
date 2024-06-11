package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    private WriteableCommandLine commandLine;
    private List<Option> options;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        options = Arrays.asList(
                Mockito.mock(Option.class),
                Mockito.mock(Option.class),
                Mockito.mock(Argument.class)
        );
    }

    @Test
    public void testGroupImplConstructor() {
        final List<Option> groupOptions = new ArrayList<>();
        groupOptions.addAll(options.subList(0, 2));

        final GroupImpl group = new GroupImpl(groupOptions, null, null, 0, 1);

        assertEquals(null, group.getName());
        assertEquals(null, group.getDescription());
        assertEquals(Collections.unmodifiableList(groupOptions), group.getOptions());
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertFalse(group.isRequired());
        assertNotNull(group.getOptions());
        assertNull(group.getAnonymous());
        assertEquals(Collections.emptySet(), group.getPrefixes());
        assertEquals(Collections.emptySet(), group.getTriggers());
    }

    @Test
    public void testCanProcess_ValidOption() {
        final String option = "option1";
        final Option option1 = Mockito.mock(Option.class);
        Mockito.when(option1.getLongOpt()).thenReturn(option);
        options.set(0, option1);

        assertTrue(new GroupImpl(options, null, null, 0, 1).canProcess(commandLine, option));
    }

    @Test
    public void testCanProcess_InvalidOption() {
        final String invalidOption = "invalidOption";

        assertFalse(new GroupImpl(options, null, null, 0, 1).canProcess(commandLine, invalidOption));
    }

    @Test
    public void testCanProcess_AnonymousArgument() {
        final String argument = "argument";

        assertTrue(new GroupImpl(options, null, null, 0, 1).canProcess(commandLine, argument));
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption() {
        final String invalidOption = "invalidOption";

        new GroupImpl(options, null, null, 0, 1).process(commandLine, commandLine.listIterator());
    }

    @Test
    public void testProcess_ValidOption() {
        final String option = "option1";
        final Option option1 = Mockito.mock(Option.class);
        Mockito.when(option1.getLongOpt()).thenReturn(option);

        Mockito.when(option1.process(commandLine, commandLine.listIterator())).thenReturn(true);

        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);
        group.process(commandLine, commandLine.listIterator());

        Mockito.verify(option1, Mockito.times(1)).process(commandLine, commandLine.listIterator());
    }

    @Test
    public void testProcess_MultipleValidOptions() {
        final String option1 = "option1";
        final String option2 = "option2";

        final Option option1Mock = Mockito.mock(Option.class);
        Mockito.when(option1Mock.getLongOpt()).thenReturn(option1);
        Mockito.when(option1Mock.process(commandLine, commandLine.listIterator())).thenReturn(true);

        final Option option2Mock = Mockito.mock(Option.class);
        Mockito.when(option2Mock.getLongOpt()).thenReturn(option2);
        Mockito.when(option2Mock.process(commandLine, commandLine.listIterator())).thenReturn(true);

        options.set(0, option1Mock);
        options.set(1, option2Mock);

        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);

        group.process(commandLine, commandLine.listIterator());

        Mockito.verify(option1Mock, Mockito.times(1)).process(commandLine, commandLine.listIterator());
        Mockito.verify(option2Mock, Mockito.times(1)).process(commandLine, commandLine.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOptionAfterValidOption() {
        final String option1 = "option1";
        final String invalidOption = "invalidOption";

        final Option option1Mock = Mockito.mock(Option.class);
        Mockito.when(option1Mock.getLongOpt()).thenReturn(option1);
        Mockito.when(option1Mock.process(commandLine, commandLine.listIterator())).thenReturn(true);

        options.set(0, option1Mock);

        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);

        group.process(commandLine, commandLine.listIterator());
        group.process(commandLine, commandLine.listIterator());

        Mockito.verify(option1Mock, Mockito.times(1)).process(commandLine, commandLine.listIterator());

        Mockito.verify(commandLine, Mockito.never()).looksLikeOption(invalidOption);
    }

    @Test(expected = OptionException.class)
    public void testProcess_AnonymousArgumentBeforeValidOption() {
        final String argument = "argument";
        final String option1 = "option1";

        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);

        group.process(commandLine, commandLine.listIterator());
        group.process(commandLine, commandLine.listIterator());
        group.process(commandLine, commandLine.listIterator());

        Mockito.verify(commandLine, Mockito.times(1)).looksLikeOption(argument);

        final Option option1Mock = Mockito.mock(Option.class);
        Mockito.when(option1Mock.getLongOpt()).thenReturn(option1);

        options.set(0, option1Mock);

        group.process(commandLine, commandLine.listIterator());

        Mockito.verify(option1Mock, Mockito.times(1)).process(commandLine, commandLine.listIterator());
        Mockito.verify(commandLine, Mockito.never()).looksLikeOption(argument);
    }

    @Test(expected = OptionException.class)
    public void testProcess_AnonymousArgumentAfterValidOptions() {
        final String argument = "argument";
        final String option1 = "option1";
        final String option2 = "option2";

        final Option option1Mock = Mockito.mock(Option.class);
        Mockito.when(option1Mock.getLongOpt()).thenReturn(option1);
        Mockito.when(option1Mock.process(commandLine, commandLine.listIterator())).thenReturn(true);

        final Option option2Mock = Mockito.mock(Option.class);
        Mockito.when(option2Mock.getLongOpt()).thenReturn(option2);
        Mockito.when(option2Mock.process(commandLine, commandLine.listIterator())).thenReturn(true);

        options.set(0, option1Mock);
        options.set(1, option2Mock);

        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);

        group.process(commandLine, commandLine.listIterator());
        group.process(commandLine, commandLine.listIterator());
        group.process(commandLine, commandLine.listIterator());

        Mockito.verify(option1Mock, Mockito.times(1)).process(commandLine, commandLine.listIterator());
        Mockito.verify(option2Mock, Mockito.times(1)).process(commandLine, commandLine.listIterator());

        Mockito.verify(commandLine, Mockito.never()).looksLikeOption(argument);

        group.process(commandLine, commandLine.listIterator());

        Mockito.verify(commandLine, Mockito.times(1)).looksLikeOption(argument);
        Mockito.verify(commandLine, Mockito.never()).looksLikeOption(option1);
        Mockito.verify(commandLine, Mockito.never()).looksLikeOption(option2);
    }

    @Test
    public void testValidate() throws OptionException {
        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);
        group.validate(commandLine);

        Mockito.verify(commandLine, Mockito.times(3)).hasOption(Mockito.anyString());
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidNumberOfOptions() throws OptionException {
        final GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        group.validate(commandLine);
    }

    @Test
    public void testValidate_ValidNumberOfOptions() throws OptionException {
        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);
        group.validate(commandLine);

        Mockito.verifyNoMoreInteractions(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        final String groupName = "groupName";
        final GroupImpl group = new GroupImpl(options, groupName, null, 0, 1);

        assertEquals(groupName, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        final String groupDescription = "groupDescription";
        final GroupImpl group = new GroupImpl(options, null, groupDescription, 0, 1);

        assertEquals(groupDescription, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final String groupName = "groupName";
        final String usage = "groupUsage";

        final GroupImpl group = new GroupImpl(options, groupName, null, 0, 1);
        final StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals(usage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final String groupName = "groupName";
        final String helpLine = "helpLine";

        final GroupImpl group = new GroupImpl(options, groupName, null, 0, 1);
        final List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);

        assertTrue(helpLines.size() > 0);
        assertEquals(helpLine, helpLines.get(0).getShortOpt());
        assertEquals(helpLine, helpLines.get(0).getLongOpt());
        assertEquals(groupName, helpLines.get(0).getOptionSpec().getName());
    }

    @Test
    public void testGetMinimum() {
        final GroupImpl group = new GroupImpl(options, null, null, 5, 5);

        assertEquals(5, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        final GroupImpl group = new GroupImpl(options, null, null, 0, 5);

        assertEquals(5, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        final GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        assertTrue(group.isRequired());
    }

    @Test
    public void testGetOptions() {
        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);

        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        final List<Argument> anonymous = new ArrayList<>();
        anonymous.add(Mockito.mock(Argument.class));

        final GroupImpl group = new GroupImpl(options, null, null, 0, 1);
        group.setAnonymous(anonymous);

        assertEquals(anonymous, group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        final String optionName = "optionName";
        final Option option = Mockito.mock(Option.class);
        Mockito.when(option.getLongOpt()).thenReturn(optionName);

        options.add(option);

        final Option foundOption = new GroupImpl(options, null, null, 0, 1).findOption(optionName);

        assertSame(option, foundOption);
    }

    @Test
    public void testFindOption_NullOptionName() {
        final Option foundOption = new GroupImpl(options, null, null, 0, 1).findOption(null);

        assertNull(foundOption);
    }

    @Test
    public void testFindOption_InvalidOptionName() {
        final String invalidOptionName = "invalidOptionName";

        final Option foundOption = new GroupImpl(options, null, null, 0, 1).findOption(invalidOptionName);

        assertNull(foundOption);
    }
}