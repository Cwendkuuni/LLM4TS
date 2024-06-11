import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType baseType = new SimpleType(String.class);
        SimpleType simpleType = new SimpleType(baseType);
        assertNotNull(simpleType);
        assertEquals(baseType.getRawClass(), simpleType.getRawClass());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = new SimpleType(Object.class);
        SimpleType simpleType = (SimpleType) baseType.narrow(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType baseType = new SimpleType(Object.class);
        baseType.narrow(Integer.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType baseType = new SimpleType(List.class);
        try {
            baseType.withContentType(SimpleType.constructUnsafe(String.class));
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType baseType = new SimpleType(Integer.class);
        Object typeHandler = new Object();
        SimpleType simpleType = baseType.withTypeHandler(typeHandler);
        assertNotNull(simpleType);
        assertEquals(typeHandler, simpleType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType baseType = new SimpleType(Set.class);
        try {
            baseType.withContentTypeHandler(new Object());
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType baseType = new SimpleType(Boolean.class);
        Object valueHandler = new Object();
        SimpleType simpleType = baseType.withValueHandler(valueHandler);
        assertNotNull(simpleType);
        assertEquals(valueHandler, simpleType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType baseType = new SimpleType(ArrayList.class);
        try {
            baseType.withContentValueHandler(new Object());
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType baseType = new SimpleType(Double.class);
        SimpleType simpleType = baseType.withStaticTyping();
        assertTrue(simpleType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType baseType = new SimpleType(List.class);
        JavaType refinedType = baseType.refine(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Byte.class);
        StringBuilder signature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/Byte;", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType baseType = new SimpleType(List.class);
        JavaType typeArgument = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType = (SimpleType) baseType.withContentType(typeArgument);
        StringBuilder signature = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", signature.toString());
    }

    @Test
    public void testEquals() {
        SimpleType baseType = new SimpleType(Long.class);
        SimpleType simpleType1 = new SimpleType(Long.class);
        SimpleType simpleType2 = new SimpleType(Long.class);
        SimpleType simpleType3 = new SimpleType(Double.class);
        assertTrue(baseType.equals(baseType));
        assertTrue(baseType.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Float.class);
        assertEquals("[simple type, class java.lang.Float]", simpleType.toString());
    }
}