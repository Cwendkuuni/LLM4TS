package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectMapper objectMapper;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    private ObjectIdGenerator<?> idGeneratorMock;
    private ObjectIdWriter idWriterMock;
    private AnnotationIntrospector introspectorMock;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
        idGeneratorMock = mock(ObjectIdGenerator.class);
        idWriterMock = mock(ObjectIdWriter.class);
        introspectorMock = mock(AnnotationIntrospector.class);
    }

    @Test
    public void testWriteAsId() throws IOException {
        Object pojo = new Object() {};

        when(idGeneratorMock.generateId(pojo)).thenReturn("id-1");
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

        WritableObjectId writableObjectId = new WritableObjectId(idGeneratorMock);

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> {
            writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, idWriterMock);
        });

        assertNotNull(exception.getCause());
        assertEquals("ObjectId generator returned non-null id, but it was not written: id-1", exception.getCause().getMessage());

        when(idGeneratorMock.generateId(pojo)).thenReturn(null);
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(true);

        writableObjectId.id = "id-2";
        writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, idWriterMock);

        verify(jsonGeneratorMock).writeObjectRef(eq("id-2"));
    }

    @Test
    public void testWriteAsId_NoNativeObjectIdSupport() throws IOException {
        Object pojo = new Object() {};

        when(idGeneratorMock.generateId(pojo)).thenReturn("id-1");
        when(jsonGeneratorMock.canWriteObjectId()).thenReturn(false);

        WritableObjectId writableObjectId = new WritableObjectId(idGeneratorMock);

        SerializableString nameMock = mock(SerializableString.class);
        when(idWriterMock.propertyName).thenReturn(nameMock);

        StdSerializer<Object> serializerMock = mock(StdSerializer.class);
        when(idWriterMock.serializer).thenReturn(serializerMock);

        TypeSerializer typeSerializerMock = mock(TypeSerializer.class);

        when(introspectorMock.findSerializedName(eq(new AnnotatedClass(pojo.getClass())), eq(new AnnotatedField(pojo.getClass(), "id"))))
                .thenReturn(nameMock);
        when(introspectorMock.findValueSerializer(eq(new AnnotatedField(pojo.getClass(), "id")), eq(objectMapper.getTypeFactory()))).thenReturn(serializerMock);
        when(introspectorMock.findSerializer(eq(new AnnotatedClass(pojo.getClass())), eq(Object.class), any())).thenReturn(typeSerializerMock);

        writableObjectId.writeAsId(jsonGeneratorMock, serializerProviderMock, idWriterMock);

        verify(jsonGeneratorMock).writeFieldName(eq(nameMock));
        verify(serializerMock).serialize(eq("id-1"), eq(jsonGeneratorMock), eq(serializerProviderMock));
    }

    @Test
    public void testGenerateId() {
        Object pojo = new Object() {};

        when(idGeneratorMock.generateId(pojo)).thenReturn("id-1");

        WritableObjectId writableObjectId = new WritableObjectId(idGeneratorMock);
        Object id = writableObjectId.generateId(pojo);

        assertEquals("id-1", id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        Object pojo = new Object() {};

        when(idGeneratorMock.generateId(pojo)).thenReturn("id-1");

        WritableObjectId writableObjectId = new WritableObjectId(idGeneratorMock);
        writableObjectId.id = "id-2";

        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);

        when(idGeneratorMock.generateId(pojo)).thenReturn(null);
        when(idWriterMock.alwaysAsId).thenReturn(true);
        when(idWriterMock.propertyName).thenReturn(new SerializableString("id"));

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> {
            writableObjectId.writeAsField(genMock, providerMock, idWriterMock);
        });

        assertNotNull(exception.getCause());
        assertEquals("Cannot generate ObjectId for null", exception.getCause().getMessage());

        when(idWriterMock.propertyName).thenReturn(new SerializableString("id"));
        when(idWriterMock.serializer).thenReturn(new ToStringSerializer(Object.class));

        writableObjectId.writeAsField(jsonGeneratorMock, serializerProviderMock, idWriterMock);

        verify(jsonGeneratorMock).writeFieldName(eq(new SerializableString("id")));
        verify(idWriterMock.serializer).serialize(eq("id-1"), eq(jsonGeneratorMock), eq(serializerProviderMock));
    }
}