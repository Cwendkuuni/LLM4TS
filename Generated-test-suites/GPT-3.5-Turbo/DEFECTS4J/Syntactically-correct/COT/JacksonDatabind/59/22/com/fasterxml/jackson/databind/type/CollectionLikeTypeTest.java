import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class CollectionLikeTypeTest {

    private CollectionLikeType collectionLikeType;

    @Before
    public void setUp() {
        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, JavaType.STRING, null, null, false);
    }

    @Test
    public void testConstruct() {
        CollectionLikeType constructedType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.STRING);
        assertEquals(Collection.class, constructedType.getRawClass());
        assertEquals(JavaType.STRING, constructedType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new CollectionLikeType(Collection.class, null, null, null, JavaType.OBJECT, null, null, false);
        JavaType elementType = JavaType.OBJECT;
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals(Collection.class, upgradedType.getRawClass());
        assertEquals(JavaType.OBJECT, upgradedType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = JavaType.OBJECT;
        CollectionLikeType newType = collectionLikeType.withContentType(newContentType);
        assertEquals(newContentType, newType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        CollectionLikeType newType = collectionLikeType.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        CollectionLikeType newType = collectionLikeType.withContentTypeHandler(handler);
        assertEquals(handler, newType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        CollectionLikeType newType = collectionLikeType.withValueHandler(handler);
        assertEquals(handler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        CollectionLikeType newType = collectionLikeType.withContentValueHandler(handler);
        assertEquals(handler, newType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType newType = collectionLikeType.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = JavaType.OBJECT;
        CollectionLikeType refinedType = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(JavaType.OBJECT, refinedType.getContentType());
    }

    @Test
    public void testIsTrueCollectionType() {
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType sameType = new CollectionLikeType(Collection.class, null, null, null, JavaType.STRING, null, null, false);
        CollectionLikeType differentType = new CollectionLikeType(Collection.class, null, null, null, JavaType.OBJECT, null, null, false);

        assertEquals(collectionLikeType, collectionLikeType);
        assertEquals(collectionLikeType, sameType);
        assertNotEquals(collectionLikeType, differentType);
        assertNotEquals(collectionLikeType, null);
        assertNotEquals(collectionLikeType, "not a Type");
    }

    @Test
    public void testToString() {
        String expectedString = "[collection-like type; class java.util.Collection, contains JAVA.lang.String]";
        assertEquals(expectedString, collectionLikeType.toString());
    }

}