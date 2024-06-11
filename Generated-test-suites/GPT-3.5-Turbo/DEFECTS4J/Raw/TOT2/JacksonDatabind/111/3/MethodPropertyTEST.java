
import static org.junit.Assert.*;
import org.junit.Test;

public class MethodPropertyTest {

    // Test MethodProperty(BeanPropertyDefinition, JavaType, TypeDeserializer, Annotations, AnnotatedMethod)
    @Test
    public void testMethodPropertyConstructor() {
        // Test valid parameters
        MethodProperty prop = new MethodProperty(propDef, type, typeDeser, annotations, method);
        assertNotNull(prop);

        // Test null parameters
        assertThrows(IllegalArgumentException.class, () -> new MethodProperty(null, null, null, null, null));
    }

    // Test MethodProperty(MethodProperty, JsonDeserializer<?>, NullValueProvider)
    @Test
    public void testMethodPropertyCopyConstructor() {
        // Test valid parameters
        MethodProperty propCopy = new MethodProperty(src, deserializer, nva);
        assertNotNull(propCopy);

        // Test null parameters
        assertThrows(IllegalArgumentException.class, () -> new MethodProperty(null, null, null));
    }

    // Test MethodProperty(MethodProperty, PropertyName)
    @Test
    public void testMethodPropertyCopyConstructorWithPropertyName() {
        // Test valid parameters
        MethodProperty propCopy = new MethodProperty(src, newName);
        assertNotNull(propCopy);

        // Test null parameters
        assertThrows(IllegalArgumentException.class, () -> new MethodProperty(null, null));
    }
}
