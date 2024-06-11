package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.fasterxml.jackson.databind.JsonMapping.ValueInstantiator.ValueType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserializeEmpty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("[]");

        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializeSingle() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("['foo']");

        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testDeserializeMultiple() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("['foo', 'bar']");

        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.deserializerForClass(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null,
                null,
                stringDeserializer,
                null,
                null);
        JsonParser parser = mapper.getFactory().createParser("['foo', 'bar']");

        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> objectDeserializer = mapper.deserializerForClass(Object.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null,
                objectDeserializer,
                null,
                null,
                null);
        JsonParser parser = mapper.getFactory().createParser("['foo', 'bar']");

        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testDeserializeWithNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("[]");
        NullValueProvider nullValueProvider = mapper.getConfig().getValueFactory().getNullProvider();

        Collection<String> result = deserializer.deserialize(parser, null, nullValueProvider);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializeWithUnwrappingSingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("'foo'");

        Collection<String> result = deserializer.deserialize(parser, null, null, true);
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testDeserializeWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.deserializerForClass(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null,
                null,
                null,
                stringDeserializer,
                null);
        JsonParser parser = mapper.getFactory().createParser("['foo']");

        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testDeserializeWithValueDeserializerAndNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.deserializerForClass(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null,
                null,
                null,
                stringDeserializer,
                mapper.getFactory().getNullValueProvider());
        JsonParser parser = mapper.getFactory().createParser("['foo', null]");

        Collection<String> result = deserializer.deserialize(parser, null);
        Set<String> expected
                = new LinkedHashSet<>(Arrays.asList("foo", null));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithValueDeserializerAndFormatFeature() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.deserializerForClass(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null,
                null,
                null,
                stringDeserializer,
                null);
        ObjectMapper mapperWithFeature = new ObjectMapper();
        ObjectMapper.SerializerFinder serializerFinder = mapperWithFeature.getSerializerProvider().getFinder();
        AnnotatedClass ac = serializerFinder.findSerializable(String[].class);
        JsonSerializer<Object> arraySerializer = serializerFinder.findValueSerializer(ac, null);
        JsonDeserializer<Object> arrayDeserializer = mapperWithFeature.findDeserializer(Object[].class, null);
        JsonParser parser = mapperWithFeature.getFactory().createParser("['foo']");
        parser.setCodec(mapperWithFeature);
        parser.setSerializers(mapperWithFeature.getSerializerProvider());
        parser.setDeserializers(mapperWithFeature.getDeserializerProvider());
        parser.setContext(mapperWithFeature.getRootValueFactory());
        DeserializationContext ctxt = mapperWithFeature.getDeserializationContext();
        JsonDeserializer<String> customValueDeserializer = ContextualSerializer.instance.findValueDeserializer(
                String.class, mapperWithFeature, ctxt, null, null, null);
        JsonDeserializer<Object> customArrayDeserializer = ContextualSerializer.instance.findDeserializer(
                Object[].class, mapperWithFeature, ctxt, null, null, null);
        JsonDeserializer<Collection<String>> customCollectionDeserializer = new StringCollectionDeserializer(
                mapperWithFeature.constructType(Collection.class, String.class),
                null,
                null,
                null,
                customValueDeserializer,
                null);
        deserializer.setDelegateDeserializer(customArrayDeserializer);
        deserializer.setValueDeserializer(customValueDeserializer);
        deserializer.setContentDeserializer(customCollectionDeserializer);
        JsonDeserializer<Object> deserializerWithFeature = deserializer.createContextual(
                mapperWithFeature.getDeserializationContext(), null);
        Collection<String> result = deserializerWithFeature.deserialize(parser, null);
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testDeserializeWithDelegateDeserializerAndFormatFeature() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> objectDeserializer = mapper.deserializerForClass(Object.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.constructType(Collection.class, String.class),
                null,
                objectDeserializer,
                null,
                null,
                null);
        ObjectMapper mapperWithFeature = new ObjectMapper();
        ObjectMapper.SerializerFinder serializerFinder = mapperWithFeature.getSerializerProvider().getFinder();
        AnnotatedClass ac = serializerFinder.findSerializable(String[].class);
        JsonSerializer<Object> arraySerializer = serializerFinder.findValueSerializer(ac, null);
        JsonDeserializer<Object> arrayDeserializer = mapperWithFeature.findDeserializer(Object[].class, null);
        JsonParser parser = mapperWithFeature.getFactory().createParser("['foo']");
        parser.setCodec(mapperWithFeature);
        parser.setSerializers(mapperWithFeature.getSerializerProvider());
        parser.setDeserializers(mapperWithFeature.getDeserializerProvider());
        parser.setContext(mapperWithFeature.getRootValueFactory());
        DeserializationContext ctxt = mapperWithFeature.getDeserializationContext();
        JsonDeserializer<String> customValueDeserializer = ContextualSerializer.instance.findValueDeserializer(
                String.class, mapperWithFeature, ctxt, null, null, null);
        JsonDeserializer<Object> customArrayDeserializer = ContextualSerializer.instance.findDeserializer(
                Object[].class, mapperWithFeature, ctxt, null, null, null);
        JsonDeserializer<Collection<String>> customCollectionDeserializer = new StringCollectionDeserializer(
                mapperWithFeature.constructType(Collection.class, String.class),
                null,
                null,
                customValueDeserializer,
                null,
                null);
        JsonDeserializer<Object> delegateDeserializer = ContextualSerializer.instance.findDeserializer(
                Object.class, mapperWithFeature, ctxt, null, null, null);
        deserializer.setDelegateDeserializer(customArrayDeserializer);
        deserializer.setValueDeserializer(customValueDeserializer);
        deserializer.setContentDeserializer(customCollectionDeserializer);
        deserializer.setDelegateDeserializer(delegateDeserializer);
        JsonDeserializer<Object> deserializerWithFeature = deserializer.createContextual(
                mapperWithFeature.getDeserializationContext(), null);
        Collection<String> result = deserializerWithFeature.deserialize(parser, null);
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testDeserializeWithNullValueProviderAndFormatFeature() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        ObjectMapper mapperWithFeature = new ObjectMapper();
        ObjectMapper.SerializerFinder serializerFinder = mapperWithFeature.getSerializerProvider().getFinder();
        AnnotatedClass ac = serializerFinder.findSerializable(String[].class);
        JsonSerializer<Object> arraySerializer = serializerFinder.findValueSerializer(ac, null);
        JsonDeserializer<Object> arrayDeserializer = mapperWithFeature.findDeserializer(Object[].class, null);
        JsonParser parser = mapperWithFeature.getFactory().createParser("['foo']");
        parser.setCodec(mapperWithFeature);
        parser.setSerializers(mapperWithFeature.getSerializerProvider());
        parser.setDeserializers(mapperWithFeature.getDeserializerProvider());
        parser.setContext(mapperWithFeature.getRootValueFactory());
        DeserializationContext ctxt = mapperWithFeature.getDeserializationContext();
        JsonDeserializer<String> customValueDeserializer = ContextualSerializer.instance.findValueDeserializer(
                String.class, mapperWithFeature, ctxt, null, null, null);
        JsonDeserializer<Object> customArrayDeserializer = ContextualSerializer.instance.findDeserializer(
                Object[].class, mapperWithFeature, ctxt, null, null, null);
        JsonDeserializer<Collection<String>> customCollectionDeserializer = new StringCollectionDeserializer(
                mapperWithFeature.constructType(Collection.class, String.class),
                mapperWithFeature.getConfig().getValueFactory().getNullProvider(),
                null,
                customValueDeserializer,
                null,
                null);
        JsonDeserializer<Object> deserializerWithFeature = deserializer.createContextual(
                mapperWithFeature.getDeserializationContext(), null);
        deserializerWithFeature.setDelegateDeserializer(customArrayDeserializer);
        deserializerWithFeature.setValueDeserializer(customValueDeserializer);
        deserializerWithFeature.setContentDeserializer(customCollectionDeserializer);
        Collection<String> result = deserializerWithFeature.deserialize(parser, null);
        assertEquals(Arrays.asList("foo"), result);
    }
}