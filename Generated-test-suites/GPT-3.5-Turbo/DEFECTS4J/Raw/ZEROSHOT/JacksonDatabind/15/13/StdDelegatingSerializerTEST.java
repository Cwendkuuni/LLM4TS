package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize_WhenDelegateValueIsNull_ShouldDefaultSerializeNull() throws Exception {
        // Arrange
        StdDelegatingSerializer serializer = new StubStdDelegatingSerializer(null, null, null);
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(provider).defaultSerializeNull(gen);
    }

    @Test
    public void testSerialize_WhenDelegateValueIsNotNull_ShouldDelegateToDelegateSerializer() throws Exception {
        // Arrange
        Object delegateValue = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StubStdDelegatingSerializer(delegateValue, delegateSerializer);
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        verify(delegateSerializer).serialize(delegateValue, gen, provider);
    }

    @Test
    public void testSerializeWithType_ShouldDelegateToDelegateSerializer() throws Exception {
        // Arrange
        Object delegateValue = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StubStdDelegatingSerializer(delegateValue, delegateSerializer);
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        // Act
        serializer.serializeWithType(value, gen, provider, typeSer);

        // Assert
        verify(delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty_WhenDelegateValueIsNotNull_ShouldDelegateToDelegateSerializer() throws Exception {
        // Arrange
        Object delegateValue = new Object();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StubStdDelegatingSerializer(delegateValue, delegateSerializer);
        Object value = new Object();

        when(delegateSerializer.isEmpty(any(SerializerProvider.class), any(Object.class))).thenReturn(true);

        // Act
        boolean result = serializer.isEmpty(null, value);

        // Assert
        verify(delegateSerializer).isEmpty(null, delegateValue);
        assert(result);
    }

    @Test
    public void testIsEmpty_WhenDelegateValueIsNull_ShouldDelegateToDelegateSerializer() throws Exception {
        // Arrange
        StdDelegatingSerializer serializer = new StubStdDelegatingSerializer(null, null);
        Object value = new Object();

        when(delegateSerializer.isEmpty(any(SerializerProvider.class), any(Object.class))).thenReturn(true);

        // Act
        boolean result = serializer.isEmpty(null, value);

        // Assert
        verify(delegateSerializer).isEmpty(null, null);
        assert(result);
    }

    private class StubStdDelegatingSerializer extends StdDelegatingSerializer {
        private final Object delegateValue;
        private final JsonSerializer<Object> delegateSerializer;

        public StubStdDelegatingSerializer(Object delegateValue, JsonSerializer<Object> delegateSerializer) {
            super(null);
            this.delegateValue = delegateValue;
            this.delegateSerializer = delegateSerializer;
        }

        public StubStdDelegatingSerializer(Converter<Object,?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
            super(converter, delegateType, delegateSerializer);
            this.delegateValue = null;
            this.delegateSerializer = null;
        }

        @Override
        protected Object convertValue(Object value) {
            return delegateValue;
        }
        
        @Override
        public JsonSerializer<?> getDelegatee() {
            return delegateSerializer;
        }
    }
}