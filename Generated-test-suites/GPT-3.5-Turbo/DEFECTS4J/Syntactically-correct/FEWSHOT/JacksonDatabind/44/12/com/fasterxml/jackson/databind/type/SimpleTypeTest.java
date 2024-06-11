import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Collection.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Map.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(int[].class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }
        
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }
    
    @Test
    public void testWith() {
        simpleType.withContentType(null);
        try {
            simpleType.withContentType(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
        
        simpleType.withTypeHandler(object);
        simpleType.withContentTypeHandler(object);
        simpleType.withValueHandler(object);
        
        try {
            simpleType.withContentValueHandler(object);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
        
        simpleType.withStaticTyping();
    }
    
    @Test
    public void testRefine() {
        SimpleType refinedType = simpleType.refine(Object.class, null, null, null);
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
        assertEquals("Ljava/lang/Object;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType anotherType = SimpleType.constructUnsafe(Object.class);
        assertTrue(simpleType.equals(anotherType));
    }
    
}