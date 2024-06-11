import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setup() {
        option = Mockito.mock(OptionImpl.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        ListIterator arguments = Arrays.asList("argument").listIterator();

        Mockito.when(option.canProcess(commandLine, "argument")).thenReturn(true);
        assertTrue(option.canProcess(commandLine, arguments));
        Mockito.verify(option).canProcess(commandLine, "argument");

        Mockito.when(option.canProcess(commandLine, "argument")).thenReturn(false);
        assertFalse(option.canProcess(commandLine, arguments));
        Mockito.verify(option).canProcess(commandLine, "argument");
    }

    @Test
    public void testToString() {
        Mockito.when(option.getPreferredName()).thenReturn("option");
        assertEquals("option", option.toString());
        Mockito.verify(option).getPreferredName();
    }

    @Test
    public void testEquals() {
        OptionImpl otherOption = Mockito.mock(OptionImpl.class, Mockito.CALLS_REAL_METHODS);

        Mockito.when(option.getId()).thenReturn(1);
        Mockito.when(option.getPreferredName()).thenReturn("option");
        Mockito.when(option.getDescription()).thenReturn("description");
        Mockito.when(option.getPrefixes()).thenReturn(new HashSet<>(Arrays.asList("-", "--")));
        Mockito.when(option.getTriggers()).thenReturn(new HashSet<>(Arrays.asList("t1", "t2")));

        Mockito.when(otherOption.getId()).thenReturn(1);
        Mockito.when(otherOption.getPreferredName()).thenReturn("option");
        Mockito.when(otherOption.getDescription()).thenReturn("description");
        Mockito.when(otherOption.getPrefixes()).thenReturn(new HashSet<>(Arrays.asList("-", "--")));
        Mockito.when(otherOption.getTriggers()).thenReturn(new HashSet<>(Arrays.asList("t1", "t2")));

        assertTrue(option.equals(otherOption));
        Mockito.verify(option).getId();
        Mockito.verify(option).getPreferredName();
        Mockito.verify(option).getDescription();
        Mockito.verify(option).getPrefixes();
        Mockito.verify(option).getTriggers();
        Mockito.verify(otherOption).getId();
        Mockito.verify(otherOption).getPreferredName();
        Mockito.verify(otherOption).getDescription();
        Mockito.verify(otherOption).getPrefixes();
        Mockito.verify(otherOption).getTriggers();

        Mockito.when(otherOption.getPreferredName()).thenReturn("different");
        assertFalse(option.equals(otherOption));
        Mockito.verify(otherOption).getPreferredName();
    }

    @Test
    public void testHashCode() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("-", "--"));
        Set<String> triggers = new HashSet<>(Arrays.asList("t1", "t2"));

        Mockito.when(option.getId()).thenReturn(1);
        Mockito.when(option.getPreferredName()).thenReturn("option");
        Mockito.when(option.getDescription()).thenReturn("description");
        Mockito.when(option.getPrefixes()).thenReturn(prefixes);
        Mockito.when(option.getTriggers()).thenReturn(triggers);

        int expectedHashCode = 1;
        expectedHashCode = (expectedHashCode * 37) + "option".hashCode();
        expectedHashCode = (expectedHashCode * 37) + "description".hashCode();
        expectedHashCode = (expectedHashCode * 37) + prefixes.hashCode();
        expectedHashCode = (expectedHashCode * 37) + triggers.hashCode();

        assertEquals(expectedHashCode, option.hashCode());
        Mockito.verify(option).getId();
        Mockito.verify(option).getPreferredName();
        Mockito.verify(option).getDescription();
        Mockito.verify(option).getPrefixes();
        Mockito.verify(option).getTriggers();
    }

    @Test
    public void testFindOption() {
        Mockito.when(option.getTriggers()).thenReturn(new HashSet<>(Arrays.asList("t1", "t2")));

        assertEquals(option, option.findOption("t1"));
        Mockito.verify(option).getTriggers();

        assertEquals(null, option.findOption("t3"));
        Mockito.verify(option).getTriggers();
    }

    @Test
    public void testIsRequired() {
        Mockito.when(option.isRequired()).thenReturn(true);
        assertTrue(option.isRequired());
        Mockito.verify(option).isRequired();
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);

        option.defaults(commandLine);
        Mockito.verify(option).defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("-", "--"));

        Mockito.when(option.getPreferredName()).thenReturn("option");
        Mockito.when(option.getTriggers()).thenReturn(new HashSet<>(Arrays.asList("t1", "t2")));

        option.checkPrefixes(prefixes);
        Mockito.verify(option).getPreferredName();
        Mockito.verify(option).getTriggers();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesIllegalArgumentException() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("--"));

        Mockito.when(option.getPreferredName()).thenReturn("option");

        option.checkPrefixes(prefixes);
        Mockito.verify(option).getPreferredName();
    }
}