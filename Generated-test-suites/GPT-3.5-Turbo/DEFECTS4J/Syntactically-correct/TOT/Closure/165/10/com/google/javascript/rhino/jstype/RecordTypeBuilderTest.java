import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.jstype.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        // Initialize the JSTypeRegistry and RecordTypeBuilder
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test adding a property with a valid name and type
        // A typical use case
        String propertyName = "age";
        JSType propertyType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Assert.assertNotNull(builder.addProperty(propertyName, propertyType, null));

        // Test adding the same property twice
        // An error scenario
        Assert.assertNull(builder.addProperty(propertyName, propertyType, null));

        // Test adding a property with an empty string name
        // An edge case
        propertyName = "";
        Assert.assertNull(builder.addProperty(propertyName, propertyType, null));

        // Test adding a property with a null type
        // An edge case
        propertyName = "name";
        Assert.assertNull(builder.addProperty(propertyName, null, null));

        // Test adding a property with null as the property node
        // An edge case
        propertyName = "gender";
        Assert.assertNotNull(builder.addProperty(propertyName, propertyType, null));
    }

    @Test
    public void testBuild() {
        // Initialize the JSTypeRegistry and RecordTypeBuilder
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test building an empty record
        // An edge case
        JSType emptyRecord = builder.build();
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), emptyRecord);

        // Test building a record with properties
        // A typical use case
        builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        builder.addProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        JSType recordWithProperties = builder.build();
        Assert.assertNotEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordWithProperties);
    }
}