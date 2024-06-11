import com.fasterxml.jackson.databind.type.*;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(Integer.class);
        
        assertEquals(Integer.class, simpleType.getRawClass());
        
        assertFalse(simpleType.isContainerType());
        
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }
    
    @Test
    public void testConstructor2() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[] {new SimpleType(Map.class)};
        
        SimpleType simpleType = new SimpleType(Long.class, bindings, superClass, superInterfaces);
        
        assertEquals(Long.class, simpleType.getRawClass());
        
        assertFalse(simpleType.isContainerType());
        
        assertEquals(simpleType.getTypeName(), "class java.lang.Long");
        
        assertEquals("[simple type, class java.lang.Long]", simpleType.toString());
    }
    
    @Test
    public void testConstructor3() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[] {new SimpleType(Map.class)};
        
        SimpleType simpleType = new SimpleType(Long.class, bindings, superClass, superInterfaces, 0, null, null, false);
        
        assertEquals(Long.class, simpleType.getRawClass());
        
        assertFalse(simpleType.isContainerType());
        
        assertEquals(simpleType.getTypeName(), "class java.lang.Long");
        
        assertEquals("[simple type, class java.lang.Long]", simpleType.toString());
    }
    
    @Test
    public void testConstructor4() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[] {new SimpleType(Map.class)};
        
        SimpleType simpleType = new SimpleType(Long.class, bindings, superClass, superInterfaces, 0, null, null, false);
        
        assertEquals(Long.class, simpleType.getRawClass());
        
        assertFalse(simpleType.isContainerType());
        
        assertEquals(simpleType.getTypeName(), "class java.lang.Long");
        
        assertEquals("[simple type, class java.lang.Long]", simpleType.toString());
    }
    
    @Test
    public void testConstructor5() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[] {new SimpleType(Map.class)};
        
        SimpleType simpleType = new SimpleType(Long.class, bindings, superClass, superInterfaces, 0, null, null, false);
        
        assertEquals(Long.class, simpleType.getRawClass());
        
        assertFalse(simpleType.isContainerType());
        
        assertEquals(simpleType.getTypeName(), "class java.lang.Long");
        
        assertEquals("[simple type, class java.lang.Long]", simpleType.toString());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        
        assertEquals(Integer.class, simpleType.getRawClass());
        
        assertFalse(simpleType.isContainerType());
        
        assertEquals("class java.lang.Integer", simpleType.getTypeName());
        
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithTypeArray() {
        SimpleType.construct(Object[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Long.class);
        
        JavaType narrowedType = simpleType._narrow(Object.class);
        
        assertEquals(Object.class, narrowedType.getRawClass());
        
        assertFalse(narrowedType.isContainerType());
        
        assertEquals("[simple type, class java.lang.Object]", narrowedType.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidClass() {
        SimpleType simpleType = new SimpleType(Long.class);
        
        simpleType._narrow(String.class);
    }
    
    // Add more test cases for each method

}