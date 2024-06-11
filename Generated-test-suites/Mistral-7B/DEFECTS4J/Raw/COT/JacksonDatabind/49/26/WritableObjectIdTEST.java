package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.databind.util.NameValuePair;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for WritableObjectId class
 *
 * @author John Doe
 */
public class WritableObjectIdTest extends TestBase {

    private ObjectIdGenerator<?> generator;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter w;
    private ObjectMapper mapper;
    private ObjectWriter objectWriter;

    @Before
    public void setup() throws Exception {
        generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(any(Object.class))).thenReturn("generatedId");

        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        w = new ObjectIdWriter(new StdSerializer<String>(String.class), null, null, null, provider);
        w.alwaysAsId = true;
        mapper = new ObjectMapper();
        objectWriter = mapper.writer(new NameValuePair[]{new NameValuePair("test", "testValue")});
    }

    @Test
    public void testWritableObjectIdConstructor() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNull(writableObjectId.id);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        when(gen.canWriteObjectId()).thenReturn(true);
        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.propertyName).thenReturn(new SerializableString("testPropertyName"));
        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() throws IOException {
        Object forPojo = new Object() {
            @ObjectIdGenerator(value = GeneratorType.AUTO)
            public Object getId() {
                return "existingId";
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.generateId(forPojo);
        assertNotNull(writableObjectId.id);
        assertNull(writableObjectId.idWritten);
    }

    @Test(expected = IOException.class)
    public void testGenerateIdWithNullForPojo() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.generateId(null);
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        when(gen.canWriteObjectId()).thenReturn(true);
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(writableObjectId.idWritten);

        when(gen.canWriteObjectId()).thenReturn(false);
        when(w.propertyName).thenReturn(new SerializableString("testPropertyName"));
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteIdAsJsonString() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        String id = "testId";
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        Object idWrapper = mapper.readValue(objectWriter.writeValueAsString(writableObjectId), Object.class);
        assertEquals(id, idWrapper);
    }

    @Test
    public void testWriteIdAsJsonLong() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        long id = 12345L;
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        Object idWrapper = mapper.readerForUpdating(WritableObjectId.class).readValue(objectWriter.writeValueAsString(writableObjectId));
        assertEquals(id, ((Number) idWrapper.getClass().getField("id").get(idWrapper)).longValue());
    }
}