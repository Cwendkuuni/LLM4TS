import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Should have thrown IllegalArgumentException for constructing SimpleType with Map class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            SimpleType.construct(Collection.class);
            fail("Should have thrown IllegalArgumentException for constructing SimpleType with Collection class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            SimpleType.construct(String[].class);
            fail("Should have thrown IllegalArgumentException for constructing SimpleType with array class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Integer.class);
        JavaType narrowedType = simpleType._narrow(Integer.class);
        assertNotNull(narrowedType);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Should have thrown IllegalArgumentException for calling withContentType() on SimpleType");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Boolean.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Should have thrown IllegalArgumentException for calling withContentTypeHandler() on SimpleType");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Double.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Should have thrown IllegalArgumentException for calling withContentValueHandler() on SimpleType");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Long.class);
        JavaType refinedType = simpleType.refine(Long.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
}