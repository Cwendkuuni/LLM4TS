import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        // Typical use case
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);

        // Edge case: type has raw class as Arrays.asList class
        type = new JavaType(CLASS_AS_ARRAYS_LIST, null);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());

        // Edge case: type has raw class as Singleton List class
        type = new JavaType(CLASS_SINGLETON_LIST, null);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());

        // Edge case: type has raw class as Singleton Set class
        type = new JavaType(CLASS_SINGLETON_SET, null);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());

        // Edge case: type has raw class as Unmodifiable List class
        type = new JavaType(CLASS_UNMODIFIABLE_LIST, null);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());

        // Edge case: type has raw class as Unmodifiable Set class
        type = new JavaType(CLASS_UNMODIFIABLE_SET, null);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        // Typical use case
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = null;
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(deserializer);

        // Edge case: type has raw class as Singleton Map class
        type = new JavaType(CLASS_SINGLETON_MAP, null);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());

        // Edge case: type has raw class as Unmodifiable Map class
        type = new JavaType(CLASS_UNMODIFIABLE_MAP, null);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testConverter() {
        // Typical use case
        int kind = 1;
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        assertNotNull(converter);

        // Edge case: input type is null
        kind = 2;
        converter = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        assertNotNull(converter);

        // Edge case: rawSuper is null
        kind = 3;
        concreteType = null;
        converter = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        assertNotNull(converter);
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        // Typical use case
        int kind = 1;
        JavaType inputType = null;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, inputType);
        assertNotNull(converter);

        // Edge case: value is null
        Object value = null;
        Object convertedValue = converter.convert(value);
        assertNull(convertedValue);

        // Edge case: kind is TYPE_SINGLETON_SET
        kind = 2;
        converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, inputType);
        assertNotNull(converter);
    }
}