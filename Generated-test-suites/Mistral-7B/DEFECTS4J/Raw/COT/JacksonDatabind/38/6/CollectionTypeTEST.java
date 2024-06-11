import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    @Test
    public void testConstructorWithArguments() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Assert.assertEquals(rawType, collectionType.getRawClass());
        Assert.assertEquals(bindings, collectionType.getBindings());
        Assert.assertNull(superClass);
        Assert.assertArrayEquals(new JavaType[0], collectionType.getSuperInterfaces());
        Assert.assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testConstructDeprecatedWithOneArgument() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        // Check default values for other arguments
        Assert.assertNull(collectionType.getBindings());
        Assert.assertNull(collectionType.getSuperClass());
        Assert.assertNull(collectionType.getSuperInterfaces());
        Assert.assertEquals(rawType, collectionType.getRawClass());
        Assert.assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testNarrowMethod() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, null, _bogusSuperClass(rawType), null, elemT, null, null, false);
        JavaType narrowedCollectionType = collectionType.narrow(LinkedList.class);
        Assert.assertNotNull(narrowedCollectionType);
        Assert.assertEquals(LinkedList.class, narrowedCollectionType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), narrowedCollectionType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), narrowedCollectionType.getSuperClass());
        Assert.assertArrayEquals(collectionType.getSuperInterfaces(), narrowedCollectionType.getSuperInterfaces());
        Assert.assertEquals(elemT, narrowedCollectionType.getContentType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowMethodWithUnsupportedType() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, null, _bogusSuperClass(rawType), null, elemT, null, null, false);
        JavaType unsupportedType = JavaType.constructType(String.class);
        JavaType narrowedType = collectionType.narrow(unsupportedType);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType newContentType = JavaType.constructCollectionType(LinkedList.class, Integer.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        Assert.assertNotSame(collectionType, newCollectionType);
        Assert.assertEquals(newContentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        Assert.assertNotSame(collectionType, newCollectionType);
        Assert.assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(valueHandler).withTypeHandler(typeHandler);
        Assert.assertNotSame(collectionType, newCollectionType);
        Assert.assertEquals(valueHandler, newCollectionType.getValueHandler());
        Assert.assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        Object valueHandler = new Object();
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        Assert.assertNotSame(collectionType, newCollectionType);
        Assert.assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentValueHandler(valueHandler).withTypeHandler(typeHandler);
        Assert.assertNotSame(collectionType, newCollectionType);
        Assert.assertEquals(valueHandler, newCollectionType.getValueHandler());
        Assert.assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        Assert.assertNotSame(collectionType, newCollectionType);
        Assert.assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testRefineMethod() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.constructType(ArrayList.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructCollectionType(LinkedList.class, Integer.class);
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType refinedType = collectionType.refine(LinkedList.class, bindings, superClass, superInts);
        Assert.assertNotNull(refinedType);
        Assert.assertEquals(LinkedList.class, refinedType.getRawClass());
        Assert.assertEquals(bindings, refinedType.getBindings());
        Assert.assertEquals(superClass, refinedType.getSuperClass());
        Assert.assertArrayEquals(superInts, refinedType.getSuperInterfaces());
        Assert.assertEquals(elemT, refinedType.getContentType());
    }
}