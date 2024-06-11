
import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetWithEnum() {
        // ...
    }

    // ... (previous tests)

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidMapping() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConsistentWithInconsistentRecord() {
        // ...
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCommentWithNullMapping() {
        // ...
    }

    @Test
    public void testGetRecordNumber() {
        // ...
    }

    // ... (previous tests)

    enum CSVField {
        FIRST_NAME
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CSVRecordTest");
    }
}
