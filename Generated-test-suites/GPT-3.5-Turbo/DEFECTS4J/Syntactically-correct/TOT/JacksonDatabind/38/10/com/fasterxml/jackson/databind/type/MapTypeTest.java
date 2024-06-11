import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTypeTest {

    // Test case for construct method
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                ClassType.construct(Root.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), mapType.getBindings());
        assertEquals(ClassType.construct(Root.class), mapType.getSuperClass());
        assertArrayEquals(new JavaType[]{}, mapType.getInterfaces());
        assertEquals(ClassType.construct(String.class), mapType.getKeyType());
        assertEquals(ClassType.construct(Integer.class), mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    // Test case for withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                null, null);
        
        Object handler = new Object();
        MapType updatedType = originalType.withTypeHandler(handler);
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(originalType.getKeyType(), updatedType.getKeyType());
        assertEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertEquals(handler, updatedType.getTypeHandler());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                null, ClassType.construct(Integer.class));
        
        Object handler = new Object();
        MapType updatedType = originalType.withContentTypeHandler(handler);
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(originalType.getKeyType(), updatedType.getKeyType());
        assertNotEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertNull(originalType.getTypeHandler());
        assertNull(updatedType.getValueHandler());
        assertEquals(handler, updatedType.getValueType().getTypeHandler());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for withValueHandler method
    @Test
    public void testWithValueHandler() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        Object handler = new Object();
        MapType updatedType = originalType.withValueHandler(handler);
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(originalType.getKeyType(), updatedType.getKeyType());
        assertEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertEquals(handler, updatedType.getValueHandler());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        Object handler = new Object();
        MapType updatedType = originalType.withContentValueHandler(handler);
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(originalType.getKeyType(), updatedType.getKeyType());
        assertEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertNull(originalType.getTypeHandler());
        assertNull(updatedType.getValueType().getValueHandler());
        assertEquals(handler, updatedType.getValueType().getTypeHandler());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        MapType updatedType = originalType.withStaticTyping();
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(originalType.getKeyType(), updatedType.getKeyType());
        assertNotEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertNull(originalType.getTypeHandler());
        assertNull(updatedType.getValueHandler());
        assertNull(updatedType.getTypeHandler());
        assertTrue(originalType.isStatic());
        assertTrue(updatedType.isStatic());
    }

    // Test case for withContentType method
    @Test
    public void testWithContentType() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        JavaType contentType = ClassType.construct(Long.class);
        MapType updatedType = originalType.withContentType(contentType);
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(originalType.getKeyType(), updatedType.getKeyType());
        assertNotEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertNull(originalType.getTypeHandler());
        assertNull(updatedType.getValueHandler());
        assertNull(updatedType.getTypeHandler());
        assertFalse(contentType == updatedType.getKeyType());
        assertEquals(contentType, updatedType.getValueType());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for withKeyType method
    @Test
    public void testWithKeyType() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        JavaType keyType = ClassType.construct(Long.class);
        MapType updatedType = originalType.withKeyType(keyType);
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertNotEquals(originalType.getKeyType(), updatedType.getKeyType());
        assertEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertNull(originalType.getTypeHandler());
        assertNull(updatedType.getValueHandler());
        assertNull(updatedType.getTypeHandler());
        assertEquals(keyType, updatedType.getKeyType());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for refine method
    @Test
    public void testRefine() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        MapType updatedType = originalType.refine(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{});
        
        assertEquals(Map.class, updatedType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), updatedType.getBindings());
        assertNull(updatedType.getSuperClass());
        assertArrayEquals(new JavaType[]{}, updatedType.getInterfaces());
        assertEquals(ClassType.construct(String.class), updatedType.getKeyType());
        assertEquals(ClassType.construct(Integer.class), updatedType.getValueType());
        assertNull(updatedType.getValueHandler());
        assertNull(updatedType.getTypeHandler());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for withKeyTypeHandler method
    @Test
    public void testWithKeyTypeHandler() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        Object handler = new Object();
        MapType updatedType = originalType.withKeyTypeHandler(handler);
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertNull(originalType.getKeyType().getValueHandler());
        assertEquals(handler, updatedType.getKeyType().getTypeHandler());
        assertEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertNull(originalType.getTypeHandler());
        assertNull(updatedType.getValueHandler());
        assertNull(updatedType.getTypeHandler());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for withKeyValueHandler method
    @Test
    public void testWithKeyValueHandler() {
        MapType originalType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        Object handler = new Object();
        MapType updatedType = originalType.withKeyValueHandler(handler);
        
        assertEquals(originalType.getRawClass(), updatedType.getRawClass());
        assertEquals(originalType.getBindings(), updatedType.getBindings());
        assertEquals(originalType.getSuperClass(), updatedType.getSuperClass());
        assertArrayEquals(originalType.getInterfaces(), updatedType.getInterfaces());
        assertEquals(originalType.getKeyType(), updatedType.getKeyType());
        assertNull(originalType.getKeyType().getValueHandler());
        assertEquals(handler, updatedType.getKeyType().getValueHandler());
        assertEquals(originalType.getValueType(), updatedType.getValueType());
        assertNull(originalType.getValueHandler());
        assertNull(originalType.getTypeHandler());
        assertNull(updatedType.getValueHandler());
        assertNull(updatedType.getTypeHandler());
        assertFalse(originalType.isStatic());
        assertFalse(updatedType.isStatic());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        
        String expected = "[map type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, mapType.toString());
    }
}