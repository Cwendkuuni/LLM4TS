import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordType = createRecordType();
    }
    
    private RecordType createRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordProperty recordProperty = new RecordProperty();
        recordProperty.setType(JSTypeNative.STRING_TYPE);
        properties.put("property1", recordProperty);
        RecordProperty recordProperty2 = new RecordProperty();
        recordProperty2.setType(JSTypeNative.NUMBER_TYPE);
        properties.put("property2", recordProperty2);
        return new RecordType(registry, properties);
    }

    @Test
    public void testGetGreatestSubtypeHelper_sameRecordType() {
        RecordType recordType2 = createRecordType();
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
        assertEquals(recordType, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeHelper_subsetRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordProperty recordProperty = new RecordProperty();
        recordProperty.setType(JSTypeNative.STRING_TYPE);
        properties.put("property1", recordProperty);
        RecordType subtypeRecordType = new RecordType(registry, properties);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(subtypeRecordType);
        assertEquals(subtypeRecordType, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeHelper_noMatchingPropertyTypes() {
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordProperty recordProperty = new RecordProperty();
        recordProperty.setType(JSTypeNative.BOOLEAN_TYPE);
        properties.put("property1", recordProperty);
        RecordType subtypeRecordType = new RecordType(registry, properties);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(subtypeRecordType);
        assertTrue(greatestSubtype instanceof JSTypeNative.NO_TYPE);
    }

    // Add more test cases for the remaining methods

}