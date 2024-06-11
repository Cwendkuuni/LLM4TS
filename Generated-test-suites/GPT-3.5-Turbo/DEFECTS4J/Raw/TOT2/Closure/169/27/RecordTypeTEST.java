import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import java.util.Map;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new Map<String, RecordProperty>();
        RecordProperty prop1 = new RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        properties.put("a", prop1);

        RecordType recordType = new RecordType(registry, properties);

        assertNotNull(recordType);
        assertTrue(recordType.isSynthetic());
        assertTrue(recordType.isFrozen());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, properties);

        recordType.defineProperty("b", registry.getNativeType(JSTypeNative.STRING_TYPE), false, new Node(Token.NAME, "b"));

        assertNotNull(recordType.getPropertyType("b"));
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);

        boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, true);

        assertTrue(result);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new Map<String, RecordProperty>();
        RecordProperty prop1 = new RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        properties.put("a", prop1);

        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);

        boolean result = recordType1.isSubtype(recordType2);

        assertTrue(result);
    }

}