package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class AtomicReferenceDeserializerTest {

    @Mock
    private DeserializationContext deserializationContextMock;
    @Mock
    private JavaType javaTypeMock;
    @Mock
    private ValueInstantiator valueInstantiatorMock;
    @Mock
    private TypeDeserializer typeDeserializerMock;
    @Mock
    private JsonDeserializer<?> jsonDeserializerMock;
    
    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Test
    public void testGetNullValue_WhenCalled_ThenReturnNewAtomicReference() throws JsonMappingException {
        // Arrange
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock, typeDeserializerMock, jsonDeserializerMock);

        // Act
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(deserializationContextMock);

        // Assert
        assert(result).isInstanceOf(AtomicReference.class);
    }

    @Test
    public void testGetEmptyValue_WhenCalled_ThenReturnNewAtomicReference() {
        // Arrange
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock, typeDeserializerMock, jsonDeserializerMock);

        // Act
        AtomicReference<Object> result = atomicReferenceDeserializer.getEmptyValue(deserializationContextMock);

        // Assert
        assert(result).isInstanceOf(AtomicReference.class);
    }

    @Test
    public void testReferenceValue_WhenCalledWithContents_ThenReturnNewAtomicReferenceWithContents() {
        // Arrange
        Object contents = new Object();
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock, typeDeserializerMock, jsonDeserializerMock);

        // Act
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);

        // Assert
        assert(result).isInstanceOf(AtomicReference.class);
        assert(result.get()).isEqualTo(contents);
    }

    @Test
    public void testGetReferenced_WhenCalledWithAtomicReference_ThenReturnReferencedObject() {
        // Arrange
        Object referencedObject = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(referencedObject);
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock, typeDeserializerMock, jsonDeserializerMock);

        // Act
        Object result = atomicReferenceDeserializer.getReferenced(reference);

        // Assert
        assert(result).isEqualTo(referencedObject);
    }

    @Test
    public void testUpdateReference_WhenCalledWithAtomicReferenceAndContents_ThenUpdateReferenceAndReturnReference() {
        // Arrange
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock, typeDeserializerMock, jsonDeserializerMock);

        // Act
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);

        // Assert
        assert(result).isEqualTo(reference);
        assert(result.get()).isEqualTo(contents);
    }

    @Test
    public void testSupportsUpdate_WhenCalled_ThenReturnTrue() {
        // Arrange
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock, typeDeserializerMock, jsonDeserializerMock);
        DeserializationConfig configMock = Mockito.mock(DeserializationConfig.class);

        // Act
        Boolean result = atomicReferenceDeserializer.supportsUpdate(configMock);

        // Assert
        assert(result).isEqualTo(Boolean.TRUE);
    }
}