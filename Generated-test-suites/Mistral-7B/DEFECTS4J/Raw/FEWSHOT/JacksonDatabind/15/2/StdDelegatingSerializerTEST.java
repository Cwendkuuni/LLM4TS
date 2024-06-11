package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConvertValue() throws JsonMappingException, JsonGenerationException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, new IdentityConverter());
        Object sourceValue = new Object();
        Object delegateValue = serializer.convertValue(sourceValue);
        Assert.assertEquals(sourceValue, delegateValue);
    }

    @Test
    public void testSerializeObject() throws JsonMappingException, JsonGenerationException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, new IdentityConverter());
        Object sourceValue = new Object();
        String expectedJson = "{\"@class\":\"java.lang.Object\"}";

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(sourceValue, gen, new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> serializableType) {
                return new SimpleObjectSerializer();
            }
        });
        gen.flush();

        Assert.assertEquals(expectedJson, System.out.getBytes());
    }

    @Test
    public void testSerializeWithType() throws JsonMappingException, JsonGenerationException, IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, new IdentityConverter());
        Object sourceValue = new Object();
        Type sourceType = Object.class;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serializeWithType(sourceValue, gen, new SerializerProvider(), new TypeSerializer());
        gen.flush();

        Assert.assertEquals("{\"@class\":\"java.lang.Object\"}", System.out.getBytes());
    }

    @Test
    public void testResolve() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, new IdentityConverter());
        SerializerProvider provider = new SerializerProvider();
        serializer.resolve(provider);
        Assert.assertNotNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testCreateContextual() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, new IdentityConverter());
        SerializerProvider provider = new SerializerProvider();
        JsonSerializer<?> delegateSerializer = new SimpleObjectSerializer();

        JsonSerializer<?> createdSerializer = serializer.createContextual(provider, null);
        Assert.assertEquals(delegateSerializer, createdSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testSubclassWithoutWithDelegate() throws IOException {
        class TestSerializers extends StdDelegatingSerializer {
            @Override
            protected StdDelegatingSerializer withDelegate(Converter<Object,?> converter,
                    JavaType delegateType, JsonSerializer<?> delegateSerializer) {
                throw new IllegalStateException();
            }
        }

        TestSerializers serializer = new TestSerializers(Object.class, new IdentityConverter());
    }

    @Test
    public void testGetDelegateSerializedType() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, new IdentityConverter());
        Object sourceValue = new Object();
        Type sourceType = Object.class;

        JsonSerializer<?> delegateSerializer = new SimpleObjectSerializer();
        serializer = serializer.withDelegate(new IdentityConverter(), serializer.getDefaultType(), delegateSerializer);

        Type delegateType = serializer.getDelegateType();
        Assert.assertEquals(delegateSerializer.getDelegateType(), delegateType);
    }

    @Test
    public void testGetDelegateSerializer() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, new IdentityConverter());
        Object sourceValue = new Object();
        Type sourceType = Object.class;

        JsonSerializer<?> delegateSerializer = new SimpleObjectSerializer();
        serializer = serializer.withDelegate(new IdentityConverter(), serializer.getDefaultType(), delegateSerializer);

        JsonSerializer<?> delegatedSerializer = serializer.getDelegateSerializer();
        Assert.assertEquals(delegateSerializer, delegatedSerializer);
    }
}