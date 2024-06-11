import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GroupImpl.class)
public class GroupImplTest {
    private GroupImpl group;
    private CommandLine commandLine;
    private Option option1;
    private Option option2;
    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        option1 = Mockito.mock(Option.class);
        option2 = Mockito.mock(Option.class);
        writeableCommandLine = Mockito.mock(WriteableCommandLine.class);
        commandLine = PowerMockito.mock(CommandLine.class);

        List<Option> list = new ArrayList<>();
        list.add(option1);
        list.add(option2);

        group = new GroupImpl(list, "Group", null, 1, 1, false);
    }

    @Test
    public void testCanProcess_WhenOptionsMatch_ReturnsTrue() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--option1"));
        PowerMockito.when(option1.canProcess(writeableCommandLine, "option1")).thenReturn(true);

        assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcess_WhenOptionsDoNotMatch_ReturnsFalse() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--anotherOption"));

        assertFalse(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcess_WhenArgumentIsANamedOption_ReturnsTrue() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--group:option1"));
        PowerMockito.when(option1.canProcess(writeableCommandLine, "option1")).thenReturn(true);

        assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcess_WhenArgumentIsAnArgument_ReturnsFalse() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("arg"));

        assertFalse(group.canProcess(commandLine, "arg"));
    }

    @Test
    public void testCanProcess_WhenArgumentIsAValidPrefix_ReturnsTrue() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--group:"));

        assertTrue(group.canProcess(commandLine, "--group"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_WhenArgumentIsNotFoundInTheList_ThrowsOptionException() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--unknownOption"));

        group.canProcess(commandLine, "unknownOption");
    }

    @Test
    public void testGetName_ReturnsNameOfGroup() {
        assertEquals("Group", group.getName());
    }

    @Test
    public void testGetDescription_ReturnsDescriptionOfGroup() {
        assertNull(group.getDescription());
    }

    @Test
    public void testGetOptions_ReturnsListOfOptionsInGroup() {
        List<Option> options = group.getOptions();

        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetAnonymous_ReturnsListOfAnonymousArguments() {
        List<Option> anonymous = group.getAnonymous();

        assertEquals(0, anonymous.size());
    }

    @Test
    public void testGetMinimum_ReturnsMinimumNumberOfOptions() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum_ReturnsMaximumNumberOfOptions() {
        assertEquals(1, group.getMaximum());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMinimum_WhenMinimumIsNegative_ThrowsIllegalStateException() {
        group = new GroupImpl(Collections.emptyList(), null, null, -1, 1, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMaximum_WhenMaximumIsNegative_ThrowsIllegalStateException() {
        group = new GroupImpl(Collections.emptyList(), null, null, 1, -1, false);
    }

    @Test
    public void testProcess_WhenAllValidOptionsAreFound_ProcessesAllOptions() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--option1 --option2"));

        group.process(commandLine, Mockito.mock(ListIterator.class));

        PowerMockito.verify(option1).process(writeableCommandLine, Mockito.any(ListIterator.class));
        PowerMockito.verify(option2).process(writeableCommandLine, Mockito.any(ListIterator.class));
    }

    @Test
    public void testProcess_WhenSomeValidOptionsAreNotFound_DoesNotProcessMissingOptions() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--option1"));

        group.process(commandLine, Mockito.mock(ListIterator.class));

        PowerMockito.verify(option1, Mockito.times(1)).process(writeableCommandLine, Mockito.any(ListIterator.class));
        PowerMockito.verify(option2, Mockito.never()).process(writeableCommandLine, Mockito.any(ListIterator.class));
    }

    @Test
    public void testProcess_WhenAnonymousArgumentsArePresent_ProcessesAnonymousArguments() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--group:arg1 arg2"));

        group.process(commandLine, Mockito.mock(ListIterator.class));

        PowerMockito.verify(option1, Mockito.never()).process(writeableCommandLine, Mockito.any(ListIterator.class));
        PowerMockito.verify(option2, Mockito.never()).process(writeableCommandLine, Mockito.any(ListIterator.class));

        PowerMockito.verify(option1, Mockito.never()).process(writeableCommandLine, Mockito.any(ListIterator.class));
        PowerMockito.verify(option2, Mockito.never()).process(writeableCommandLine, Mockito.any(ListIterator.class));

        PowerMockito.verify(option1, Mockito.never()).validate(writeableCommandLine);
        PowerMockito.verify(option2, Mockito.never()).validate(writeableCommandLine);

        PowerMockito.verify(option1.findArgument(Mockito.eq("arg1")), Mockito.times(1)).process(writeableCommandLine, Mockito.any(ListIterator.class));
        PowerMockito.verify(option2.findArgument(Mockito.eq("arg2")), Mockito.times(1)).process(writeableCommandLine, Mockito.any(ListIterator.class));
    }

    @Test(expected = OptionException.class)
    public void testProcess_WhenSomeValidOptionsAreNotFoundAndAnOptionRequiresArgument_ThrowsOptionException() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--option1"));
        Option option1WithArgument = PowerMockito.mock(Option.class);
        PowerMockito.when(option1.canProcess(writeableCommandLine, "option1")).thenReturn(true);
        PowerMockito.when(option1.findArgument(Mockito.eq("arg1"))).thenReturn(option1WithArgument);
        PowerMockito.when(option1WithArgument.validate(writeableCommandLine)).thenThrow(new OptionException("Exception"));

        group.process(commandLine, Mockito.mock(ListIterator.class));

        PowerMockito.verify(option1, Mockito.times(1)).process(writeableCommandLine, Mockito.any(ListIterator.class));
        PowerMockito.verify(option1WithArgument, Mockito.never()).process(writeableCommandLine, Mockito.any(ListIterator.class));

        PowerMockito.verify(option1, Mockito.never()).validate(writeableCommandLine);
        PowerMockito.verify(option1WithArgument, Mockito.never()).validate(writeableCommandLine);

        PowerMockito.verify(option1, Mockito.times(1)).validate(writeableCommandLine);

        fail();
    }

    @Test
    public void testValidate_WhenAllValidOptionsAreFound_ValidatesAllOptions() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--option1 --option2"));

        group.validate(commandLine);

        PowerMockito.verify(option1, Mockito.times(1)).validate(writeableCommandLine);
        PowerMockito.verify(option2, Mockito.times(1)).validate(writeableCommandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_WhenSomeValidOptionsAreNotFound_ThrowsOptionException() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--option1"));

        group.validate(commandLine);

        PowerMockito.verify(option1, Mockito.times(1)).validate(writeableCommandLine);
        PowerMockito.verify(option2, Mockito.never()).validate(writeableCommandLine);

        PowerMockito.verify(option1, Mockito.never()).process(writeableCommandLine, Mockito.any(ListIterator.class));
        PowerMockito.verify(option2, Mockito.never()).process(writeableCommandLine, Mockito.any(ListIterator.class));

        PowerMockito.verify(option1, Mockito.times(1)).validate(writeableCommandLine);

        fail();
    }

    @Test(expected = OptionException.class)
    public void testValidate_WhenSomeValidOptionsHaveInvalidArguments_ThrowsOptionException() {
        PowerMockito.when(commandLine.getArgList()).thenReturn(Collections.singletonList("--option1 arg"));
        PowerMockito.when(option1.validate(writeableCommandLine)).thenThrow(new OptionException("Exception"));

        group.validate(commandLine);

        PowerMockito.verify(option1, Mockito.times(1)).validate(writeableCommandLine);

        fail();
    }
}