
import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    private JavaUtilCollectionsDeserializers deserializers;

    @Before
    public void setup() {
        deserializers = new JavaUtilCollectionsDeserializers() {
        };
    }

    @Test
    public void testFindForCollection_shouldReturnNullForUnknownType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForCollection(ctxt, type);

        assertNull(result);
    }

    @Test
    public void testFindForCollection_shouldReturnStdDelegatingDeserializerForArraysList() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructCollectionType(List.class, Object.class);

        JsonDeserializer<?> result = deserializers.findForCollection(ctxt, type);

        assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }

    @Test
    public void testFindForCollection_shouldReturnStdDelegatingDeserializerForSingletonList() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);

        JsonDeserializer<?> result = deserializers.findForCollection(ctxt, type);

        assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }

    @Test
    public void testFindForCollection_shouldReturnStdDelegatingDeserializerForSingletonSet() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);

        JsonDeserializer<?> result = deserializers.findForCollection(ctxt, type);

        assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }

    @Test
    public void testFindForCollection_shouldReturnStdDelegatingDeserializerForUnmodifiableList() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);

        JsonDeserializer<?> result = deserializers.findForCollection(ctxt, type);

        assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }

    @Test
    public void testFindForCollection_shouldReturnStdDelegatingDeserializerForUnmodifiableSet() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);

        JsonDeserializer<?> result = deserializers.findForCollection(ctxt, type);

        assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }

    @Test
    public void testFindForMap_shouldReturnNullForUnknownType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        JsonDeserializer<?> result = deserializers.findForMap(ctxt, type);

        assertNull(result);
    }

    @Test
    public void testFindForMap_shouldReturnStdDelegatingDeserializerForSingletonMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);

        JsonDeserializer<?> result = deserializers.findForMap(ctxt, type);

        assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }

    @Test
    public void testFindForMap_shouldReturnStdDelegatingDeserializerForUnmodifiableMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);

        JsonDeserializer<?> result = deserializers.findForMap(ctxt, type);

        assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }

    @Test
    public void testConverter_shouldReturnJavaUtilCollectionsConverter() {
        int kind = 1;
        JavaType concreteType = null;
        Class<?> rawSuper = null;

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter result = deserializers.converter(kind, concreteType, rawSuper);

        assertNotNull(result);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, result.getClass());
    }
}
