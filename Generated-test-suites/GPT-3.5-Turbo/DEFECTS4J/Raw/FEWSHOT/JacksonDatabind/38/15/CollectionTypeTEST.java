import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstructor1() {
        Class<?> collT = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        assertEquals(collT, collectionType.getCollectionT());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInts());
        assertEquals(elemT, collectionType.getElemT());
        assertEquals(valueHandler, collectionType.getValueHandler());
        assertEquals(typeHandler, collectionType.getTypeHandler());
        assertEquals(asStatic, collectionType.isAsStatic());
    }

    @Test
    public void testConstructor2() {
        TypeBase base = new TypeBase();
        JavaType elemT = new JavaType();
        
        CollectionType collectionType = new CollectionType(base, elemT);
        
        assertEquals(base, collectionType.getBase());
        assertEquals(elemT, collectionType.getElemT());
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        assertEquals(rawType, collectionType.getRawType());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInts());
        assertEquals(elemT, collectionType.getElemT());
    }

    @Test
    @Deprecated
    public void testConstruct_Deprecated() {
        Class<?> rawType = Object.class;
        JavaType elemT = new JavaType();
        
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        
        assertEquals(rawType, collectionType.getRawType());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInts());
        assertEquals(elemT, collectionType.getElemT());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elementType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(subclass, bindings, superClass, superInterfaces, elementType, valueHandler, typeHandler, asStatic);
        
        JavaType narrowedType = collectionType._narrow(CollectionLikeType.class);
        
        assertEquals(CollectionLikeType.class, narrowedType.getRawType());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(superInterfaces, narrowedType.getSuperInts());
        assertEquals(elementType, narrowedType.getElemT());
        assertEquals(valueHandler, narrowedType.getValueHandler());
        assertEquals(typeHandler, narrowedType.getTypeHandler());
        assertEquals(asStatic, narrowedType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> collT = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        JavaType contentType = new JavaType();
        
        CollectionType modifiedType = collectionType.withContentType(contentType);
        
        assertEquals(collT, modifiedType.getCollectionT());
        assertEquals(bindings, modifiedType.getBindings());
        assertEquals(superClass, modifiedType.getSuperClass());
        assertEquals(superInts, modifiedType.getSuperInts());
        assertEquals(contentType, modifiedType.getElemT());
        assertEquals(valueHandler, modifiedType.getValueHandler());
        assertEquals(typeHandler, modifiedType.getTypeHandler());
        assertEquals(asStatic, modifiedType.isAsStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> collT = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object newTypeHandler = new Object();
        
        CollectionType modifiedType = collectionType.withTypeHandler(newTypeHandler);
        
        assertEquals(collT, modifiedType.getCollectionT());
        assertEquals(bindings, modifiedType.getBindings());
        assertEquals(superClass, modifiedType.getSuperClass());
        assertEquals(superInts, modifiedType.getSuperInts());
        assertEquals(elemT, modifiedType.getElemT());
        assertEquals(valueHandler, modifiedType.getValueHandler());
        assertEquals(newTypeHandler, modifiedType.getTypeHandler());
        assertEquals(asStatic, modifiedType.isAsStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> collT = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object newTypeHandler = new Object();
        
        CollectionType modifiedType = collectionType.withContentTypeHandler(newTypeHandler);
        
        assertEquals(collT, modifiedType.getCollectionT());
        assertEquals(bindings, modifiedType.getBindings());
        assertEquals(superClass, modifiedType.getSuperClass());
        assertEquals(superInts, modifiedType.getSuperInts());
        assertEquals(elemT.withTypeHandler(newTypeHandler), modifiedType.getElemT());
        assertEquals(valueHandler, modifiedType.getValueHandler());
        assertEquals(typeHandler, modifiedType.getTypeHandler());
        assertEquals(asStatic, modifiedType.isAsStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> collT = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object newValueHandler = new Object();
        
        CollectionType modifiedType = collectionType.withValueHandler(newValueHandler);
        
        assertEquals(collT, modifiedType.getCollectionT());
        assertEquals(bindings, modifiedType.getBindings());
        assertEquals(superClass, modifiedType.getSuperClass());
        assertEquals(superInts, modifiedType.getSuperInts());
        assertEquals(elemT, modifiedType.getElemT());
        assertEquals(newValueHandler, modifiedType.getValueHandler());
        assertEquals(typeHandler, modifiedType.getTypeHandler());
        assertEquals(asStatic, modifiedType.isAsStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> collT = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object newContentValueHandler = new Object();
        
        CollectionType modifiedType = collectionType.withContentValueHandler(newContentValueHandler);
        
        assertEquals(collT, modifiedType.getCollectionT());
        assertEquals(bindings, modifiedType.getBindings());
        assertEquals(superClass, modifiedType.getSuperClass());
        assertEquals(superInts, modifiedType.getSuperInts());
        assertEquals(elemT.withValueHandler(newContentValueHandler), modifiedType.getElemT());
        assertEquals(valueHandler, modifiedType.getValueHandler());
        assertEquals(typeHandler, modifiedType.getTypeHandler());
        assertEquals(asStatic, modifiedType.isAsStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> collT = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        CollectionType modifiedType = collectionType.withStaticTyping();
        
        assertEquals(collT, modifiedType.getCollectionT());
        assertEquals(bindings, modifiedType.getBindings());
        assertEquals(superClass, modifiedType.getSuperClass());
        assertEquals(superInts, modifiedType.getSuperInts());
        assertEquals(elemT.withStaticTyping(), modifiedType.getElemT());
        assertEquals(valueHandler, modifiedType.getValueHandler());
        assertEquals(typeHandler, modifiedType.getTypeHandler());
        assertEquals(true, modifiedType.isAsStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elementType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elementType, valueHandler, typeHandler, asStatic);
        Class<?> newRawType = CollectionLikeType.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType newSuperClass = new JavaType();
        JavaType[] newSuperInterfaces = new JavaType[0];
        
        JavaType refinedType = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInterfaces);
        
        assertEquals(newRawType, refinedType.getRawType());
        assertEquals(newBindings, refinedType.getBindings());
        assertEquals(newSuperClass, refinedType.getSuperClass());
        assertEquals(newSuperInterfaces, refinedType.getSuperInts());
        assertEquals(elementType, refinedType.getElemT());
        assertEquals(valueHandler, refinedType.getValueHandler());
        assertEquals(typeHandler, refinedType.getTypeHandler());
        assertEquals(asStatic, refinedType.isAsStatic());
    }

    @Test
    public void testToString() {
        Class<?> collT = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        
        assertEquals("[collection type; class " + collT.getName() + ", contains " + elemT + "]", collectionType.toString());
    }
}