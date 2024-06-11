import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertNotNull(type);
        assertTrue(type.isTrueCollectionType());
        assertEquals(Collection.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getElementType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStaticTyping());
        assertFalse(type.isContainerType());
        assertTrue(type.isCollectionLikeType());
        assertNull(type.getContentType());
        assertNull(type.getContentValueHandler());
        assertNull(type.getContentTypeHandler());
        assertFalse(type.hasHandlers());
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("java.util.Collection", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        type.getGenericSignature(sb2);
        assertEquals("java.util.Collection<null>;", sb2.toString());
        assertEquals("[collection-like type; class java.util.Collection, contains null]", type.toString());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(null);
        JavaType elementType = null;
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(type);
        assertTrue(type.isTrueCollectionType());
        assertEquals(SimpleType.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getElementType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStaticTyping());
        assertFalse(type.isContainerType());
        assertTrue(type.isCollectionLikeType());
        assertNull(type.getContentType());
        assertNull(type.getContentValueHandler());
        assertNull(type.getContentTypeHandler());
        assertFalse(type.hasHandlers());
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("javax.print.SimpleType", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        type.getGenericSignature(sb2);
        assertEquals("javax.print.SimpleType<null>;", sb2.toString());
        assertEquals("[collection-like type; class javax.print.SimpleType, contains null]", type.toString());
    }

    @Test
    public void testNarrow() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType subclass = null;
        JavaType narrowed = type._narrow(subclass);
        assertNotNull(narrowed);
        assertTrue(narrowed.isTrueCollectionType());
        assertNull(narrowed.getRawClass());
        assertNull(narrowed.getBindings());
        assertNull(narrowed.getSuperClass());
        assertNull(narrowed.getSuperInterfaces());
        assertNull(narrowed.getElementType());
        assertNull(narrowed.getValueHandler());
        assertNull(narrowed.getTypeHandler());
        assertFalse(narrowed.isStaticTyping());
        assertFalse(narrowed.isContainerType());
        assertTrue(narrowed.isCollectionLikeType());
        assertNull(narrowed.getContentType());
        assertNull(narrowed.getContentValueHandler());
        assertNull(narrowed.getContentTypeHandler());
        assertFalse(narrowed.hasHandlers());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object h = null;
        CollectionLikeType newType = type.withTypeHandler(h);
        assertNotNull(newType);
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object h = null;
        CollectionLikeType newType = type.withContentTypeHandler(h);
        assertNotNull(newType);
    }

    @Test
    public void testWithValueHandler() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object h = null;
        CollectionLikeType newType = type.withValueHandler(h);
        assertNotNull(newType);
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object h = null;
        CollectionLikeType newType = type.withContentValueHandler(h);
        assertNotNull(newType);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType newType = type.withStaticTyping();
        assertNotNull(newType);
    }

    @Test
    public void testRefine() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refined = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(refined);
        assertTrue(refined.isTrueCollectionType());
        assertNull(refined.getRawClass());
        assertNull(refined.getBindings());
        assertNull(refined.getSuperClass());
        assertNull(refined.getSuperInterfaces());
        assertNull(refined.getElementType());
        assertNull(refined.getValueHandler());
        assertNull(refined.getTypeHandler());
        assertFalse(refined.isStaticTyping());
        assertFalse(refined.isContainerType());
        assertTrue(refined.isCollectionLikeType());
        assertNull(refined.getContentType());
        assertNull(refined.getContentValueHandler());
        assertNull(refined.getContentTypeHandler());
        assertFalse(refined.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean isCollectionType = type.isTrueCollectionType();
        assertFalse(isCollectionType);
    }

    @Test
    public void testEquals() {
        CollectionLikeType type1 = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType type2 = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean isEqual = type1.equals(type2);
        assertTrue(isEqual);
    }

    @Test
    public void testToString() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        String str = type.toString();
        assertEquals("[collection-like type; class null, contains null]", str);
    }
}