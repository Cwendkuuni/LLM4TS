import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("property1", new JSType(), true, null);
        assertTrue(recordType.hasProperty("property1"));
    }

    @Test
    public void testGetGreatestSubtype() {
        RecordType recordType1 = new RecordType();
        RecordType recordType2 = new RecordType();
        recordType1.defineProperty("property1", new JSType(), true, null);
        recordType2.defineProperty("property2", new JSType(), true, null);
        assertEquals(recordType1, recordType2.getGreatestSubtypeHelper(recordType1));
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = new RecordType();
        RecordType recordType2 = new RecordType();
        recordType1.defineProperty("property1", new JSType(), true, null);
        recordType2.defineProperty("property1", new JSType(), true, null);
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType();
        RecordType recordType2 = new RecordType();
        recordType1.defineProperty("property1", new JSType(), true, null);
        recordType2.defineProperty("property1", new JSType(), true, null);
        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("property1", new JSType(), true, null);
        recordType.defineProperty("property2", new JSType(), true, null);
        JSType resolvedType = recordType.resolveInternal(new ErrorReporter(), new StaticScope());
        assertNotNull(resolvedType);
    }

    @Test
    public void testIsFrozen() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("property1", new JSType(), true, null);
        recordType.defineProperty("property2", new JSType(), true, null);
        recordType.freeze();
        assertTrue(recordType.isFrozen());
    }

}