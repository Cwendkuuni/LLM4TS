
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertSame(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getTypeHandler());
        assertNull(simpleType.getValueHandler());
        assertTrue(simpleType.isStaticTyping());
        assertTrue(simpleType.hasGenericTypes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithMap() {
        new SimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithCollection() {
        new SimpleType(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithArray() {
        new SimpleType(Object[].class);
    }

    @Test
    public void testConstructUnsafeSimpleType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertSame(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());

    }

    @Test
    public void testNarrowSimpleType() {
        SimpleType simpleType = new SimpleType(Number.class);
        SimpleType narrowedType = (SimpleType) simpleType.narrow(Integer.class);
        assertSame(Integer.class, narrowedType.getRawClass());
        assertFalse(narrowedType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSimpleType() {
        SimpleType simpleType = new SimpleType(Number.class);
        simpleType.narrow(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withContentType(Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefineSimpleType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertNull(simpleType.refine(null, null, null, null));
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Integer.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/Integer;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/util/List<*>;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);
        assertEquals(simpleType1, simpleType2);
    }
}
