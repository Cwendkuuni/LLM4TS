import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

@RunWith(MockitoJUnitRunner.class)
public class WritableObjectIdTest {

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private ObjectIdWriter writer;

    @Mock
    private ObjectIdGenerator<?> generator;

    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrue_AlwaysAsIdTrue_ReturnsTrue() throws IOException {
        writableObjectId.id = 1;
        writableObjectId.idWritten = true;
        writer.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrue_AlwaysAsIdFalse_CanWriteObjectIdTrue_ReturnsTrue() throws IOException {
        writableObjectId.id = 1;
        writableObjectId.idWritten = true;
        writer.alwaysAsId = false;
        gen.canWriteObjectId = true;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenTrue_AlwaysAsIdFalse_CanWriteObjectIdFalse_ReturnsTrue() throws IOException {
        writableObjectId.id = 1;
        writableObjectId.idWritten = true;
        writer.alwaysAsId = false;
        gen.canWriteObjectId = false;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalse_AlwaysAsIdTrue_ReturnsFalse() throws IOException {
        writableObjectId.id = 1;
        writableObjectId.idWritten = false;
        writer.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertFalse(result);
    }
    
    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalse_AlwaysAsIdFalse_CanWriteObjectIdTrue_ReturnsFalse() throws IOException {
        writableObjectId.id = 1;
        writableObjectId.idWritten = false;
        writer.alwaysAsId = false;
        gen.canWriteObjectId = true;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertFalse(result);
    }
    
    @Test
    public void testWriteAsId_WhenIdNotNullAndIdWrittenFalse_AlwaysAsIdFalse_CanWriteObjectIdFalse_ReturnsFalse() throws IOException {
        writableObjectId.id = 1;
        writableObjectId.idWritten = false;
        writer.alwaysAsId = false;
        gen.canWriteObjectId = false;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertFalse(result);
    }
    
    @Test
    public void testWriteAsId_WhenIdNull_ReturnsFalse() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        writer.alwaysAsId = true;

        boolean result = writableObjectId.writeAsId(gen, provider, writer);

        assertFalse(result);
    }

    @Test
    public void testGenerateId_ReturnsGeneratedId() {
        Object forPojo = new Object();
        
        Object result = writableObjectId.generateId(forPojo);

        assertEquals(writableObjectId.id, result);
    }
    
    @Test
    public void testGenerateId_WhenIdNotNull_ReturnsExistingId() {
        Object forPojo = new Object();
        writableObjectId.id = 1;

        Object result = writableObjectId.generateId(forPojo);

        assertEquals(writableObjectId.id, result);
    }

    @Test
    public void testWriteAsField_WhenCanWriteObjectIdTrue_WritesObjectIdAsString() throws IOException {
        gen.canWriteObjectId = true;
        writableObjectId.id = 1;
        writer.propertyName = null;

        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen).writeObjectId("1");
    }

    @Test
    public void testWriteAsField_WhenCanWriteObjectIdFalse_WritesFieldNameAndId() throws IOException {
        gen.canWriteObjectId = false;
        writableObjectId.id = 1;
        writer.propertyName = mock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, writer);

        verify(gen).writeFieldName(writer.propertyName);
        verify(writer.serializer).serialize(writableObjectId.id, gen, provider);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenFalse_SetsIdWrittenTrue() throws IOException {
        gen.canWriteObjectId = false;
        writableObjectId.id = 1;
        writer.propertyName = mock(SerializableString.class);

        writableObjectId.writeAsField(gen, provider, writer);

        assertTrue(writableObjectId.idWritten);
    }
}