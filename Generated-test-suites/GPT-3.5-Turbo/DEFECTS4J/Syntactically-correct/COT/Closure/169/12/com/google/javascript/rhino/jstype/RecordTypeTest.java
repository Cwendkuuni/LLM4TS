import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsSynthetic() {
        RecordType recordType = new RecordType();
        boolean result = recordType.isSynthetic();
        assertFalse(result);
    }
}