import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StdSerializerTest {

    private StdSerializer<TestObject> serializer;

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    @Before
    public void setUp() {
        serializer = new StdSerializer<>(TestObject.class) {
            @Override
            public void serialize(TestObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                // Do nothing
            }
        };
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        TestObject testObject = new TestObject("test");
        serializer.serialize(testObject, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeObject(testObject);
    }
}