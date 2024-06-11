import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setUp() {
        jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
    }

    @Test
    public void testSerialize_NullBean_DefaultSerializeNullCalled() throws IOException {
        // Arrange
        Object bean = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider prov = mock(SerializerProvider.class);

        // Act
        jsonValueSerializer.serialize(bean, gen, prov);

        // Assert
        verify(prov).defaultSerializeNull(gen);
    }

    // Add test cases for other public methods

}