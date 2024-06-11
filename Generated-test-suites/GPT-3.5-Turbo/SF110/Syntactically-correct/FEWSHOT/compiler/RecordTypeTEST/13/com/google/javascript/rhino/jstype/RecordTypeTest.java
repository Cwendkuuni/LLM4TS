import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.javascript.rhino.jstype.Ref;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        recordType = createRecordType(false);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecordType = createRecordType(true);
        assertFalse(recordType.checkRecordEquivalenceHelper(otherRecordType, EquivalenceMethod.SCRIPT));
    }

    @Test
    public void testGetPropertyType() {
        JSType propType = recordType.getPropertyType("property");
        assertEquals(JSTypeNative.BOOLEAN_TYPE, propType);
    }

    @Test
    public void testDefineProperty() {
        assertFalse(recordType.defineProperty("property", JSTypeNative.NULL_TYPE, false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType expectedSubtype = registry.getNativeObjectType(JSTypeNative.NO_TYPE);
        JSType subtype = recordType.getGreatestSubtypeHelper(expectedSubtype);
        assertEquals(expectedSubtype, subtype);
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType maybeRecordType = recordType.toMaybeRecordType();
        assertEquals(recordType, maybeRecordType);
    }

    @Test
    public void testIsSubtype() {
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordType.isSubtype(objectType));
    }

    @Test
    public void testIsSubtype_withRecordType() {
        RecordType otherRecordType = createRecordType(false);
        assertTrue(RecordType.isSubtype(recordType, otherRecordType));
    }

    private RecordType createRecordType(boolean declared) {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
        return new RecordType(registry, properties, declared);
    }
}