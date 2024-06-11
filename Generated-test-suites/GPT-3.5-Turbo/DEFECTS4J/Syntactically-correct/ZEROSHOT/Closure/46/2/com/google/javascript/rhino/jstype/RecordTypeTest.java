import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    
    private RecordType recordType;

    @Before
    public void setUp() {
        // Create properties for the record type
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1", null));
        properties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2", null));

        // Create the record type
        recordType = new RecordType(new JSTypeRegistry(), properties);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType other = new RecordType(new JSTypeRegistry(), new HashMap<>());
        assertFalse(recordType.isEquivalentTo(other));
        
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1", null));
        properties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2", null));
        RecordType another = new RecordType(new JSTypeRegistry(), properties);
        assertTrue(recordType.isEquivalentTo(another));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertEquals("OBJECT_TYPE", implicitPrototype.toString());
    }

    @Test
    public void testDefineProperty() {
        boolean result = recordType.defineProperty("c", new JSType(), false, null);
        assertTrue(result);
    }

    @Test
    public void testGetLeastSupertype() {
        JSType that = new JSType();
        JSType leastSupertype = recordType.getLeastSupertype(that);
        assertEquals("NO_TYPE", leastSupertype.toString());
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType maybeRecordType = recordType.toMaybeRecordType();
        assertEquals(recordType, maybeRecordType);
    }

    @Test
    public void testIsSubtype() {
        JSType that = new JSType();
        assertFalse(recordType.isSubtype(that));

        RecordType other = new RecordType(new JSTypeRegistry(), new HashMap<>());
        assertTrue(recordType.isSubtype(other));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();

        JSType resolvedType = recordType.resolveInternal(t, scope);
        assertEquals(recordType, resolvedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testRecordTypeConstructorThrowsExceptionWhenPropIsNull() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new TreeMap<>();
        properties.put("a", null);
        new RecordType(new JSTypeRegistry(), properties);
    }

}