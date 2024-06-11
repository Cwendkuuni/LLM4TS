import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInts = new JavaType[]{new SimpleType()};
        JavaType elemT = new SimpleType();
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        Assert.assertEquals(rawType, collectionType.getRawClass());
        Assert.assertEquals(bindings, collectionType.getBindings());
        Assert.assertEquals(superClass, collectionType.getSuperClass());
        Assert.assertArrayEquals(superInts, collectionType.getInterfaces());
        Assert.assertEquals(elemT, collectionType.getContentType());
        Assert.assertNull(collectionType.getValueHandler());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertFalse(collectionType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType();
        CollectionType originalType = new CollectionType();
        CollectionType newType = originalType.withContentType(contentType);
        
        Assert.assertEquals(contentType, newType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType originalType = new CollectionType();
        CollectionType newType = originalType.withTypeHandler(typeHandler);
        
        Assert.assertEquals(typeHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        CollectionType originalType = new CollectionType();
        CollectionType newType = originalType.withContentTypeHandler(contentTypeHandler);
        
        Assert.assertEquals(contentTypeHandler, newType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType originalType = new CollectionType();
        CollectionType newType = originalType.withValueHandler(valueHandler);
        
        Assert.assertEquals(valueHandler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        CollectionType originalType = new CollectionType();
        CollectionType newType = originalType.withContentValueHandler(contentValueHandler);
        
        Assert.assertEquals(contentValueHandler, newType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType originalType = new CollectionType();
        CollectionType newType = originalType.withStaticTyping();
        
        Assert.assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInts = new JavaType[]{new SimpleType()};
        JavaType elemT = new SimpleType();
        CollectionType originalType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        Class newRawType = ArrayList.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType newSuperClass = new SimpleType();
        JavaType[] newSuperInts = new JavaType[]{new SimpleType()};
        JavaType newElemT = new SimpleType();
        JavaType newSuperType = new SimpleType();
        JavaType[] newSuperTypes = new JavaType[]{new SimpleType(), new SimpleType()};
        
        CollectionType refinedType = (CollectionType) originalType.refine(newRawType, newBindings, newSuperClass, newSuperInts);
        
        Assert.assertEquals(newRawType, refinedType.getRawClass());
        Assert.assertEquals(newBindings, refinedType.getBindings());
        Assert.assertEquals(newSuperClass, refinedType.getSuperClass());
        Assert.assertArrayEquals(newSuperInts, refinedType.getInterfaces());
        Assert.assertEquals(newSuperType, refinedType.getSuperType());
        Assert.assertArrayEquals(newSuperTypes, refinedType.getInterfaces());
        Assert.assertEquals(newElemT, refinedType.getContentType());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType();
        String expected = "[collection type; class com.fasterxml.jackson.databind.type.CollectionType, contains null]";
        Assert.assertEquals(expected, collectionType.toString());
    }
}