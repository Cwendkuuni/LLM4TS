import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    private SimpleType simpleType;
    
    @Before
    public void setUp() {
        simpleType = new SimpleType(SimpleType.class);
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType constructedType = SimpleType.constructUnsafe(SimpleType.class);
        assertEquals(SimpleType.class, constructedType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Collection.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Object[].class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }
        
        SimpleType constructedType = SimpleType.construct(String.class);
        assertEquals(String.class, constructedType.getRawClass());
    }
    
    @Test
    public void testNarrow() {
        SimpleType narrowedType = (SimpleType) simpleType.narrow(SimpleType.class);
        assertEquals(SimpleType.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(SimpleType.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType newType = simpleType.withTypeHandler(typeHandler);
        assertSame(typeHandler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType newType = simpleType.withValueHandler(valueHandler);
        assertSame(valueHandler, newType.getValueHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        simpleType.withContentValueHandler(new Object());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType newType = simpleType.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        JavaType refinedType = simpleType.refine(SimpleType.class, null, null, null);
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
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.SimpleType]", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.SimpleType]", sb.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.SimpleType]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType sameType = new SimpleType(SimpleType.class);
        assertTrue(simpleType.equals(sameType));
        
        SimpleType differentType = new SimpleType(String.class);
        assertFalse(simpleType.equals(differentType));
    }
}