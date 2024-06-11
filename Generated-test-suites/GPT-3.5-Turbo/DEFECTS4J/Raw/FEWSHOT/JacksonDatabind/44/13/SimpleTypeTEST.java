import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertEquals("java.lang.Object", type.buildCanonicalName());
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals("java.lang.Integer", type.buildCanonicalName());
        
        try {
            SimpleType.construct(Map.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Collection.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Object[].class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }
    }
    
    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(Integer.class);
        SimpleType narrowedType = (SimpleType) type._narrow(Number.class);
        assertEquals("java.lang.Number", narrowedType.buildCanonicalName());
        
        SimpleType sameType = (SimpleType) type._narrow(Integer.class);
        assertEquals("java.lang.Integer", sameType.buildCanonicalName());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(List.class);
        try {
            type.withContentType(SimpleType.construct(Object.class));
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.construct(Object.class);
        SimpleType typeHandlerType = type.withTypeHandler(Object.class);
        assertEquals("java.lang.Object", typeHandlerType.buildCanonicalName());
        assertTrue(typeHandlerType.getTypeHandler() instanceof Object);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.construct(List.class);
        try {
            type.withContentTypeHandler(Object.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(Object.class);
        SimpleType staticType = type.withStaticTyping();
        assertEquals("java.lang.Object", staticType.buildCanonicalName());
        assertTrue(staticType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(Object.class);
        assertNull(type.refine(String.class, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(Object.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(Object.class);
        assertEquals("Ljava/lang/Object;", type.getErasedSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(List.class);
        assertEquals("L" + List.class.getName().replace(".", "/") + "<>;", type.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(Object.class);
        assertEquals("[simple type, class java.lang.Object]", type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(Object.class);
        SimpleType type2 = SimpleType.construct(Object.class);
        assertTrue(type1.equals(type2));
        
        SimpleType type3 = SimpleType.construct(List.class);
        assertFalse(type1.equals(type3));
    }
}