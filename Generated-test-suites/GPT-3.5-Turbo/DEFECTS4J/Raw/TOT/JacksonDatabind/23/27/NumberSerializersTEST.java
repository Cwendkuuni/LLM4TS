import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
        // Check if the proper serializer instances are added
        assertEquals(NumberSerializers.ShortSerializer.instance, allDeserializers.get(Short.class.getName()));
        assertEquals(NumberSerializers.IntegerSerializer.instance, allDeserializers.get(Integer.class.getName()));
        assertEquals(NumberSerializers.LongSerializer.instance, allDeserializers.get(Long.class.getName()));
        assertEquals(NumberSerializers.IntLikeSerializer.instance, allDeserializers.get(Byte.class.getName()));
        assertEquals(NumberSerializers.ShortSerializer.instance, allDeserializers.get(Short.class.getName()));
        assertEquals(NumberSerializers.FloatSerializer.instance, allDeserializers.get(Float.class.getName()));
        assertEquals(NumberSerializers.DoubleSerializer.instance, allDeserializers.get(Double.class.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 123;

        // Test the serialize method
        JsonGenerator gen = null; // we don't need a real JsonGenerator for this test
        SerializerProvider provider = null; // we don't need a real SerializerProvider for this test
        serializer.serialize(value, gen, provider);

        // No assertion made, just checking if the method is working without any exceptions
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        Integer value = 123;

        // Test the serialize method
        JsonGenerator gen = null; // we don't need a real JsonGenerator for this test
        SerializerProvider provider = null; // we don't need a real SerializerProvider for this test
        serializer.serialize(value, gen, provider);

        // No assertion made, just checking if the method is working without any exceptions
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Byte value = 123;

        // Test the serialize method
        JsonGenerator gen = null; // we don't need a real JsonGenerator for this test
        SerializerProvider provider = null; // we don't need a real SerializerProvider for this test
        serializer.serialize(value, gen, provider);

        // No assertion made, just checking if the method is working without any exceptions
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();
        Long value = 123L;

        // Test the serialize method
        JsonGenerator gen = null; // we don't need a real JsonGenerator for this test
        SerializerProvider provider = null; // we don't need a real SerializerProvider for this test
        serializer.serialize(value, gen, provider);

        // No assertion made, just checking if the method is working without any exceptions
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = 123.45f;

        // Test the serialize method
        JsonGenerator gen = null; // we don't need a real JsonGenerator for this test
        SerializerProvider provider = null; // we don't need a real SerializerProvider for this test
        serializer.serialize(value, gen, provider);

        // No assertion made, just checking if the method is working without any exceptions
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
        Double value = 123.45;

        // Test the serialize method
        JsonGenerator gen = null; // we don't need a real JsonGenerator for this test
        SerializerProvider provider = null; // we don't need a real SerializerProvider for this test
        serializer.serialize(value, gen, provider);

        // No assertion made, just checking if the method is working without any exceptions
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();

        SerializerProvider provider = null; // we don't need a real SerializerProvider for this test
        JavaType typeHint = null; // we don't need a real JavaType for this test
        JsonNode schema = serializer.getSchema(provider, typeHint);

        // No assertion made, just checking if the method is working without any exceptions
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();

        JsonFormatVisitorWrapper visitor = null; // we don't need a real JsonFormatVisitorWrapper for this test
        JavaType typeHint = null; // we don't need a real JavaType for this test
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // No assertion made, just checking if the method is working without any exceptions
    }

    @Test
    public void testBaseCreateContextualWithNullProperty() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        SerializerProvider prov = null; // we don't need a real SerializerProvider for this test
        BeanProperty property = null; // we don't need a real BeanProperty for this test

        JsonSerializer<?> contextualSerializer = serializer.createContextual(prov, property);

        assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testBaseCreateContextualWithNonNullPropertyWithoutFormatAnnotation() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        SerializerProvider prov = null; // we don't need a real SerializerProvider for this test
        // Creating a dummy BeanProperty with null member since we don't need a real one for this test
        BeanProperty property = new BeanProperty() {
			@Override
			public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> arg0) {
				return null;
			}
			@Override
			public AnnotatedMember getMember() {
				return null;
			}
			@Override
			public String getName() {
				return null;
			}
			@Override
			public boolean isRequired() {
				return false;
			}
			@Override
			public Class<?> getType() {
				return null;
			}
        };

        JsonSerializer<?> contextualSerializer = serializer.createContextual(prov, property);

        assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testBaseCreateContextualWithNonNullPropertyWithFormatAnnotation() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        SerializerProvider prov = null; // we don't need a real SerializerProvider for this test
        BeanProperty property = new BeanProperty() {
			@Override
			public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> arg0) {
				// simulating a format annotation with shape STRING
				return (A) new com.fasterxml.jackson.annotation.JsonFormat.Value().withShape(com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING);
			}
			@Override
			public AnnotatedMember getMember() {
				return null;
			}
			@Override
			public String getName() {
				return null;
			}
			@Override
			public boolean isRequired() {
				return false;
			}
			@Override
			public Class<?> getType() {
				return null;
			}
        };

        JsonSerializer<?> contextualSerializer = serializer.createContextual(prov, property);

        assertEquals(NumberSerializers.ToStringSerializer.instance, contextualSerializer);
    }

}