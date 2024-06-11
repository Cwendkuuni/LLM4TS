import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertTrue(recordType1.isEquivalentTo(recordType2));
        assertTrue(recordType2.isEquivalentTo(recordType1));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType1 = new RecordType(registry, properties1);

        assertEquals(recordType1, recordType1.resolveInternal(t, scope));
    }

    // Add more test methods for other methods in the Util class

}