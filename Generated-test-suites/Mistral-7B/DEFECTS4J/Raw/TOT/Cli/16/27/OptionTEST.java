import org.apache.commons.cli2.*;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OptionTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        option = Mockito.mock(Option.class);
    }

    @Test
    public void testProcess() throws OptionException {
        List<String> args = Arrays.asList("--option", "value").subList(0, 2);
        Mockito.when(option.canProcess(commandLine, args.listIterator())).thenReturn(true);
        option.process(commandLine, args.listIterator());
        Mockito.verify(commandLine).addOption(option);
        Mockito.verify(commandLine).setValue(option, "value");
    }

    @Test
    public void testProcess_cannotProcess() throws OptionException {
        List<String> args = Arrays.asList("--invalid-option", "value").subList(0, 2);
        Mockito.when(option.canProcess(commandLine, args.listIterator())).thenReturn(false);
        Mockito.doThrow(OptionException.class).when(option).process(commandLine, args.listIterator());
        assertThrows(OptionException.class, () -> option.process(commandLine, args.listIterator()));
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        Mockito.verify(commandLine, Mockito.never()).setValue(option, Mockito.anyString());
    }

    @Test
    public void testCanProcess() throws OptionException {
        Set<String> triggers = new LinkedHashSet<>(Arrays.asList("--option"));
        Set<String> prefixes = new LinkedHashSet<>(Arrays.asList("--"));
        Mockito.when(option.getTriggers()).thenReturn(triggers);
        Mockito.when(option.getPrefixes()).thenReturn(prefixes);
        Mockito.when(option.canProcess(commandLine, Mockito.any(String.class))).thenReturn(true);
        assertTrue(option.canProcess(commandLine, "value"));
    }

    @Test
    public void testCanProcess_cannotProcess() throws OptionException {
        Set<String> triggers = new LinkedHashSet<>(Arrays.asList("--option"));
        Set<String> prefixes = new LinkedHashSet<>(Arrays.asList("--"));
        Mockito.when(option.getTriggers()).thenReturn(triggers);
        Mockito.when(option.getPrefixes()).thenReturn(prefixes);
        Mockito.when(option.canProcess(commandLine, Mockito.any(String.class))).thenReturn(false);
        assertFalse(option.canProcess(commandLine, "value"));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new LinkedHashSet<>(Arrays.asList("--option"));
        Mockito.when(option.getTriggers()).thenReturn(triggers);
        assertEquals(triggers, option.getTriggers());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = new LinkedHashSet<>(Arrays.asList("--"));
        Mockito.when(option.getPrefixes()).thenReturn(prefixes);
        assertEquals(prefixes, option.getPrefixes());
    }

    @Test
    public void testValidate() {
        Mockito.doThrow(OptionException.class).when(option).validate(commandLine);
        assertThrows(OptionException.class, () -> option.validate(commandLine));
    }

    @Test
    public void testHelpLines() {
        Set<String> helpSettings = new LinkedHashSet<>();
        Comparator<Option> comp = (o1, o2) -> o1.getId() - o2.getId();
        List<HelpLine> helpLines = option.helpLines(1, helpSettings, comp);
        Mockito.verify(option).appendUsage(Mockito.any(StringBuffer.class), helpSettings, comp);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("--option", helpLine.getOptionSpec().getLongOpt());
        assertEquals("option", helpLine.getOptionSpec().getOpt());
        assertEquals("Option description", helpLine.getShortDescription());
        assertEquals("Option description\n", helpLine.getLongDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<String> helpSettings = new LinkedHashSet<>();
        Comparator<Option> comp = (o1, o2) -> o1.getId() - o2.getId();
        option.appendUsage(buffer, helpSettings, comp);
        assertEquals("Option description\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Mockito.when(option.getPreferredName()).thenReturn("option");
        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Mockito.when(option.getDescription()).thenReturn("Option description");
        assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testgetId() {
        Mockito.when(option.getId()).thenReturn(1);
        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option1 = Mockito.mock(Option.class);
        Option option2 = Mockito.mock(Option.class);
        option.findOption("trigger1").equals(option1);
        Mockito.when(option.findOption("trigger2")).thenReturn(option2);
        assertEquals(option2, option.findOption("trigger2"));
        assertNull(option.findOption("trigger3"));
    }

    @Test
    public void testIsRequired() {
        Mockito.when(option.isRequired()).thenReturn(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testHasParent() {
        Option parentOption = Mockito.mock(Option.class);
        Mockito.when(option.getParent()).thenReturn(parentOption);
        assertEquals(parentOption, option.getParent());
    }

    @Test
    public void testSetParent() {
        Option parentOption = Mockito.mock(Option.class);
        option.setParent(parentOption);
        assertEquals(parentOption, option.getParent());
    }
}