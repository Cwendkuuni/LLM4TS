import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testProcess() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator args = mock(ListIterator.class);

        try {
            option.process(commandLine, args);
            verify(commandLine).addOption(any());
        } catch (OptionException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = OptionException.class)
    public void testProcessWithException() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator args = mock(ListIterator.class);

        when(args.next()).thenReturn("invalid_argument");

        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        option.defaults(commandLine);

        verify(commandLine).defaultValue("default_value");
    }

    @Test
    public void testCanProcess() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator arguments = mock(ListIterator.class);

        when(arguments.next()).thenReturn("valid_argument");

        assertTrue(option.canProcess(commandLine, arguments));

        when(arguments.next()).thenReturn("invalid_argument");

        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testGetTriggers() {
        Option option = mock(Option.class);

        Set triggers = option.getTriggers();

        assertNotNull(triggers);
    }

    @Test
    public void testGetPrefixes() {
        Option option = mock(Option.class);

        Set prefixes = option.getPrefixes();

        assertNotNull(prefixes);
    }

    @Test(expected = OptionException.class)
    public void testValidate() {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = mock(Option.class);
        int depth = 1;
        Set helpSettings = mock(Set.class);
        Comparator comp = mock(Comparator.class);

        List helpLines = option.helpLines(depth, helpSettings, comp);

        assertNotNull(helpLines);
    }

    @Test
    public void testAppendUsage() {
        Option option = mock(Option.class);
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = mock(Set.class);
        Comparator comp = mock(Comparator.class);

        option.appendUsage(buffer, helpSettings, comp);

        assertTrue(buffer.length() > 0);
    }

    @Test
    public void testGetPreferredName() {
        Option option = mock(Option.class);

        String name = option.getPreferredName();

        assertNotNull(name);
    }

    @Test
    public void testGetDescription() {
        Option option = mock(Option.class);

        String description = option.getDescription();

        assertNotNull(description);
    }

    @Test
    public void testGetId() {
        Option option = mock(Option.class);

        int id = option.getId();

        assertNotEquals(-1, id);
    }

    @Test
    public void testFindOption() {
        Option parentOption = mock(Option.class);
        Option childOption = mock(Option.class);
        Option option = mock(Option.class);

        when(childOption.findOption("trigger")).thenReturn(option);
        when(option.getParent()).thenReturn(parentOption);
        when(parentOption.getOptions()).thenReturn(new Option[0]);

        Option foundOption = option.findOption("trigger");

        assertEquals(option, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = mock(Option.class);

        assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = mock(Option.class);
        Option option = mock(Option.class);

        option.setParent(parentOption);

        assertEquals(parentOption, option.getParent());
    }
}