import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    @Test
    public void testProcess() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator<String> args = Arrays.asList("--option", "value").listIterator();

        when(option.canProcess(commandLine, args)).thenReturn(true);
        when(option.process(commandLine, args)).thenReturn();

        option.process(commandLine, args);
        assertTrue(args.hasNext()); // check that the iterator was not consumed
    }

    @Test(expected = OptionException.class)
    public void testProcessWithException() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator<String> args = Arrays.asList("--option", "value").listIterator();

        when(option.canProcess(commandLine, args)).thenReturn(true);
        when(option.process(commandLine, args)).thenThrow(new OptionException("Test Exception"));

        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        option.defaults(commandLine);

        assertNotNull(commandLine.getDefaults());
    }

    @Test
    public void testCanProcess() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator<String> args = Arrays.asList("--option", "value").listIterator();

        when(option.canProcess(commandLine, args)).thenReturn(true);
        when(option.canProcess(commandLine, args)).thenReturn(false);

        assertTrue(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Option option = mock(Option.class);
        Set<String> triggers = new HashSet<>();
        triggers.add("--option");
        triggers.add("-o");

        when(option.getTriggers()).thenReturn(triggers);

        Set<String> result = option.getTriggers();

        assertNotNull(result);
        assertTrue(result.containsAll(triggers));
    }

    @Test
    public void testGetPrefixes() {
        Option option = mock(Option.class);
        Set<String> prefixes = new LinkedHashSet<>();
        prefixes.add("--");
        prefixes.add("-");

        when(option.getPrefixes()).thenReturn(prefixes);

        Set<String> result = option.getPrefixes();

        assertNotNull(result);
        assertTrue(result.containsAll(prefixes));
    }

    @Test(expected = NullPointerException.class)
    public void testValidateNullCommandLine() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = null;

        option.validate(commandLine);
    }

    @Test
    public void testValidate() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = mock(Option.class);
        int depth = 0;
        Set<String> helpSettings = new HashSet<>();
        Comparator<Option> comp = (o1, o2) -> 0;

        List<Object> result = option.helpLines(depth, helpSettings, comp);

        assertNotNull(result);
    }

    @Test
    public void testAppendUsage() {
        Option option = mock(Option.class);
        StringBuffer buffer = new StringBuffer();
        Set<String> helpSettings = new HashSet<>();
        Comparator<Option> comp = (o1, o2) -> 0;

        option.appendUsage(buffer, helpSettings, comp);

        assertNotNull(buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = mock(Option.class);
        String preferredName = "option";

        when(option.getPreferredName()).thenReturn(preferredName);

        String result = option.getPreferredName();

        assertNotNull(result);
        assertEquals(preferredName, result);
    }

    @Test
    public void testGetDescription() {
        Option option = mock(Option.class);
        String description = "description";

        when(option.getDescription()).thenReturn(description);

        String result = option.getDescription();

        assertNotNull(result);
        assertEquals(description, result);
    }

    @Test
    public void testgetId() {
        Option option = mock(Option.class);
        int id = 1;

        when(option.getId()).thenReturn(id);

        int result = option.getId();

        assertNotNull(result);
        assertEquals(id, result);
    }

    @Test
    public void testFindOptionNullTrigger() {
        Option option = mock(Option.class);
        String trigger = null;

        Option result = option.findOption(trigger);

        assertNull(result);
    }

    @Test
    public void testFindOption() {
        Option parentOption = mock(Option.class);
        Option option = mock(Option.class);
        String trigger = "option";

        when(parentOption.findOption(trigger)).thenReturn(option);

        Option result = parentOption.findOption(trigger);

        assertNotNull(result);
        assertEquals(option, result);
    }

    @Test
    public void testIsRequired() {
        Option option = mock(Option.class);
        boolean isRequired = false;

        when(option.isRequired()).thenReturn(isRequired);

        boolean result = option.isRequired();

        assertNotNull(result);
        assertEquals(isRequired, result);
    }

    @Test
    public void testGetParent() {
        Option parentOption = mock(Option.class);
        Option option = mock(Option.class);

        when(option.getParent()).thenReturn(parentOption);

        Option result = option.getParent();

        assertNotNull(result);
        assertEquals(parentOption, result);
    }

    @Test
    public void testSetParent() {
        Option option = mock(Option.class);
        Option parentOption = mock(Option.class);

        option.setParent(parentOption);

        Option result = option;

        verify(option).setParent(parentOption);
        assertEquals(parentOption, result.getParent());
    }
}