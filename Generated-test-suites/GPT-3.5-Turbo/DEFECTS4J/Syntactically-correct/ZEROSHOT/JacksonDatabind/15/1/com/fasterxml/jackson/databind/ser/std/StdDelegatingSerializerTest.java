import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    public class TestConverter implements Converter<Object, Object> {
        @Override
        public Object convert(Object value) {
            return null;
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return null;
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return null;
        }
    }

    class TestDelegateSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {

        }
    }

    public class TestStdDelegatingSerializer extends StdDelegatingSerializer {

        /**
         * Fully resolved delegate type, with generic information if any available.
         */
        protected JavaType _delegateType;

        /**
         * Underlying serializer for type <code>T<.code>.
         */
        protected final JsonSerializer<Object> _delegateSerializer;

        public TestStdDelegatingSerializer(Converter<?, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
            super(converter, delegateType, delegateSerializer);
            _delegateType = delegateType;
            _delegateSerializer = (JsonSerializer<Object>) delegateSerializer;
        }

        public void setDelegateType(JavaType delegateType) {
            _delegateType = delegateType;
        }

        public JavaType getDelegateType() {
            return _delegateType;
        }

        public JsonSerializer<Object> getDelegateSerializer() {
            return _delegateSerializer;
        }

        protected TestStdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {

            return new TestStdDelegatingSerializer(_converter, delegateType, delegateSerializer);
        }

        @Override
        public void resolve(SerializerProvider provider) throws JsonMappingException {

        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
                throws JsonMappingException {
            return null;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                                      TypeSerializer typeSer) throws IOException {

        }

        @Override
        public boolean isEmpty(SerializerProvider prov, Object value) {
            return false;
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                throws JsonMappingException {
            return null;
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                throws JsonMappingException {

        }
    }

    public void testSerialize() throws IOException {
        TestConverter converter = new TestConverter();
        JavaType delegateType = null;
        TestDelegateSerializer delegateSerializer = new TestDelegateSerializer();
        TestStdDelegatingSerializer serializer = new TestStdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;

        serializer.serialize(value, gen, provider);
    }

    public void testSerializeWithType() throws IOException {
        TestConverter converter = new TestConverter();
        JavaType delegateType = null;
        TestDelegateSerializer delegateSerializer = new TestDelegateSerializer();
        TestStdDelegatingSerializer serializer = new TestStdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer = null;

        serializer.serializeWithType(value, gen, provider, typeSer);
    }

    public void testIsEmptyWithDefaultSerializeNull() throws IOException {
        TestConverter converter = new TestConverter();
        JavaType delegateType = null;
        TestDelegateSerializer delegateSerializer = new TestDelegateSerializer();
        TestStdDelegatingSerializer serializer = new TestStdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;

        serializer.serialize(value, gen, provider);
    }

    public void testIsEmpty() throws IOException {
        TestConverter converter = new TestConverter();
        JavaType delegateType = null;
        TestDelegateSerializer delegateSerializer = new TestDelegateSerializer();
        TestStdDelegatingSerializer serializer = new TestStdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;

        serializer.serialize(value, gen, provider);
    }

    public void testGetSchema() throws JsonMappingException {
        TestConverter converter = new TestConverter();
        JavaType delegateType = null;
        TestDelegateSerializer delegateSerializer = new TestDelegateSerializer();
        TestStdDelegatingSerializer serializer = new TestStdDelegatingSerializer(converter, delegateType, delegateSerializer);

        SerializerProvider provider = null;
        Type typeHint = null;

        serializer.getSchema(provider, typeHint);
    }

    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        TestConverter converter = new TestConverter();
        JavaType delegateType = null;
        TestDelegateSerializer delegateSerializer = new TestDelegateSerializer();
        TestStdDelegatingSerializer serializer = new TestStdDelegatingSerializer(converter, delegateType, delegateSerializer);

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}