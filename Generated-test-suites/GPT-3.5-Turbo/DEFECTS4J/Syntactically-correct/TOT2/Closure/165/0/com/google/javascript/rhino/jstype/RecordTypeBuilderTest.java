import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        
        assert recordTypeBuilder.addProperty("name", stringType, propertyNode) != null;
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType recordType = recordTypeBuilder.build();
        JSType objectType = new JSTypeRegistry().getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        
        assertEquals("Empty record should return OBJECT_TYPE", objectType, recordType);
    }

    @Test
    public void testBuildNonEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("age", stringType, propertyNode);
        
        JSType recordType = recordTypeBuilder.build();
        
        assertEquals("Non-empty record should return RecordType", RecordType.class, recordType.getClass());
        // Add more assertion for the properties being added
    }

}