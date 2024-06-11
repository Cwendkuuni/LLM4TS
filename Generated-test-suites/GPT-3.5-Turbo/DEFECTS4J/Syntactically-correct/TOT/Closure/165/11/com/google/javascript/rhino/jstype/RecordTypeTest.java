import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        Map<String, RecordProperty> properties = new HashMap<>();

        // Test Case: Typical Use Case
        properties.put("a", new RecordProperty(TYPE_1, null));
        properties.put("b", new RecordProperty(TYPE_2, null));
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testDefineProperty() {
        // Test Case: Typical Use Case
        boolean result = recordType.defineProperty("c", TYPE_3, true, null);
        assertTrue(result);

        // Test Case: Edge Case (Property already defined)
        result = recordType.defineProperty("a", TYPE_4, false, null);
        assertFalse(result);

        // Test Case: Edge Case (Type is inferred)
        result = recordType.defineProperty("d", TYPE_5, true, null);
        assertTrue(result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        // Test Case: Typical Use Case (that is a RecordType)
        RecordType that = createRecordType();
        JSType result = recordType.getGreatestSubtypeHelper(that);
        assertEquals(recordType, result);

        // Test Case: Edge Case (that is an ObjectType)
        ObjectType that = createObjectType();
        result = recordType.getGreatestSubtypeHelper(that);
        assertEquals(NO_OBJECT_TYPE, result);
    }

    @Test
    public void testIsSubtype() {
        // Test Case: Typical Use Case (that is a RecordType)
        RecordType that = createRecordType();
        boolean result = recordType.isSubtype(that);
        assertTrue(result);

        // Test Case: Typical Use Case (that is a ObjectType)
        ObjectType that = createObjectType();
        result = recordType.isSubtype(that);
        assertTrue(result);

        // Test Case: Edge Case (that is not a RecordType or ObjectType)
        JSType that = createJSType();
        result = recordType.isSubtype(that);
        assertFalse(result);
    }

    @Test
    public void testIsEquivalentTo() {
        // Test Case: Typical Use Case
        RecordType other = createRecordType();
        boolean result = recordType.isEquivalentTo(other);
        assertTrue(result);

        // Test Case: Edge Case (Other type is not a RecordType)
        JSType other = createJSType();
        result = recordType.isEquivalentTo(other);
        assertFalse(result);
    }

    private RecordType createRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty(TYPE_1, null));
        properties.put("b", new RecordProperty(TYPE_2, null));
        return new RecordType(registry, properties);
    }

    private ObjectType createObjectType() {
        return registry.getNativeObjectType(OBJECT_TYPE);
    }

    private JSType createJSType() {
        return registry.getNativeObjectType(SOME_TYPE);
    }
}