import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;

import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> collT = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        CollectionLikeType collectionLikeType = new CollectionLikeType(collT, bindings, superClass, superInts, elemT,
                valueHandler, typeHandler, asStatic);

        assertEquals(collT, collectionLikeType.getRawClass());
        assertEquals(bindings, collectionLikeType.getBindings());
        assertEquals(superClass, collectionLikeType.getSuperClass());
        assertEquals(superInts, collectionLikeType.getInterfaces());
        assertEquals(elemT, collectionLikeType.getElementType());
        assertEquals(valueHandler, collectionLikeType.getValueHandler());
        assertEquals(typeHandler, collectionLikeType.getTypeHandler());
        assertTrue(collectionLikeType.isStatic());
    }

    @Test
    public void testConstructWithTypeBase() {
        JavaType base = new JavaType();
        JavaType elemT = new JavaType();
        CollectionLikeType collectionLikeType = new CollectionLikeType(base, elemT);

        assertEquals(base.getRawClass(), collectionLikeType.getRawClass());
        assertEquals(base.getBindings(), collectionLikeType.getBindings());
        assertEquals(base.getSuperClass(), collectionLikeType.getSuperClass());
        assertEquals(base.getInterfaces(), collectionLikeType.getInterfaces());
        assertEquals(elemT, collectionLikeType.getElementType());
        assertEquals(base.getValueHandler(), collectionLikeType.getValueHandler());
        assertEquals(base.getTypeHandler(), collectionLikeType.getTypeHandler());
        assertEquals(base.isStatic(), collectionLikeType.isStatic());
    }


    @Test
    public void testConstructWithDeprecatedMethod() {
        Class<?> rawType = Class.class;
        JavaType elemT = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);

        assertEquals(rawType, collectionLikeType.getRawClass());
        assertTrue(collectionLikeType.getBindings().isEmpty());
        assertEquals(rawType.getClass(), collectionLikeType.getSuperClass().getClass());
        assertEquals(null, collectionLikeType.getInterfaces());
        assertEquals(elemT, collectionLikeType.getElementType());
        assertEquals(null, collectionLikeType.getValueHandler());
        assertEquals(null, collectionLikeType.getTypeHandler());
        assertFalse(collectionLikeType.isStatic());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertEquals(baseType.getRawClass(), collectionLikeType.getRawClass());
        assertEquals(baseType.getBindings(), collectionLikeType.getBindings());
        assertEquals(baseType.getSuperClass(), collectionLikeType.getSuperClass());
        assertEquals(baseType.getInterfaces(), collectionLikeType.getInterfaces());
        assertEquals(elementType, collectionLikeType.getElementType());
        assertEquals(baseType.getValueHandler(), collectionLikeType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), collectionLikeType.getTypeHandler());
        assertEquals(baseType.isStatic(), collectionLikeType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elementType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        CollectionLikeType collectionLikeType = new CollectionLikeType(subclass, bindings, superClass, superInterfaces,
                elementType, valueHandler, typeHandler, asStatic);

        assertEquals(subclass, collectionLikeType.getRawClass());
        assertEquals(bindings, collectionLikeType.getBindings());
        assertEquals(superClass, collectionLikeType.getSuperClass());
        assertEquals(superInterfaces, collectionLikeType.getInterfaces());
        assertEquals(elementType, collectionLikeType.getElementType());
        assertEquals(valueHandler, collectionLikeType.getValueHandler());
        assertEquals(typeHandler, collectionLikeType.getTypeHandler());
        assertTrue(collectionLikeType.isStatic());
    }
    
    
    @Test
    public void testWithContentType() {
        Class<?> collT = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        CollectionLikeType collectionLikeType = new CollectionLikeType(collT, bindings, superClass, superInts, elemT,
                valueHandler, typeHandler, asStatic);

        JavaType contentType = new JavaType();

        CollectionLikeType updatedCollectionLikeType = (CollectionLikeType) collectionLikeType.withContentType(contentType);

        assertEquals(collT, updatedCollectionLikeType.getRawClass());
        assertEquals(bindings, updatedCollectionLikeType.getBindings());
        assertEquals(superClass, updatedCollectionLikeType.getSuperClass());
        assertEquals(superInts, updatedCollectionLikeType.getInterfaces());
        assertEquals(contentType, updatedCollectionLikeType.getElementType());
        assertEquals(valueHandler, updatedCollectionLikeType.getValueHandler());
        assertEquals(typeHandler, updatedCollectionLikeType.getTypeHandler());
        assertTrue(updatedCollectionLikeType.isStatic());
    }


    @Test
    public void testWithStaticTyping() {
        Class<?> collT = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        CollectionLikeType collectionLikeType = new CollectionLikeType(collT, bindings, superClass, superInts, elemT,
                valueHandler, typeHandler, asStatic);

        CollectionLikeType updatedCollectionLikeType = (CollectionLikeType) collectionLikeType.withStaticTyping();

        assertEquals(collT, updatedCollectionLikeType.getRawClass());
        assertEquals(bindings, updatedCollectionLikeType.getBindings());
        assertEquals(superClass, updatedCollectionLikeType.getSuperClass());
        assertEquals(superInts, updatedCollectionLikeType.getInterfaces());
        assertEquals(updatedCollectionLikeType.getElementType().withStaticTyping(), updatedCollectionLikeType.getElementType());
        assertEquals(valueHandler, updatedCollectionLikeType.getValueHandler());
        assertEquals(typeHandler, updatedCollectionLikeType.getTypeHandler());
        assertTrue(updatedCollectionLikeType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elementType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInterfaces,
                elementType, valueHandler, typeHandler, asStatic);

        Class<?> subclass = Class.class;
        TypeBindings updatedBindings = new TypeBindings();
        JavaType updatedSuperClass = new JavaType();
        JavaType[] updatedSuperInterfaces = new JavaType[0];
        JavaType updatedElementType = new JavaType();

        CollectionLikeType refinedCollectionLikeType = (CollectionLikeType) collectionLikeType.refine(subclass, updatedBindings,
                updatedSuperClass, updatedSuperInterfaces);

        assertEquals(subclass, refinedCollectionLikeType.getRawClass());
        assertEquals(updatedBindings, refinedCollectionLikeType.getBindings());
        assertEquals(updatedSuperClass, refinedCollectionLikeType.getSuperClass());
        assertEquals(updatedSuperInterfaces, refinedCollectionLikeType.getInterfaces());
        assertEquals(updatedElementType, refinedCollectionLikeType.getElementType());
        assertEquals(valueHandler, refinedCollectionLikeType.getValueHandler());
        assertEquals(typeHandler, refinedCollectionLikeType.getTypeHandler());
        assertTrue(refinedCollectionLikeType.isStatic());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType baseType = new JavaType();
        JavaType elementType1 = new JavaType();
        JavaType elementType2 = new JavaType();

        CollectionLikeType collectionLikeType1 = CollectionLikeType.upgradeFrom(baseType, elementType1);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.upgradeFrom(baseType, elementType2);

        assertTrue(collectionLikeType1.equals(collectionLikeType1));
        assertFalse(collectionLikeType1.equals(null));
        assertFalse(collectionLikeType1.equals(new Object()));
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testToString() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertEquals("[collection-like type; class java.lang.Class, contains " + elementType + "]",
                collectionLikeType.toString());
    }
}