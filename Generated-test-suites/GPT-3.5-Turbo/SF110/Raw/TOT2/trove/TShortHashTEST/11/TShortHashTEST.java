import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        short val = 10;
        int result = tShortHash.index(val);
        assertEquals(-1, result);
    }
}