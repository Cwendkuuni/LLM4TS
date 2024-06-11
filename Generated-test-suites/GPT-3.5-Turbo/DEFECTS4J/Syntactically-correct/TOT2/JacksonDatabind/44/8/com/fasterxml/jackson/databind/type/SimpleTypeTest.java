import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException for constructing SimpleType with Map class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType._narrow(Object.class);
        
        assertEquals(Object.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException for calling withContentType on SimpleType");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object();
        
        SimpleType typeWithHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, typeWithHandler.getTypeHandler());
    }
    
    @Test
    public void testContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException for calling withContentValueHandler on SimpleType");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Map.class);
        String canonicalName = simpleType.buildCanonicalName();
        
        assertEquals("java.util.Map", canonicalName);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        
        assertNotNull(erasedSignature);
        assertEquals("Ljava/util/List;", erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        
        assertNotNull(genericSignature);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        JavaType refinedType = simpleType.refine(List.class, TypeBindings.emptyBindings(), null, new JavaType[]{});
        
        assertNull(refinedType);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        
        assertEquals(simpleType1, simpleType2);
    }

}