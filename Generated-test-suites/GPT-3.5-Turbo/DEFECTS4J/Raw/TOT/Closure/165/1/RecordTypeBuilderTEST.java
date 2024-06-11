import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
    
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void addProperty_WhenValidNameAndType_ShouldReturnBuilderObject() {
        String propertyName = "age";
        JSType propertyType = new JSType();
        Node propertyNode = new Node();

        RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

        assertNotNull(result);
    }

    @Test
    public void addProperty_WhenDuplicatePropertyName_ShouldReturnNull() {
        String propertyName = "age";
        JSType propertyType = new JSType();
        Node propertyNode = new Node();

        recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

        assertNull(result);
    }

    @Test
    public void build_WhenEmptyRecord_ShouldReturnObjectType() {
        JSType expectedType = new JSType();
        expectedType.setObjectType(JSTypeNative.OBJECT_TYPE);

        JSType result = recordTypeBuilder.build();

        assertEquals(expectedType, result);
    }

    @Test
    public void build_WhenNonEmptyRecord_ShouldReturnRecordTypeWithProperties() {
        String propertyName1 = "name";
        JSType propertyType1 = new JSType();
        Node propertyNode1 = new Node();

        String propertyName2 = "age";
        JSType propertyType2 = new JSType();
        Node propertyNode2 = new Node();

        recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1);
        recordTypeBuilder.addProperty(propertyName2, propertyType2, propertyNode2);

        JSType result = recordTypeBuilder.build();

        assertTrue(result instanceof RecordType);

        RecordType recordType = (RecordType) result;

        assertTrue(recordType.getProperties().containsKey(propertyName1));
        assertTrue(recordType.getProperties().containsKey(propertyName2));

        assertEquals(propertyType1, recordType.getProperties().get(propertyName1).getType());
        assertEquals(propertyType2, recordType.getProperties().get(propertyName2).getType());
    }
}