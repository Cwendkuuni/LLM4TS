package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private ObjectIdGenerator<?> idGenerator;
    private ObjectIdWriter idWriter;
    private SerializerProvider provider;
    private StdSerializer<Object> objectSerializer;
    private JsonGenerator jsonGenerator;
    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        idGenerator = mock(ObjectIdGenerator.class);
        idWriter = mock(ObjectIdWriter.class);
        provider = mock(SerializerProvider.class);
        objectSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(String.valueOf(value));
            }
        };
        jsonGenerator = mock(JsonGenerator.class);
        when(provider.findValueSerializer(any(AnnotatedClass.class), any(TypeSerializer.class)))
                .thenReturn(objectSerializer);
        when(jsonGenerator.canWriteObjectId()).thenReturn(true);
        writableObjectId = new WritableObjectId(idGenerator);
    }

    @Test
    public void testWriteAsIdWithNoIdAndNoAlwaysAsId() throws IOException {
        when(idGenerator.generateId(any())).thenReturn(null);

        assertFalse(writableObjectId.writeAsId(jsonGenerator, provider, idWriter));
    }

    @Test
    public void testWriteAsIdWithIdAndNotAlwaysAsId() throws IOException {
        Object id = new Object();
        when(idGenerator.generateId(any())).thenReturn(id);

        assertTrue(writableObjectId.writeAsId(jsonGenerator, provider, idWriter));
        verify(idWriter, verifyNoMoreInteractions()).alwaysAsId = true;
        verify(jsonGenerator).writeObjectRef(String.valueOf(id));
    }

    @Test
    public void testWriteAsIdWithIdAndAlwaysAsId() throws IOException {
        Object id = new Object();
        when(idGenerator.generateId(any())).thenReturn(id);

        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer idSerializer = mapper.getTypeSerializer();

        idWriter = new ObjectIdWriter(mapper) {
            @Override
            public void serializeIds(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter.SerializedName idName,
                    ObjectWriter objectWriter) throws IOException {
                this.alwaysAsId = true;
                gen.writeFieldName("id");
                idSerializer.serialize(id, gen, provider);
            }
        };

        assertTrue(writableObjectId.writeAsId(jsonGenerator, provider, idWriter));
        verify(idWriter).alwaysAsId = true;
        verify(jsonGenerator).writeFieldName("id");
        verify(jsonGenerator).writeObjectId(String.valueOf(id));
    }

    @Test
    public void testGenerateId() throws JsonMappingException {
        Object forPojo = new Object();
        Object id = new Object();
        when(idGenerator.generateId(forPojo)).thenReturn(id);

        assertEquals(id, writableObjectId.generateId(forPojo));
    }

    @Test
    public void testWriteAsFieldWithNoIdAndNoAlwaysAsId() throws IOException {
        when(idGenerator.generateId(any())).thenReturn(null);

        writableObjectId.writeAsField(jsonGenerator, provider, idWriter);
        verify(jsonGenerator, verifyNoInteractions()).writeFieldName(anyString());
        verify(jsonGenerator, verifyNoInteractions()).writeObjectId(anyString());
        verify(jsonGenerator, verifyNoInteractions()).writeString(anyString());
    }

    @Test
    public void testWriteAsFieldWithIdAndNotAlwaysAsId() throws IOException {
        Object id = new Object();
        when(idGenerator.generateId(any())).thenReturn(id);

        verify(jsonGenerator, verifyNoInteractions()).writeFieldName(anyString());
        verify(jsonGenerator, verifyNoInteractions()).writeObjectId(String.valueOf(id));

        writableObjectId.writeAsField(jsonGenerator, provider, idWriter);
        idWritten = writableObjectId.idWritten;
        assertTrue(idWritten);
    }

    @Test
    public void testWriteAsFieldWithIdAndAlwaysAsId() throws IOException {
        Object id = new Object();
        when(idGenerator.generateId(any())).thenReturn(id);

        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer idSerializer = mapper.getTypeSerializer();

        idWriter = new ObjectIdWriter(mapper) {
            @Override
            public void serializeIds(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter.SerializedName idName,
                    ObjectWriter objectWriter) throws IOException {
                this.alwaysAsId = true;
                gen.writeFieldName("id");
                idSerializer.serialize(id, gen, provider);
            }
        };

        verify(jsonGenerator, verifyNoInteractions()).writeFieldName("id");
        verify(jsonGenerator, verifyNoInteractions()).writeObjectId(String.valueOf(id));

        writableObjectId.writeAsField(jsonGenerator, provider, idWriter);
        idWritten = writableObjectId.idWritten;
        assertTrue(idWritten);
    }

    @Test
    public void testWriteAsFieldWithNullId() throws IOException {
        when(idGenerator.generateId(any())).thenReturn(null);

        writableObjectId = new WritableObjectId(idGenerator) {
            @Override
            public void writeAsField(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter w) throws IOException {
                super.writeAsField(gen, provider, w);
                idWritten = true;
            }
        };

        writableObjectId.writeAsField(jsonGenerator, provider, idWriter);
        assertFalse(idWritten);
    }
}