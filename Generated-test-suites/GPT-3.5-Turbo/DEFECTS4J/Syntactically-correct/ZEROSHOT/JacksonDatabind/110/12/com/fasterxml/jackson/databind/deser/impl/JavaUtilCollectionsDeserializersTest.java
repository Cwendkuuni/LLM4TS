import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result);

        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);

        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);

        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);

        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);

        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);
    }

    @Test
    public void testFindForMap() {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);

        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
        result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer<?>);
    }

    @Test
    public void testConverter() {
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JavaType concreteType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, concreteType, List.class);
        assertNotNull(converter);

        Object value = new ArrayList<>();
        Object convertedValue = converter.convert(value);
        assertEquals(value, convertedValue);

        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType inputType = converter.getInputType(typeFactory);
        assertEquals(concreteType, inputType);

        JavaType outputType = converter.getOutputType(typeFactory);
        assertEquals(concreteType, outputType);
    }

}