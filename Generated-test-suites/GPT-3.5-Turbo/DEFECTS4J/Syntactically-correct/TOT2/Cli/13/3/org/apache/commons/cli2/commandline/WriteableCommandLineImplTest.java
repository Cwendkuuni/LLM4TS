import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    /* Test Case 1 - Test for addOption method */
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList());
        cmd.addOption(new Option("newOption"));
        assertTrue(cmd.hasOption(new Option("newOption")));
    }

    /* Test Case 2 - Test for addValue method */
    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList());
        cmd.addValue(new Argument("testArg"), "value");
        assertEquals("value", cmd.getValues(new Argument("testArg"), new ArrayList()));
    }

    /* Test Case 3 - Test for addSwitch method */
    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList());
        cmd.addSwitch(new Option("testSwitch"), true);
        assertTrue(cmd.getSwitch(new Option("testSwitch"), false));
    }

    /* Test Case 4 - Test for getProperty method */
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList());
        cmd.addProperty("testProp", "value");
        assertEquals("value", cmd.getProperty("testProp", "default"));
    }

    /* Test Case 5 - Test for looksLikeOption method */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList());
        assertTrue(cmd.looksLikeOption("-t"));
    }

}