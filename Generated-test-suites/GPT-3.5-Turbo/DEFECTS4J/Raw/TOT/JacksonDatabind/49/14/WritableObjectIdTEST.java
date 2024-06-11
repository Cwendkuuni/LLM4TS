import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> generator;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private ObjectIdWriter w;

    private WritableObjectId objectId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_WhenIdIsNull_ShouldReturnFalse() throws IOException {
        boolean result = objectId.writeAsId(gen, provider, w);
        assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWritten_ShouldReturnTrue() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = true;
        boolean result = objectId.writeAsId(gen, provider, w);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdNotWrittenAndAlwaysAsIdIsTrue_ShouldReturnTrue() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        w.alwaysAsId = true;
        boolean result = objectId.writeAsId(gen, provider, w);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenGenCanWriteObjectIdAndIdIsNotNullAndIdNotWrittenAndAlwaysAsIdIsFalse_ShouldReturnTrue() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        w.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(true);
        boolean result = objectId.writeAsId(gen, provider, w);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenGenCanNotWriteObjectIdAndIdIsNotNullAndIdNotWrittenAndAlwaysAsIdIsFalse_ShouldReturnTrue() throws IOException {
        objectId.id = new Object();
        objectId.idWritten = false;
        w.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(false);
        boolean result = objectId.writeAsId(gen, provider, w);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenGenCanWriteObjectIdAndIdIsNullAndAlwaysAsIdIsFalse_ShouldReturnFalse() throws IOException {
        objectId.id = null;
        objectId.idWritten = false;
        w.alwaysAsId = false;
        when(gen.canWriteObjectId()).thenReturn(true);
        boolean result = objectId.writeAsId(gen, provider, w);
        assertFalse(result);
    }

    @Test
    public void testGenerateId_WhenForPojoIsNotNull_ShouldAssignGeneratedIdToIdAndReturnId() {
        Object forPojo = new Object();
        Object id = new Object();
        when(generator.generateId(forPojo)).thenReturn(id);
        Object result = objectId.generateId(forPojo);
        assertSame(id, objectId.id);
        assertSame(id, result);
    }

    @Test
    public void testGenerateId_WhenForPojoIsNull_ShouldAssignGeneratedIdToIdAndReturnId() {
        Object forPojo = null;
        Object id = new Object();
        when(generator.generateId(forPojo)).thenReturn(id);
        Object result = objectId.generateId(forPojo);
        assertSame(id, objectId.id);
        assertSame(id, result);
    }

    @Test
    public void testWriteAsField_WhenNameIsNull_ShouldSetIdWrittenToTrue() throws IOException {
        w.propertyName = null;
        objectId.writeAsField(gen, provider, w);
        assertTrue(objectId.idWritten);
    }

    @Test
    public void testWriteAsField_WhenNameIsNotNullAndGenCanWriteObjectId_ShouldWriteObjectIdAndReturn() throws IOException {
        w.propertyName = new MockSerializableString();
        when(gen.canWriteObjectId()).thenReturn(true);
        objectId.writeAsField(gen, provider, w);
        assertTrue(objectId.idWritten);
    }

    @Test
    public void testWriteAsField_WhenNameIsNotNullAndGenCanNotWriteObjectId_ShouldWriteFieldNameAndSerializeId() throws IOException {
        w.propertyName = new MockSerializableString();
        when(gen.canWriteObjectId()).thenReturn(false);
        objectId.writeAsField(gen, provider, w);
        assertTrue(objectId.idWritten);
    }

    private class MockSerializableString implements SerializableString {

        @Override
        public String getValue() {
            return null;
        }

        @Override
        public int charLength() {
            return 0;
        }

        @Override
        public char[] asQuotedChars() {
            return null;
        }

        @Override
        public byte[] asUnquotedUTF8() {
            return null;
        }

        @Override
        public byte[] asQuotedUTF8() {
            return null;
        }

        @Override
        public int appendQuotedUTF8(byte[] buffer, int offset) {
            return 0;
        }

        @Override
        public int appendQuoted(char[] buffer, int offset) {
            return 0;
        }
    }
}