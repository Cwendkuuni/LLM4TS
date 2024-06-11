import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        // Typical use case
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("Expected class is not equal", String.class, simpleType.getRawClass());

        // Edge case
        try {
            SimpleType.constructUnsafe(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testConstruct() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals("Expected class is not equal", Integer.class, simpleType.getRawClass());

        // Error scenario
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            SimpleType.construct(ArrayList.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            SimpleType.construct(int[].class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void test_withTypeHandler() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType newSimpleType = simpleType.withTypeHandler("typeHandler");
        assertEquals("Expected type handler is not equal", "typeHandler", newSimpleType.getTypeHandler());

        // Edge case
        SimpleType sameSimpleType = simpleType.withTypeHandler(simpleType.getTypeHandler());
        assertSame("Expected same instance of SimpleType", simpleType, sameSimpleType);
    }

    @Test
    public void test_withValueHandler() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType newSimpleType = simpleType.withValueHandler("valueHandler");
        assertEquals("Expected value handler is not equal", "valueHandler", newSimpleType.getValueHandler());

        // Edge case
        SimpleType sameSimpleType = simpleType.withValueHandler(simpleType.getValueHandler());
        assertSame("Expected same instance of SimpleType", simpleType, sameSimpleType);
    }

    @Test
    public void test_withStaticTyping() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue("Expected asStatic is true", newSimpleType.isAsStatic());

        // Edge case
        SimpleType sameSimpleType = simpleType.withStaticTyping();
        assertSame("Expected same instance of SimpleType", simpleType, sameSimpleType);
    }

    @Test
    public void test_refine() {
        // Edge case
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNull("Expected null", simpleType.refine(String.class, null, null, null));
    }

    @Test
    public void test_isContainerType() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse("Expected false", simpleType.isContainerType());
    }

    @Test
    public void test_getErasedSignature() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);
        assertNotNull("Expected not null", result);
        assertEquals("Expected StringBuilder is same", sb, result);
    }

    @Test
    public void test_getGenericSignature() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);
        assertNotNull("Expected not null", result);
        assertEquals("Expected StringBuilder is same", sb, result);
    }

    @Test
    public void test_toString() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        String result = simpleType.toString();
        assertNotNull("Expected not null", result);
        assertTrue("Expected starts with '[simple type, class'", result.startsWith("[simple type, class"));
        assertTrue("Expected ends with ']'", result.endsWith("]"));
    }

    @Test
    public void test_equals() {
        // Typical use case
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertFalse("Expected false", simpleType1.equals(simpleType2));
    }

}