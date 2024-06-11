import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;


import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    private JSTypeRegistry registry;

    private void setUp() {
        registry = new JSTypeRegistry();
    }

    @Test
    public void testRecordTypeCreation() {
        setUp();

        Map<String, RecordProperty> properties = new HashMap<>();
        RecordProperty prop1 = new RecordProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node());
        RecordProperty prop2 = new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node());
        properties.put("a", prop1);
        properties.put("b", prop2);

        RecordType recordType = new RecordType(registry, properties);

        Assert.assertNotNull(recordType);
        Assert.assertEquals(properties.keySet(), recordType.getProperties().keySet());
        Assert.assertEquals(prop1.getType(), recordType.getPropertyType("a"));
        Assert.assertEquals(prop2.getType(), recordType.getPropertyType("b"));
    }

    @Test
    public void testRecordTypeIsEquivalentTo() {
        setUp();

        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties1.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties2.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        Assert.assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testRecordTypeIsSubtype() {
        setUp();

        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties1.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties2.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        Assert.assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testRecordTypeGetLeastSupertype() {
        setUp();

        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties1.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        properties2.put("b", new RecordProperty("b", registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node()));
        properties2.put("c", new RecordProperty("c", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), new Node()));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        ObjectType leastSupertype = recordType1.getLeastSupertype(recordType2);

        Assert.assertNotNull(leastSupertype);
        Assert.assertTrue(leastSupertype instanceof RecordType);
        RecordType leastSupertypeRecord = (RecordType) leastSupertype;
        Assert.assertTrue(leastSupertypeRecord.getProperties().keySet().containsAll(properties1.keySet()));
        Assert.assertTrue(leastSupertypeRecord.getProperties().keySet().containsAll(properties2.keySet()));
    }

    @Test
    public void testRecordTypeIsFrozen() {
        setUp();

        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty("a", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));

        RecordType recordType = new RecordType(registry, properties);

        Assert.assertTrue(recordType.isFrozen());
    }

}