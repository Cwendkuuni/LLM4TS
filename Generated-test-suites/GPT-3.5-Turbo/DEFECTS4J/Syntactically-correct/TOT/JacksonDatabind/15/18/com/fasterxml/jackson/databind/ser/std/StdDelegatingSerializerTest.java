package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.testutil.NoCheckSubTypeValidator;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Mock private Converter<Object, Object> converter;
    @Mock private JsonSerializer<Object> delegateSerializer;
    @Mock private JavaType delegateType;
    @Mock private JsonSerializer<?> jsonSerializer;
    @Mock private SerializerProvider provider;
    @Mock private BeanProperty property;
    @Mock private Object value;
    @Mock private JsonGenerator generator;
    @Mock private TypeSerializer typeSerializer;

    public StdDelegatingSerializerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void resolve_and_createContextual_with_delegateSerializer() throws JsonMappingException {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        delegatingSerializer.resolve(provider);
        verify(((ResolvableSerializer) delegateSerializer)).resolve(provider);

        StdDelegatingSerializer result = (StdDelegatingSerializer) delegatingSerializer.createContextual(provider, property);
        verify(provider).handleSecondaryContextualization(delegateSerializer, property);
    }

    @Test
    public void resolve_and_createContextual_without_delegateSerializer() throws JsonMappingException {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);

        delegatingSerializer.resolve(provider);
        verifyNoInteractions(delegateSerializer);

        when(provider.getTypeFactory()).thenReturn(mock(JavaType.class));
        when(provider.findValueSerializer(any(JavaType.class))).thenReturn(jsonSerializer);
        when(jsonSerializer instanceof ContextualSerializer).thenReturn(true);
        when(provider.handleSecondaryContextualization(jsonSerializer, property)).thenReturn(jsonSerializer);

        StdDelegatingSerializer result = (StdDelegatingSerializer) delegatingSerializer.createContextual(provider, property);
        verify(provider).handleSecondaryContextualization(jsonSerializer, property);
    }

    @Test
    public void serialize_with_null_delegateValue() throws IOException {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        when(converter.convert(any())).thenReturn(null);

        delegatingSerializer.serialize(value, generator, provider);
        verify(provider).defaultSerializeNull(generator);
        verifyNoInteractions(delegateSerializer);
    }

    @Test
    public void serialize_with_delegateValue() throws IOException {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object delegateValue = new Object();
        when(converter.convert(any())).thenReturn(delegateValue);

        delegatingSerializer.serialize(value, generator, provider);
        verify(delegateSerializer).serialize(delegateValue, generator, provider);
    }

    @Test
    public void serializeWithType() throws IOException {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object delegateValue = new Object();
        when(converter.convert(any())).thenReturn(delegateValue);

        delegatingSerializer.serializeWithType(value, generator, provider, typeSerializer);
        verify(delegateSerializer).serializeWithType(delegateValue, generator, provider, typeSerializer);
    }

    @Test
    public void isEmpty_with_null_delegateValue() {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        when(converter.convert(any())).thenReturn(null);

        delegatingSerializer.isEmpty(value);
        verify(delegateSerializer).isEmpty(null);
    }

    @Test
    public void isEmpty_with_delegateValue() {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object delegateValue = new Object();
        when(converter.convert(any())).thenReturn(delegateValue);

        delegatingSerializer.isEmpty(value);
        verify(delegateSerializer).isEmpty(delegateValue);
    }

    @Test
    public void isEmpty_withProvider_and_null_delegateValue() {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        when(converter.convert(any())).thenReturn(null);

        delegatingSerializer.isEmpty(provider, value);
        verify(delegateSerializer).isEmpty(provider, null);
    }

    @Test
    public void isEmpty_withProvider_and_delegateValue() {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object delegateValue = new Object();
        when(converter.convert(any())).thenReturn(delegateValue);

        delegatingSerializer.isEmpty(provider, value);
        verify(delegateSerializer).isEmpty(provider, delegateValue);
    }

    @Test
    public void getSchema_with_SchemaAware_delegateSerializer() throws JsonMappingException {
        SchemaAwareDelegateSerializer schemaAwareSerializer = new SchemaAwareDelegateSerializer();
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, schemaAwareSerializer);

        delegatingSerializer.getSchema(provider, delegateType);
        verify(schemaAwareSerializer).getSchema(provider, delegateType);
    }

    @Test
    public void getSchema_without_SchemaAware_delegateSerializer() throws JsonMappingException {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        delegatingSerializer.getSchema(provider, delegateType);
        verify(provider).findValueSerializer(delegateType);
    }

    @Test
    public void getSchema_with_isOptional() throws JsonMappingException {
        SchemaAwareDelegateSerializer schemaAwareSerializer = new SchemaAwareDelegateSerializer();
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, schemaAwareSerializer);

        boolean isOptional = true;
        delegatingSerializer.getSchema(provider, delegateType, isOptional);
        verify(schemaAwareSerializer).getSchema(provider, delegateType, isOptional);
    }

    @Test
    public void acceptJsonFormatVisitor() throws JsonMappingException {
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        delegatingSerializer.acceptJsonFormatVisitor(Mockito.any(), Mockito.any());
        verify(delegateSerializer).acceptJsonFormatVisitor(Mockito.any(), Mockito.any());
    }

    private static class Converter<T, R> extends StdConverter<T, R> {
        @Override
        public R convert(T value) {
            return null;
        }
    }

    private static class SchemaAwareDelegateSerializer extends JsonSerializer<Object> implements SchemaAware {
        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return null;
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional) {
            return null;
        }

    }

    @SuppressWarnings("rawtypes")
    private static class NonConstructableSerializerFactory extends SerializerFactory {
        @Override
        protected SerializerFactory withConfig(SerializerFactory.Config config) {
            return null;
        }

        @Override
        public JsonSerializer<Object> createKeySerializer(SerializerProvider ctxt, JavaType keyType,
                                                           JsonSerializer<Object> defaultImpl) {
            return null;
        }

        @Override
        public JsonSerializer<Object> createKeySerializer(SerializerProvider ctxt,
                                                           JavaType keyType,
                                                           JsonSerializer<Object> defaultImpl,
                                                           boolean useDefaultKeySerializers) {
            return null;
        }

        @Override
        public JsonSerializer<Object> createSerializer(SerializerProvider ctxt, JavaType valueType) {
            return null;
        }

        @Override
        public JsonSerializer<Object> createSerializer(SerializerProvider ctxt, JavaType valueType,
                                                           BeanProperty property) {
            return null;
        }

        @Override
        public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType type,
                                                           BeanProperty property, boolean staticTyping) {
            return null;
        }

        @Override
        public TypeSerializer createTypeSerializer(SerializationConfig config,
                                                          JavaType baseType) {
            return null;
        }

        @Override
        public JsonSerializer<Object> findKeySerializer(SerializerProvider ctxt,
                                                            JsonSerializer<Object> defaultImpl) {
            return null;
        }

        @Override
        public JsonSerializer<Object> findTypedValueSerializer(SerializationConfig config,
                                                                    JavaType valueType,
                                                                    boolean cache,
                                                                    BeanProperty property)
        {
            return null;
        }

        @Override
        public JsonSerializer<Object> findTypedValueSerializer(SerializationConfig config,
                                                                    JavaType valueType,
                                                                    boolean cache) {
            return null;
        }

        @Override
        public JsonSerializer<Object> findTypedValueSerializer(SerializationConfig config,
                                                                    Class<?> valueType,
                                                                    boolean cache,
                                                                    BeanProperty property)
        {
            return null;
        }

        @Override
        public JsonSerializer<Object> findTypedValueSerializer(SerializationConfig config,
                                                                    Class<?> valueType,
                                                                    boolean cache) {
            return null;
        }

        @Override
        public JsonSerializer<Object> getNullKeySerializer() {
            return null;
        }

        @Override
        public JsonSerializer<Object> getNullValueSerializer() {
            return null;
        }

        @Override
        public boolean hasKeySerializers() {
            return false;
        }
    }
}