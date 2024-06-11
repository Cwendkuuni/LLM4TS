import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    @Test
    public void testConstructor() {
        SimpleType type = new SimpleType(SomeClass.class);
        assertEquals(SomeClass.class, type.getRawClass());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(SomeClass.class);
        assertEquals(SomeClass.class, type.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType type = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException for constructing SimpleType for a Map");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType type = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException for constructing SimpleType for a Collection");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType type = SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException for constructing SimpleType for an array");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
        
        SimpleType type = SimpleType.construct(SomeClass.class);
        assertEquals(SomeClass.class, type.getRawClass());
    }
    
    @Test
    public void testNarrow() {
        SimpleType type = new SimpleType(SomeClass.class);
        SimpleType narrowType = (SimpleType) type._narrow(SomeSubclass.class);
        assertEquals(SomeSubclass.class, narrowType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        try {
            type.withContentType(JavaType.STRING);
            fail("Expected IllegalArgumentException for calling withContentType on a SimpleType");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType type = new SimpleType(SomeClass.class);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        try {
            type.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException for calling withContentTypeHandler on a SimpleType");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = new SimpleType(SomeClass.class);
        Object handler = new Object();
        SimpleType newType = type.withValueHandler(handler);
        assertEquals(handler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        try {
            type.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException for calling withContentValueHandler on a SimpleType");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        JavaType refinedType = type.refine(SomeSubclass.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder returnedSb = type.getErasedSignature(sb);
        
        assertEquals(sb, returnedSb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/SomeClass;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder returnedSb = type.getGenericSignature(sb);
        
        assertEquals(sb, returnedSb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/SomeClass;", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = new SimpleType(SomeClass.class);
        
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.SomeClass]", type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(SomeClass.class);
        SimpleType type2 = new SimpleType(SomeClass.class);
        
        assertEquals(type1, type2);
        
        SimpleType type3 = new SimpleType(SomeSubclass.class);
        
        assertNotEquals(type1, type3);
    }
}