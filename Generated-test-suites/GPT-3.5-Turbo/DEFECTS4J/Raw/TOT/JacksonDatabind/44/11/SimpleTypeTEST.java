import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        assertNotNull(simpleType);
        assertEquals(TestClass.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(TestClass.class);
        assertNotNull(simpleType);
        assertEquals(TestClass.class, simpleType.getRawClass());
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
        SimpleType.construct(Object[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        SimpleType narrowedType = (SimpleType) simpleType.narrow(ChildClass.class);
        assertNotNull(narrowedType);
        assertEquals(ChildClass.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(JavaType.INT));
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        Object handler = new TestHandler();
        SimpleType updatedType = (SimpleType) simpleType.withTypeHandler(handler);
        assertNotNull(updatedType);
        assertEquals(handler, updatedType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(new TestHandler()));
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        Object handler = new TestHandler();
        SimpleType updatedType = (SimpleType) simpleType.withValueHandler(handler);
        assertNotNull(updatedType);
        assertEquals(handler, updatedType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentValueHandler(new TestHandler()));
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        SimpleType updatedType = simpleType.withStaticTyping();
        assertNotNull(updatedType);
        assertTrue(updatedType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        JavaType refinedType = simpleType.refine(ChildClass.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = simpleType.getErasedSignature(sb);
        assertNotNull(signature);
        assertTrue(signature.toString().contains("TestClass"));
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = simpleType.getGenericSignature(sb);
        assertNotNull(signature);
        assertTrue(signature.toString().contains("TestClass"));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        assertNotNull(simpleType.toString());
        assertTrue(simpleType.toString().contains("TestClass"));
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(TestClass.class);
        SimpleType simpleType2 = new SimpleType(TestClass.class);
        SimpleType simpleType3 = new SimpleType(ChildClass.class);
        
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
    
    private static class TestClass {
        
    }
    
    private static class ChildClass extends TestClass {
        
    }
    
    private static class TestHandler {
        
    }
}