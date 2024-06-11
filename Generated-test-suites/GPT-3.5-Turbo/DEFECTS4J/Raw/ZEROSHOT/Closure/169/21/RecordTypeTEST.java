import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import java.util.Map;
import java.util.TreeMap;

public class RecordTypeTest {
    private RecordType recordType;
    private JSTypeRegistry registry;
    private Map<String, RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new TreeMap<>();
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testDefineProperty() {
        String propertyName = "property";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        boolean inferred = true;
        Node propertyNode = new Node();

        boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);

        assertTrue(result);
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType implicitPrototype = recordType.getImplicitPrototype();

        assertNotNull(implicitPrototype);
    }

    @Test
    public void testToString() {
        String toString = recordType.toString();

        assertNotNull(toString);
    }

    @Test
    public void testIsSubtype() {
        JSType thatType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        boolean result = recordType.isSubtype(thatType);

        assertTrue(result);
    }

    @Test
    public void testGetProperties() {
        Map<String, JSType> properties = recordType.getProperties();

        assertNotNull(properties);
    }

    @Test
    public void testHasProperty() {
        String propertyName = "property";

        boolean result = recordType.hasProperty(propertyName);

        assertFalse(result);
    }

    @Test
    public void testGetPropertyType() {
        String propertyName = "property";

        JSType propertyType = recordType.getPropertyType(propertyName);

        assertNull(propertyType);
    }

    @Test
    public void testGetPropertyNode() {
        String propertyName = "property";

        Node propertyNode = recordType.getPropertyNode(propertyName);

        assertNull(propertyNode);
    }
}