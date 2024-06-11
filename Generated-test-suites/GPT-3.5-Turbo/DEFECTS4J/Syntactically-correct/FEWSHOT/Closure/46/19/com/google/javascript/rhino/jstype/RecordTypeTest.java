import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    private RecordType recordType;
    
    @Before
    public void setUp() {
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(null));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(null));
        SortedMap<String, JSType> sortedProperties = new TreeMap<>(properties);
        recordType = new RecordType(null, sortedProperties);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType other = new RecordType(null, null);
        assertFalse(recordType.isEquivalentTo(null));
        assertFalse(recordType.isEquivalentTo(new StringType()));
        assertTrue(recordType.isEquivalentTo(recordType));
        assertTrue(recordType.isEquivalentTo(other));
    }
    
    @Test
    public void testGetImplicitPrototype() {
        assertNull(recordType.getImplicitPrototype());
    }
    
    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("property3", new StringType(), false, null));
    }
    
    @Test
    public void testGetLeastSupertype() {
        UnionTypeBuilder builder = new UnionTypeBuilder(null);
        assertEquals(builder.build(), recordType.getLeastSupertype(new StringType()));
    }
    
    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
    
    @Test
    public void testIsSubtype() {
        assertFalse(recordType.isSubtype(null));
        assertFalse(recordType.isSubtype(new StringType()));
        assertTrue(recordType.isSubtype(recordType));
        assertTrue(recordType.isSubtype(new RecordType(null, null)));
    }
    
    @Test
    public void testResolveInternal() {
        assertNull(recordType.resolveInternal(null, null));
    }
}