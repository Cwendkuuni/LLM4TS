package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    @Mock
    private WriteableCommandLine commandLine;

    @Mock
    private Option option1;

    @Mock
    private Option option2;

    @Mock
    private Argument argument1;

    @Mock
    private Argument argument2;

    private GroupImpl group;

    @Before
    public void setUp() {
        Mockito.when(option1.canProcess(commandLine, ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(option1.getTriggers()).thenReturn(Collections.singleton("option1"));
        Mockito.when(option1.validate(commandLine)).thenReturn(true);
        Mockito.when(option1.getDefaultValues()).thenReturn(Collections.emptyList());

        Mockito.when(option2.canProcess(commandLine, ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(option2.getTriggers()).thenReturn(Collections.singleton("option2"));
        Mockito.when(option2.validate(commandLine)).thenReturn(true);
        Mockito.when(option2.getDefaultValues()).thenReturn(Collections.emptyList());

        Mockito.when(argument1.canProcess(commandLine, ArgumentMatchers.anyList())).thenReturn(true);
        Mockito.when(argument1.getValue()).thenReturn("value1");

        Mockito.when(argument2.canProcess(commandLine, ArgumentMatchers.anyList())).thenReturn(true);
        Mockito.when(argument2.getValue()).thenReturn("value2");

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        List<Argument> anonymous = new ArrayList<>();
        anonymous.add(argument1);
        anonymous.add(argument2);

        Map<String, Option> optionMap = new HashMap<>();
        optionMap.put("option1", option1);
        optionMap.put("option2", option2);

        Set<String> prefixes = new HashSet<>();

        group = new GroupImpl(options, "group", "group description", 1, 3, prefixes, optionMap);
    }

    @Test
    public void testCanProcessTrue() {
        String arg = "option1";
        assertTrue(group.canProcess(commandLine, arg));
    }

    @Test
    public void testCanProcessFalse() {
        String arg = "invalid";
        assertFalse(group.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        assertNotNull(group.getPrefixes());
        assertTrue(group.getPrefixes().isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.containsAll(Collections.singletonList("option1")));
        assertTrue(triggers.containsAll(Collections.singletonList("option2")));
    }

    @Test
    public void testProcessDefault() {
        ListIterator arguments = Mockito.mock(ListIterator.class);
        group.process(commandLine, arguments);
        Mockito.verifyNoInteractions(commandLine, argument1, argument2, option1, option2);
    }

    @Test
    public void testProcessOption1() {
        String[] args = {"--option1"};
        Mockito.when(commandLine.getArgs()).thenReturn(args);

        ListIterator arguments = Mockito.mock(ListIterator.class);
        Mockito.when(arguments.next()).thenReturn(args[0]);
        Mockito.when(arguments.next()).thenReturn(null);

        group.process(commandLine, arguments);

        Mockito.verify(option1).process(commandLine, arguments);
        Mockito.verifyNoMoreInteractions(commandLine, argument1, argument2, option1, option2);
    }

    @Test
    public void testProcessOption2() {
        String[] args = {"--option1", "--option2"};
        Mockito.when(commandLine.getArgs()).thenReturn(args);

        ListIterator arguments = Mockito.mock(ListIterator.class);
        Mockito.when(arguments.next()).thenReturn("--option1");
        Mockito.when(arguments.next()).thenReturn("--option2");
        Mockito.when(arguments.next()).thenReturn(null);

        group.process(commandLine, arguments);

        Mockito.verify(option1).process(commandLine, arguments);
        Mockito.verify(option2).process(commandLine, arguments);
        Mockito.verifyNoMoreInteractions(commandLine, argument1, argument2, option1, option2);
    }

    @Test
    public void testProcessArgument() {
        String[] args = {"--option1", "value1", "--option2", "value2"};
        Mockito.when(commandLine.getArgs()).thenReturn(args);

        ListIterator arguments = Mockito.mock(ListIterator.class);
        Mockito.when(arguments.next()).thenReturn("--option1");
        Mockito.when(arguments.next()).thenReturn("value1");
        Mockito.when(arguments.next()).thenReturn("--option2");
        Mockito.when(arguments.next()).thenReturn("value2");
        Mockito.when(arguments.next()).thenReturn(null);

        group.process(commandLine, arguments);

        Mockito.verify(argument1).process(commandLine, arguments);
        Mockito.verify(argument2).process(commandLine, arguments);
        Mockito.verifyNoMoreInteractions(commandLine, argument1, argument2, option1, option2);
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidOption1() {
        String[] args = {"--invalid"};
        Mockito.when(commandLine.getArgs()).thenReturn(args);

        ListIterator arguments = Mockito.mock(ListIterator.class);
        Mockito.when(arguments.next()).thenReturn("--invalid");
        Mockito.when(arguments.next()).thenReturn(null);

        group.process(commandLine, arguments);

        Mockito.verify(commandLine).addOption(ArgumentMatchers.any(Option.class));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidOption2() {
        String[] args = {"--option1", "--invalid"};
        Mockito.when(commandLine.getArgs()).thenReturn(args);

        ListIterator arguments = Mockito.mock(ListIterator.class);
        Mockito.when(arguments.next()).thenReturn("--option1");
        Mockito.when(arguments.next()).thenReturn("--invalid");
        Mockito.when(arguments.next()).thenReturn(null);

        group.process(commandLine, arguments);

        Mockito.verify(commandLine).addOption(ArgumentMatchers.any(Option.class));
    }

    @Test
    public void testValidateDefault() {
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() {
        Mockito.when(commandLine.hasOption("option1")).thenReturn(false);

        group.validate(commandLine);

        Mockito.verify(commandLine).addOption(ArgumentMatchers.any(Option.class));
    }

    @Test(expected = OptionException.class)
    public void testValidateTooFew() {
        Mockito.when(commandLine.getArgs()).thenReturn(Collections.emptyList().toArray(new String[0]));

        group.validate(commandLine);

        Mockito.verify(commandLine).addOption(ArgumentMatchers.any(Option.class));
    }

    @Test(expected = OptionException.class)
    public void testValidateTooMany() {
        String[] args = {"--option1", "--option1"};
        Mockito.when(commandLine.getArgs()).thenReturn(args);

        group.validate(commandLine);

        Mockito.verify(commandLine).addOption(ArgumentMatchers.any(Option.class));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, new HashSet<>(), null);

        String expected = "group description\n" +
                "[OPTIONS]\n" +
                "  --option1\n" +
                "      description for option1\n" +
                "  --option2\n" +
                "      description for option2\n" +
                "  --\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        StringBuffer buffer = new StringBuffer();
        List helpLines = group.helpLines(0, new HashSet<>(), null);

        for (Object line : helpLines) {
            HelpLine helpLine = (HelpLine) line;
            helpLine.printHelp(buffer, "", null, null, null, false);
        }

        String expected = "group description\n" +
                "[OPTIONS]\n" +
                "  --option1\n" +
                "      description for option1\n" +
                "  --option2\n" +
                "      description for option2\n" +
                "\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testGetOptions() {
        List options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetAnonymous() {
        List anonymous = group.getAnonymous();
        assertEquals(2, anonymous.size());
        assertTrue(anonymous.contains(argument1));
        assertTrue(anonymous.contains(argument2));
    }

    @Test
    public void testFindOption() {
        Option found = group.findOption("option1");
        assertTrue(found != null);
        assertEquals(option1, found);

        found = group.findOption("option2");
        assertTrue(found != null);
        assertEquals(option2, found);

        found = group.findOption("invalid");
        assertNull(found);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        Mockito.verify(option1).defaults(commandLine);
        Mockito.verify(option2).defaults(commandLine);
        Mockito.verify(argument1).defaults(commandLine);
        Mockito.verify(argument2).defaults(commandLine);
    }

    @Test
    public void testFindOptionWithEmptyList() {
        GroupImpl emptyGroup = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        Option found = emptyGroup.findOption("option1");
        assertNull(found);
    }
}