import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructor2() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInts = { new SimpleType(Comparable.class) };
        SimpleType simpleType = new SimpleType(String.class, null, superClass, superInts);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInts, simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructor3() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInts = { new SimpleType(Comparable.class) };
        SimpleType[] interfaces = { new SimpleType(CharSequence.class) };
        SimpleType simpleType = new SimpleType(String.class, null, superClass, superInts, null, null, true);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInts, simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertTrue(simpleType.isStatic());
    }

    @Test
    public void testConstructor4() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInts = { new SimpleType(Comparable.class) };
        SimpleType[] interfaces = { new SimpleType(CharSequence.class) };
        SimpleType simpleType = new SimpleType(String.class, null, superClass, superInts, null, null, interfaces, 0, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInts, simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType base = new SimpleType(Object.class);
        SimpleType simpleType = new SimpleType(base);
        assertEquals(base.getRawClass(), simpleType.getRawClass());
        assertEquals(base.getBindings(), simpleType.getBindings());
        assertEquals(base.getSuperClass(), simpleType.getSuperClass());
        assertArrayEquals(base.getInterfaces(), simpleType.getInterfaces());
        assertEquals(base.getValueHandler(), simpleType.getValueHandler());
        assertEquals(base.getTypeHandler(), simpleType.getTypeHandler());
        assertEquals(base.isStatic(), simpleType.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(Array.class);
    }

    @Test
    public void testNarrow() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType base = new SimpleType(Comparable.class, null, superClass, new SimpleType[] {});
        SimpleType subClass = new SimpleType(String.class, null, base, new SimpleType[] {});
        SimpleType narrowed = subClass.narrow(String.class);
        assertEquals(String.class, narrowed.getRawClass());
        assertEquals(base, narrowed.getSuperClass());
        assertNull(narrowed.getBindings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidType() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType base = new SimpleType(Comparable.class, null, superClass, new SimpleType[] {});
        SimpleType subClass = new SimpleType(String.class, null, base, new SimpleType[] {});
        subClass.narrow(Object.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(new SimpleType(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
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

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        SimpleType withStatic = simpleType.withStaticTyping();
        assertTrue(withStatic.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInts = { new SimpleType(Comparable.class) };
        SimpleType simpleType = new SimpleType(String.class, null, superClass, superInts);
        assertNull(simpleType.refine(String.class, null, null, null));
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