import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordTypeConstructor() {
        RecordType.Builder builder = new RecordType.Builder();
        builder.addProperty("a", Type.INT_TYPE);
        builder.addProperty("b", Type.STRING_TYPE);
        RecordType recordType = builder.build();
        assertNotNull(recordType);
    }

    @Test
    public void testRecordTypeProperties() {
        RecordType.Builder builder = new RecordType.Builder();
        builder.addProperty("a", Type.INT_TYPE);
        builder.addProperty("b", Type.STRING_TYPE);
        RecordType recordType = builder.build();
        assertTrue(recordType.hasProperty("a"));
        assertTrue(recordType.hasProperty("b"));
    }

    @Test
    public void testRecordTypeEquivalentTo() {
        RecordType.Builder builder1 = new RecordType.Builder();
        builder1.addProperty("a", Type.INT_TYPE);
        builder1.addProperty("b", Type.STRING_TYPE);
        RecordType recordType1 = builder1.build();

        RecordType.Builder builder2 = new RecordType.Builder();
        builder2.addProperty("a", Type.INT_TYPE);
        builder2.addProperty("b", Type.STRING_TYPE);
        RecordType recordType2 = builder2.build();

        RecordType.Builder builder3 = new RecordType.Builder();
        builder3.addProperty("a", Type.INT_TYPE);
        builder3.addProperty("c", Type.STRING_TYPE);
        RecordType recordType3 = builder3.build();

        assertTrue(recordType1.isEquivalentTo(recordType2));
        assertFalse(recordType1.isEquivalentTo(recordType3));
    }

    @Test
    public void testRecordTypeSubtype() {
        RecordType.Builder builder1 = new RecordType.Builder();
        builder1.addProperty("a", Type.INT_TYPE);
        builder1.addProperty("b", Type.STRING_TYPE);
        RecordType recordType1 = builder1.build();

        RecordType.Builder builder2 = new RecordType.Builder();
        builder2.addProperty("a", Type.INT_TYPE);
        builder2.addProperty("b", Type.STRING_TYPE);
        builder2.addProperty("c", Type.BOOLEAN_TYPE);
        RecordType recordType2 = builder2.build();

        RecordType.Builder builder3 = new RecordType.Builder();
        builder3.addProperty("a", Type.INT_TYPE);
        builder3.addProperty("b", Type.STRING_TYPE);
        builder3.addProperty("c", Type.BOOLEAN_TYPE);
        builder3.addProperty("d", Type.OBJECT_TYPE);
        RecordType recordType3 = builder3.build();

        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType3));
        assertFalse(recordType1.isSubtype(recordType3));
    }

    @Test
    public void testRecordTypeGetPropertyType() {
        RecordType.Builder builder = new RecordType.Builder();
        builder.addProperty("a", Type.INT_TYPE);
        builder.addProperty("b", Type.STRING_TYPE);
        RecordType recordType = builder.build();

        assertEquals(Type.INT_TYPE, recordType.getPropertyType("a"));
        assertEquals(Type.STRING_TYPE, recordType.getPropertyType("b"));
    }

    @Test
    public void testRecordTypeDefineProperty() {
        RecordType.Builder builder = new RecordType.Builder();
        RecordType recordType = builder.build();

        assertFalse(recordType.hasProperty("a"));
        assertTrue(recordType.defineProperty("a", Type.INT_TYPE));
        assertTrue(recordType.hasProperty("a"));
        assertEquals(Type.INT_TYPE, recordType.getPropertyType("a"));
        assertFalse(recordType.defineProperty("a", Type.STRING_TYPE));
        assertEquals(Type.INT_TYPE, recordType.getPropertyType("a"));
    }

    @Test
    public void testRecordTypeGetGreatestSubtypeHelper() {
        RecordType.Builder builder1 = new RecordType.Builder();
        builder1.addProperty("a", Type.OBJECT_TYPE);
        builder1.addProperty("b", Type.BOOLEAN_TYPE);
        RecordType recordType1 = builder1.build();

        RecordType.Builder builder2 = new RecordType.Builder();
        builder2.addProperty("a", Type.OBJECT_TYPE);
        builder2.addProperty("b", Type.BOOLEAN_TYPE);
        builder2.addProperty("c", Type.STRING_TYPE);
        RecordType recordType2 = builder2.build();

        RecordType.Builder builder3 = new RecordType.Builder();
        builder3.addProperty("a", Type.OBJECT_TYPE);
        builder3.addProperty("b", Type.BOOLEAN_TYPE);
        builder3.addProperty("c", Type.STRING_TYPE);
        builder3.addProperty("d", Type.ARRAY_TYPE);
        RecordType recordType3 = builder3.build();

        assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType2));
        assertEquals(recordType2, recordType2.getGreatestSubtypeHelper(recordType3));
        assertNull(recordType1.getGreatestSubtypeHelper(recordType3));
    }

    @Test
    public void testRecordTypeIsSubtype() {
        RecordType.Builder builder1 = new RecordType.Builder();
        builder1.addProperty("a", Type.INT_TYPE);
        builder1.addProperty("b", Type.STRING_TYPE);
        RecordType recordType1 = builder1.build();

        RecordType.Builder builder2 = new RecordType.Builder();
        builder2.addProperty("a", Type.INT_TYPE);
        builder2.addProperty("b", Type.STRING_TYPE);
        builder2.addProperty("c", Type.BOOLEAN_TYPE);
        RecordType recordType2 = builder2.build();

        RecordType.Builder builder3 = new RecordType.Builder();
        builder3.addProperty("a", Type.INT_TYPE);
        builder3.addProperty("b", Type.STRING_TYPE);
        builder3.addProperty("c", Type.BOOLEAN_TYPE);
        builder3.addProperty("d", Type.OBJECT_TYPE);
        RecordType recordType3 = builder3.build();

        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType3));
        assertFalse(recordType1.isSubtype(recordType3));
    }

}