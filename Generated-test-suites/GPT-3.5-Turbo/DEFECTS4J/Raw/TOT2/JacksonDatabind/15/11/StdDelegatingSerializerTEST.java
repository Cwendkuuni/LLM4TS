import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize_NullValue() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        });

        ObjectMapper mapper = new ObjectMapper();
        FakeJsonGenerator generator = new FakeJsonGenerator();
        FakeSerializerProvider provider = new FakeSerializerProvider();

        serializer.serialize(null, generator, provider);

        assertNull(generator.getLastValue());
    }

    @Test
    public void testConvertValue_Conversion() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value + " converted";
            }
        });

        Object converted = serializer.convertValue("test");

        assertEquals("test converted", converted);
    }

    // More test cases can be added here for other methods in StdDelegatingSerializer class

}