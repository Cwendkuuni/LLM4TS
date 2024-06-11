import static org.junit.Assert.*;
import org.junit.*;

public class SimpleTypeTest {

    // Test constructors
    @Test
    public void testConstructors() {
        SimpleType type1 = new SimpleType(Object.class);
        SimpleType type2 = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);
        SimpleType type3 = new SimpleType(type2);
        SimpleType type4 = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType type5 = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null, null, null, false, false);
        
        assertNotNull(type1);
        assertNotNull(type2);
        assertNotNull(type3);
        assertNotNull(type4);
        assertNotNull(type5);

    }

    // Test constructUnsafe method
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
    }
    
    // Test construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Map.class);
    }

    // Test _narrow method
    @Test
    public void testNarrow() {
        SimpleType type1 = new SimpleType(Object.class);
        SimpleType type2 = type1._narrow(String.class);

        assertNotNull(type2);
        assertEquals(String.class, type2.getRawClass());
    }
    
    // Test withContentType method
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType type = new SimpleType(Object.class);
        type.withContentType(Object.class);
    }
    
    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        SimpleType type1 = new SimpleType(Object.class);
        SimpleType type2 = type1.withTypeHandler(null);

        assertNotNull(type2);
        assertEquals(null, type2.getTypeHandler());
    }

    // Test withContentTypeHandler method
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(Object.class);
        type.withContentTypeHandler(null);
    }
    
    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        SimpleType type1 = new SimpleType(Object.class);
        SimpleType type2 = type1.withValueHandler(null);

        assertNotNull(type2);
        assertEquals(null, type2.getValueHandler());
    }
    
    // Test withContentValueHandler method
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(Object.class);
        type.withContentValueHandler(null);
    }
    
    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        SimpleType type1 = new SimpleType(Object.class);
        SimpleType type2 = type1.withStaticTyping();

        assertNotNull(type2);
        assertTrue(type2.isStatic());
    }
    
    // Test refine method
    @Test
    public void testRefine() {
        SimpleType type1 = new SimpleType(Object.class);
        SimpleType type2 = (SimpleType) type1.refine(Object.class, TypeBindings.emptyBindings(), null, null);

        assertNotNull(type2);
        assertEquals(Object.class, type2.getRawClass());
    }
    
    // TODO Add more test methods to test the remaining methods in SimpleType class
    
}