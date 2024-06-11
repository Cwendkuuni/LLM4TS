
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder();
    }

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        Node dummyNode = new Node();

        recordTypeBuilder.addProperty("name", expectedType, dummyNode);

        RecordTypeBuilder.RecordProperty recordProperty = recordTypeBuilder.properties.get("name");
        Assert.assertEquals(expectedType, recordProperty.getType());
        Assert.assertEquals(dummyNode, recordProperty.getPropertyNode());
    }

    @Test
    public void testAddDuplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        Node dummyNode = new Node();

        recordTypeBuilder.addProperty("name", expectedType, dummyNode);
        RecordTypeBuilder duplicateBuilder = recordTypeBuilder.addProperty("name", expectedType, dummyNode);

        Assert.assertNull(duplicateBuilder);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        JSType actualType = recordTypeBuilder.build();

        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testBuildRecordWithProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType expectedType = registry.createRecordType(Collections.unmodifiableMap(recordTypeBuilder.properties));

        recordTypeBuilder.addProperty("name", expectedType, new Node());
        recordTypeBuilder.addProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node());

        JSType actualType = recordTypeBuilder.build();

        Assert.assertEquals(expectedType, actualType);
    }

}
