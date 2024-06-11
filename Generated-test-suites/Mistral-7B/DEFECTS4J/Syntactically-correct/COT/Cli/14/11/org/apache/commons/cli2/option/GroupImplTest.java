package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    @Mock
    private WriteableCommandLine commandLine;

    private Group group;
    private List<Option> options;
    private Option option1;
    private Option option2;
    private Argument arg1;
    private Argument arg2;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        option1 = Mockito.mock(Option.class);
        option2 = Mockito.mock(Option.class);
        arg1 = Mockito.mock(Argument.class);
        arg2 = Mockito.mock(Argument.class);
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, "testGroup", "Test Group", 1, Integer.MAX_VALUE);
    }

    @Test
    public void testConstructor() {
        assertEquals("testGroup", group.getPreferredName());
        assertEquals("Test Group", group.getDescription());
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testCanProcess() {
        Mockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());
        Mockito.when(commandLine.looksLikeOption(ArgumentMatchers.anyString())).thenReturn(false);

        Mockito.when(option1.canProcess(commandLine, ArgumentMatchers.eq("arg1"))).thenReturn(true);
        Mockito.when(option1.canProcess(commandLine, ArgumentMatchers.eq("arg2"))).thenReturn(false);

        Mockito.when(option2.canProcess(commandLine, ArgumentMatchers.eq("arg1"))).thenReturn(false);
        Mockito.when(option2.canProcess(commandLine, ArgumentMatchers.eq("arg2"))).thenReturn(true);

        assertTrue(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "arg2"));
        assertFalse(group.canProcess(commandLine, "arg3"));
    }

    @Test
    public void testProcess() {
        Mockito.when(commandLine.getArguments()).thenReturn(Arrays.asList("arg1", "arg2"));

        Mockito.when(option1.process(commandLine, ArgumentMatchers.any(ListIterator.class))).thenCallRealMethod();
        Mockito.when(option2.process(commandLine, ArgumentMatchers.any(ListIterator.class))).thenCallRealMethod();

        Mockito.when(arg1.process(commandLine, ArgumentMatchers.any(ListIterator.class))).thenCallRealMethod();
        Mockito.when(arg2.process(commandLine, ArgumentMatchers.any(ListIterator.class))).thenCallRealMethod();

        group.process(commandLine, commandLine.listIterator());

        assertEquals(1, group.getOptions().size());
        assertEquals(1, group.getAnonymous().size());

        Mockito.verify(option1, Mockito.times(1)).process(commandLine, ArgumentMatchers.any(ListIterator.class));
        Mockito.verify(option2, Mockito.times(1)).process(commandLine, ArgumentMatchers.any(ListIterator.class));
        Mockito.verify(arg1, Mockito.times(1)).process(commandLine, ArgumentMatchers.any(ListIterator.class));
        Mockito.verify(arg2, Mockito.times(1)).process(commandLine, ArgumentMatchers.any(ListIterator.class));
    }

    @Test
    public void testValidate() {
        Mockito.when(commandLine.getOptions()).thenReturn(Collections.emptyMap());

        Mockito.when(option1.isRequired()).thenReturn(false);
        Mockito.when(option2.isRequired()).thenReturn(true);

        Mockito.when(commandLine.hasOption(ArgumentMatchers.eq("arg1"))).thenReturn(false);
        Mockito.when(commandLine.hasOption(ArgumentMatchers.eq("arg2"))).thenReturn(true);

        assertThrows(OptionException.class, () -> group.validate(commandLine));

        Mockito.verify(option1, Mockito.times(1)).validate(commandLine);
        Mockito.verify(option2, Mockito.times(1)).validate(commandLine);

        Mockito.verify(commandLine, Mockito.times(1)).validate();
    }

    @Test
    public void testGetPrefixes() {
        Set<String> expectedPrefixes = new HashSet<>();
        expectedPrefixes.add("test");

        Mockito.when(option1.getTriggers()).thenReturn(Collections.singleton("test"));
        Mockito.when(option2.getTriggers()).thenReturn(Collections.singleton("test"));

        Set<String> prefixes = group.getPrefixes();

        assertEquals(expectedPrefixes, prefixes);
    }

    @Test
    public void testGetTriggers() {
        Map<String, Option> expectedTriggers = new TreeMap<>();
        expectedTriggers.put("test1", option1);
        expectedTriggers.put("test2", option2);

        Mockito.when(option1.getTriggers()).thenReturn(Arrays.asList("test1"));
        Mockito.when(option2.getTriggers()).thenReturn(Arrays.asList("test2"));

        Set<String> triggers = group.getTriggers();

        assertEquals(expectedTriggers, group.optionMap);
    }
}