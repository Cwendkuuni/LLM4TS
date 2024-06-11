import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    //Test method for SimpleType.construct
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }
    
    //Test method for SimpleType.construct
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }
    
    //Test method for SimpleType.construct
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Object[].class);
    }
    
    //Test method for SimpleType.withContentType
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(String.class);
    }
    
    //Test method for SimpleType.withTypeHandler
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType newType = simpleType.withTypeHandler(handler);
        assertSame("Type handler not set correctly", handler, newType.getTypeHandler());
    }
    
    //Test method for SimpleType.withContentTypeHandler
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler(new Object());
    }
    
    //Test method for SimpleType.withValueHandler
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType newType = simpleType.withValueHandler(handler);
        assertSame("Value handler not set correctly", handler, newType.getValueHandler());
    }
    
    //Test method for SimpleType.withContentValueHandler
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(new Object());
    }
    
    //Test method for SimpleType.withStaticTyping
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse("Static typing should be disabled by default", simpleType.isStaticTyping());
        SimpleType newType = simpleType.withStaticTyping();
        assertTrue("Static typing not enabled", newType.isStaticTyping());
    }
    
    //Test method for SimpleType.refine
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull("Refined type should be null for SimpleType", refinedType);
    }
    
    //Test method for SimpleType.getGenericSignature
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Generic signature not built correctly", "Ljava/lang/String;", sb.toString());
    }
    
    //Test method for SimpleType.toString
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String expected = "[simple type, class java.lang.String]";
        assertEquals(expected, simpleType.toString());
    }
    
    //Test method for SimpleType.equals
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue("SimpleType not equal to itself", simpleType1.equals(simpleType1));
        assertTrue("Different SimpleTypes with same class not equal", simpleType1.equals(simpleType2));
        assertFalse("SimpleType equal to null", simpleType1.equals(null));
    }
    
}