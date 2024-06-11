import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ObjectIdWriter;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<Object> generatorMock = mock(ObjectIdGenerator.class);
        ObjectIdWriter idWriterMock = mock(ObjectIdWriter.class);
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        WritableObjectId writableObjectId = new WritableObjectId(generatorMock);
        when(generatorMock.generateId(any())).thenReturn(1L);

        boolean result = writableObjectId.writeAsId(genMock, providerMock, idWriterMock);
        verify(genMock).writeObjectId("1");
        assertTrue(result);
    }

    // Add test cases for exception handling (NullPointerException, JsonGenerationException, etc.)
}