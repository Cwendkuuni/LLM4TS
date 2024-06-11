import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsEquivalentTo() {
        RecordType record1 = new RecordType(null, null);
        RecordType record2 = new RecordType(null, null);
        RecordType record3 = new RecordType(null, null);

        record1.properties.put("prop1", new JSType());
        record2.properties.put("prop1", new JSType());
        record1.properties.put("prop2", new JSType());
        record2.properties.put("prop2", new JSType());

        assertTrue(record1.isEquivalentTo(record2));
        assertTrue(record2.isEquivalentTo(record1));

        assertFalse(record1.isEquivalentTo(record3));
        assertFalse(record3.isEquivalentTo(record1));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType record = new RecordType(null, null);
        ObjectType objectType = record.getImplicitPrototype();

        assertNotNull(objectType);
        assertEquals("Object", objectType.getName());
    }

    @Test
    public void testDefinePropertyNotFrozen() {
        RecordType record = new RecordType(null, null);
        boolean result = record.defineProperty("prop1", new JSType(), false, null);

        assertTrue(result);
        assertEquals(1, record.properties.size());
    }

    @Test
    public void testDefinePropertyFrozen() {
        RecordType record = new RecordType(null, null);
        record.isFrozen = true;

        boolean result = record.defineProperty("prop1", new JSType(), false, null);

        assertFalse(result);
        assertEquals(0, record.properties.size());
    }

    @Test
    public void testGetLeastSupertype() {
        RecordType record1 = new RecordType(null, null);
        record1.properties.put("prop1", new JSType());
        record1.properties.put("prop2", new JSType());
        record1.properties.put("prop3", new JSType());

        RecordType record2 = new RecordType(null, null);
        record2.properties.put("prop1", new JSType());
        record2.properties.put("prop3", new JSType());

        JSType leastSupertype = record1.getLeastSupertype(record2);

        assertTrue(leastSupertype instanceof RecordType);
        RecordType resultRecord = (RecordType) leastSupertype;
        assertEquals(2, resultRecord.properties.size());
        assertTrue(resultRecord.properties.containsKey("prop1"));
        assertTrue(resultRecord.properties.containsKey("prop3"));
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType record = new RecordType(null, null);
        RecordType result = record.toMaybeRecordType();

        assertEquals(record, result);
    }

    @Test
    public void testIsSubtype() {
        RecordType record1 = new RecordType(null, null);
        record1.properties.put("prop1", new JSType());
        record1.properties.put("prop2", new JSType());

        RecordType record2 = new RecordType(null, null);
        record2.properties.put("prop1", new JSType());
        record2.properties.put("prop2", new JSType());

        assertTrue(record1.isSubtype(record2));
        assertFalse(record2.isSubtype(record1));

        assertFalse(record1.isSubtype(null));
        assertFalse(record1.isSubtype(new JSType()));
    }

    @Test
    public void testIsSubtypeHelper() {
        RecordType record1 = new RecordType(null, null);
        RecordType record2 = new RecordType(null, null);

        assertTrue(RecordType.isSubtype(record1, record2));
        assertFalse(RecordType.isSubtype(record2, record1));
    }

    @Test
    public void testResolveInternal() {
        RecordType record = new RecordType(null, null);
        record.properties.put("prop1", new JSType());
        record.properties.put("prop2", new JSType());
        ErrorReporter reporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();

        JSType resolvedType = record.resolveInternal(reporter, scope);

        assertEquals(record, resolvedType);
    }
}