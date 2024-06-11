import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> generator;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private ObjectIdWriter writer;

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertFalse(writableObjectId.writeAsId(gen, provider, writer));

        writableObjectId.id = "testId";
        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
        
        Mockito.when(gen.canWriteObjectId()).thenReturn(true);
        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = writableObjectId.generateId("testPojo");
        assertNotNull(id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        writableObjectId.id = "testId";
        writableObjectId.writeAsField(gen, provider, writer);
        assertTrue(writableObjectId.idWritten);
    }

}