
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUtilCollectionsDeserializersTest {

    private static class DummyDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws java.io.IOException, com.fasterxml.jackson.core.JsonProcessingException {
            return null;
        }
    }

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test for CLASS_AS_ARRAYS_LIST
        type = TypeFactory.defaultInstance().constructType(Arrays.asList(null, null).getClass());
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, result.getClass());
        
        // Test for CLASS_SINGLETON_LIST
        type = TypeFactory.defaultInstance().constructType(Collections.singletonList(Boolean.TRUE).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, result.getClass());
        
        // Test for CLASS_SINGLETON_SET
        type = TypeFactory.defaultInstance().constructType(Collections.singleton(Boolean.TRUE).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, result.getClass());
        
        // Test for CLASS_UNMODIFIABLE_LIST
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, result.getClass());
        
        // Test for CLASS_UNMODIFIABLE_SET
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test for CLASS_SINGLETON_MAP
        type = TypeFactory.defaultInstance().constructType(Collections.singletonMap("a", "b").getClass());
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, result.getClass());

        // Test for CLASS_UNMODIFIABLE_MAP
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());
        result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, result.getClass());
    }
}
