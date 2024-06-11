import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.Node;
import java.util.Map;
import java.util.HashMap;

public class RecordTypeTest {
    private RecordType recordType;
    private JSTypeRegistry registry;
    private Map<String, RecordProperty> properties;
    private RecordProperty recordProperty;
    private Node propertyNode;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
        recordType = new RecordType(registry, properties);
        recordProperty = new RecordProperty();
        propertyNode = new Node();
    }

    @Test
    public void testDefineProperty() {
        assertTrue(recordType.defineProperty("property1", new JSType(), true, propertyNode));
        assertFalse(recordType.defineProperty("property2", new JSType(), true, propertyNode));
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType(registry, properties);
        properties.put("property1", recordProperty);
        otherRecord.defineDeclaredProperty("property1", new JSType(), propertyNode);
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.EQUAL));
        properties.put("property2", recordProperty);
        assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.EQUAL));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        properties.put("property1", recordProperty);
        JSType result = recordType.getGreatestSubtypeHelper(new JSType());
        assertEquals(result, registry.getNativeObjectType(JSTypeNative.NO_TYPE));
        properties.put("property2", recordProperty);
        RecordType thatRecord = new RecordType(registry, properties);
        thatRecord.defineDeclaredProperty("property2", new JSType(), propertyNode);
        result = recordType.getGreatestSubtypeHelper(thatRecord);
        assertEquals(result, registry.getNativeObjectType(JSTypeNative.NO_TYPE));
    }

    @Test
    public void testIsSubtype() {
        assertTrue(recordType.isSubtype(new JSType()));
        assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
        RecordType thatRecord = new RecordType(registry, properties);
        properties.put("property1", recordProperty);
        thatRecord.defineDeclaredProperty("property1", new JSType(), propertyNode);
        assertTrue(recordType.isSubtype(thatRecord));
        properties.put("property2", recordProperty);
        assertFalse(recordType.isSubtype(thatRecord));
    }
}