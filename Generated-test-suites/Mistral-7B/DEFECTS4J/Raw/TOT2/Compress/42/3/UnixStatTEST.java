import static org.apache.commons.compress.archivers.zip.UnixStat.*;
import static org.junit.Assert.*; // For assertions

public class UnixStatTest {

    @Test
    public void testPermMask() {
        int permMask = PERM_MASK;
        int expectedResult = 07777;
        assertEquals("PERM_MASK should be equal to 07777", expectedResult, permMask);
    }
}