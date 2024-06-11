import org.junit.Test;
import org.apache.commons.cli2.option.OptionImpl;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionImplNegativeIdConstructor() {
        try {
            OptionImpl option = new OptionImpl(-1, true);
        } catch (IllegalArgumentException e) {
            assertEquals("OptionImpl id cannot be negative", e.getMessage());
        }
    }

    @Test
    public void testOptionImplFalseRequiredConstructor() {
        OptionImpl option = new OptionImpl(1, false);
        assertEquals(1, option.getId());
        assertFalse(option.isRequired());
    }

    //...
}