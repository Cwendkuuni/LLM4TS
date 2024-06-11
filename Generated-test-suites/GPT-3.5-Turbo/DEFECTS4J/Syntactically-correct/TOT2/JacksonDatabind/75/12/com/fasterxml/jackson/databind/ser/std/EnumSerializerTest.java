import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;

public class EnumSerializerTest {

    @Mock
    private EnumValues mockEnumValues;

    @Test
    public void testConstructor() {
        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        assertNotNull(enumSerializer);
    }

    @Test
    public void testGetEnumValues() {
        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        assertEquals(mockEnumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testSerializeUsingIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        assertEquals(true, enumSerializer._serializeAsIndex(mock(SerializerProvider.class)));
    }

    @Test
    public void testSerialize() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        enumSerializer.serialize(Enum.VAL1, gen, serializers);
        Mockito.verify(gen).writeNumber(Enum.VAL1.ordinal());
    }

    @Test
    public void testGetSchemaWithIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = null;
        assertNotNull(enumSerializer.getSchema(provider, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitorWithIndex() throws JsonMappingException {
        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).getProvider();
    }

    @Test
    public void testIsShapeWrittenUsingIndexWithIndex() {
        Class<?> enumClass = Enum.class;
        JsonFormat.Value format = null;
        boolean fromClass = true;
        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, fromClass);
        assertNull(result);
    }
}