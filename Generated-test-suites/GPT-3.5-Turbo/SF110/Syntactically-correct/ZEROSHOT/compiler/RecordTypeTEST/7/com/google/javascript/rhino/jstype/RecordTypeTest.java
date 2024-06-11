import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    @Test
    public void testConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        Assert.assertNotNull(recordType);
    }

    @Test
    public void testIsSynthetic() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        boolean isSynthetic = recordType.isSynthetic();
        Assert.assertFalse(isSynthetic);
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        EquivalenceMethod eqMethod = new EquivalenceMethod();
        boolean result = recordType1.checkRecordEquivalenceHelper(recordType2, eqMethod);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        Assert.assertNotNull(implicitPrototype);
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        boolean result = recordType.defineProperty("property", new JSType(), false, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        Assert.assertNotNull(greatestSubtype);
    }

    @Test
    public void testToMaybeRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        RecordType maybeRecordType = recordType.toMaybeRecordType();
        Assert.assertEquals(recordType, maybeRecordType);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        boolean isSubtype = recordType1.isSubtype(recordType2);
        Assert.assertTrue(isSubtype);
    }

    @Test
    public void testIsSubtypeWithObjectType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        ObjectType objectType = new ObjectType();
        boolean isSubtype = recordType.isSubtype(objectType);
        Assert.assertTrue(isSubtype);
    }

    @Test
    public void testIsSubtypeWithNonRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        JSType nonRecordType = new JSType();
        boolean isSubtype = recordType.isSubtype(nonRecordType);
        Assert.assertFalse(isSubtype);
    }

    @Test
    public void testIsSubtypeHelper() {
        boolean result = RecordType.isSubtype(new ObjectType(), new RecordType());
        Assert.assertFalse(result);
    }

}