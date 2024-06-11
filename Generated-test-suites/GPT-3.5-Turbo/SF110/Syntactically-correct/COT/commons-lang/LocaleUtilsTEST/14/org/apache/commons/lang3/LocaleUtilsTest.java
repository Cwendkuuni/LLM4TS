import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
}