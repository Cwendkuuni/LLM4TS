import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("Object", simpleType.toString());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[]{new SimpleType(List.class)};
        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts);
        assertEquals("Object", simpleType.toString());
    }

    @Test
    public void testConstructor3() {
        TypeBase base = new SimpleType(Object.class);
        SimpleType simpleType = new SimpleType(base);
        assertEquals("Object", simpleType.toString());
    }

    @Test
    public void testConstructor4() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[]{new SimpleType(List.class)};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertEquals("Object", simpleType.toString());
    }

    @Test
    public void testConstructor5() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[]{new SimpleType(List.class)};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        int extraHash = 0;
        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
        assertEquals("Object", simpleType.toString());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals("Object", simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType, simpleType._narrow(Object.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(new SimpleType(List.class));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object typeHandler = null;
        assertEquals(simpleType, simpleType.withTypeHandler(typeHandler));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentTypeHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object valueHandler = null;
        assertEquals(simpleType, simpleType.withValueHandler(valueHandler));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType, simpleType.withStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(Object.class, new TypeBindings(), new SimpleType(Collection.class), new JavaType[]{new SimpleType(List.class)}));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("java.lang.Object", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}