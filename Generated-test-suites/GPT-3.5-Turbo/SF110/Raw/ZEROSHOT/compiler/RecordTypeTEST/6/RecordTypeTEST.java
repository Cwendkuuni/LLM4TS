import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty());
        properties.put("property2", new RecordTypeBuilder.RecordProperty());
        recordType = new RecordType(new JSTypeRegistry(), properties);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(new JSTypeRegistry(), new HashMap<>());
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, new EquivalenceMethod()));
    }

    @Test
    public void testDefineProperty() {
        assertTrue(recordType.defineProperty("property3", new JSType(), true, new Node()));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType();
        assertEquals(new JSType(), recordType.getGreatestSubtypeHelper(that));
    }

    @Test
    public void testIsSubtype() {
        assertFalse(recordType.isSubtype(new JSType()));
    }

    @Test
    public void testIsSubtypeWithTypeAIsNotRecordType() {
        assertFalse(recordType.isSubtype(new JSType()));
    }

    @Test
    public void testIsSubtypeWithTypeBIsNotRecordType() {
        assertFalse(recordType.isSubtype(new JSType()));
    }
}