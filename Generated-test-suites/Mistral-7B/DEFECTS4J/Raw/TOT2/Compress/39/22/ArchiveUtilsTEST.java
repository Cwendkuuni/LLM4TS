import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 0 test.txt";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringNullEntry() {
        ArchiveEntry entry = null;
        String result = ArchiveUtils.toString(entry);
        fail("Should throw NullPointerException");
    }
}