import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        RecordType recordType = new RecordType(null, null);

        // Test getImplicitPrototype
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertEquals(null, implicitPrototype);

        // Test isSubtype
        boolean isSubtype = recordType.isSubtype(null);
        assertEquals(false, isSubtype);
    }

    @Test
    public void testRecordType_declared() {
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
        recordTypeBuilder.setSynthesized(false);

        // test defineProperty
        boolean defineProperty = recordTypeBuilder.defineProperty(null, null, false, null);
        assertEquals(false, defineProperty);

        // test getGreatestSubtypeHelper
        JSType greatestSubtypeHelper = recordTypeBuilder.getGreatestSubtypeHelper(null);
        assertEquals(null, greatestSubtypeHelper);
    }

    @Test
    public void testRecordType_synthetic() {
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
        recordTypeBuilder.setSynthesized(true);

        // test defineProperty
        boolean defineProperty = recordTypeBuilder.defineProperty(null, null, false, null);
        assertEquals(false, defineProperty);

        // test getGreatestSubtypeHelper
        JSType greatestSubtypeHelper = recordTypeBuilder.getGreatestSubtypeHelper(null);
        assertEquals(null, greatestSubtypeHelper);
    }    
}