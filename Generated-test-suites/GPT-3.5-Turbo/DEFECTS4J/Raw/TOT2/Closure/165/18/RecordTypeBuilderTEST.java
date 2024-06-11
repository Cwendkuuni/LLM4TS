import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        // Typical use case
        assertTrue(builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
        
        // Edge case - adding a property with the same name should return null
        assertNull(builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node()));
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        // Typical use case - building a non-empty record type
        builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), new Node());
        assertNotNull(builder.build());
        
        // Edge case - building an empty record type should return OBJECT_TYPE
        builder = new RecordTypeBuilder(registry);
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), builder.build());
    }
}