import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollectionTypeTest {
    
    private CollectionType collectionType;
    
    @Before
    public void setUp() {
        collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, null);
    }
    
    @Test
    public void testConstruct_WithRawTypeAndElemT_ShouldReturnValidCollectionType() {
        JavaType elemType = JavaType.forType(String.class);
        CollectionType collectionType = CollectionType.construct(List.class, elemType);
        
        Assert.assertEquals(List.class, collectionType.getRawClass());
        Assert.assertEquals(elemType, collectionType.getContentType());
    }
    
    @Test
    public void testConstruct_WithRawTypeAndNoElemT_ShouldReturnValidCollectionType() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class);
        
        Assert.assertEquals(ArrayList.class, collectionType.getRawClass());
        Assert.assertNull(collectionType.getContentType());
    }
    
    @Test
    public void testNarrow_WithSubclass_ShouldReturnNarrowedCollectionType() {
        CollectionType narrowedType = collectionType.narrow(Vector.class);
        
        Assert.assertEquals(Vector.class, narrowedType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), narrowedType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), narrowedType.getSuperClass());
        Assert.assertEquals(collectionType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
        Assert.assertEquals(collectionType.getContentType(), narrowedType.getContentType());
    }
    
    @Test
    public void testWithContentType_WithDifferentContentType_ShouldReturnModifiedCollectionType() {
        JavaType contentType = JavaType.forType(Integer.class);
        CollectionType modifiedType = collectionType.withContentType(contentType);
        
        Assert.assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        Assert.assertEquals(collectionType.getSuperInterfaces(), modifiedType.getSuperInterfaces());
        Assert.assertEquals(contentType, modifiedType.getContentType());
    }
    
    @Test
    public void testWithTypeHandler_WithDifferentTypeHandler_ShouldReturnModifiedCollectionType() {
        Object typeHandler = new CustomTypeHandler();
        CollectionType modifiedType = collectionType.withTypeHandler(typeHandler);
        
        Assert.assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        Assert.assertEquals(collectionType.getSuperInterfaces(), modifiedType.getSuperInterfaces());
        Assert.assertEquals(collectionType.getContentType(), modifiedType.getContentType());
        Assert.assertEquals(typeHandler, modifiedType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler_WithDifferentTypeHandler_ShouldReturnModifiedCollectionType() {
        Object typeHandler = new CustomTypeHandler();
        CollectionType modifiedType = collectionType.withContentTypeHandler(typeHandler);
        
        Assert.assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        Assert.assertEquals(collectionType.getSuperInterfaces(), modifiedType.getSuperInterfaces());
        Assert.assertNotEquals(collectionType.getContentType(), modifiedType.getContentType());
        Assert.assertEquals(typeHandler, modifiedType.getContentType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler_WithDifferentValueHandler_ShouldReturnModifiedCollectionType() {
        Object valueHandler = new CustomValueHandler();
        CollectionType modifiedType = collectionType.withValueHandler(valueHandler);
        
        Assert.assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        Assert.assertEquals(collectionType.getSuperInterfaces(), modifiedType.getSuperInterfaces());
        Assert.assertEquals(collectionType.getContentType(), modifiedType.getContentType());
        Assert.assertEquals(valueHandler, modifiedType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler_WithDifferentValueHandler_ShouldReturnModifiedCollectionType() {
        Object valueHandler = new CustomValueHandler();
        CollectionType modifiedType = collectionType.withContentValueHandler(valueHandler);
        
        Assert.assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        Assert.assertEquals(collectionType.getSuperInterfaces(), modifiedType.getSuperInterfaces());
        Assert.assertNotEquals(collectionType.getContentType(), modifiedType.getContentType());
        Assert.assertEquals(valueHandler, modifiedType.getContentType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping_WithAsStaticFlagSet_ShouldReturnModifiedCollectionType() {
        CollectionType modifiedType = collectionType.withStaticTyping();
        
        Assert.assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        Assert.assertEquals(collectionType.getSuperInterfaces(), modifiedType.getSuperInterfaces());
        Assert.assertEquals(collectionType.getContentType().withStaticTyping(), modifiedType.getContentType());
        Assert.assertTrue(modifiedType.isStaticTyping());
    }
    
    @Test
    public void testRefine_WithDifferentParameters_ShouldReturnModifiedCollectionType() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.forType(AbstractList.class);
        JavaType[] superInterfaces = new JavaType[] {JavaType.forType(Serializable.class)};
        
        CollectionType refinedType = collectionType.refine(ArrayList.class, bindings, superClass, superInterfaces);
        
        Assert.assertEquals(ArrayList.class, refinedType.getRawClass());
        Assert.assertEquals(bindings, refinedType.getBindings());
        Assert.assertEquals(superClass, refinedType.getSuperClass());
        Assert.assertEquals(superInterfaces, refinedType.getSuperInterfaces());
        Assert.assertEquals(collectionType.getContentType(), refinedType.getContentType());
    }
    
    @Test
    public void testToString_ShouldReturnStringRepresentationOfCollectionType() {
        String expected = "[collection type; class " + List.class.getName() + ", contains " + null + "]";
        
        Assert.assertEquals(expected, collectionType.toString());
    }
}