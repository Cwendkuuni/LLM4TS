
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstructor() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);

        Assert.assertEquals(collT, collectionType.getRawClass());
        Assert.assertEquals(bindings, collectionType.getBindings());
        Assert.assertEquals(superClass, collectionType.getSuperClass());
        Assert.assertEquals(superInts, collectionType.getInterfaces());
        Assert.assertEquals(elemT, collectionType.getContentType());
        Assert.assertEquals(valueHandler, collectionType.getValueHandler());
        Assert.assertEquals(typeHandler, collectionType.getTypeHandler());
        Assert.assertTrue(collectionType.isStatic());
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        Assert.assertEquals(rawType, collectionType.getRawClass());
        Assert.assertEquals(bindings, collectionType.getBindings());
        Assert.assertEquals(superClass, collectionType.getSuperClass());
        Assert.assertEquals(superInts, collectionType.getInterfaces());
        Assert.assertEquals(elemT, collectionType.getContentType());
        Assert.assertNull(collectionType.getValueHandler());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = CollectionType.class;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        Assert.assertEquals(rawType, collectionType.getRawClass());
        Assert.assertNull(collectionType.getBindings());
        Assert.assertNotNull(collectionType.getSuperClass());
        Assert.assertNull(collectionType.getInterfaces());
        Assert.assertEquals(elemT, collectionType.getContentType());
        Assert.assertNull(collectionType.getValueHandler());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertFalse(collectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = CollectionType.class;

        CollectionType collectionType = new CollectionType(subclass, null, null, null, null, null, null, false);

        JavaType narrowedType = collectionType.narrow(subclass);

        Assert.assertEquals(subclass, narrowedType.getRawClass());
        Assert.assertNull(narrowedType.getBindings());
        Assert.assertNull(narrowedType.getSuperClass());
        Assert.assertNull(narrowedType.getInterfaces());
        Assert.assertNull(narrowedType.getContentType());
        Assert.assertNull(narrowedType.getValueHandler());
        Assert.assertNull(narrowedType.getTypeHandler());
        Assert.assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
        JavaType contentType = null;

        JavaType newType = collectionType.withContentType(contentType);

        Assert.assertEquals(collectionType.getRawClass(), newType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), newType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        Assert.assertEquals(collectionType.getInterfaces(), newType.getInterfaces());
        Assert.assertEquals(contentType, newType.getContentType());
        Assert.assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        Assert.assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        Assert.assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
        Object handler = null;

        CollectionType newType = collectionType.withTypeHandler(handler);

        Assert.assertEquals(collectionType.getRawClass(), newType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), newType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        Assert.assertEquals(collectionType.getInterfaces(), newType.getInterfaces());
        Assert.assertEquals(collectionType.getContentType(), newType.getContentType());
        Assert.assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        Assert.assertEquals(handler, newType.getTypeHandler());
        Assert.assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
        Object handler = null;

        CollectionType newType = collectionType.withContentTypeHandler(handler);

        Assert.assertEquals(collectionType.getRawClass(), newType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), newType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        Assert.assertEquals(collectionType.getInterfaces(), newType.getInterfaces());
        Assert.assertEquals(collectionType.getContentType().withTypeHandler(handler), newType.getContentType());
        Assert.assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        Assert.assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        Assert.assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
        Object handler = null;

        CollectionType newType = collectionType.withValueHandler(handler);

        Assert.assertEquals(collectionType.getRawClass(), newType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), newType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        Assert.assertEquals(collectionType.getInterfaces(), newType.getInterfaces());
        Assert.assertEquals(collectionType.getContentType(), newType.getContentType());
        Assert.assertEquals(handler, newType.getValueHandler());
        Assert.assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        Assert.assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
        Object handler = null;

        CollectionType newType = collectionType.withContentValueHandler(handler);

        Assert.assertEquals(collectionType.getRawClass(), newType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), newType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        Assert.assertEquals(collectionType.getInterfaces(), newType.getInterfaces());
        Assert.assertEquals(collectionType.getContentType().withValueHandler(handler), newType.getContentType());
        Assert.assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        Assert.assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        Assert.assertEquals(collectionType.isStatic(), newType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        CollectionType newType = collectionType.withStaticTyping();

        Assert.assertEquals(collectionType.getRawClass(), newType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), newType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        Assert.assertEquals(collectionType.getInterfaces(), newType.getInterfaces());
        Assert.assertEquals(collectionType.getContentType().withStaticTyping(), newType.getContentType());
        Assert.assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        Assert.assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        Assert.assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(CollectionType.class, null, null, null, null);
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInts);

        Assert.assertEquals(rawType, refinedType.getRawClass());
        Assert.assertEquals(bindings, refinedType.getBindings());
        Assert.assertEquals(superClass, refinedType.getSuperClass());
        Assert.assertEquals(superInts, refinedType.getInterfaces());
        Assert.assertEquals(elemT, refinedType.getContentType());
        Assert.assertEquals(collectionType.getValueHandler(), refinedType.getValueHandler());
        Assert.assertEquals(collectionType.getTypeHandler(), refinedType.getTypeHandler());
        Assert.assertEquals(collectionType.isStatic(), refinedType.isStatic());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);

        String expectedString = "[collection type; class com.fasterxml.jackson.databind.type.CollectionType, contains null]";
        Assert.assertEquals(expectedString, collectionType.toString());
    }
}

