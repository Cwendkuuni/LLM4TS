package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for WritableObjectId class.
 */
public class WritableObjectIdTest {

    private ObjectIdGenerator<Object> idGenerator;
    private ObjectIdWriter idWriter;
    private SerializerProvider provider;
    private JsonGenerator gen;
    private ObjectIdWriter alwaysAsIdWriter;

    @Before
    public void setUp() throws Exception {
        idGenerator = mock(ObjectIdGenerator.class);
        idWriter = new ObjectIdWriter(null, null, null, null, null);
        provider = mock(SerializerProvider.class);
        gen = mock(JsonGenerator.class);
        alwaysAsIdWriter = new ObjectIdWriter(null, null, idGenerator, null, null) {
            @Override
            public boolean alwaysAsId() {
                return true;
            }
        };
    }

    @Test
    public void testWriteAsIdWithIdWritten() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;

        when(gen.canWriteObjectId()).thenReturn(true);
        when(gen.writeObjectId(String.valueOf(id))).thenReturn(null);

        boolean result = writableObjectId.writeAsId(gen, provider, idWriter);
        assertNotNull(result);
        assertNotNull(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithoutIdWritten() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        when(gen.canWriteObjectId()).thenReturn(true);
        when(gen.writeObjectId(String.valueOf(id))).thenReturn(null);

        boolean result = writableObjectId.writeAsId(gen, provider, idWriter);
        assertNotNull(result);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsIdWithoutObjectIdSupport() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        when(gen.canWriteObjectId()).thenReturn(false);
        when(provider.getSerializerProvider().findValueSerializer(id.getClass()))
                .thenReturn(new StdSerializer<Object>(id.getClass(), null));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonMapping.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        boolean result = writableObjectId.writeAsId(gen, provider, idWriter);
        assertNotNull(result);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testGenerateId() throws JsonMappingException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        Object generatedId = writableObjectId.generateId(new Object());
        assertNotNull(generatedId);
        assertSame(generatedId, writableObjectId.id);
    }

    @Test
    public void testWriteAsFieldWithIdWritten() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        writableObjectId.id = id;
        SerializableString name = new SerializableString("name");

        when(gen.canWriteObjectId()).thenReturn(true);

        writableObjectId.writeAsField(gen, provider, idWriter);
        assertNotNull(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithoutIdWritten() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        SerializableString name = new SerializableString("name");

        when(gen.canWriteObjectId()).thenReturn(false);
        when(provider.getSerializerProvider().findValueSerializer(id.getClass()))
                .thenReturn(new StdSerializer<Object>(id.getClass(), null));

        writableObjectId.writeAsField(gen, provider, idWriter);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldWithAlwaysAsId() throws IOException {
        Object id = new Object();
        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
        WritableObjectId alwaysAsIdWritableObjectId = new WritableObjectId(idGenerator) {
            @Override
            protected boolean idWritten() {
                return true;
            }
        };

        when(gen.canWriteObjectId()).thenReturn(false);
        when(provider.getSerializerProvider().findValueSerializer(id.getClass()))
                .thenReturn(new StdSerializer<Object>(id.getClass(), null));
        when(idGenerator.generateId(anyObject())).thenReturn(id);

        alwaysAsIdWritableObjectId.writeAsField(gen, provider, alwaysAsIdWriter);
        assertNotNull(alwaysAsIdWritableObjectId.idWritten);
    }
}