import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class NumberSerializersTest {

    @Mock
    private JsonGenerator mockGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSerialize_IntegerSerializer() throws IOException {
        IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = 10;

        serializer.serialize(value, mockGenerator, null);

        verify(mockGenerator, times(1)).writeNumber(10);
    }

    @Test
    public void testSerialize_IntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        Number value = 10;

        serializer.serialize(value, mockGenerator, null);

        verify(mockGenerator, times(1)).writeNumber(10);
    }

    @Test
    public void testSerialize_ShortSerializer() throws IOException {
        ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 10;

        serializer.serialize(value, mockGenerator, null);

        verify(mockGenerator, times(1)).writeNumber(10);
    }

    @Test
    public void testSerialize_LongSerializer() throws IOException {
        LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = 10L;

        serializer.serialize(value, mockGenerator, null);

        verify(mockGenerator, times(1)).writeNumber(10L);
    }

    @Test
    public void testSerialize_FloatSerializer() throws IOException {
        FloatSerializer serializer = FloatSerializer.instance;
        Float value = 10.5f;

        serializer.serialize(value, mockGenerator, null);

        verify(mockGenerator, times(1)).writeNumber(10.5f);
    }

    @Test
    public void testSerialize_DoubleSerializer() throws IOException {
        DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = 10.5;

        serializer.serialize(value, mockGenerator, null);

        verify(mockGenerator, times(1)).writeNumber(10.5);
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Verify that all expected serializers are added to the map
        assert(allDeserializers.containsKey(Integer.class.getName()));
        assert(allDeserializers.containsKey(Integer.TYPE.getName()));
        assert(allDeserializers.containsKey(Long.class.getName()));
        assert(allDeserializers.containsKey(Long.TYPE.getName()));
        assert(allDeserializers.containsKey(Byte.class.getName()));
        assert(allDeserializers.containsKey(Byte.TYPE.getName()));
        assert(allDeserializers.containsKey(Short.class.getName()));
        assert(allDeserializers.containsKey(Short.TYPE.getName()));
        assert(allDeserializers.containsKey(Double.class.getName()));
        assert(allDeserializers.containsKey(Double.TYPE.getName()));
        assert(allDeserializers.containsKey(Float.class.getName()));
        assert(allDeserializers.containsKey(Float.TYPE.getName()));
    }
}