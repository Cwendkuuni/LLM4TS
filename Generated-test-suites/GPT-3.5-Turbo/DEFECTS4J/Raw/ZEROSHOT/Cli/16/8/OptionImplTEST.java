import static org.junit.Assert.*;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList<String>().listIterator();
        arguments.add("argument1");
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true);
        String expected = "OptionImpl[id=1, required=true]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);
        assertEquals(option1, option2);
    }

    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, true);
        int expected = 31 * (31 * 1 + true.hashCode());
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true);
        Option result = option.findOption("trigger");
        assertNull(result);
    }

    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        option.defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true);
        Set prefixes = new HashSet();
        prefixes.add("-");
        prefixes.add("--");
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefix() {
        OptionImpl option = new OptionImpl(1, true);
        Set prefixes = new HashSet();
        prefixes.add("-");
        prefixes.add("--");
        option.checkPrefix(prefixes, "-trigger");
    }
}