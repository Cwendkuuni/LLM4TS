import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = new MapSerializer(mapper.getFactory(), null);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new Object() {{
            put("name", "John Doe");
            put("age", 30);
        }};

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(bean)).thenReturn(mapOf("name", "John Doe", "age", 30));

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        ObjectNode expectedJson = mapper.createObjectNode();
        expectedJson.put("name", "John Doe");
        expectedJson.put("age", 30);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("name");
        verify(gen).writeValue(eq("John Doe"));
        verify(gen).writeFieldName("age");
        verify(gen).writeValue(eq(30));
        verify(gen).writeEndObject();
    }
}