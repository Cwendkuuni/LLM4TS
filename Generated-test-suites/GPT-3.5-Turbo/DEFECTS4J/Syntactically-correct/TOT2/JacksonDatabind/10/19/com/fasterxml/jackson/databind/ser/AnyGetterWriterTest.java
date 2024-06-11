import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        writer.getAndSerialize(null, gen, provider);

        // Test that null value does not throw exception
        verify(gen, never()).writeObject(any());
    }

    @Test
    public void testGetAndSerialize_WrongValueType() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, serializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object bean = new Object();

        // Test that exception is thrown for non-Map value
        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    // additional test cases for other methods can be added here

}