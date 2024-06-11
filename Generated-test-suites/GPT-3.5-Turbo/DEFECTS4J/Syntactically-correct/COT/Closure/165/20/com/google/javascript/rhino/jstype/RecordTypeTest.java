import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {
    private RecordType recordType;

    @Before
    public void setUp() {
        recordType = new RecordType(new JSTypeRegistry(), new HashMap<>());
    }

    @Test
    public void testRecordType() {
        assertNotNull(recordType);
    }
}