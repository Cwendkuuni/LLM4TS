import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer serializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        property = new BeanProperty();
        accessor = new AnnotatedMember();
        serializer = new MapSerializer();
        anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        assertNull("The method should not serialize null value", gen.getOutputTarget());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_NonMapValue() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        // Exception expected
    }

    @Test
    public void testGetAndSerialize_MapValue() throws Exception {
        // Arrange
        Map<String, Integer> value = new HashMap<>();
        value.put("key", 1);
        Object bean = new Object() {
            public Map<String, Integer> anyGetter() {
                return value;
            }
        };
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        // Assert that the serialized value is correct
        gen.flush();
        assertEquals("{\"key\":1}", gen.getOutputTarget().toString());
    }

    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean include(BeanPropertyWriter writer) {
                return true;
            }

            @Override
            public boolean include(PropertyWriter writer) {
                return true;
            }

            @Override
            public boolean includeElement(Object elementValue) {
                return true;
            }
        };

        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Assert
        assertNull("The method should not serialize null value", gen.getOutputTarget());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_NonMapValue() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean include(BeanPropertyWriter writer) {
                return true;
            }

            @Override
            public boolean include(PropertyWriter writer) {
                return true;
            }

            @Override
            public boolean includeElement(Object elementValue) {
                return true;
            }
        };

        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Assert
        // Exception expected
    }

    @Test
    public void testGetAndFilter_MapValue() throws Exception {
        // Arrange
        Map<String, Integer> value = new HashMap<>();
        value.put("key", 1);
        Object bean = new Object() {
            public Map<String, Integer> anyGetter() {
                return value;
            }
        };
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean include(BeanPropertyWriter writer) {
                return true;
            }

            @Override
            public boolean include(PropertyWriter writer) {
                return true;
            }

            @Override
            public boolean includeElement(Object elementValue) {
                return true;
            }
        };

        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Assert
        // Assert that the filtered serialized value is correct
        gen.flush();
        assertEquals("{\"key\":1}", gen.getOutputTarget().toString());
    }

    @Test
    public void testResolve() throws Exception {
        // Arrange
        SerializerProvider provider = objectMapper.getSerializerProvider();

        // Act
        anyGetterWriter.resolve(provider);

        // Assert
        // Assert that the _mapSerializer is resolved
        assertEquals(serializer, anyGetterWriter._mapSerializer);
    }
}