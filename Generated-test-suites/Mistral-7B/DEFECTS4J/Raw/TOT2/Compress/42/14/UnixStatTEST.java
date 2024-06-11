
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testWrongConstantType() {
        assertFalse(UnixStat.PERM_MASK instanceof String);
    }

    @Test
    public void testMinMaxConstantValues() {
        assertEquals(0, Integer.bitCount(UnixStat.PERM_MASK & ~UnixStat.PERM_MASK));
        assertEquals(7, Integer.bitCount(UnixStat.PERM_MASK));
    }

    @Test
    public void testIncorrectFlagCombination() {
        int invalidCombination = UnixStat.PERM_MASK | UnixStat.LINK_FLAG | UnixStat.DIR_FLAG;
        assertEquals(UnixStat.PERM_MASK, invalidCombination & UnixStat.PERM_MASK);
        assertEquals(0, invalidCombination & UnixStat.LINK_FLAG);
        assertEquals(0, invalidCombination & UnixStat.DIR_FLAG);
    }
}
