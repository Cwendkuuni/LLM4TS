
import static org.junit.Assert.*;

import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, ' ', ',', null, "--", null, 1);
        assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, ' ', ',', null, "--", null, 1);
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, ' ', ',', null, "--", null, 1);
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, ' ', ',', null, "--", null, 1);
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, ' ', ',', null, "--", null, 1);
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, ' ', ',', null, "--", null, 1);
        assertTrue(argument.isRequired());
    }
}
