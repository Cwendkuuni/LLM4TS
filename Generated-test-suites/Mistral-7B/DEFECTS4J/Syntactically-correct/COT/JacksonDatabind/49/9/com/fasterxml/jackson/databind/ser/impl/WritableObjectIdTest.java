package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstructor() {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertEquals(generator, writableObjectId.generator);
    }

    @Test
    public void testWriteAsIdFalse() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        ObjectIdWriter w = createMock(ObjectIdWriter.class);

        replay(gen, provider, w);

        assertFalse(writableObjectId.writeAsId(gen, provider, w));

        verify(gen, provider, w);
    }

    @Test
    public void testWriteAsIdTrue() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        ObjectIdWriter w = createMock(ObjectIdWriter.class);

        w.alwaysAsId = true;

        replay(gen, provider, w);

        Object id = new Object() {
            @Override
            public String toString() {
                return "testId";
            }
        };
        writableObjectId.id = id;

        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        verify(gen, provider, w);
    }

    @Test
    public void testGenerateIdNullForPojo() {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        assertNull(writableObjectId.generateId(null));
    }

    @Test
    public void testGenerateIdNotIdWritten() {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        Object forPojo = new Object() {
            @Override
            public String toString() {
                return "testPojo";
            }
        };

        assertNull(writableObjectId.generateId(forPojo));
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testGenerateIdIdWritten() {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        Object forPojo = new Object() {
            @Override
            public String toString() {
                return "testPojo";
            }
        };

        writableObjectId.generateId(forPojo);
        Object id = new Object() {
            @Override
            public String toString() {
                return "testId";
            }
        };
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        assertEquals(id, writableObjectId.generateId(forPojo));
    }

    @Test(expected = IOException.class)
    public void testWriteAsIdIOException() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                throw new IOException("Test IOException");
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        ObjectIdWriter w = createMock(ObjectIdWriter.class);

        replay(gen, provider, w);

        writableObjectId.writeAsId(gen, provider, w);

        verify(gen, provider, w);
    }

    @Test
    public void testWriteAsFieldNullName() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        ObjectIdWriter w = createMock(ObjectIdWriter.class);

        replay(gen, provider, w);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, provider, w);
    }

    @Test
    public void testWriteAsFieldEmptyName() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        ObjectIdWriter w = createMock(ObjectIdWriter.class);
        SerializableString name = createMock(SerializableString.class);

        w.propertyName = name;

        replay(gen, provider, w, name);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, provider, w, name);
    }

    @Test
    public void testWriteAsFieldIdWrittenFalse() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        ObjectIdWriter w = createMock(ObjectIdWriter.class);
        SerializableString name = createMock(SerializableString.class);

        replay(gen, provider, w, name);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, provider, w, name);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldIdWrittenTrue() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        ObjectIdWriter w = createMock(ObjectIdWriter.class);
        SerializableString name = createMock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, w);

        assertTrue(writableObjectId.idWritten);

        replay(gen, provider, w, name);

        Object id = new Object() {
            @Override
            public String toString() {
                return "testId";
            }
        };
        writableObjectId.id = id;
        writableObjectId.idWritten = true;

        w.propertyName = name;

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, provider, w, name);
    }

    @Test(expected = IOException.class)
    public void testWriteAsFieldIOException() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider provider = createMock(SerializerProvider.class);
        ObjectIdWriter w = createMock(ObjectIdWriter.class);
        SerializableString name = createMock(SerializableString.class);

        replay(gen, provider, w, name);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, provider, w, name);

        w.serializer = createMock(StdSerializer.class);
        doThrow(new IOException("Test IOException")).when(w.serializer).serialize(writableObjectId.id, gen, provider);

        writableObjectId.writeAsField(gen, provider, w);

        verify(gen, provider, w, name);
    }

    private JsonGenerator createMock(Class<?> clazz) {
        return mock(JsonGenerator.class);
    }

    private SerializerProvider createMock(Class<?> clazz) {
        return mock(SerializerProvider.class);
    }

    private ObjectIdWriter createMock(Class<?> clazz) {
        return mock(ObjectIdWriter.class);
    }

    private SerializableString createMock(Class<?> clazz) {
        return mock(SerializableString.class);
    }

    private ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    private void replay(Object... mocks) {
        for (Object mock : mocks) {
            if (mock != null) {
                replay(mock);
            }
        }
    }

    private void verify(Object... mocks) {
        for (Object mock : mocks) {
            if (mock != null) {
                verify(mock);
            }
        }
    }
}