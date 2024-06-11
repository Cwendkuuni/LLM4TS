import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {
    
    @Test
    public void testConstruct() {
        MapType mapType1 = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        MapType mapType2 = MapType.construct(LinkedHashMap.class, TypeBindings.emptyBindings(), MapType.class,
                new JavaType[]{}, String.class, Integer.class);
        
        // Typical use cases
        assertEquals(HashMap.class, mapType1.getClass());
        assertEquals(LinkedHashMap.class, mapType2.getClass());
        
        // Edge cases
        assertEquals(0, mapType1.getSuperClass().getInterfaces().size());
        assertEquals(0, mapType2.getSuperClass().getInterfaces().size());
    }
    
    @Test
    public void testNarrow() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        
        // Typical use case
        assertEquals(MapType.class, mapType._narrow(MapType.class).getClass());
        
        // Edge case
        assertNull(mapType._narrow(null));
    }
    
    @Test
    public void testWithTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        Object handler = new Object();
        
        // Typical use case
        assertEquals(mapType, mapType.withTypeHandler(handler));
        
        // Edge case
        assertNull(mapType.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        Object handler = new Object();
        
        // Typical use case
        assertEquals(mapType, mapType.withContentTypeHandler(handler));
        
        // Edge case
        assertNull(mapType.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        Object handler = new Object();
        
        // Typical use case
        assertEquals(mapType, mapType.withValueHandler(handler));
        
        // Edge case
        assertNull(mapType.withValueHandler(null));
    }
    
    @Test
    public void testWithContentValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        Object handler = new Object();
        
        // Typical use case
        assertEquals(mapType, mapType.withContentValueHandler(handler));
        
        // Edge case
        assertNull(mapType.withContentValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType1 = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        MapType mapType2 = MapType.construct(LinkedHashMap.class, TypeBindings.emptyBindings(), MapType.class,
                new JavaType[]{}, String.class, Integer.class);
        
        // Typical use case
        assertEquals(mapType1, mapType1.withStaticTyping());
        assertEquals(mapType2, mapType2.withStaticTyping());
        
        // Edge case
        assertTrue(mapType1.withStaticTyping()._asStatic);
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType1 = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        MapType mapType2 = MapType.construct(LinkedHashMap.class, TypeBindings.emptyBindings(), MapType.class,
                new JavaType[]{}, String.class, Integer.class);
        
        // Typical use case
        assertEquals(mapType1, mapType1.withContentType(Integer.class));
        assertEquals(mapType2, mapType2.withContentType(Integer.class));
        
        // Edge case
        assertEquals(mapType1, mapType1.withContentType(mapType1.getValueType()));
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType1 = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        MapType mapType2 = MapType.construct(LinkedHashMap.class, TypeBindings.emptyBindings(), MapType.class,
                new JavaType[]{}, String.class, Integer.class);
        
        // Typical use case
        assertEquals(mapType1, mapType1.withKeyType(String.class));
        assertEquals(mapType2, mapType2.withKeyType(String.class));
        
        // Edge case
        assertEquals(mapType2, mapType2.withKeyType(mapType2.getKeyType()));
    }
    
    @Test
    public void testRefine() {
        MapType mapType1 = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        MapType mapType2 = MapType.construct(LinkedHashMap.class, TypeBindings.emptyBindings(), MapType.class,
                new JavaType[]{}, String.class, Integer.class);
        
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        
        // Typical use case
        assertEquals(mapType1, mapType1.refine(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{keyType, valueType}));
        assertEquals(mapType2, mapType2.refine(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{keyType, valueType}));
        
        // Edge cases
        assertEquals(mapType1, mapType1.refine(mapType1.getRawClass(), mapType1.getBindings(), mapType1.getSuperClass(),
                mapType1.getInterfaces()));
        assertEquals(mapType2, mapType2.refine(mapType2.getRawClass(), mapType2.getBindings(), mapType2.getSuperClass(),
                mapType2.getInterfaces()));
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        Object handler = new Object();
        
        // Typical use case
        assertEquals(mapType, mapType.withKeyTypeHandler(handler));
        
        // Edge case
        assertNull(mapType.withKeyTypeHandler(null));
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        Object handler = new Object();
        
        // Typical use case
        assertEquals(mapType, mapType.withKeyValueHandler(handler));
        
        // Edge case
        assertNull(mapType.withKeyValueHandler(null));
    }
    
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null,
                new JavaType[]{}, String.class, Integer.class);
        
        assertEquals("[map type; class java.util.HashMap, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}