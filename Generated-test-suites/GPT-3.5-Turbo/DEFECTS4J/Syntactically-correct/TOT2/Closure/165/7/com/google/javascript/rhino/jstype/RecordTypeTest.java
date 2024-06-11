import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperties() {
        RecordType recordType = new RecordType();

        assertTrue(recordType.defineProperty("property1", JSType.NUMBER_TYPE, false, null));
        assertFalse(recordType.defineProperty("property1", JSType.STRING_TYPE, false, null));
        assertTrue(recordType.defineProperty("property2", JSType.BOOLEAN_TYPE, false, null));
        assertFalse(recordType.defineProperty("property2", JSType.NULL_TYPE, false, null));
    }

    @Test
    public void testIsSubtype() {
        RecordType typeA = new RecordType();
        RecordType typeB = new RecordType();
        typeB.defineProperty("property1", JSType.NUMBER_TYPE, false, null);

        assertTrue(RecordType.isSubtype(typeA, typeB));

        typeA.defineProperty("property1", JSType.BOOLEAN_TYPE, false, null);

        assertFalse(RecordType.isSubtype(typeA, typeB));
    }

    @Test
    public void testGreatestSubtypeHelper() {
        RecordType typeA = new RecordType();
        RecordType typeB = new RecordType();
        typeA.defineProperty("property1", JSType.NULL_TYPE, false, null);
        typeA.defineProperty("property2", JSType.STRING_TYPE, false, null);
        typeB.defineProperty("property1", JSType.NULL_TYPE, false, null);
        typeB.defineProperty("property3", JSType.BOOLEAN_TYPE, false, null);

        JSType greatestSubtype = typeA.getGreatestSubtypeHelper(typeB);

        assertTrue(greatestSubtype.isRecordType());
        assertTrue(greatestSubtype.toMaybeRecordType().hasProperty("property1"));
        assertFalse(greatestSubtype.toMaybeRecordType().hasProperty("property2"));
        assertTrue(greatestSubtype.toMaybeRecordType().hasProperty("property3"));
    }
}