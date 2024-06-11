import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
    }

    @Test
    public void testNarrow() {
        SimpleType base = SimpleType.construct(Number.class);
        SimpleType type = base.narrow(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertEquals(base.getValueHandler(), type.getValueHandler());
        assertEquals(base.getTypeHandler(), type.getTypeHandler());
    }

    @Test
    public void testWithContentType() {
        SimpleType base = SimpleType.construct(List.class);
        SimpleType type = base.withContentType(SimpleType.construct(String.class));
        assertEquals(List.class, type.getRawClass());
        assertEquals(base.getValueHandler(), type.getValueHandler());
        assertEquals(base.getTypeHandler(), type.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeException() {
        SimpleType base = SimpleType.construct(Integer.class);
        base.withContentType(SimpleType.construct(Long.class));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType base = SimpleType.construct(String.class);
        Object handler = new Object();
        SimpleType type = base.withTypeHandler(handler);
        assertEquals(String.class, type.getRawClass());
        assertEquals(handler, type.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerException() {
        SimpleType base = SimpleType.construct(String.class);
        base.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType base = SimpleType.construct(String.class);
        Object handler = new Object();
        SimpleType type = base.withContentValueHandler(handler);
        assertEquals(String.class, type.getRawClass());
        assertEquals(handler, type.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType base = SimpleType.construct(List.class);
        SimpleType type = base.withStaticTyping();
        assertEquals(List.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertEquals(true, type.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType base = SimpleType.construct(Collection.class);
        SimpleType type = base.refine(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        assertNull(type);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(false, type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(String.class);
        SimpleType type2 = SimpleType.construct(String.class);
        assertEquals(true, type1.equals(type2));
    }
}