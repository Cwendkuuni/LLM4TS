import org.junit.Assert;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        Assert.assertNotNull(collectionType);
        Assert.assertEquals(List.class, collectionType.getRawClass());
        Assert.assertEquals(new TypeBindings(), collectionType.getBindings());
        Assert.assertNull(collectionType.getSuperClass());
        Assert.assertNull(collectionType.getSuperInterfaces());
        Assert.assertEquals(String.class, collectionType.getContentType());
        Assert.assertNull(collectionType.getValueHandler());
        Assert.assertNull(collectionType.getTypeHandler());
        Assert.assertFalse(collectionType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType updatedType = collectionType.withContentType(Integer.class);
        Assert.assertNotEquals(collectionType, updatedType);
        Assert.assertEquals(List.class, updatedType.getRawClass());
        Assert.assertEquals(new TypeBindings(), updatedType.getBindings());
        Assert.assertNull(updatedType.getSuperClass());
        Assert.assertNull(updatedType.getSuperInterfaces());
        Assert.assertEquals(Integer.class, updatedType.getContentType());
        Assert.assertNull(updatedType.getValueHandler());
        Assert.assertNull(updatedType.getTypeHandler());
        Assert.assertFalse(updatedType.isAsStatic());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType updatedType = collectionType.withTypeHandler(SomeTypeHandler.class);
        Assert.assertNotEquals(collectionType, updatedType);
        Assert.assertEquals(List.class, updatedType.getRawClass());
        Assert.assertEquals(new TypeBindings(), updatedType.getBindings());
        Assert.assertNull(updatedType.getSuperClass());
        Assert.assertNull(updatedType.getSuperInterfaces());
        Assert.assertEquals(String.class, updatedType.getContentType());
        Assert.assertNull(updatedType.getValueHandler());
        Assert.assertEquals(SomeTypeHandler.class, updatedType.getTypeHandler());
        Assert.assertFalse(updatedType.isAsStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType updatedType = collectionType.withContentTypeHandler(SomeTypeHandler.class);
        Assert.assertNotEquals(collectionType, updatedType);
        Assert.assertEquals(List.class, updatedType.getRawClass());
        Assert.assertEquals(new TypeBindings(), updatedType.getBindings());
        Assert.assertNull(updatedType.getSuperClass());
        Assert.assertNull(updatedType.getSuperInterfaces());
        Assert.assertEquals(String.class, updatedType.getContentType());
        Assert.assertNull(updatedType.getValueHandler());
        Assert.assertNull(updatedType.getTypeHandler());
        Assert.assertFalse(updatedType.isAsStatic());
        Assert.assertEquals(SomeTypeHandler.class, updatedType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType updatedType = collectionType.withStaticTyping();
        Assert.assertNotEquals(collectionType, updatedType);
        Assert.assertEquals(List.class, updatedType.getRawClass());
        Assert.assertEquals(new TypeBindings(), updatedType.getBindings());
        Assert.assertNull(updatedType.getSuperClass());
        Assert.assertNull(updatedType.getSuperInterfaces());
        Assert.assertEquals(String.class, updatedType.getContentType());
        Assert.assertNull(updatedType.getValueHandler());
        Assert.assertNull(updatedType.getTypeHandler());
        Assert.assertTrue(updatedType.isAsStatic());
    }

    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType refinedType = collectionType.refine(ArrayList.class, new TypeBindings(), null, null);
        Assert.assertNotEquals(collectionType, refinedType);
        Assert.assertEquals(ArrayList.class, refinedType.getRawClass());
        Assert.assertEquals(new TypeBindings(), refinedType.getBindings());
        Assert.assertNull(refinedType.getSuperClass());
        Assert.assertNull(refinedType.getSuperInterfaces());
        Assert.assertEquals(String.class, refinedType.getContentType());
        Assert.assertNull(refinedType.getValueHandler());
        Assert.assertNull(refinedType.getTypeHandler());
        Assert.assertFalse(refinedType.isAsStatic());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        Assert.assertEquals("[collection type; class java.util.List, contains class java.lang.String]", collectionType.toString());
    }
}