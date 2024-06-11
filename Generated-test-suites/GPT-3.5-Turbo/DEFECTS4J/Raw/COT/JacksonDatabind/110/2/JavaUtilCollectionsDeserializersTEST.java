import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        ctxt = new DeserializationContext();
        type = new JavaType();
    }

    @Test
    public void testFindForCollection_WithMatchingType_ReturnsJsonDeserializer() throws JsonMappingException {
        JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
    }

    @Test
    public void testFindForCollection_WithNonMatchingType_ReturnsNull() throws JsonMappingException {
        JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
    }

    @Test
    public void testFindForMap_WithMatchingType_ReturnsJsonDeserializer() throws JsonMappingException {
        JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
    }

    @Test
    public void testFindForMap_WithNonMatchingType_ReturnsNull() throws JsonMappingException {
        JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
    }

    @Test
    public void testConverter_WithSingletonSetKind_ReturnsJavaUtilCollectionsConverter() {
        JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type, Set.class);
    }

    @Test
    public void testConverter_WithSingletonListKind_ReturnsJavaUtilCollectionsConverter() {
        JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, type, List.class);
    }

    @Test
    public void testConverter_WithSingletonMapKind_ReturnsJavaUtilCollectionsConverter() {
        JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, type, Map.class);
    }

    @Test
    public void testConverter_WithUnmodifiableSetKind_ReturnsJavaUtilCollectionsConverter() {
        JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, type, Set.class);
    }

    @Test
    public void testConverter_WithUnmodifiableListKind_ReturnsJavaUtilCollectionsConverter() {
        JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, type, List.class);
    }

    @Test
    public void testConverter_WithUnmodifiableMapKind_ReturnsJavaUtilCollectionsConverter() {
        JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, type, Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverter_WithInvalidSize_ThrowsIllegalArgumentException() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type);
        converter._checkSingleton(0);
    }
}