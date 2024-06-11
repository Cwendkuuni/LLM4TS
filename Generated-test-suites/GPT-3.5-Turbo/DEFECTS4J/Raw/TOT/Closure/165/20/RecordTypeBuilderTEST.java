import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty_typicalCase() {
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();

        RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

        assertNotNull(result);
        assertEquals(propertyType, recordTypeBuilder.build().getProperty(propertyName));
    }

    @Test
    public void testAddProperty_duplicateName() {
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();

        recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

        assertNull(result);
        assertEquals(propertyType, recordTypeBuilder.build().getProperty(propertyName));
    }

    @Test
    public void testBuild_emptyRecord() {
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        JSType result = recordTypeBuilder.build();

        assertEquals(expectedType, result);
        assertTrue(result.getProperties().isEmpty());
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();

        recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

        JSType result = recordTypeBuilder.build();

        assertEquals(propertyType, result.getProperty(propertyName));
    }

    @Test
    public void testBuild_multipleProperties() {
        String propertyName1 = "name";
        JSType propertyType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode1 = new Node();

        String propertyName2 = "age";
        JSType propertyType2 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode2 = new Node();

        recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1);
        recordTypeBuilder.addProperty(propertyName2, propertyType2, propertyNode2);

        JSType result = recordTypeBuilder.build();

        assertEquals(propertyType1, result.getProperty(propertyName1));
        assertEquals(propertyType2, result.getProperty(propertyName2));
    }

}