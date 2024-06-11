import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    private JSTypeRegistry registry = new JSTypeRegistry();
    private Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<String, RecordTypeBuilder.RecordProperty>();

    @Test
    public void testRecordTypeCreation() {
        RecordType recordType = new RecordType(registry, properties);
        Assert.assertNotNull(recordType);
    }

    @Test
    public void testRecordTypeCreationWithProperties() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType = new RecordType(registry, properties);
        Assert.assertEquals(properties.size(), recordType.getOwnPropertyNames().size());

        for (String property : properties.keySet()) {
            Assert.assertTrue(recordType.hasProperty(property));
        }
    }

    @Test
    public void testRecordTypeCreationWithNullProperty() {
        properties.put("property1", null);

        try {
            RecordType recordType = new RecordType(registry, properties);
            Assert.fail("Expected IllegalStateException not thrown");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testRecordTypeCreationWithDeclaredProperties() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType = new RecordType(registry, properties, true);
        Assert.assertEquals(properties.size(), recordType.getOwnPropertyNames().size());

        for (String property : properties.keySet()) {
            Assert.assertTrue(recordType.hasProperty(property));
            Assert.assertTrue(recordType.isPropertyTypeDeclared(property));
        }
    }

    @Test
    public void testRecordTypeCreationWithSynthesizedProperties() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType = new RecordType(registry, properties, false);
        Assert.assertEquals(properties.size(), recordType.getOwnPropertyNames().size());

        for (String property : properties.keySet()) {
            Assert.assertTrue(recordType.hasProperty(property));
            Assert.assertFalse(recordType.isPropertyTypeDeclared(property));
        }
    }

    @Test
    public void testRecordTypeIsSynthetic() {
        RecordType recordType = new RecordType(registry, properties, false);
        Assert.assertTrue(recordType.isSynthetic());

        recordType = new RecordType(registry, properties, true);
        Assert.assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testRecordTypeCheckRecordEquivalenceHelper() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        Assert.assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.EQ));

        properties.remove("property3");

        RecordType recordType3 = new RecordType(registry, properties);
        Assert.assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.EQ));
    }

    @Test
    public void testRecordTypeGetImplicitPrototype() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType = new RecordType(registry, properties);
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    }

    @Test
    public void testRecordTypeDefineProperty() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType = new RecordType(registry, properties);

        Assert.assertFalse(recordType.defineProperty("property4", registry.getNativeType(JSTypeNative.STRING_TYPE), true, Node.newString("property4")));
        Assert.assertTrue(recordType.defineProperty("property4", registry.getNativeType(JSTypeNative.STRING_TYPE), false, Node.newString("property4")));
    }

    @Test
    public void testRecordTypeGetGreatestSubtypeHelper() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType1.getGreatestSubtypeHelper(recordType2));

        properties.put("property4", new RecordTypeBuilder.RecordProperty("property4", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property4")));

        RecordType recordType3 = new RecordType(registry, properties);
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType1.getGreatestSubtypeHelper(recordType3));
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.STRING_TYPE)));
    }

    @Test
    public void testRecordTypeToMaybeRecordType() {
        RecordType recordType = new RecordType(registry, properties);
        Assert.assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testRecordTypeIsSubtype() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType1 = new RecordType(registry, properties);

        Assert.assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        Assert.assertFalse(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE)));
        Assert.assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.STRING_TYPE)));

        RecordType recordType2 = new RecordType(registry, properties, true);
        Assert.assertTrue(recordType2.isSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE)));
    }

    @Test
    public void testRecordTypeIsSubtypeWithRecordType() {
        properties.put("property1", new RecordTypeBuilder.RecordProperty("property1", registry.getNativeType(JSTypeNative.STRING_TYPE), Node.newString("property1")));
        properties.put("property2", new RecordTypeBuilder.RecordProperty("property2", registry.getNativeType(JSTypeNative.NUMBER_TYPE), Node.newNumber(1)));
        properties.put("property3", new RecordTypeBuilder.RecordProperty("property3", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), Node.newBoolean(true)));

        RecordType recordType1 = new RecordType(registry, properties);

        properties.remove("property3");
        RecordType recordType2 = new RecordType(registry, properties);

        Assert.assertTrue(RecordType.isSubtype(recordType1, recordType2));
        Assert.assertFalse(RecordType.isSubtype(recordType2, recordType1));
    }
}