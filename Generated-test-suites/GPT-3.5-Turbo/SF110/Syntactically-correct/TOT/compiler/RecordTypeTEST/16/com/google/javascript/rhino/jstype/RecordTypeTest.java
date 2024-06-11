import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.jstype.RecordType;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    private RecordType recordType;
    private Map<String, RecordType.RecordProperty> properties;

    @Before
    public void setUp() {
        properties = new HashMap<>();
        recordType = new RecordType(null, properties);
    }

    @Test
    public void testRecordType_constructor() {
        assertNotNull(recordType);
    }

    @Test
    public void testRecordType_declaredProperty() {
        properties.put("name", new RecordType.RecordProperty("string", null));
        properties.put("age", new RecordType.RecordProperty("number", null));
        
        assertFalse(recordType.isSynthetic());
        assertTrue(recordType.hasProperty("name"));
        assertTrue(recordType.hasProperty("age"));
        assertEquals("string", recordType.getPropertyType("name"));
        assertEquals("number", recordType.getPropertyType("age"));
    }

    @Test
    public void testRecordType_synthesizedProperty() {
        properties.put("price", new RecordType.RecordProperty("number", null));

        assertTrue(recordType.isSynthetic());
        assertTrue(recordType.hasProperty("price"));
        assertEquals("number", recordType.getPropertyType("price"));
    }

    @Test
    public void testRecordType_checkRecordEquivalenceHelper() {
        properties.put("name", new RecordType.RecordProperty("string", null));
        properties.put("age", new RecordType.RecordProperty("number", null));

        RecordType otherRecord = new RecordType(null, properties);
        
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.BY_VALUE));
    }

    @Test
    public void testRecordType_getImplicitPrototype() {
        assertNotNull(recordType.getImplicitPrototype());
    }

    @Test
    public void testRecordType_defineProperty() {
        properties.put("name", new RecordType.RecordProperty("string", null));

        assertFalse(recordType.defineProperty("name", "string", false, null));
        assertFalse(recordType.defineProperty("name", "number", false, null));
    }

    @Test
    public void testRecordType_getGreatestSubtypeHelper() {
        properties.put("name", new RecordType.RecordProperty("string", null));
        properties.put("age", new RecordType.RecordProperty("number", null));

        RecordType that = new RecordType(null, properties);

        assertNotNull(recordType.getGreatestSubtypeHelper(that));
    }

    @Test
    public void testRecordType_toMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testRecordType_isSubtype() {
        properties.put("name", new RecordType.RecordProperty("string", null));
        properties.put("age", new RecordType.RecordProperty("number", null));

        RecordType that = new RecordType(null, properties);

        assertTrue(recordType.isSubtype(that));
    }

    @Test
    public void testRecordType_isSubtype_differentProperties() {
        properties.put("name", new RecordType.RecordProperty("string", null));
        properties.put("age", new RecordType.RecordProperty("number", null));

        Map<String, RecordType.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("price", new RecordType.RecordProperty("number", null));

        RecordType that = new RecordType(null, otherProperties);

        assertFalse(recordType.isSubtype(that));
    }

    @Test
    public void testRecordType_isSubtype_differentTypes() {
        properties.put("name", new RecordType.RecordProperty("string", null));
        properties.put("age", new RecordType.RecordProperty("number", null));

        Map<String, RecordType.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("name", new RecordType.RecordProperty("number", null));
        otherProperties.put("age", new RecordType.RecordProperty("string", null));

        RecordType that = new RecordType(null, otherProperties);

        assertFalse(recordType.isSubtype(that));
    }

}