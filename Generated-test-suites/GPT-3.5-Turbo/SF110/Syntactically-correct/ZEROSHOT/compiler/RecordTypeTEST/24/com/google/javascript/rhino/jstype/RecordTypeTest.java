import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testIsSynthetic() {
        RecordType recordType = new RecordType(null, null);
        boolean isSynthetic = recordType.isSynthetic();
        Assert.assertEquals(false, isSynthetic);
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        boolean equivalence = recordType1.checkRecordEquivalenceHelper(recordType2, null);
        Assert.assertEquals(true, equivalence);
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(null, null);
        boolean defineProperty = recordType.defineProperty("propertyName", null, false, null);
        Assert.assertEquals(false, defineProperty);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType = new RecordType(null, null);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(null);
        Assert.assertNotNull(greatestSubtype);
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType recordType = new RecordType(null, null);
        RecordType maybeRecordType = recordType.toMaybeRecordType();
        Assert.assertEquals(recordType, maybeRecordType);
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        boolean isSubtype = recordType1.isSubtype(recordType2);
        Assert.assertEquals(true, isSubtype);
    }

}