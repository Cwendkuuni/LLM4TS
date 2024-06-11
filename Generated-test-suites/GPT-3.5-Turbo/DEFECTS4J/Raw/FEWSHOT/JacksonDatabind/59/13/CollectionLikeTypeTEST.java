import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.lang.reflect.TypeVariable;

import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.INT);
        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(JavaType.INT, collectionType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.STRING;
        JavaType elementType = JavaType.OBJECT;
        CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals(baseType, collectionType.getBaseType());
        assertEquals(elementType, collectionType.getElementType());
    }

    @Test
    public void testWithContentType() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        JavaType newContentType = JavaType.INT;
        CollectionLikeType newCollectionType = collectionType.withContentType(newContentType);
        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newCollectionType.getBindings());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(newContentType, newCollectionType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        Object typeHandler = new Object();
        CollectionLikeType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newCollectionType.getBindings());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(JavaType.STRING, newCollectionType.getElementType());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        Object typeHandler = new Object();
        CollectionLikeType newCollectionType = collectionType.withContentTypeHandler(typeHandler);
        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newCollectionType.getBindings());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(JavaType.STRING, newCollectionType.getElementType());
        assertEquals(typeHandler, newCollectionType.getContentTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        CollectionLikeType newCollectionType = collectionType.withStaticTyping();
        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newCollectionType.getBindings());
        assertNull(newCollectionType.getSuperClass());
        assertNull(newCollectionType.getSuperInterfaces());
        assertEquals(JavaType.STRING.withStaticTyping(), newCollectionType.getElementType());
    }

    @Test
    public void testRefine() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        Class<?> rawType = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.OBJECT;
        JavaType[] superInterfaces = {JavaType.SERIALIZABLE};
        CollectionLikeType newCollectionType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertArrayEquals(superInterfaces, newCollectionType.getSuperInterfaces());
        assertEquals(JavaType.STRING, newCollectionType.getElementType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        assertTrue(collectionType.isTrueCollectionType());
        CollectionLikeType nonCollectionType = new CollectionLikeType(Object.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        assertFalse(nonCollectionType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType collectionType1 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        CollectionLikeType collectionType2 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        CollectionLikeType collectionType3 = new CollectionLikeType(Object.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        CollectionLikeType collectionType4 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, null, null, false);

        assertTrue(collectionType1.equals(collectionType1));
        assertTrue(collectionType1.equals(collectionType2));
        assertFalse(collectionType1.equals(collectionType3));
        assertFalse(collectionType1.equals(collectionType4));
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.STRING, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.String]", collectionType.toString());
    }

}