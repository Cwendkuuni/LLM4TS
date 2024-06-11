import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals("Simple type, class java.lang.String", simpleType.toString());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals("Simple type, class java.lang.Integer", simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = SimpleType.construct(String.class);
        SimpleType narrowedType = baseType.narrow(CharSequence.class);
        assertNotNull(narrowedType);
        assertEquals(CharSequence.class, narrowedType.getRawClass());
        assertFalse(narrowedType.isContainerType());
        assertEquals("Simple type, class java.lang.CharSequence", narrowedType.toString());
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType baseType = SimpleType.construct(String.class);
        SimpleType typeWithHandler = baseType.withTypeHandler(String.class);
        assertNotNull(typeWithHandler);
        assertEquals(String.class, typeWithHandler.getTypeHandler());
        assertEquals("Simple type, class java.lang.String", typeWithHandler.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType baseType = SimpleType.construct(String.class);
        baseType.withContentType(SimpleType.construct(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType baseType = SimpleType.construct(String.class);
        baseType.withContentValueHandler(String.class);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType baseType = SimpleType.construct(String.class);
        SimpleType staticType = baseType.withStaticTyping();
        assertNotNull(staticType);
        assertEquals(String.class, staticType.getRawClass());
        assertFalse(staticType.isContainerType());
        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType baseType = SimpleType.construct(String.class);
        JavaType refinedType = baseType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("L;java.lang.String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType baseType = SimpleType.construct(String.class);
        SimpleType simpleType = baseType.withTypeHandler(String.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("L;java.lang.String;", sb.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        SimpleType simpleType3 = SimpleType.construct(Integer.class);
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}