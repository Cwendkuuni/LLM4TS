import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType._narrow(Object.class);
        assertNotSame(simpleType, narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected an exception to be thrown
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object();
        SimpleType modifiedType = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType, modifiedType);
        assertEquals(typeHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected an exception to be thrown
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object valueHandler = new Object();
        SimpleType modifiedType = simpleType.withValueHandler(valueHandler);
        assertNotSame(simpleType, modifiedType);
        assertEquals(valueHandler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected an exception to be thrown
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertNotSame(simpleType, modifiedType);
        assertTrue(modifiedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
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
}