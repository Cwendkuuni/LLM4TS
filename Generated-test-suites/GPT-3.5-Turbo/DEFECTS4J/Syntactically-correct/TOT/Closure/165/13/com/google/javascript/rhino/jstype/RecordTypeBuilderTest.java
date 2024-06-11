import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        // Typical use case
        builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        
        // Edge case: adding property with same name
        assertNull(builder.addProperty("name", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        
        // Error scenario: adding null property
        assertNull(builder.addProperty(null, registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builderEmpty = new RecordTypeBuilder(registry);
        
        // Typical use case: empty record
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), builderEmpty.build());
        
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
        builder.addProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
        
        // Typical use case: non-empty record
        assertNotNull(builder.build());
    }
}