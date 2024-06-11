import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {
    @Test
    public void testRecordTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);

        assertNotNull(recordType);
    }

    @Test
    public void testRecordTypeProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        properties.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType = new RecordType(registry, properties);

        assertTrue(recordType.hasProperty("property1"));
        assertTrue(recordType.hasProperty("property2"));
        assertFalse(recordType.hasProperty("property3"));

        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("property1"));
        assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("property2"));
        assertNull(recordType.getPropertyType("property3"));
    }

    @Test
    public void testRecordTypeSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        properties1.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertTrue(recordType1.isSubtype(recordType1));
        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testRecordTypeSubtypeStatic() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        properties1.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(RecordType.isSubtype(recordType1, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertTrue(RecordType.isSubtype(recordType1, recordType1));
        assertFalse(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testRecordTypeGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        properties1.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType2 = new RecordType(registry, properties2);

        ObjectType greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        assertTrue(greatestSubtype.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType1);
        assertTrue(greatestSubtype.isEquivalentTo(recordType1));

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertTrue(greatestSubtype.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.NO_TYPE)));
    }

    @Test
    public void testRecordTypeCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        properties1.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("property2", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        RecordType recordType2 = new RecordType(registry, properties2);

        boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, null);
        assertFalse(result);

        properties2.put("property1", new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        result = recordType1.checkRecordEquivalenceHelper(recordType2, null);
        assertTrue(result);
    }

    @Test
    public void testRecordTypeDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);

        boolean result = recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, null);
        assertTrue(result);

        result = recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, null);
        assertFalse(result);
    }

    @Test
    public void testRecordTypeToMaybeRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);

        RecordType maybeRecordType = recordType.toMaybeRecordType();

        assertSame(recordType, maybeRecordType);
    }

    @Test
    public void testRecordTypeIsSynthetic() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);

        assertFalse(recordType.isSynthetic());
    }
}