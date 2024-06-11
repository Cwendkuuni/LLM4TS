import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.impl.BeanSerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private BeanSerializerFactory beanSerializerFactory;

    @Before
    public void setup() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.beanSerializerFactory = this.objectMapper.getSerializationConfig().getDefaultSerializerProvider().getBeanSerializerFactory();
    }

    @Test
    public void testBuilderWithBasicProperties() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(this.objectMapper.getSerializationConfig(), this.objectMapper.findBeanDescription(DummyClass.class));
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(this.beanSerializerFactory, null, null, null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testBuilderWithCustomSerializationType() throws Exception {
        DummyClassWithCustomSerialization dummyClassWithCustomSerialization = new DummyClassWithCustomSerialization();
        PropertyBuilder propertyBuilder = new PropertyBuilder(this.objectMapper.getSerializationConfig(), this.objectMapper.findBeanDescription(DummyClassWithCustomSerialization.class));
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(this.beanSerializerFactory, null, TypeFactory.defaultInstance().constructType(DummyClassWithCustomSerialization.class), null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertNotNull(beanPropertyWriter.getSerializer());
        assertEquals(DummyClassWithCustomSerializationSerializer.class, beanPropertyWriter.getSerializer().getClass());
    }

    @Test
    public void testBuilderWithCustomNullSerializer() throws Exception {
        DummyClassWithCustomNullSerializer dummyClassWithCustomNullSerializer = new DummyClassWithCustomNullSerializer();
        PropertyBuilder propertyBuilder = new PropertyBuilder(this.objectMapper.getSerializationConfig(), this.objectMapper.findBeanDescription(DummyClassWithCustomNullSerializer.class));
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(this.beanSerializerFactory, null, null, null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertNotNull(beanPropertyWriter.getSerializer());
        assertEquals(DummyClassWithCustomNullSerializerSerializer.class, beanPropertyWriter.getSerializer().getClass());
        assertEquals(DummyClassWithCustomNullSerializerNullSerializer.class, beanPropertyWriter.getNullSerializer().getClass());
    }

    @Test
    public void testBuilderWithCustomUnwrappingNameTransformer() throws Exception {
        DummyClassWithCustomUnwrappingNameTransformer dummyClassWithCustomUnwrappingNameTransformer = new DummyClassWithCustomUnwrappingNameTransformer();
        PropertyBuilder propertyBuilder = new PropertyBuilder(this.objectMapper.getSerializationConfig(), this.objectMapper.findBeanDescription(DummyClassWithCustomUnwrappingNameTransformer.class));
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(this.beanSerializerFactory, null, null, null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertNotNull(beanPropertyWriter.getSerializer());
        assertEquals(DummyClassWithCustomUnwrappingNameTransformerSerializer.class, beanPropertyWriter.getSerializer().getClass());
        assertEquals(DummyClassWithCustomUnwrappingNameTransformerUnwrappingNameTransformer.class, beanPropertyWriter.getUnwrappingNameTransformer().getClass());
    }

    @Test
    public void testBuilderWithJsonIncludeAnnotation() throws Exception {
        DummyClassWithJsonInclude dummyClassWithJsonInclude = new DummyClassWithJsonInclude();
        PropertyBuilder propertyBuilder = new PropertyBuilder(this.objectMapper.getSerializationConfig(), this.objectMapper.findBeanDescription(DummyClassWithJsonInclude.class));
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(this.beanSerializerFactory, null, null, null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertEquals(JsonInclude.Include.NON_DEFAULT, beanPropertyWriter.getSerializerProperties().findValue("include"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithInvalidPropertyName() throws Exception {
        DummyClassWithInvalidPropertyName dummyClassWithInvalidPropertyName = new DummyClassWithInvalidPropertyName();
        PropertyBuilder propertyBuilder = new PropertyBuilder(this.objectMapper.getSerializationConfig(), this.objectMapper.findBeanDescription(DummyClassWithInvalidPropertyName.class));
        propertyBuilder.buildWriter(this.beanSerializerFactory, null, null, null, null, null, null, true);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuilderWithInvalidSerializationType() throws Exception {
        DummyClassWithInvalidSerializationType dummyClassWithInvalidSerializationType = new DummyClassWithInvalidSerializationType();
        PropertyBuilder propertyBuilder = new PropertyBuilder(this.objectMapper.getSerializationConfig(), this.objectMapper.findBeanDescription(DummyClassWithInvalidSerializationType.class));
        propertyBuilder.buildWriter(this.beanSerializerFactory, null, null, null, null, null, null, true);
    }

    static class DummyClass {
        // empty class
    }

    static class DummyClassWithCustomSerialization {
        @JsonSerialize(using = DummyClassWithCustomSerializationSerializer.class)
        private int customField;
    }

    public static class DummyClassWithCustomSerializationSerializer
            extends JsonSerializer<DummyClassWithCustomSerialization> {
        // custom serializer implementation
    }

    static class DummyClassWithCustomNullSerializer {
        @JsonSerialize(using = DummyClassWithCustomNullSerializerSerializer.class, nulls = JsonNull.LANCEMENT)
        private String nullableField;

        public static class DummyClassWithCustomNullSerializerSerializer
                extends JsonSerializer<DummyClassWithCustomNullSerializer> {
            // custom null serializer implementation
        }

        public static class DummyClassWithCustomNullSerializerNullSerializer
                extends JsonSerializer<Object> {
            // custom null serializer implementation
        }
    }

    static class DummyClassWithCustomUnwrappingNameTransformer {
        @JsonUnwrapped
        private DummyClassWithCustomUnwrappingNameTransformerInner inner;

        public static class DummyClassWithCustomUnwrappingNameTransformerInner {
            private String name;
        }

        public static class DummyClassWithCustomUnwrappingNameTransformerUnwrappingNameTransformer
                extends NameTransformer {
            // custom unwrapping name transformer implementation
        }
    }

    static class DummyClassWithJsonInclude {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private String jsonIncludeField;
    }

    static class DummyClassWithInvalidPropertyName {
        @JsonProperty("invalid_name") // property name should be "name"
        private String name;
    }

    static class DummyClassWithInvalidSerializationType {
        @JsonSerialize(using = Object.class) // cannot use Object.class as custom serializer type
        private String serializationField;
    }
}