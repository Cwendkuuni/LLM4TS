import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        // Test construct method
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        CollectionLikeType collectionType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);

        // Assert that the constructed collection type has correct values
        assertSame(rawType, collectionType.getRawClass());
        assertSame(bindings, collectionType.getBindings());
        assertSame(superClass, collectionType.getSuperClass());
        assertSame(superInts, collectionType.getSuperInterfaces());
        assertSame(elemT, collectionType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        // Test upgradeFrom method
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, elementType);

        // Assert that the upgraded collection type has correct values
        assertSame(baseType, upgradedType.getBaseType());
        assertSame(elementType, upgradedType.getElementType());
    }

    @Test
    public void testWithContentType() {
        // Test withContentType method
        CollectionLikeType originalType = new CollectionLikeType();
        JavaType contentType = new JavaType();
        CollectionLikeType contentTypeType = originalType.withContentType(contentType);

        // Assert that the type with content type has correct values
        assertSame(originalType.getRawClass(), contentTypeType.getRawClass());
        assertSame(originalType.getBindings(), contentTypeType.getBindings());
        assertSame(originalType.getSuperClass(), contentTypeType.getSuperClass());
        assertSame(originalType.getSuperInterfaces(), contentTypeType.getSuperInterfaces());
        assertNotSame(originalType.getContentType(), contentTypeType.getContentType());
        assertSame(contentType, contentTypeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        // Test withTypeHandler method
        CollectionLikeType originalType = new CollectionLikeType();
        Object typeHandler = new Object();
        CollectionLikeType typeHandlerType = originalType.withTypeHandler(typeHandler);

        // Assert that the type with type handler has correct values
        assertSame(originalType.getRawClass(), typeHandlerType.getRawClass());
        assertSame(originalType.getBindings(), typeHandlerType.getBindings());
        assertSame(originalType.getSuperClass(), typeHandlerType.getSuperClass());
        assertSame(originalType.getSuperInterfaces(), typeHandlerType.getSuperInterfaces());
        assertSame(originalType.getContentType(), typeHandlerType.getContentType());
        assertSame(typeHandler, typeHandlerType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test withContentTypeHandler method
        CollectionLikeType originalType = new CollectionLikeType();
        Object contentTypeHandler = new Object();
        CollectionLikeType contentTypeHandlerType = originalType.withContentTypeHandler(contentTypeHandler);

        // Assert that the type with content type handler has correct values
        assertSame(originalType.getRawClass(), contentTypeHandlerType.getRawClass());
        assertSame(originalType.getBindings(), contentTypeHandlerType.getBindings());
        assertSame(originalType.getSuperClass(), contentTypeHandlerType.getSuperClass());
        assertSame(originalType.getSuperInterfaces(), contentTypeHandlerType.getSuperInterfaces());
        assertNotSame(originalType.getContentType().getTypeHandler(), contentTypeHandlerType.getContentType().getTypeHandler());
        assertSame(contentTypeHandler, contentTypeHandlerType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // Test withValueHandler method
        CollectionLikeType originalType = new CollectionLikeType();
        Object valueHandler = new Object();
        CollectionLikeType valueHandlerType = originalType.withValueHandler(valueHandler);

        // Assert that the type with value handler has correct values
        assertSame(originalType.getRawClass(), valueHandlerType.getRawClass());
        assertSame(originalType.getBindings(), valueHandlerType.getBindings());
        assertSame(originalType.getSuperClass(), valueHandlerType.getSuperClass());
        assertSame(originalType.getSuperInterfaces(), valueHandlerType.getSuperInterfaces());
        assertSame(originalType.getContentType(), valueHandlerType.getContentType());
        assertSame(valueHandler, valueHandlerType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Test withContentValueHandler method
        CollectionLikeType originalType = new CollectionLikeType();
        Object contentValueHandler = new Object();
        CollectionLikeType contentValueHandlerType = originalType.withContentValueHandler(contentValueHandler);

        // Assert that the type with content value handler has correct values
        assertSame(originalType.getRawClass(), contentValueHandlerType.getRawClass());
        assertSame(originalType.getBindings(), contentValueHandlerType.getBindings());
        assertSame(originalType.getSuperClass(), contentValueHandlerType.getSuperClass());
        assertSame(originalType.getSuperInterfaces(), contentValueHandlerType.getSuperInterfaces());
        assertNotSame(originalType.getContentType().getValueHandler(), contentValueHandlerType.getContentType().getValueHandler());
        assertSame(contentValueHandler, contentValueHandlerType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Test withStaticTyping method
        CollectionLikeType originalType = new CollectionLikeType();
        CollectionLikeType staticType = originalType.withStaticTyping();

        // Assert that the type with static typing has correct values
        assertSame(originalType.getRawClass(), staticType.getRawClass());
        assertSame(originalType.getBindings(), staticType.getBindings());
        assertSame(originalType.getSuperClass(), staticType.getSuperClass());
        assertSame(originalType.getSuperInterfaces(), staticType.getSuperInterfaces());
        assertSame(originalType.getContentType().withStaticTyping(), staticType.getContentType());
        assertTrue(staticType.isStaticTyping());
    }
    
}