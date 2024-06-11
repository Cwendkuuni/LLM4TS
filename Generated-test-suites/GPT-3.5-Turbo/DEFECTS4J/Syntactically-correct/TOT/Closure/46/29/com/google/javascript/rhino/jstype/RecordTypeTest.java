import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;
    private JSTypeRegistry registry;
    private Map<String, RecordTypeBuilder.RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsEquivalentTo_sameRecordType_returnsTrue() {
        RecordType other = new RecordType(registry, properties);
        assertTrue(recordType.isEquivalentTo(other));
    }

    @Test
    public void testIsEquivalentTo_differentRecordType_returnsFalse() {
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        RecordType other = new RecordType(registry, otherProperties);
        assertFalse(recordType.isEquivalentTo(other));
    }

    @Test
    public void testIsSubtype_subtypeRecordType_returnsTrue() {
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        RecordType other = new RecordType(registry, otherProperties);
        assertTrue(other.isSubtype(recordType));
    }

    @Test
    public void testIsSubtype_supertypeRecordType_returnsFalse() {
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        RecordType other = new RecordType(registry, otherProperties);
        assertFalse(recordType.isSubtype(other));
    }

    @Test
    public void testGetGreatestSubtypeHelper_bothRecordTypes_returnsGreatestSubtype() {
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));

        properties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        properties.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));

        RecordType other = new RecordType(registry, otherProperties);

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(other);
        assertNotNull(greatestSubtype);
        assertTrue(greatestSubtype.isRecordType());

        RecordType greatestSubtypeRecord = greatestSubtype.toMaybeRecordType();
        assertTrue(greatestSubtypeRecord.hasProperty("a"));
        assertEquals(greatestSubtypeRecord.getPropertyType("a"), new JSType());

        assertTrue(greatestSubtypeRecord.hasProperty("b"));
        assertEquals(greatestSubtypeRecord.getPropertyType("b"), new JSType());

        assertFalse(greatestSubtypeRecord.hasProperty("c"));
    }

    @Test
    public void testGetLeastSupertype_bothRecordTypes_returnsLeastSupertype() {
        Map<String, RecordTypeBuilder.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        otherProperties.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));

        properties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        properties.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        properties.put("c", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));

        RecordType other = new RecordType(registry, otherProperties);

        JSType leastSupertype = recordType.getLeastSupertype(other);
        assertNotNull(leastSupertype);
        assertTrue(leastSupertype.isRecordType());

        RecordType leastSupertypeRecord = leastSupertype.toMaybeRecordType();
        assertTrue(leastSupertypeRecord.hasProperty("a"));
        assertEquals(leastSupertypeRecord.getPropertyType("a"), new JSType());

        assertTrue(leastSupertypeRecord.hasProperty("b"));
        assertEquals(leastSupertypeRecord.getPropertyType("b"), new JSType());

        assertFalse(leastSupertypeRecord.hasProperty("c"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRecordType_constructorWithNullProperties_throwsException() {
        new RecordType(registry, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testIsSubtype_withNonRecordType_throwsException() {
        recordType.isSubtype(new ObjectType());
    }
}