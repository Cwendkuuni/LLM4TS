import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType constructedType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType constructedType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType constructedType = SimpleType.construct(Object[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }
        
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = SimpleType.construct(String.class).withTypeHandler(typeHandler);
        assertEquals(typeHandler, simpleType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            SimpleType simpleType = SimpleType.construct(String.class).withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = SimpleType.construct(String.class).withValueHandler(valueHandler);
        assertEquals(valueHandler, simpleType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            SimpleType simpleType = SimpleType.construct(String.class).withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(String.class).withStaticTyping();
        assertTrue(simpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(String.class).refine(String.class, null, null, null);
        assertNull(simpleType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("java.lang.String", simpleType.toCanonical());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("Ljava/lang/String;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("Ljava/lang/String;", simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
    
}