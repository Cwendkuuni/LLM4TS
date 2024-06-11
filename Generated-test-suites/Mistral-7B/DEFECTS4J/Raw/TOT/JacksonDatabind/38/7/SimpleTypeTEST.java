import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test constructor with a simple class
        Class<String> stringClass = String.class;
        JavaType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
        assertEquals(0, simpleType.getTypeParameters().length);

        // Test constructor with bindings, superclass, and superinterfaces
        JavaType bindingType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType complexType = new SimpleType(String.class, TypeBindings.builder()
                .putBinding(String.class, new SimpleModule.SimpleTypeSerializer(String.class))
                .putBinding(Integer.class, new SimpleModule.SimpleTypeSerializer(Integer.class))
                .build(), bindingType, null);
        assertEquals(String.class, complexType.getRawClass());
        assertEquals(bindingType, complexType.getSuperClass());
        assertArrayEquals(new Class[]{}, complexType.getSuperTypes());
        assertEquals(0, complexType.getTypeParameters().length);
    }

    @Test
    public void testSimpleTypeMethods() {
        Class<String> stringClass = String.class;
        JavaType simpleType = new SimpleType(stringClass);

        // test isContainerType
        assertFalse(simpleType.isContainerType());

        // test withContentType
        try {
            simpleType.withContentType(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Pass
        }

        // test withTypeHandler
        Object handler = new Object();
        JavaType typeWithHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, typeWithHandler);
        assertSame(handler, typeWithHandler.getTypeHandler());

        // test withContentTypeHandler
        try {
            simpleType.withContentTypeHandler(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Pass
        }

        // test withValueHandler
        Object valueHandler = new Object();
        JavaType typeWithValueHandler = simpleType.withValueHandler(valueHandler);
        assertNotSame(simpleType, typeWithValueHandler);
        assertSame(valueHandler, typeWithValueHandler.getValueHandler());

        // test withContentValueHandler
        try {
            simpleType.withContentValueHandler(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Pass
        }

        // test withStaticTyping
        JavaType staticType = simpleType.withStaticTyping();
        assertNotSame(simpleType, staticType);
        assertTrue(staticType.isStatic());

        // test refine
        try {
            simpleType.refine(int[].class, null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // Pass
        }
    }

    @Test
    public void testUnsafeConstruction() {
        // Test that the unsafe constructor throws an IllegalArgumentException
        // when passed a class that is not a simple type
        try {
            SimpleType.constructUnsafe(ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Pass
        }
    }

    @Test
    public void testDeprecatedConstructMethod() {
        // Test that the deprecated constructor throws an IllegalStateException
        try {
            SimpleType.construct(String.class);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // Pass
        }
    }
}