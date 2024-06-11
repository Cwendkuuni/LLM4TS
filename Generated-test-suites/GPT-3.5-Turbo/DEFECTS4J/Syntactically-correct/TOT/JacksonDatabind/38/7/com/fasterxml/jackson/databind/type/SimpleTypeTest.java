
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setup() {
        simpleType = SimpleType.construct(Integer.class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType unsafeType = SimpleType.constructUnsafe(String.class);
        assertNotNull(unsafeType);
        assertEquals(String.class, unsafeType.getRawClass());
    }

    @Test
    public void testConstructWithMap() {
        try {
            SimpleType.construct(Map.class);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstructWithCollection() {
        try {
            SimpleType.construct(Collection.class);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
    }

    @Test
    public void testConstructWithArray() {
        try {
            SimpleType.construct(Integer[].class);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Integer;)", e.getMessage());
        }
    }

    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(SimpleType.construct(String.class));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType staticType = simpleType.withStaticTyping();
        assertNotNull(staticType);
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType refinedType = simpleType.refine(Integer.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/Integer;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/Integer;", sb.toString());
    }

    @Test
    public void testEquals() {
        SimpleType sameType = SimpleType.construct(Integer.class);
        assertEquals(simpleType, sameType);

        SimpleType differentType = SimpleType.construct(String.class);
        assertNotEquals(simpleType, differentType);
    }

    @Test
    public void testToString() {
        String str = simpleType.toString();
        assertEquals("[simple type, class java.lang.Integer]", str);
    }
}

