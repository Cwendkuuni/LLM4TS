import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Test constructor with Class argument
    @Test
    public void testConstructWithClass() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getType());
    }

    // Test constructor with Class, TypeBindings, JavaType superClass, and JavaType[] superInterfaces arguments
    @Test
    public void testConstructWithArgs() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getType());
    }

    // Test copy constructor
    @Test
    public void testCopyConstructor() {
        SimpleType original = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    // Test _narrow method with sub-class
    @Test
    public void testNarrowMethodWithSubclass() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType subclassType = new SimpleType(StringBuilder.class, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = simpleType.narrow(StringBuilder.class);
        assertEquals(subclassType, narrowedType);
    }

    // Test _narrow method with non-sub-class
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithNonSubclass() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType nonSubclassType = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        simpleType.narrow(Integer.class);
    }

    // Test isContainerType method
    @Test
    public void testIsContainerTypeMethod() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    // Test methods withTypeHandler, withContentTypeHandler, withValueHandler, and withContentValueHandler
    @Test
    public void testMethodsWithHandlers() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();

        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(handler, withTypeHandler.getTypeHandler());

        SimpleType withContentTypeHandler = simpleType.withContentTypeHandler(handler);
        assertNotSame(simpleType, withContentTypeHandler);
        assertEquals(handler, withContentTypeHandler.getContentTypeHandler());

        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, withValueHandler);
        assertEquals(handler, withValueHandler.getValueHandler());

        SimpleType withContentValueHandler = simpleType.withContentValueHandler(handler);
        assertNotSame(simpleType, withContentValueHandler);
        assertEquals(handler, withContentValueHandler.getContentValueHandler());
    }

    // Test withStaticTyping method
    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStaticTyping());
    }

    // Test refine method
    @Test
    public void testRefineMethod() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType refinedType = simpleType.refine(List.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    // Test toString method
    @Test
    public void testToStringMethod() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        String toString = simpleType.toString();
        assertTrue(toString.contains("[simple type, class "));
        assertTrue(toString.contains(String.class.getName()));
    }

    // Test equals method
    @Test
    public void testEqualsMethod() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);

        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }

    // Test constructUnsafe method
    @Test
    public void testConstructUnsafeMethod() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test construct method
    @Test
    public void testConstructDeprecatedMethod() {
        try {
            SimpleType simpleType = SimpleType.construct(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}