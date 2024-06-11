package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    private GroupImpl group;

    @Before
    public void setUp() {
        final List<Option> options = new ArrayList<>();
        options.add(Mockito.spy(new Option("opt1", "opt1", true, "description1")));
        options.add(Mockito.spy(new Option("opt2", "opt2", false, "description2")));

        group = new GroupImpl(options, null, null, 1, 1, false);
    }

    @Test
    public void testCanProcess_NoArg_WithValidOption() {
        Mockito.when(commandLine.hasOption("opt1")).thenReturn(true);

        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_NoArg_WithValidOptionAndBursting() {
        Mockito.when(commandLine.hasOption("opt1")).thenReturn(false);

        final Option opt2 = group.getOptions().get(1);

        assertTrue(group.canProcess(commandLine, null, opt2.getKey()));
    }

    @Test
    public void testCanProcess_NoArg_WithInvalidOption() {
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_NoArg_WithInvalidOptionAndBursting() {
        Mockito.when(commandLine.hasOption("opt1")).thenReturn(false);

        group.canProcess(commandLine, null, "invalidOption");
    }

    @Test
    public void testCanProcess_WithArg_WithValidOption() {
        Mockito.when(commandLine.hasOption("opt1")).thenReturn(false);
        Mockito.when(commandLine.getArgument(ArgumentMatchers.equalTo(0))).thenReturn("arg");

        final Option opt1 = group.getOptions().get(0);

        assertTrue(group.canProcess(commandLine, "arg", opt1.getKey()));
    }

    @Test
    public void testCanProcess_WithArg_WithInvalidOption() {
        Mockito.when(commandLine.getArgument(ArgumentMatchers.equalTo(0))).thenReturn("arg");

        assertFalse(group.canProcess(commandLine, "arg"));
    }

    @Test
    public void testCanProcess_WithArg_WithInvalidOptionAndBursting() {
        Mockito.when(commandLine.getArgument(ArgumentMatchers.equalTo(0))).thenReturn("arg");

        final Option opt1 = group.getOptions().get(0);

        assertThrows(OptionException.class, () -> group.canProcess(commandLine, "arg", "invalidOption"));
    }

    @Test
    public void testProcess_WithValidOptions() {
        final String[] args = {"--opt1"};

        group.process(commandLine, Mockito.mock(ListIterator.class));

        Mockito.verify(commandLine, Mockito.times(1)).parseArgument("arg", null, 0);
        Mockito.verify(commandLine, Mockito.times(1)).setValue(ArgumentMatchers.eq("arg"), ArgumentMatchers.eq(0));
        Mockito.verify(commandLine, Mockito.times(1)).setValue(ArgumentMatchers.eq("arg"), ArgumentMatchers.eq(1));
    }

    @Test
    public void testProcess_WithInvalidOption() {
        final String[] args = {"--invalidOption"};

        group.process(commandLine, Mockito.mock(ListIterator.class));

        Mockito.verify(commandLine, Mockito.never()).parseArgument(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.anyInt());
        Mockito.verify(commandLine, Mockito.never()).setValue(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
    }

    @Test
    public void testProcess_WithInvalidOptionAndBursting() {
        final String[] args = {"--invalidOption"};

        final Option opt1 = group.getOptions().get(0);

        group.process(commandLine, Mockito.mock(ListIterator.class), opt1.getKey());

        Mockito.verify(commandLine, Mockito.never()).parseArgument(ArgumentMatchers.anyString(), ArgumentMatchers.any(Class.class), ArgumentMatchers.anyInt());
        Mockito.verify(commandLine, Mockito.never()).setValue(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
    }

    @Test
    public void testValidate_WithValidOptions() {
        Mockito.when(commandLine.hasOption("opt1")).thenReturn(true);
        Mockito.when(commandLine.hasOption("opt2")).thenReturn(true);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithInvalidNumberOfOptions() {
        Mockito.when(commandLine.hasOption("opt1")).thenReturn(true);

        group.validate(commandLine);

        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.eq("opt2"), ArgumentMatchers.eq("option2"), ArgumentMatchers.eq(false), ArgumentMatchers.eq("description2"));
        Mockito.verify(commandLine, Mockito.times(1)).parseOption("opt2", ArgumentMatchers.eq(false), ArgumentMatchers.eq("option2"), ArgumentMatchers.eq("description2"));
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithInvalidOption() {
        Mockito.when(commandLine.hasOption("opt1")).thenReturn(true);
        Mockito.when(commandLine.hasOption("opt3")).thenReturn(true);

        group.validate(commandLine);

        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.eq("opt3"), ArgumentMatchers.eq("option3"), ArgumentMatchers.eq(false), ArgumentMatchers.eq("description3"));
        Mockito.verify(commandLine, Mockito.times(1)).parseOption("opt3", ArgumentMatchers.eq(false), ArgumentMatchers.eq("option3"), ArgumentMatchers.eq("description3"));
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithInvalidOptionAndBursting() {
        Mockito.when(commandLine.hasOption("opt1")).thenReturn(true);
        Mockito.when(commandLine.hasOption("opt3")).thenReturn(true);

        group.validate(commandLine);

        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.eq("opt3"), ArgumentMatchers.eq("option3"), ArgumentMatchers.eq(false), ArgumentMatchers.eq("description3"));
        Mockito.verify(commandLine, Mockito.times(1)).parseOption("opt3", ArgumentMatchers.eq(false), ArgumentMatchers.eq("option3"), ArgumentMatchers.eq("description3"));
    }

    @Test
    public void testGetOptions() {
        assertEquals(2, group.getOptions().size());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(0, group.getAnonymous().size());
    }

    @Test
    public void testFindOption_WithValidTrigger() {
        final Option found = group.findOption("opt1");

        assertNotNull(found);
    }

    @Test
    public void testFindOption_WithInvalidTrigger() {
        assertNull(group.findOption("invalidOption"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        Mockito.verify(commandLine, Mockito.times(2)).clearParsedOptions();
        Mockito.verify(commandLine, Mockito.times(2)).clearValues();
    }
}