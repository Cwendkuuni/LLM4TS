import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return true;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, Set<Long> expansions) {
                // do nothing
            }

            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "This is a test option";
            }

            @Override
            public Set<String> getPrefixes() {
                return Set.of("-");
            }

            @Override
            public Set<String> getTriggers() {
                return Set.of("t", "test");
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // do nothing
            }

        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;

        boolean result = option.canProcess(commandLine, arguments);

        assertTrue(result);
    }

    @Test
    public void testToString() {
        String expected = "testOption";

        String result = option.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return true;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, Set<Long> expansions) {
                // do nothing
            }

            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "This is a test option";
            }

            @Override
            public Set<String> getPrefixes() {
                return Set.of("-");
            }

            @Override
            public Set<String> getTriggers() {
                return Set.of("t", "test");
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // do nothing
            }

        };

        boolean result = option.equals(option2);

        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        int expected = option.getId() * 37 + option.getPreferredName().hashCode();
        expected = expected * 37 + option.getDescription().hashCode();
        expected = expected * 37 + option.getPrefixes().hashCode();
        expected = expected * 37 + option.getTriggers().hashCode();

        int result = option.hashCode();

        assertEquals(expected, result);
    }

    @Test
    public void testFindOption() {
        Option result = option.findOption("t");

        assertEquals(option, result);
    }

    @Test
    public void testIsRequired() {
        boolean result = option.isRequired();

        assertTrue(result);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;

        option.defaults(commandLine);
    }
}