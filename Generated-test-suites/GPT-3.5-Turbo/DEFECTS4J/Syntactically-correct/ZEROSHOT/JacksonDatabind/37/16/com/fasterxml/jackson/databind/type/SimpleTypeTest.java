import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            SimpleType.construct(Object[].class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void test_narrow() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(simpleType, simpleType._narrow(Object.class));
        SimpleType newType = simpleType._narrow(String.class);
        assertNotEquals(simpleType, newType);
        assertEquals(String.class, newType.getRawClass());
    }

    @Test
    public void test_withTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(simpleType, simpleType.withTypeHandler(null));
        Object typeHandler = new Object();
        SimpleType newType = simpleType.withTypeHandler(typeHandler);
        assertNotEquals(simpleType, newType);
        assertEquals(typeHandler, newType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_withContentType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.withContentType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_withContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.withContentTypeHandler(null);
    }

    @Test
    public void test_withValueHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(simpleType, simpleType.withValueHandler(null));
        Object valueHandler = new Object();
        SimpleType newType = simpleType.withValueHandler(valueHandler);
        assertNotEquals(simpleType, newType);
        assertEquals(valueHandler, newType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_withContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void test_withStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(simpleType, simpleType.withStaticTyping());
        SimpleType newType = simpleType.withStaticTyping();
        assertNotEquals(simpleType, newType);
        assertTrue(newType.isStatic());
    }

    @Test
    public void test_refine() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    @Test
    public void test_isContainerType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void test_getErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(Object.class.getName(), sb.toString());
    }

    @Test
    public void test_getGenericTypeSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(Object.class.getName(), sb.toString());
    }

    @Test
    public void test_toString() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        String expected = "[simple type, class " + Object.class.getName() + "]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void test_equal() {
        SimpleType simpleType1 = SimpleType.construct(Object.class);
        SimpleType simpleType2 = SimpleType.construct(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}