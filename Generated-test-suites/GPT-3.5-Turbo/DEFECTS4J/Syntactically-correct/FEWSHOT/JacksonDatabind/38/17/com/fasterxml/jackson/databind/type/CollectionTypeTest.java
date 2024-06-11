
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {
    
    @Test
    public void testConstruct() {
        JavaType elemType = new JavaType(Object.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        
        CollectionType collectionType = CollectionType.construct(Object.class, bindings, superClass, superInts, elemType);
        
        assertNotNull(collectionType);
        assertEquals(Object.class, collectionType.getRawType());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemType, collectionType.getElementType());
    }
    
    @Test
    public void testConstruct_Deprecated() {
        JavaType elemType = new JavaType(Object.class);
        
        CollectionType collectionType = CollectionType.construct(Object.class, elemType);
        
        assertNotNull(collectionType);
        assertEquals(Object.class, collectionType.getRawType());
        assertNull(collectionType.getBindings());
        assertNotNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(elemType, collectionType.getElementType());
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        JavaType elemType = new JavaType(Object.class);
        
        CollectionType collectionType = new CollectionType(subclass, bindings, superClass, superInts, elemType, null, null, false);
        
        JavaType narrowedType = collectionType._narrow(subclass);
        
        assertNotNull(narrowedType);
        assertEquals(subclass, narrowedType.getRawType());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(superInts, narrowedType.getSuperInterfaces());
        assertEquals(elemType, narrowedType.getElementType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(String.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        JavaType elemType = new JavaType(Object.class);
        
        CollectionType collectionType = new CollectionType(Object.class, bindings, superClass, superInts, elemType, null, null, false);
        
        JavaType withContentTypeType = collectionType.withContentType(contentType);
        
        assertNotNull(withContentTypeType);
        assertEquals(Object.class, withContentTypeType.getRawType());
        assertEquals(bindings, withContentTypeType.getBindings());
        assertEquals(superClass, withContentTypeType.getSuperClass());
        assertEquals(superInts, withContentTypeType.getSuperInterfaces());
        assertEquals(contentType, withContentTypeType.getElementType());
    }
    
    @Test
    public void testWithTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        JavaType elemType = new JavaType(Object.class);
        Object typeHandler = new Object();
        
        CollectionType collectionType = new CollectionType(Object.class, bindings, superClass, superInts, elemType, null, null, false);
        
        CollectionType withTypeHandlerType = collectionType.withTypeHandler(typeHandler);
        
        assertNotNull(withTypeHandlerType);
        assertEquals(Object.class, withTypeHandlerType.getRawType());
        assertEquals(bindings, withTypeHandlerType.getBindings());
        assertEquals(superClass, withTypeHandlerType.getSuperClass());
        assertEquals(superInts, withTypeHandlerType.getSuperInterfaces());
        assertEquals(elemType, withTypeHandlerType.getElementType());
        assertEquals(typeHandler, withTypeHandlerType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        JavaType elemType = new JavaType(Object.class);
        Object typeHandler = new Object();
        
        CollectionType collectionType = new CollectionType(Object.class, bindings, superClass, superInts, elemType, null, null, false);
        
        CollectionType withContentTypeHandlerType = collectionType.withContentTypeHandler(typeHandler);
        
        assertNotNull(withContentTypeHandlerType);
        assertEquals(Object.class, withContentTypeHandlerType.getRawType());
        assertEquals(bindings, withContentTypeHandlerType.getBindings());
        assertEquals(superClass, withContentTypeHandlerType.getSuperClass());
        assertEquals(superInts, withContentTypeHandlerType.getSuperInterfaces());
        assertEquals(typeHandler, withContentTypeHandlerType.getContentType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        JavaType elemType = new JavaType(Object.class);
        Object valueHandler = new Object();
        
        CollectionType collectionType = new CollectionType(Object.class, bindings, superClass, superInts, elemType, null, null, false);
        
        CollectionType withValueHandlerType = collectionType.withValueHandler(valueHandler);
        
        assertNotNull(withValueHandlerType);
        assertEquals(Object.class, withValueHandlerType.getRawType());
        assertEquals(bindings, withValueHandlerType.getBindings());
        assertEquals(superClass, withValueHandlerType.getSuperClass());
        assertEquals(superInts, withValueHandlerType.getSuperInterfaces());
        assertEquals(elemType, withValueHandlerType.getElementType());
        assertEquals(valueHandler, withValueHandlerType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        JavaType elemType = new JavaType(Object.class);
        Object valueHandler = new Object();
        
        CollectionType collectionType = new CollectionType(Object.class, bindings, superClass, superInts, elemType, null, null, false);
        
        CollectionType withContentValueHandlerType = collectionType.withContentValueHandler(valueHandler);
        
        assertNotNull(withContentValueHandlerType);
        assertEquals(Object.class, withContentValueHandlerType.getRawType());
        assertEquals(bindings, withContentValueHandlerType.getBindings());
        assertEquals(superClass, withContentValueHandlerType.getSuperClass());
        assertEquals(superInts, withContentValueHandlerType.getSuperInterfaces());
        assertEquals(valueHandler, withContentValueHandlerType.getElementType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        JavaType elemType = new JavaType(Object.class);
        
        CollectionType collectionType = new CollectionType(Object.class, bindings, superClass, superInts, elemType, null, null, false);
        
        CollectionType withStaticTypingType = collectionType.withStaticTyping();
        
        assertNotNull(withStaticTypingType);
        assertEquals(Object.class, withStaticTypingType.getRawType());
        assertEquals(bindings, withStaticTypingType.getBindings());
        assertEquals(superClass, withStaticTypingType.getSuperClass());
        assertEquals(superInts, withStaticTypingType.getSuperInterfaces());
        assertTrue(withStaticTypingType.getElementType().isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = { new JavaType(Object.class) };
        JavaType elemType = new JavaType(Object.class);
        
        CollectionType collectionType = new CollectionType(Object.class, bindings, superClass, superInts, elemType, null, null, false);
        
        Class<?> rawType = String.class;
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInts);
        
        assertNotNull(refinedType);
        assertEquals(rawType, refinedType.getRawType());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInts, refinedType.getSuperInterfaces());
        assertEquals(elemType, refinedType.getElementType());
    }
}
