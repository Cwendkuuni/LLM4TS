import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, st.getRawClass());
        assertEquals(null, st.getBindings());
        assertEquals(null, st.getSuperClass());
        assertEquals(0, st.getInterfaces().size());
        assertEquals(null, st.getValueHandler());
        assertEquals(null, st.getTypeHandler());
        assertFalse(st.isContainerType());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }
        
        SimpleType st = SimpleType.construct(String.class);
        assertEquals(String.class, st.getRawClass());
        assertEquals(null, st.getBindings());
        assertEquals(null, st.getSuperClass());
        assertEquals(0, st.getInterfaces().size());
        assertEquals(null, st.getValueHandler());
        assertEquals(null, st.getTypeHandler());
        assertFalse(st.isContainerType());
    }

    @Test
    public void testNarrow() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        SimpleType narrowed = (SimpleType) st._narrow(Object.class);
        assertEquals(Object.class, narrowed.getRawClass());
        assertEquals(null, narrowed.getBindings());
        assertEquals(st, narrowed.getSuperClass());
        assertEquals(0, narrowed.getInterfaces().size());
        assertEquals(st.getValueHandler(), narrowed.getValueHandler());
        assertEquals(st.getTypeHandler(), narrowed.getTypeHandler());
        assertFalse(narrowed.isContainerType());
    }

    @Test
    public void testWithContentType() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        try {
            st.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        Object typeHandler = new Object();
        SimpleType withTypeHandler = st.withTypeHandler(typeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertEquals(st.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(st.getBindings(), withTypeHandler.getBindings());
        assertEquals(st.getSuperClass(), withTypeHandler.getSuperClass());
        assertEquals(st.getInterfaces(), withTypeHandler.getInterfaces());
        assertEquals(st.getValueHandler(), withTypeHandler.getValueHandler());
        assertEquals(st.isContainerType(), withTypeHandler.isContainerType());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        try {
            st.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        Object valueHandler = new Object();
        SimpleType withValueHandler = st.withValueHandler(valueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertEquals(st.getRawClass(), withValueHandler.getRawClass());
        assertEquals(st.getBindings(), withValueHandler.getBindings());
        assertEquals(st.getSuperClass(), withValueHandler.getSuperClass());
        assertEquals(st.getInterfaces(), withValueHandler.getInterfaces());
        assertEquals(st.getTypeHandler(), withValueHandler.getTypeHandler());
        assertEquals(st.isContainerType(), withValueHandler.isContainerType());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        try {
            st.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        SimpleType withStaticTyping = st.withStaticTyping();
        assertEquals(st.isStatic(), withStaticTyping.isStatic());
        assertEquals(st.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(st.getBindings(), withStaticTyping.getBindings());
        assertEquals(st.getSuperClass(), withStaticTyping.getSuperClass());
        assertEquals(st.getInterfaces(), withStaticTyping.getInterfaces());
        assertEquals(st.getValueHandler(), withStaticTyping.getValueHandler());
        assertEquals(st.getTypeHandler(), withStaticTyping.getTypeHandler());
        assertEquals(st.isContainerType(), withStaticTyping.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        String canonicalName = st.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        assertFalse(st.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuffer erasedSignature = st.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuffer genericSignature = st.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", genericSignature.toString());
    }

    @Test
    public void testToString() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", st.toString());
    }

    @Test
    public void testEquals() {
        SimpleType st1 = SimpleType.constructUnsafe(String.class);
        SimpleType st2 = SimpleType.constructUnsafe(String.class);
        assertTrue(st1.equals(st2));
    }
}