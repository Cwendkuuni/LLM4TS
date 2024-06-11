import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
    
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        JSTypeRegistry registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType("String");
        Node propertyNode = new Node();
        RecordTypeBuilder builder = recordTypeBuilder.addProperty("testProp", type, propertyNode);
        assertNotNull(builder);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType type = recordTypeBuilder.build();
        assertEquals("Object type should be returned for empty record", 
                     JSTypeNative.OBJECT_TYPE, type.getNativeType());
    }

    @Test
    public void testBuildNonEmptyRecord() {
        JSType type1 = new JSType("String");
        Node propertyNode1 = new Node();
        recordTypeBuilder.addProperty("prop1", type1, propertyNode1);

        JSType type2 = new JSType("Number");
        Node propertyNode2 = new Node();
        recordTypeBuilder.addProperty("prop2", type2, propertyNode2);

        JSType recordType = recordTypeBuilder.build();
        assertEquals("RecordType should have two properties", 
                     2, ((RecordType)recordType).getProperties().size());
    }
}