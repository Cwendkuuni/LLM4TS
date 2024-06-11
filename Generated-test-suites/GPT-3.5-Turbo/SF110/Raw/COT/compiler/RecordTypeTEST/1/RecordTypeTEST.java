
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testCheckRecordEquivalenceHelper() {
        // Test case 1: Test with two record types that are equivalent
        RecordType record1 = new RecordType();
        record1.addProperty("name", new StringType());
        record1.addProperty("age", new NumberType());

        RecordType record2 = new RecordType();
        record2.addProperty("name", new StringType());
        record2.addProperty("age", new NumberType());

        assertTrue(record1.checkRecordEquivalenceHelper(record2));
        
        // Test case 2: Test with two record types that are not equivalent
        RecordType record3 = new RecordType();
        record3.addProperty("name", new StringType());
        record3.addProperty("age", new NumberType());

        RecordType record4 = new RecordType();
        record4.addProperty("name", new StringType());

        assertFalse(record3.checkRecordEquivalenceHelper(record4));
    }

    @Test
    public void testIsSubtype() {
        // Test case 1: RecordType is a subtype of ObjectType
        RecordType record1 = new RecordType();
        ObjectType objectType = new ObjectType();

        assertTrue(record1.isSubtype(objectType));
        
        // Test case 2: RecordType is not a subtype of another RecordType with different properties
        RecordType record2 = new RecordType();
        record2.addProperty("name", new StringType());
        record2.addProperty("age", new NumberType());

        RecordType record3 = new RecordType();
        record3.addProperty("name", new StringType());

        assertFalse(record2.isSubtype(record3));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        // Test case 1: Test with two record types that have common properties
        RecordType record1 = new RecordType();
        record1.addProperty("name", new StringType());
        record1.addProperty("age", new NumberType());

        RecordType record2 = new RecordType();
        record2.addProperty("name", new StringType());
        record2.addProperty("age", new NumberType());

        assertEquals(record1, record1.getGreatestSubtypeHelper(record2));

        // Test case 2: Test with record type and ObjectType
        RecordType record3 = new RecordType();
        record3.addProperty("name", new StringType());
        record3.addProperty("age", new NumberType());

        ObjectType objectType = new ObjectType();

        assertEquals(objectType, record3.getGreatestSubtypeHelper(objectType));

        // Test case 3: Test with record type and NullType
        RecordType record4 = new RecordType();
        record4.addProperty("name", new StringType());
        record4.addProperty("age", new NumberType());

        NullType nullType = new NullType();

        assertNull(record4.getGreatestSubtypeHelper(nullType));
    }

    @Test
    public void testDefineProperty() {
        // Test case 1: Test defining a property in a non-frozen record type
        RecordType record1 = new RecordType();
        boolean result1 = record1.defineProperty("name", new StringType(), true, null);
        assertTrue(result1);

        // Test case 2: Test defining a property in a frozen record type
        RecordType record2 = new RecordType();
        record2.addProperty("name", new StringType(), true, null);
        record2.setFrozen(true);

        boolean result2 = record2.defineProperty("age", new NumberType(), true, null);
        assertFalse(result2);
    }
}