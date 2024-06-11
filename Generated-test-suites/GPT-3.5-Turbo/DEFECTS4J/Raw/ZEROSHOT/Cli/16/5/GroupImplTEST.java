import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setUp() {
        OptionImpl option1 = new OptionImpl("option1", "Option 1", false);
        OptionImpl option2 = new OptionImpl("option2", "Option 2", false);
        List<Option> options = new ArrayList<Option>();
        options.add(option1);
        options.add(option2);
        group = new GroupImpl(options, "", "Group", 1, 2);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertEquals(0, anonymous.size());
    }

    @Test
    public void testFindOption() {
        Option option = group.findOption("option1");
        assertNotNull(option);
        assertEquals("option1", option.getPreferredName());
    }

    @Test
    public void testGetMinimum() {
        int minimum = group.getMinimum();
        assertEquals(1, minimum);
    }

    @Test
    public void testGetMaximum() {
        int maximum = group.getMaximum();
        assertEquals(2, maximum);
    }

    @Test
    public void testIsRequired() {
        boolean required = group.isRequired();
        assertTrue(required);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        group.defaults(commandLine);

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
    }
}