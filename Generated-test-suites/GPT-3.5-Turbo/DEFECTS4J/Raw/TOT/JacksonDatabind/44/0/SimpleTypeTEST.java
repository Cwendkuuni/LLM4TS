
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        JavaType contentType = SimpleType.constructUnsafe(String.class);
        SimpleType result = simpleType.withContentType(contentType);
        assertEquals(List.class, result.getRawClass());
        assertFalse(result.isContainerType());
        assertNotNull(result.getContentType());
        assertEquals(String.class, result.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeThrowsException() {
        SimpleType simpleType = SimpleType.constructUnsafe(Exception.class);
        JavaType contentType = SimpleType.constructUnsafe(String.class);
        simpleType.withContentType(contentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerThrowsException() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        simpleType.withContentTypeHandler(new CustomValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType result = simpleType.withStaticTyping();
        assertEquals(String.class, result.getRawClass());
        assertFalse(result.isContainerType());
        assertNull(result.getContentType());
        assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        TypeBindings bindings = TypeBindings.create(new JavaType[]{SimpleType.constructUnsafe(String.class)});
        SimpleType superClass = SimpleType.constructUnsafe(Collection.class)
                .withContentType(SimpleType.constructUnsafe(Object.class));
        SimpleType[] superInterfaces = new SimpleType[]{SimpleType.constructUnsafe(Serializable.class)};
        JavaType result = simpleType.refine(List.class, bindings, superClass, superInterfaces);
        assertEquals(List.class, result.getRawClass());
        assertFalse(result.isContainerType());
        assertNotNull(result.getSuperClass());
        assertEquals(SimpleType.constructUnsafe(Collection.class), result.getSuperClass());
        assertArrayEquals(superInterfaces, result.getInterfaces());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = new StringBuilder();
        assertEquals("Ljava/util/List;", simpleType.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class)
                .withContentType(SimpleType.constructUnsafe(String.class))
                .withTypeHandler(new CustomTypeHandler())
                .withValueHandler(new CustomValueHandler());
        StringBuilder sb = new StringBuilder();
        assertEquals("Ljava/util/List<L\021java/lang/String;>;;", simpleType.getGenericSignature(sb).toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(List.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(List.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(String.class);

        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class)
                .withContentType(SimpleType.constructUnsafe(String.class));
        assertEquals("[simple type, class java.util.List<java.lang.String>]", simpleType.toString());
    }
}

