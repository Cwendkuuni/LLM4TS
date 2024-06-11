import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructorWithTypeBindings() {
        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType = new SimpleType(String.class, bindings, null, null);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNotNull(simpleType.getBindings());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
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
        SimpleType.construct(int[].class);
    }

    @Test
    public void testCopyConstructor() {
        SimpleType base = new SimpleType(String.class);
        SimpleType simpleType = new SimpleType(base);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertNotNull(newSimpleType);
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object valueHandler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertNotNull(newSimpleType);
        assertEquals(valueHandler, newSimpleType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertNotNull(newSimpleType);
        assertTrue(newSimpleType.isStatic());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertNotNull(erasedSignature);
        assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertNotNull(genericSignature);
        assertEquals("Ljava/lang/String;", genericSignature.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(String.class, null, null, null));
    }

}