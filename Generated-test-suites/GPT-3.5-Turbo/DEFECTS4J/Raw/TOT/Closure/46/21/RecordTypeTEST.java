import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testisEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Create two record types with the same properties, but different property types
        RecordProperty propertyA = new RecordProperty(new JSType(), new Node());
        RecordProperty propertyB = new RecordProperty(new JSType(), new Node());
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", propertyA);
        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("a", propertyB);

        RecordType typeA = new RecordType(registry, propertiesA);
        RecordType typeB = new RecordType(registry, propertiesB);

        // The two record types should not be equivalent
        Assert.assertFalse(typeA.isEquivalentTo(typeB));
    }

    @Test
    public void testisSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Create two record types where one is a subtype of the other
        RecordProperty propertyA = new RecordProperty(new JSType(), new Node());
        RecordProperty propertyB = new RecordProperty(new JSType(), new Node());
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", propertyA);
        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("a", propertyB);

        RecordType typeA = new RecordType(registry, propertiesA);
        RecordType typeB = new RecordType(registry, propertiesB);

        // typeA should be a subtype of typeB
        Assert.assertTrue(typeA.isSubtype(typeB));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Create two record types with different properties
        RecordProperty propertyA = new RecordProperty(new JSType(), new Node());
        RecordProperty propertyB = new RecordProperty(new JSType(), new Node());
        Map<String, RecordProperty> propertiesA = new HashMap<>();
        propertiesA.put("a", propertyA);
        Map<String, RecordProperty> propertiesB = new HashMap<>();
        propertiesB.put("b", propertyB);

        RecordType typeA = new RecordType(registry, propertiesA);
        RecordType typeB = new RecordType(registry, propertiesB);

        // The least supertype of the two record types should be OBJECT_TYPE
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), typeA.getLeastSupertype(typeB));
    }

}