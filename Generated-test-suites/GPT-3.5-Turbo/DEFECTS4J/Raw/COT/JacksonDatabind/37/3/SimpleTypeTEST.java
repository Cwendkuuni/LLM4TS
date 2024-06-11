import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        assertNotNull(st);
        assertEquals(String.class, st.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        SimpleType st = SimpleType.construct(Integer.class);
        assertNotNull(st);
        assertEquals(Integer.class, st.getRawClass());
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
        SimpleType.construct(Integer[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType st = SimpleType.construct(String.class);
        SimpleType narrowed = (SimpleType) st.narrow(Object.class);
        assertEquals(Object.class, narrowed.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithSubclass() {
        SimpleType st = SimpleType.construct(Number.class);
        st.narrow(Integer.class); // Integer is a subclass of Number, so this should throw an exception
    }
    
    @Test
    public void testWithContentType() {
        SimpleType st = SimpleType.construct(String.class);
        try {
            st.withContentType(SimpleType.construct(Integer.class));
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType st = SimpleType.construct(String.class);
        Object handler = new Object();
        SimpleType withHandler = (SimpleType) st.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType st = SimpleType.construct(String.class);
        try {
            st.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType st = SimpleType.construct(String.class);
        Object handler = new Object();
        SimpleType withHandler = (SimpleType) st.withValueHandler(handler);
        assertEquals(handler, withHandler.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType st = SimpleType.construct(String.class);
        try {
            st.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType st = SimpleType.construct(String.class);
        SimpleType withStaticTyping = (SimpleType) st.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType st = SimpleType.construct(String.class);
        assertNull(st.refine(Object.class, TypeBindings.emptyBindings(), null, null));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType st = SimpleType.construct(String.class);
        assertFalse(st.isContainerType());
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType st = SimpleType.construct(String.class);
        assertEquals("java.lang.String", st.buildCanonicalName());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType st = SimpleType.construct(String.class);
        StringBuilder sb = st.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType st = SimpleType.construct(String.class);
        StringBuilder sb = st.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;;", sb.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType st1 = SimpleType.construct(String.class);
        SimpleType st2 = SimpleType.construct(String.class);
        assertTrue(st1.equals(st2));
        
        SimpleType st3 = SimpleType.construct(Integer.class);
        assertFalse(st2.equals(st3));
    }
    
    @Test
    public void testToString() {
        SimpleType st = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.lang.String]", st.toString());
    }
}