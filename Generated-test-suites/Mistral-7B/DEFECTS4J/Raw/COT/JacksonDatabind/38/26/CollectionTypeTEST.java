import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        Assert.assertEquals(ArrayList.class, collectionType.getRawClass());
        Assert.assertEquals(elemType, collectionType.getContentType());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertNull(collectionType.getValueHandler());
        Assert.assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType elemType = JavaType.construct(int[].class);
        try {
            CollectionType collectionType = CollectionType.construct(ArrayList.class, elemType);
            fail("Method should be deprecated");
        } catch (DeprecatedException e) {
            // Expected deprecated exception
        }
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        CollectionType arrayListType = (CollectionType) listType.narrow(ArrayList[].class);
        Assert.assertEquals(ArrayList[].class, arrayListType.getRawClass());
        Assert.assertEquals(Integer.TYPE, arrayListType.getContentType());
    }

    @Test
    public void testConstructWithContentType() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        CollectionType collectionTypeWithContentType = new CollectionType(ArrayList.class, null, null, null, elemType, null, null, false).withContentType(JavaType.constructCollectionType(ArrayList.class, Integer.TYPE));
        Assert.assertEquals(ArrayList.class, collectionType.getRawClass());
        Assert.assertEquals(JavaType.constructCollectionType(ArrayList.class, Integer.TYPE), collectionTypeWithContentType.getContentType());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertNull(collectionType.getValueHandler());
        Assert.assertFalse(collectionType.isStatic());
        Assert.assertFalse(collectionTypeWithContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        CollectionType collectionTypeWithTypeHandler = new CollectionType(ArrayList.class, null, null, null, elemType, new Object() {}, null, false).withTypeHandler(new Object() {});
        Assert.assertEquals(ArrayList.class, collectionType.getRawClass());
        Assert.assertEquals(elemType, collectionType.getContentType());
        Assert.assertEquals(new Object(), collectionTypeWithTypeHandler.getTypeHandler());
        Assert.assertNull(collectionType.getValueHandler());
        Assert.assertFalse(collectionType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        CollectionType collectionTypeWithContentTypeHandler = new CollectionType(ArrayList.class, null, null, null, elemType, null, new Object() {}, false).withContentTypeHandler(new Object() {});
        Assert.assertEquals(ArrayList.class, collectionType.getRawClass());
        Assert.assertEquals(elemType, collectionType.getContentType());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertEquals(new Object(), collectionTypeWithContentTypeHandler.getValueHandler());
        Assert.assertFalse(collectionType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        CollectionType collectionTypeWithValueHandler = new CollectionType(ArrayList.class, null, null, null, elemType, null, null, false).withValueHandler(new Object() {});
        Assert.assertEquals(ArrayList.class, collectionType.getRawClass());
        Assert.assertEquals(elemType, collectionType.getContentType());
        Assert.assertEquals(new Object(), collectionTypeWithValueHandler.getValueHandler());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertFalse(collectionType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        CollectionType collectionTypeWithContentValueHandler = new CollectionType(ArrayList.class, null, null, null, elemType, null, null, false).withContentValueHandler(new Object() {});
        Assert.assertEquals(ArrayList.class, collectionType.getRawClass());
        Assert.assertEquals(elemType, collectionType.getContentType());
        Assert.assertEquals(new Object(), collectionTypeWithContentValueHandler.getValueHandler());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertFalse(collectionType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, elemType, null, null, false);
        CollectionType staticCollectionType = collectionType.withStaticTyping();
        Assert.assertEquals(ArrayList.class, collectionType.getRawClass());
        Assert.assertEquals(elemType, collectionType.getContentType());
        Assert.assertFalse(collectionType.isStatic());
        Assert.assertTrue(staticCollectionType.isStatic());
    }

    @Test(expected = ClassCastException.class)
    public void testRefine() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        CollectionType arrayListType = (CollectionType) listType.refine(LinkedList.class, null, null, null);
        fail("Method should throw ClassCastException");
    }

    @Test
    public void testToString() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        String expected = "[collection type; class " + ArrayList.class.getName() + ", contains " + elemType + "]";
        String actual = collectionType.toString();
        assertEquals(expected, actual);
    }
}