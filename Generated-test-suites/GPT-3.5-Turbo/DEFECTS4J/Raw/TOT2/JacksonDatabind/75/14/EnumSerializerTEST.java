import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        // Typical use case
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(EnumClass.class), true);
        // Add test for serialization
        
        // Edge case - testing serialization when enum values are null
        // Add test for serialization
        
        // Error scenario - serialization with a null Enum value
        // Add test for serialization
    }

    @Test
    public void testConstruct() {
        // Typical use case
        EnumSerializer enumSerializer = EnumSerializer.construct(EnumClass.class, config, beanDesc, format);
        assertNotNull(enumSerializer);
        assertTrue(enumSerializer instanceof EnumSerializer);
        
        // Edge case - constructing with null enumClass
        // Add test for construction
        
        // Error scenario - construction with null config
        // Add test for construction
    }

    @Test
    public void testCreateContextual() {
        // Typical use case
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(EnumClass.class), true);
        JsonSerializer<?> contextualSerializer = enumSerializer.createContextual(serializerProvider, beanProperty);
        assertNotNull(contextualSerializer);
        
        // Edge case - creating contextual serializer with null beanProperty
        // Add test for createContextual
        
        // Error scenario - creating contextual serializer with null serializerProvider
        // Add test for createContextual
    }

    // Add more test methods for the remaining public methods in EnumSerializer class

}