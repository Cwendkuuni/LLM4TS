import static org.apache.commons.compress.archivers.zip.UnixStat.PERM_MASK;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {
    @Test
    public void testPermMask() {
        int expectedPermMask = 07777;
        int actualPermMask = PERM_MASK;
        assertEquals(expectedPermMask, actualPermMask);
    }
}