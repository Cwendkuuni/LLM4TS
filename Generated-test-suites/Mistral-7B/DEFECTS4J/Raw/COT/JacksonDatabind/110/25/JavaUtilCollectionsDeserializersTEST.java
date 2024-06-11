import com.fasterxml.jackson.core.base.TypeBase;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(TypeBase.getDefaultInstance(), new StandardDeserializer());
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StdDelegatingDeserializer);

        List list = Arrays.asList(Boolean.TRUE);
        Object deserialized = deserializer.deserialize(context, list, Boolean[].class);
        Assert.assertInstanceOf(List.class, deserialized);
        Assert.assertEquals(Boolean.TRUE, ((List) deserialized).get(0));
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(TypeBase.getDefaultInstance(), new StandardDeserializer());
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StdDelegatingDeserializer);

        Set set = Collections.singleton(Boolean.TRUE);
        Object deserialized = deserializer.deserialize(context, set, Boolean.class);
        Assert.assertInstanceOf(Set.class, deserialized);
        Assert.assertEquals(Boolean.TRUE, deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(TypeBase.getDefaultInstance(), new StandardDeserializer());
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StdDelegatingDeserializer);

        List list = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
        Object deserialized = deserializer.deserialize(context, list, Boolean[].class);
        Assert.assertInstanceOf(List.class, deserialized);
        Assert.assertEquals(Boolean.TRUE, ((List) deserialized).get(0));
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(TypeBase.getDefaultInstance(), new StandardDeserializer());
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StdDelegatingDeserializer);

        Set set = Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE));
        Object deserialized = deserializer.deserialize(context, set, Boolean.class);
        Assert.assertInstanceOf(Set.class, deserialized);
        Assert.assertEquals(Boolean.TRUE, deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_InvalidType() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(TypeBase.getDefaultInstance(), new StandardDeserializer());
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
        Assert.assertNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(TypeBase.getDefaultInstance(), new StandardDeserializer());
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StdDelegatingDeserializer);

        Map map = Collections.singletonMap("key", 1);
        Object deserialized = deserializer.deserialize(context, map, Map.class);
        Assert.assertInstanceOf(Map.class, deserialized);
        Assert.assertEquals(1, ((Map) deserialized).get("key"));
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(TypeBase.getDefaultInstance(), new StandardDeserializer());
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
        Assert.assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StdDelegatingDeserializer);

        Map map = Collections.unmodifiableMap(Collections.singletonMap("key", 1));
        Object deserialized = deserializer.deserialize(context, map, Map.class);
        Assert.assertInstanceOf(Map.class, deserialized);
        Assert.assertEquals(1, ((Map) deserialized).get("key"));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMap_InvalidType() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(TypeBase.getDefaultInstance(), new StandardDeserializer());
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
        Assert.assertNull(deserializer);
    }

    @Test
    public void testConverver_SingletonSet() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType, Set.class);
        Assert.assertNotNull(converter);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, converter.getKind());
        Assert.assertEquals(javaType, converter.getInputType(null));
        Assert.assertEquals(javaType, converter.getOutputType(null));
    }

    @Test
    public void testConverter_SingletonList() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaType, List.class);
        Assert.assertNotNull(converter);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, converter.getKind());
        Assert.assertEquals(javaType, converter.getInputType(null));
        Assert.assertEquals(javaType, converter.getOutputType(null));
    }

    @Test
    public void testConverter_UnmodifiableSet() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, javaType, Set.class);
        Assert.assertNotNull(converter);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, converter.getKind());
        Assert.assertEquals(javaType, converter.getInputType(null));
        Assert.assertEquals(javaType, converter.getOutputType(null));
    }

    @Test
    public void testConverter_UnmodifiableList() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, javaType, List.class);
        Assert.assertNotNull(converter);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, converter.getKind());
        Assert.assertEquals(javaType, converter.getInputType(null));
        Assert.assertEquals(javaType, converter.getOutputType(null));
    }

    @Test
    public void testConverter_AsList() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, javaType, List.class);
        Assert.assertNotNull(converter);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, converter.getKind());
        Assert.assertEquals(javaType, converter.getInputType(null));
        Assert.assertEquals(javaType, converter.getOutputType(null));
    }
}