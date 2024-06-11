import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Test;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(JavaType.nullType(), StringDeserializer.instance, ValueInstantiator.instance);
        List<String> expected = Arrays.asList("one", "two", "three");
        JsonFactory factory = new JsonFactory(new ObjectMapper());
        JsonParser parser = factory.createParser("[\"one\",\"two\",\"three\"]");
        DeserializationContext ctxt = factory.getCodec().getDeserializationContext();
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(JavaType.nullType(), StringDeserializer.instance, ValueInstantiator.instance);
        List<String> expected = Arrays.asList("one", null, "three");
        JsonFactory factory = new JsonFactory(new ObjectMapper());
        JsonParser parser = factory.createParser("[\"one\",null,\"three\"]");
        DeserializationContext ctxt = factory.getCodec().getDeserializationContext();
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(JavaType.nullType(), StringDeserializer.instance, ValueInstantiator.instance);
        List<String> expected = new ArrayList<>();
        JsonFactory factory = new JsonFactory(new ObjectMapper());
        JsonParser parser = factory.createParser("[]");
        DeserializationContext ctxt = factory.getCodec().getDeserializationContext();
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithSingleValueNotInArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(JavaType.nullType(), StringDeserializer.instance, ValueInstantiator.instance);
        List<String> expected = Arrays.asList("one");
        JsonFactory factory = new JsonFactory(new ObjectMapper());
        JsonParser parser = factory.createParser("\"one\"");
        DeserializationContext ctxt = factory.getCodec().getDeserializationContext();
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(JavaType.nullType(), StringDeserializer.instance, ValueInstantiator.instance);
        List<String> expected = Arrays.asList("one", "two", "three");
        JsonFactory factory = new JsonFactory(new ObjectMapper());
        JsonParser parser = factory.createParser("[\"one\",\"two\",\"three\"]");
        DeserializationContext ctxt = factory.getCodec().getDeserializationContext();
        TypeDeserializer typeDeserializer = factory.getCodec().findTypeDeserializer(factory.getCodec().getConfig(), JavaType.nullType());
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        Assert.assertEquals(expected, result);
    }

}