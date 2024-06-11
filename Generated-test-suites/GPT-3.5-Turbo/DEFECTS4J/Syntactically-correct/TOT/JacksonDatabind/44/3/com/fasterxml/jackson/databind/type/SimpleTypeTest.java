import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setup() {
        simpleType = new SimpleType(Object.class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType1.getRawClass());
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
    public void testConstructWithValidClass() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType1.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType1 = simpleType.narrow(String.class);
        assertEquals(String.class, simpleType1.getRawClass());
    }

    @Test
    public void testWithContentType() {
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentType(String.class);
        });
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType simpleType1 = simpleType.withTypeHandler(handler);
        assertEquals(handler, simpleType1.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentTypeHandler(new Object());
        });
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType simpleType1 = simpleType.withValueHandler(handler);
        assertEquals(handler, simpleType1.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentValueHandler(new Object());
        });
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType1 = simpleType.withStaticTyping();
        assertTrue(simpleType1.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder("Ljava/lang/Object;");
        simpleType.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder("Ljava/lang/Object;;");
        simpleType.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testToString() {
        String expected = "[simple type, class java.lang.Object]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        assertTrue(simpleType.equals(simpleType1));
    }
}