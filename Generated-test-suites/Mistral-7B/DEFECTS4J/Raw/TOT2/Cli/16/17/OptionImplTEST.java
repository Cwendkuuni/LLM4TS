import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {
    @Test
    public void testConstructor() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertEquals(true, option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeId() {
        new OptionImpl(-1, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonBooleanRequired() {
        new OptionImpl(1, "test");
    }

    @Test
    public void testConstructorWithNullId() {
        try {
            new OptionImpl(null, true);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithNullRequired() {
        try {
            new OptionImpl(1, null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }
}