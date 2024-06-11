import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(TYPE_1, propertyNodeA));
        propertiesA.put("b", new RecordProperty(TYPE_2, propertyNodeB));
        RecordType recordA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty(TYPE_1, propertyNodeA));
        propertiesB.put("b", new RecordProperty(TYPE_2, propertyNodeB));
        RecordType recordB = new RecordType(registry, propertiesB);

        Assert.assertTrue(recordA.isEquivalentTo(recordB));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordType record = new RecordType(registry, properties);
        ObjectType prototype = record.getImplicitPrototype();
        Assert.assertEquals(JSTypeNative.OBJECT_TYPE, prototype.getType());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordType record = new RecordType(registry, properties);
        boolean success = record.defineProperty("a", TYPE_1, false, propertyNodeA);
        Assert.assertTrue(success);
    }

    @Test
    public void testToMaybeRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordType record = new RecordType(registry, properties);
        RecordType maybeRecord = record.toMaybeRecordType();
        Assert.assertEquals(record, maybeRecord);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(TYPE_1, propertyNodeA));
        propertiesA.put("b", new RecordProperty(TYPE_2, propertyNodeB));
        RecordType recordA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty(TYPE_1, propertyNodeA));
        propertiesB.put("b", new RecordProperty(TYPE_2, propertyNodeB));
        RecordType recordB = new RecordType(registry, propertiesB);

        Assert.assertTrue(recordA.isSubtype(recordB));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordType record = new RecordType(registry, properties);
        ErrorReporter errorReporter = new ErrorReporter();

        JSType resolvedType = record.resolveInternal(errorReporter, null);
        Assert.assertEquals(record, resolvedType);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty(TYPE_1, propertyNodeA));
        RecordType recordA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty(TYPE_1, propertyNodeA));
        propertiesB.put("b", new RecordProperty(TYPE_2, propertyNodeB));
        RecordType recordB = new RecordType(registry, propertiesB);

        JSType greatestSubtype = recordA.getGreatestSubtypeHelper(recordB);
        Assert.assertEquals(recordB, greatestSubtype);
    }
}