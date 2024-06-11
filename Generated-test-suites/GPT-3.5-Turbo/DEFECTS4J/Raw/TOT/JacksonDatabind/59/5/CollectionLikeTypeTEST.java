import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CollectionLikeTypeTest {

    private CollectionLikeType collectionLikeType;

    @Before
    public void setup() {
        collectionLikeType = CollectionLikeType.construct(Collection.class, new TypeBindings(), null, null, SimpleType.construct(String.class));
    }

    @Test
    public void testConstructorWithArguments() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = SimpleType.construct(Object.class);
        JavaType[] superInts = { SimpleType.construct(CharSequence.class) };
        JavaType elemT = SimpleType.construct(Integer.class);

        CollectionLikeType type = new CollectionLikeType(Collection.class, bindings, superClass, superInts, elemT,
                new Object(), new Object(), true);

        assertEquals(Collection.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(1, type.getInterfaces().length);
        assertEquals(CharSequence.class, type.getInterfaces()[0].getRawClass());
        assertEquals(elemT, type.getContentType());
        assertTrue(type.isContainerType());
        assertTrue(type.isCollectionLikeType());
    }

    @Test
    public void testConstructorWithBaseType() {
        TypeBase base = new TypeBase(Object.class) {
            private static final long serialVersionUID = 1L;
        };
        JavaType elemT = SimpleType.construct(Integer.class);

        CollectionLikeType type = new CollectionLikeType(base, elemT);

        assertEquals(Object.class, type.getRawClass());
        assertEquals(elemT, type.getContentType());
    }

    @Test
    public void testConstructMethodWithRawTypeAndBindings() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = SimpleType.construct(Object.class);
        JavaType[] superInts = { SimpleType.construct(Map.class) };
        JavaType elemT = SimpleType.construct(Double.class);

        CollectionLikeType type = CollectionLikeType.construct(List.class, bindings, superClass, superInts, elemT);

        assertEquals(List.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(1, type.getInterfaces().length);
        assertEquals(Map.class, type.getInterfaces()[0].getRawClass());
        assertEquals(elemT, type.getContentType());
        assertTrue(type.isContainerType());
        assertTrue(type.isCollectionLikeType());
    }

    @Test
    public void testConstructMethodWithRawTypeAndElementType() {
        JavaType elemT = SimpleType.construct(String.class);

        CollectionLikeType type = CollectionLikeType.construct(Collection.class, elemT);

        assertEquals(Collection.class, type.getRawClass());
        assertEquals(elemT, type.getContentType());
        assertTrue(type.isContainerType());
        assertTrue(type.isCollectionLikeType());
    }

    @Test
    public void testUpgradeFromMethodWithTypeBase() {
        TypeBase baseType = new TypeBase(List.class) {
            private static final long serialVersionUID = 1L;
        };
        JavaType elementType = SimpleType.construct(Float.class);

        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertEquals(List.class, type.getRawClass());
        assertEquals(elementType, type.getContentType());
        assertTrue(type.isContainerType());
        assertTrue(type.isCollectionLikeType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromMethodWithNonTypeBase() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType elementType = SimpleType.construct(Integer.class);

        CollectionLikeType.upgradeFrom(baseType, elementType);
    }

    @Test
    public void testNarrowMethod() {
        JavaType elemT = SimpleType.construct(Object.class);

        JavaType narrowedType = collectionLikeType._narrow(ArrayList.class);

        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(elemT, narrowedType.getContentType());
        assertTrue(narrowedType.isContainerType());
        assertTrue(narrowedType.isCollectionLikeType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = SimpleType.construct(Double.class);

        JavaType newType = collectionLikeType.withContentType(contentType);

        assertEquals(Collection.class, newType.getRawClass());
        assertEquals(contentType, newType.getContentType());
        assertTrue(newType.isContainerType());
        assertTrue(newType.isCollectionLikeType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();

        CollectionLikeType newType = collectionLikeType.withTypeHandler(typeHandler);

        assertEquals(typeHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();

        CollectionLikeType newType = collectionLikeType.withContentTypeHandler(contentTypeHandler);

        assertEquals(contentTypeHandler, newType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();

        CollectionLikeType newType = collectionLikeType.withValueHandler(valueHandler);

        assertEquals(valueHandler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();

        CollectionLikeType newType = collectionLikeType.withContentValueHandler(contentValueHandler);

        assertEquals(contentValueHandler, newType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType staticType = SimpleType.construct(Serializable.class);

        JavaType newType = collectionLikeType.withStaticTyping();

        assertEquals(Collection.class, newType.getRawClass());
        assertEquals(staticType, newType.getContentType());
        assertTrue(newType.isContainerType());
        assertTrue(newType.isCollectionLikeType());
    }

    @Test
    public void testRefine() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = SimpleType.construct(Object.class);
        JavaType[] superInts = { SimpleType.construct(CharSequence.class) };
        JavaType elemT = SimpleType.construct(Integer.class);

        JavaType refinedType = collectionLikeType.refine(List.class, bindings, superClass, superInts);

        assertEquals(List.class, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(1, refinedType.getInterfaces().length);
        assertEquals(CharSequence.class, refinedType.getInterfaces()[0].getRawClass());
        assertEquals(elemT, refinedType.getContentType());
        assertTrue(refinedType.isContainerType());
        assertTrue(refinedType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        TypeBase base = new TypeBase(List.class) {

            private static final long serialVersionUID = 1L;
        };
        JavaType elemT = SimpleType.construct(Double.class);

        CollectionLikeType type1 = CollectionLikeType.upgradeFrom(base, elemT);
        CollectionLikeType type2 = CollectionLikeType.upgradeFrom(base, elemT);
        CollectionLikeType type3 = CollectionLikeType.upgradeFrom(TypeBase.construct(String.class), elemT);

        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }

    @Test
    public void testToString() {
        String expected = "[collection-like type; class java.util.Collection, contains " + SimpleType.construct(String.class)
                + "]";

        assertEquals(expected, collectionLikeType.toString());
    }

}