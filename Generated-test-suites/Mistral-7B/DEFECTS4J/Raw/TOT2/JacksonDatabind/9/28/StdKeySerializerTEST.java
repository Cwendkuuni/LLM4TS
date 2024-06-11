import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.fasterxml.jackson.core.test.BaseTestUtils.*;

public class StdKeySerializerTest {
    @Test
    public void testSerializeDate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jgen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), getSerializationConfig());
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();

        serializer.serialize(date, jgen, new SerializerProvider() {});

        // Verify that the Date is serialized correctly
        verify(jgen, times(1)).writeFieldName(equalTo("" + date.getTime()));
    }

    @Test
    public void testSerializeString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jgen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), getSerializationConfig());
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "test";

        serializer.serialize(str, jgen, new SerializerProvider() {});

        // Verify that the String is serialized correctly
        verify(jgen, times(1)).writeFieldName(equalTo(str));
    }
}