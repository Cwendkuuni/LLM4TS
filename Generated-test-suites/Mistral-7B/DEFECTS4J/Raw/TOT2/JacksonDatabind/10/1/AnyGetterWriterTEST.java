import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import static org.mockito.MockitoAnnotations.initMocks;

public class AnyGetterWriterTest {
    private AnyGetterWriter anyGetterWriter;
    private AnnotatedMember annotatedMember;
    private BeanProperty beanProperty;
    private MapSerializer mapSerializer;
    private Object bean;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        initMocks(this);
        annotatedMember = mock(AnnotatedMember.class);
        beanProperty = mock(BeanProperty.class);
        mapSerializer = mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
        bean = new Object();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testGetAndSerializeNullBean() throws Exception {
        when(_accessor.getValue(bean)).thenReturn(null);
        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
        verify(jsonGenerator, never()).writeStartObject();
        verify(jsonGenerator, never()).writeFieldName("anyGetterWriter");
    }

    @Test
    public void testGetAndSerializeEmptyMap() throws Exception {
        when(_accessor.getValue(bean)).thenReturn(new HashMap<>());
        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
        // As per the code, when _mapSerializer is null, no serialization is performed.
        verify(jsonGenerator, never()).writeStartObject();
        verify(jsonGenerator, never()).writeFieldName("anyGetterWriter");
    }

    @Test
    public void testGetAndSerializeValidMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        when(_accessor.getValue(bean)).thenReturn(map);
        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeFieldName("anyGetterWriter");
        verify(mapSerializer).serializeFields(map, jsonGenerator, serializerProvider);
    }
}