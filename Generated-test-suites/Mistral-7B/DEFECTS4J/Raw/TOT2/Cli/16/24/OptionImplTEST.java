import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertEquals(true, option.isRequired());
    }
}