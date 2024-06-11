import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_AsList() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testConverter_TypeSingletonSet() {
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, concreteType, rawSuper);
        Object value = null;
        Object result = converter.convert(value);
        assertNull(result);
    }

    @Test
    public void testConverter_TypeSingletonList() {
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, concreteType, rawSuper);
        Object value = null;
        Object result = converter.convert(value);
        assertNull(result);
    }

    @Test
    public void testConverter_TypeSingletonMap() {
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, concreteType, rawSuper);
        Object value = null;
        Object result = converter.convert(value);
        assertNull(result);
    }

    @Test
    public void testConverter_TypeUnmodifiableSet() {
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, concreteType, rawSuper);
        Object value = null;
        Object result = converter.convert(value);
        assertNull(result);
    }

    @Test
    public void testConverter_TypeUnmodifiableList() {
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, concreteType, rawSuper);
        Object value = null;
        Object result = converter.convert(value);
        assertNull(result);
    }

    @Test
    public void testConverter_TypeUnmodifiableMap() {
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, concreteType, rawSuper);
        Object value = null;
        Object result = converter.convert(value);
        assertNull(result);
    }

    @Test
    public void testConverter_TypeAsList() {
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, concreteType, rawSuper);
        Object value = null;
        Object result = converter.convert(value);
        assertNull(result);
    }

    @Test
    public void testCheckSingleton_Success() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        int size = 1;
        converter._checkSingleton(size); // no exception thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSingleton_Failure() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        int size = 0;
        converter._checkSingleton(size);
    }
}