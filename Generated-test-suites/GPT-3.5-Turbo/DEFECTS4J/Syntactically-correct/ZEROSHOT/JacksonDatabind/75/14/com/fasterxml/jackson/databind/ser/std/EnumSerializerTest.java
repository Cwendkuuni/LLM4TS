import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import java.io.IOException;

public class EnumSerializerTest {
    
    @Test
    public void testSerialize() throws IOException {
        EnumValues values = new EnumValues();
        Enum<?> en = Enum.ONE;
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        EnumSerializer serializer = new EnumSerializer(values, true);
        try {
            serializer.serialize(en, gen, serializers);
        } catch(IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetEnumValues() {
        EnumValues values = new EnumValues();
        EnumSerializer serializer = new EnumSerializer(values, true);
        assertEquals(values, serializer.getEnumValues());
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        EnumValues values = new EnumValues();
        EnumSerializer serializer = new EnumSerializer(values, true);
        SerializerProvider serializers = null;
        BeanProperty property = null;
        try {
            serializer.createContextual(serializers, property);
        } catch(JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSerializeAsIndex() {
        EnumValues values = new EnumValues();
        EnumSerializer serializer = new EnumSerializer(values, true);
        SerializerProvider serializers = null;
        assertEquals(true, serializer.serializeAsIndex(serializers));
    }
    
    @Test
    public void testIsShapeWrittenUsingIndex() {
        EnumValues values = new EnumValues();
        EnumSerializer serializer = new EnumSerializer(values, true);
        JsonFormat.Value format = null;
        boolean fromClass = false;
        try {
            EnumSerializer._isShapeWrittenUsingIndex(Enum.class, format, fromClass);
        } catch(IllegalArgumentException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}