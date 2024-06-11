import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

public class CollectionLikeTypeTest {
    
    @Test
    public void testConstructor() {
        Class<?> collT = java.util.ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.forType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionLikeType type = new CollectionLikeType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        assertEquals(collT, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertEquals(elemT, type.getElementType());
        assertFalse(type.hasValueHandler());
        assertFalse(type.hasTypeHandler());
        assertFalse(type.isStatic());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.forType(java.util.List.class);
        JavaType elementType = JavaType.forType(String.class);
        
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        assertTrue(type.isCollectionLikeType());
        assertEquals(baseType, type.getSuperClass());
        assertEquals(elementType, type.getElementType());
    }
    
    @Test
    public void testNarrow() {
        Class<?> clazz = java.util.HashMap.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] interfaces = null;
        JavaType elementType = JavaType.forType(Integer.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionLikeType base = new CollectionLikeType(clazz, bindings, superClass, interfaces, elementType, valueHandler, typeHandler, asStatic);
        JavaType subclass = JavaType.forType(java.util.HashMap.class);
        
        CollectionLikeType narrowedType = (CollectionLikeType) base._narrow(subclass.getRawClass());
        
        assertEquals(subclass.getRawClass(), narrowedType.getRawClass());
        assertEquals(clazz, narrowedType.getSuperClass());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(interfaces, narrowedType.getInterfaces());
        assertEquals(elementType, narrowedType.getElementType());
        assertEquals(valueHandler, narrowedType.getValueHandler());
        assertEquals(typeHandler, narrowedType.getTypeHandler());
        assertEquals(asStatic, narrowedType.isStatic());
    }
    
    @Test
    public void testWithContentType() {
        Class<?> clazz = java.util.List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] interfaces = null;
        JavaType elementType = JavaType.forType(Integer.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionLikeType base = new CollectionLikeType(clazz, bindings, superClass, interfaces, elementType, valueHandler, typeHandler, asStatic);
        JavaType contentType = JavaType.forType(String.class);
        
        CollectionLikeType newType = (CollectionLikeType) base.withContentType(contentType);
        
        assertEquals(clazz, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(interfaces, newType.getInterfaces());
        assertEquals(contentType, newType.getElementType());
        assertEquals(valueHandler, newType.getValueHandler());
        assertEquals(typeHandler, newType.getTypeHandler());
        assertEquals(asStatic, newType.isStatic());
    }
    
    @Test
    public void testWithGenericTypeHandler() {
        Class<?> clazz = java.util.Map.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] interfaces = null;
        JavaType elementType = JavaType.forType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionLikeType type = new CollectionLikeType(clazz, bindings, superClass, interfaces, elementType, valueHandler, typeHandler, asStatic);
        Object newTypeHandler = new Object();
        
        CollectionLikeType newType = type.withGenericTypeHandler(newTypeHandler);
        
        assertEquals(clazz, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(interfaces, newType.getInterfaces());
        assertEquals(elementType, newType.getElementType());
        assertEquals(valueHandler, newType.getValueHandler());
        assertEquals(newTypeHandler, newType.getTypeHandler());
        assertEquals(asStatic, newType.isStatic());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> clazz = java.util.Map.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] interfaces = null;
        JavaType elementType = JavaType.forType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionLikeType type = new CollectionLikeType(clazz, bindings, superClass, interfaces, elementType, valueHandler, typeHandler, asStatic);
        Object newContentTypeHandler = new Object();
        
        CollectionLikeType newType = type.withContentTypeHandler(newContentTypeHandler);
        
        assertEquals(clazz, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(interfaces, newType.getInterfaces());
        assertEquals(elementType.withContentTypeHandler(newContentTypeHandler), newType.getElementType());
        assertEquals(valueHandler, newType.getValueHandler());
        assertEquals(typeHandler, newType.getTypeHandler());
        assertEquals(asStatic, newType.isStatic());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> clazz = java.util.Map.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] interfaces = null;
        JavaType elementType = JavaType.forType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionLikeType type = new CollectionLikeType(clazz, bindings, superClass, interfaces, elementType, valueHandler, typeHandler, asStatic);
        Object newContentValueHandler = new Object();
        
        CollectionLikeType newType = type.withContentValueHandler(newContentValueHandler);
        
        assertEquals(clazz, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(interfaces, newType.getInterfaces());
        assertEquals(elementType.withValueHandler(newContentValueHandler), newType.getElementType());
        assertEquals(valueHandler, newType.getValueHandler());
        assertEquals(typeHandler, newType.getTypeHandler());
        assertEquals(asStatic, newType.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> clazz = java.util.Collection.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] interfaces = null;
        JavaType elementType = JavaType.forType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionLikeType type = new CollectionLikeType(clazz, bindings, superClass, interfaces, elementType, valueHandler, typeHandler, asStatic);
        
        CollectionLikeType newType = type.withStaticTyping();
        
        assertEquals(clazz, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(interfaces, newType.getInterfaces());
        assertEquals(elementType.withStaticTyping(), newType.getElementType());
        assertEquals(valueHandler, newType.getValueHandler());
        assertEquals(typeHandler, newType.getTypeHandler());
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        Class<?> clazz = java.util.Collection.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] interfaces = null;
        JavaType elementType = JavaType.forType(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionLikeType type = new CollectionLikeType(clazz, bindings, superClass, interfaces, elementType, valueHandler, typeHandler, asStatic);
        
        Class<?> rawType = java.util.List.class;
        JavaType[] newInterfaces = new JavaType[] { JavaType.forType(Collections.class) };
        JavaType newElementType = JavaType.forType(Integer.class);
        
        CollectionLikeType newType = (CollectionLikeType) type.refine(rawType, bindings, superClass, newInterfaces);
        
        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(newInterfaces, newType.getInterfaces());
        assertEquals(newElementType, newType.getElementType());
        assertEquals(valueHandler, newType.getValueHandler());
        assertEquals(typeHandler, newType.getTypeHandler());
        assertEquals(asStatic, newType.isStatic());
    }
}