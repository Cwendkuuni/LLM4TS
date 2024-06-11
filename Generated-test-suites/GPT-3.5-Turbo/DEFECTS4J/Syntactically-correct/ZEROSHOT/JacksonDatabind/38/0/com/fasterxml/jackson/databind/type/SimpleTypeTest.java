import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @BeforeClass
    public void setUp() {
        simpleType = new SimpleType(String.class);
    }

    @Test
    public void testConstructor1() {
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = new JavaType[2];
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        simpleType = new SimpleType(String.class, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);

        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInts, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertTrue(simpleType.isStaticTyping());
    }

    @Test
    public void testConstructUnsafe() {
        simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }

    @Test
    public void testConstruct() {
        try {
            simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            simpleType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            simpleType = SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }

        simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNotNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }

    @Test
    public void testNarrow() {
        JavaType narrowedType = simpleType.narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
        assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        assertEquals(simpleType.getSuperClass(), narrowedType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
        assertEquals(simpleType.getValueHandler(), narrowedType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(simpleType.isStaticTyping(), narrowedType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        simpleType.withContentType(new JavaType(String.class));
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType newType = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType.getRawClass(), newType.getRawClass());
        assertEquals(simpleType.getBindings(), newType.getBindings());
        assertEquals(simpleType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(simpleType.getValueHandler(), newType.getValueHandler());
        assertEquals(typeHandler, newType.getTypeHandler());
        assertEquals(simpleType.isStaticTyping(), newType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        simpleType.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType newType = simpleType.withValueHandler(valueHandler);
        assertEquals(simpleType.getRawClass(), newType.getRawClass());
        assertEquals(simpleType.getBindings(), newType.getBindings());
        assertEquals(simpleType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(valueHandler, newType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(simpleType.isStaticTyping(), newType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType newType = simpleType.withStaticTyping();
        assertEquals(simpleType.getRawClass(), newType.getRawClass());
        assertEquals(simpleType.getBindings(), newType.getBindings());
        assertEquals(simpleType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(simpleType.getValueHandler(), newType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newType.getTypeHandler());
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType refinedType = simpleType.refine(String.class, new TypeBindings(), new JavaType(Object.class), new JavaType[]{new JavaType(Object.class)});
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        String expectedString = new StringBuilder()
                .append("L")
                .append(simpleType.getRawClass().getName().replace('.', '/'))
                .append(";")
                .toString();
        assertEquals(expectedString, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        String expectedString = new StringBuilder("L")
                .append(simpleType.getRawClass().getName().replace('.', '/'))
                .append("<>;")
                .toString();
        assertEquals(expectedString, sb.toString());
    }

    @Test
    public void testToString() {
        String expectedString = new StringBuilder("[simple type, class ")
                .append(simpleType.getRawClass().getCanonicalName())
                .append("]")
                .toString();
        assertEquals(expectedString, simpleType.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(simpleType.equals(simpleType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new Object()));

        SimpleType other = new SimpleType(String.class);
        assertTrue(simpleType.equals(other));

        other = new SimpleType(Integer.class);
        assertFalse(simpleType.equals(other));
    }
}