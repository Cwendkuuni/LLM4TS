import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Assert;
import org.junit.Test;
import java.util.Collections;
import java.util.HashMap;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty_Success() {
        // Setup
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      
        // Create a sample property
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
      
        // Execute
        RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);
      
        // Verify
        Assert.assertNotNull(result);
        Assert.assertEquals(Collections.singletonMap(propertyName, new RecordTypeBuilder.RecordProperty(propertyType, propertyNode)), builder.getProperties());
    }

    @Test
    public void testAddProperty_DuplicateProperty() {
        // Setup
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      
        // Create a sample property
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
      
        // Add the property for the first time
        builder.addProperty(propertyName, propertyType, propertyNode);
      
        // Execute
        RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);
      
        // Verify
        Assert.assertNull(result);
        Assert.assertEquals(Collections.singletonMap(propertyName, new RecordTypeBuilder.RecordProperty(propertyType, propertyNode)), builder.getProperties());
    }

    @Test
    public void testBuild_EmptyRecord() {
        // Setup
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      
        // Execute
        JSType result = builder.build();
      
        // Verify
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testBuild_NonEmptyRecord() {
        // Setup
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      
        // Create a sample property
        String propertyName = "name";
        JSType propertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
      
        // Add the property
        builder.addProperty(propertyName, propertyType, propertyNode);
      
        // Execute
        JSType result = builder.build();
      
        // Verify
        Assert.assertEquals(new RecordType(registry, Collections.singletonMap(propertyName, new RecordTypeBuilder.RecordProperty(propertyType, propertyNode))), result);
    }

}