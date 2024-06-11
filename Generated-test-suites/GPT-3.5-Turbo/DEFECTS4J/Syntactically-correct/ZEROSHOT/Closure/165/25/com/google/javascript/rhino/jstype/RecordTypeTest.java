import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        RecordProperty prop1 = new RecordProperty("a", JSTypeNative.NUMBER_TYPE, null);
        RecordProperty prop2 = new RecordProperty("b", JSTypeNative.NUMBER_TYPE, null);
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", prop1);
        properties1.put("b", prop2);
        RecordType type1 = new RecordType(registry, properties1);

        RecordProperty prop3 = new RecordProperty("a", JSTypeNative.NUMBER_TYPE, null);
        RecordProperty prop4 = new RecordProperty("b", JSTypeNative.NUMBER_TYPE, null);
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", prop3);
        properties2.put("b", prop4);
        RecordType type2 = new RecordType(registry, properties2);

        assertTrue(type1.isEquivalentTo(type2));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordProperty prop1 = new RecordProperty("a", JSTypeNative.NUMBER_TYPE, null);
        RecordProperty prop2 = new RecordProperty("b", JSTypeNative.NUMBER_TYPE, null);
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", prop1);
        properties.put("b", prop2);
        RecordType type = new RecordType(registry, properties);

        ObjectType prototype = type.getImplicitPrototype();
        assertTrue(prototype.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testIsSubtype() {
        RecordProperty prop1 = new RecordProperty("a", JSTypeNative.NUMBER_TYPE, null);
        RecordProperty prop2 = new RecordProperty("b", JSTypeNative.NUMBER_TYPE, null);
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", prop1);
        properties1.put("b", prop2);
        RecordType type1 = new RecordType(registry, properties1);

        RecordProperty prop3 = new RecordProperty("a", JSTypeNative.NUMBER_TYPE, null);
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", prop3);
        RecordType type2 = new RecordType(registry, properties2);

        assertTrue(type2.isSubtype(type1));
    }
}