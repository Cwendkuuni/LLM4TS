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
        SimpleType constructedType = SimpleType.constructUnsafe(Object.class);
        assertEquals(constructedType, simpleType);
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType constructedType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            SimpleType constructedType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            SimpleType constructedType = SimpleType.construct(Object[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        SimpleType constructedType = SimpleType.construct(String.class);
        assertEquals(constructedType, simpleType);
    }

    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType modifiedType = simpleType.withTypeHandler(handler);
        assertSame(handler, modifiedType.getTypeHandler());
        assertNotSame(simpleType, modifiedType);
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType modifiedType = simpleType.withValueHandler(handler);
        assertSame(handler, modifiedType.getValueHandler());
        assertNotSame(simpleType, modifiedType);
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertTrue(modifiedType.isStatic());
        assertNotSame(simpleType, modifiedType);
    }

    @Test
    public void testRefine() {
        SimpleType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
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
        assertEquals(simpleType.getRawClass().getName(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals(simpleType.getRawClass().getName() + ";", sb.toString());
    }

    @Test
    public void testToString() {
        String expected = "[simple type, class " + simpleType.getCanonicalName() + "]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(simpleType.equals(simpleType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new Object()));

        SimpleType equalType = new SimpleType(Object.class);
        assertTrue(simpleType.equals(equalType));

        SimpleType differentType = new SimpleType(String.class);
        assertFalse(simpleType.equals(differentType));

        JavaType subclass = new SimpleType(Object.class);
        assertFalse(simpleType.equals(subclass));

        SimpleType modifiedType = simpleType.withTypeHandler(new Object());
        assertFalse(simpleType.equals(modifiedType));

        TypeBindings bindings = TypeBindings.emptyBindings();
        modifiedType = new SimpleType(Object.class, bindings, subclass, null, null, null, false);
        assertFalse(simpleType.equals(modifiedType));

        modifiedType = new SimpleType(Object.class, bindings, null, null, null, null, false);
        assertFalse(simpleType.equals(modifiedType));
    }
}