import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        // given
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();

        // when
        anyGetterWriter.getAndSerialize(null, gen, provider);

        // then
        // No assertion, this method does not have any return value
    }

    @Test
    public void testGetAndSerialize_withNonNullMap() throws Exception {
        // given
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        Map<String, Integer> map = new HashMap<>();
        map.put("key", 1);

        // when
        anyGetterWriter.getAndSerialize(map, gen, provider);

        // then
        // No assertion, this method does not have any return value
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_withNonMap() throws Exception {
        // given
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        String nonMap = "nonMap";

        // when
        anyGetterWriter.getAndSerialize(nonMap, gen, provider);

        // then
        // expect JsonMappingException to be thrown
    }

    @Test
    public void testGetAndFilter() throws Exception {
        // given
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();

        // when
        anyGetterWriter.getAndFilter(null, gen, provider, filter);

        // then
        // No assertion, this method does not have any return value
    }

    @Test
    public void testGetAndFilter_withNonNullMap() throws Exception {
        // given
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        Map<String, Integer> map = new HashMap<>();
        map.put("key", 1);

        // when
        anyGetterWriter.getAndFilter(map, gen, provider, filter);

        // then
        // No assertion, this method does not have any return value
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_withNonMap() throws Exception {
        // given
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        String nonMap = "nonMap";

        // when
        anyGetterWriter.getAndFilter(nonMap, gen, provider, filter);

        // then
        // expect JsonMappingException to be thrown
    }

    @Test
    public void testResolve() throws Exception {
        // given
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        SerializerProvider provider = new SerializerProvider();

        // when
        anyGetterWriter.resolve(provider);

        // then
        Assert.assertNotNull(anyGetterWriter._mapSerializer);
    }
}