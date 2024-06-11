import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext context;

    @Before
    public void setUp() {
        context = new DeserializationContext(null, null, null);
    }

    @Test
    public void testFindForCollection_AsArraysList() throws JsonMappingException {
        JavaType type = context.constructType(Arrays.asList(null, null).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        List<?> list = Collections.singletonList(Boolean.TRUE);
        JavaType type = context.constructType(list.getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        List<?> set = Collections.singleton(Boolean.TRUE);
        JavaType type = context.constructType(set.getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        List<?> list = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE));
        JavaType type = context.constructType(list.getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        List<?> set = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
        JavaType type = context.constructType(set.getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_InvalidType() throws JsonMappingException {
        List<?> list = Collections.emptySet();
        JavaType type = context.constructType(list.getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        Assert.assertNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        Map<?, ?> map = Collections.singletonMap("a", "b");
        JavaType type = context.constructType(map.getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        Map<?, ?> map = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
        JavaType type = context.constructType(map.getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_InvalidType() throws JsonMappingException {
        Map<?, ?> map = Collections.emptySet();
        JavaType type = context.constructType(map.getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
        Assert.assertNull(deserializer);
    }
}