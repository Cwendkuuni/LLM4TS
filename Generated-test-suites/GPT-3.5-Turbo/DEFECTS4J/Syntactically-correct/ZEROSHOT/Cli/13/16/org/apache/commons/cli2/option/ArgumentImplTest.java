import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {
    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl(
            "name",
            "description",
            1,
            5,
            '-',
            ',',
            null,
            "--",
            null,
            0
        );
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('-', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(5, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }
}