import org.junit.*;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType elementType = JavaTypeBuilder.build(Example.class);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertEquals(elementType, collectionType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        ValueHandler valueHandler = new ValueHandler();
        JavaType elementType = JavaTypeBuilder.build(Example.class).withValueHandler(valueHandler);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertEquals(valueHandler, collectionType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        TypeHandler typeHandler = new TypeHandler();
        JavaType elementType = JavaTypeBuilder.build(Example.class).withTypeHandler(typeHandler);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertEquals(typeHandler, collectionType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        ValueHandler valueHandler = new ValueHandler();
        TypeHandler typeHandler = new TypeHandler();
        JavaType elementType = JavaTypeBuilder.build(Example.class).withValueHandler(valueHandler).withTypeHandler(typeHandler);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertTrue(collectionType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("L");
        expected.append(Collection.class.getName().replace('.', '/'));
        expected.append(";");
        StringBuilder actual = collectionType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType elementType = JavaTypeBuilder.build(Example.class);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("<");
        expected.append(elementType.getGenericSignature(new StringBuilder()));
        expected.append(">;");
        StringBuilder actual = collectionType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType elementType = JavaTypeBuilder.build(Example.class);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        String expected = Collection.class.getName() + "<" + elementType.toCanonical() + ">";
        assertEquals(expected, collectionType.buildCanonicalName());
    }

    @Test
    public void testRefine() {
        JavaType elementType = JavaTypeBuilder.build(Example.class);
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);

        Class<?> rawType = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, refinedType.getRawClass());
        assertNull(refinedType.getBindings());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getInterfaces());
        assertEquals(elementType, refinedType.getContentType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertEquals(collectionType.isStaticTyping(), refinedType.isStaticTyping());
    }

}