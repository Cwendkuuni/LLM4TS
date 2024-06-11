import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private BeanSerializerFactory factory;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        factory = mapper.getSerializerProvider().getBeanSerializerFactory();
    }

    @Test
    public void testSimpleProperty() throws JsonMappingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(DummyClass.class);
        PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

        JavaType type = mapper.getTypeFactory().constructType(int.class);
        BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializerProvider(),
                new BeanPropertyDefinition(new AnnotatedMethod("getA", DummyClass.class), type), type, null, null, null, null, false);

        assertEquals(0, writer.getNullValue());
        assertEquals(JsonInclude.Include.NON_NULL, writer.getDefaultInclude());
        assertNull(writer.getDefaultValue());

        // Test serialization
        ObjectWriter ow = mapper.writerFor(new TypeReference<Object>() {});
        DummyClass obj = new DummyClass(1);
        ow.writeValue(System.out, obj);
        assertEquals(1, ow.toString().trim().charAt(0));
    }

    @Test
    public void testNullProperty() throws JsonMappingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(DummyClass.class);
        PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

        JavaType type = mapper.getTypeFactory().constructType(String.class);
        BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializerProvider(),
                new BeanPropertyDefinition(new AnnotatedMethod("getB", DummyClass.class), type), type, null, null, null, null, false);

        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, writer.getNullValue());
        assertEquals(JsonInclude.Include.NON_NULL, writer.getDefaultInclude());
        assertNull(writer.getDefaultValue());

        // Test serialization
        ObjectWriter ow = mapper.writerFor(new TypeReference<Object>() {});
        DummyClass obj = new DummyClass(null);
        ow.writeValue(System.out, obj);
        assertEquals("null", ow.toString().trim());
    }

    @Test
    public void testDefaultValueProperty() throws JsonMappingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(DummyClassWithDefault.class);
        PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

        JavaType type = mapper.getTypeFactory().constructType(int.class);
        BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializerProvider(),
                new BeanPropertyDefinition(new AnnotatedMethod("getDefaultValue", DummyClassWithDefault.class), type), type, null, null, null, null, false);

        assertEquals(0, writer.getNullValue());
        assertEquals(JsonInclude.Include.ALWAYS, writer.getDefaultInclude());
        assertEquals(0, writer.getDefaultValue());

        // Test serialization
        ObjectWriter ow = mapper.writerFor(new TypeReference<Object>() {});
        DummyClassWithDefault obj = new DummyClassWithDefault();
        ow.writeValue(System.out, obj);
        assertEquals("0", ow.toString().trim());
    }

    @Test
    public void testEmptyCollection() throws JsonMappingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(DummyClassWithList.class);
        PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

        JavaType type = mapper.getTypeFactory().constructType(List.class);
        BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializerProvider(),
                new BeanPropertyDefinition(new AnnotatedField("listProperty", DummyClassWithList.class), type), type, null, null, null, null, false);

        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, writer.getNullValue());
        assertEquals(JsonInclude.Include.NON_EMPTY, writer.getDefaultInclude());
        assertNull(writer.getDefaultValue());

        // Test serialization
        ObjectWriter ow = mapper.writerFor(new TypeReference<Object>() {});
        DummyClassWithList obj = new DummyClassWithList();
        ow.writeValue(System.out, obj);
        assertEquals("[]", ow.toString().trim());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidSerializationType() throws JsonMappingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(DummyClass.class);
        PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

        JavaType type = mapper.getTypeFactory().constructType(Boolean.class);
        try {
            builder.buildWriter(mapper.getSerializerProvider(),
                    new BeanPropertyDefinition(new AnnotatedMethod("getA", DummyClass.class), type), type, null, null, null, null, false);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Error instantiating value serializer: Can not instantiate value serializer for type [simple type, class java.lang.Boolean]: not supported", e.getMessage());
        }
    }

    @Test
    public void testCustomSerializer() throws JsonMappingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(DummyClassWithCustomSerializer.class);
        PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

        JavaType type = mapper.getTypeFactory().constructType(Integer.class);
        BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializerProvider(),
                new BeanPropertyDefinition(new AnnotatedMethod("getA", DummyClassWithCustomSerializer.class), type), type, null, null, null, null, false);

        assertEquals(0, writer.getNullValue());
        assertEquals(JsonInclude.Include.NON_NULL, writer.getDefaultInclude());
        assertNull(writer.getDefaultValue());

        JsonSerializer<Integer> serializer = new StdSerializer<Integer>(Integer.class) {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value * value);
            }
        };

        writer.assignNullSerializer(serializers.serializerInstance(new AnnotatedMethod("getA", DummyClassWithCustomSerializer.class), serializer.getClass()));

        // Test serialization
        ObjectWriter ow = mapper.writerFor(new TypeReference<Object>() {});
        DummyClassWithCustomSerializer obj = new DummyClassWithCustomSerializer(1);
        ow.writeValue(System.out, obj);
        assertEquals("1 1", ow.toString().trim());
    }

    @Test
    public void testJsonIncludeAnnotation() throws JsonMappingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(DummyClassWithJsonInclude.class);
        PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

        JavaType type = mapper.getTypeFactory().constructType(String.class);
        BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializerProvider(),
                new BeanPropertyDefinition(new AnnotatedField("jsonIncludeProperty", DummyClassWithJsonInclude.class), type), type, null, null, null, null, false);

        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, writer.getNullValue());
        assertEquals(JsonInclude.Include.NON_EMPTY, writer.getDefaultInclude());
        assertNull(writer.getDefaultValue());

        // Test serialization
        ObjectWriter ow = mapper.writerFor(new TypeReference<Object>() {});
        DummyClassWithJsonInclude obj = new DummyClassWithJsonInclude();
        ow.writeValue(System.out, obj);
        assertEquals("\"foo\"", ow.toString().trim());
    }

    @Test
    public void testUnwrappingNameTransformer() throws JsonMappingException {
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(DummyClassWithUnwrappingNameTransformer.class);
        PropertyBuilder builder = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);

        JavaType type = mapper.getTypeFactory().constructType(String.class);
        BeanPropertyWriter writer = builder.buildWriter(mapper.getSerializerProvider(),
                new BeanPropertyDefinition(new AnnotatedField("unwrappingNameTransformerProperty", DummyClassWithUnwrappingNameTransformer.class), type), type, null, null, null, null, false);

        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, writer.getNullValue());
        assertEquals(JsonInclude.Include.NON_EMPTY, writer.getDefaultInclude());
        assertNull(writer.getDefaultValue());

        // Test serialization
        ObjectWriter ow = mapper.writerFor(new TypeReference<Object>() {});
        DummyClassWithUnwrappingNameTransformer obj = new DummyClassWithUnwrappingNameTransformer("foo");
        ow.writeValue(System.out, obj);
        assertEquals("\"bar\"", ow.toString().trim());
    }

    static class DummyClass {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int a;

        public int getA() {
            return a;
        }

        public DummyClass(int a) {
            this.a = a;
        }
    }

    static class DummyClassWithDefault {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int a;

        @JsonSerialize(using = JsonEmptyBeanSerializer.class)
        public int getDefaultValue() {
            return 0;
        }
    }

    static class DummyClassWithList {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        List<Integer> listProperty;

        public List<Integer> getListProperty() {
            return listProperty;
        }

        public DummyClassWithList() {
            listProperty = new ArrayList<>();
        }
    }

    static class DummyClassWithCustomSerializer {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int a;

        public int getA() {
            return a;
        }

        public DummyClassWithCustomSerializer(int a) {
            this.a = a;
        }
    }

    static class DummyClassWithJsonInclude {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        String jsonIncludeProperty;

        public String getJsonIncludeProperty() {
            return jsonIncludeProperty;
        }

        public DummyClassWithJsonInclude() {
            jsonIncludeProperty = "foo";
        }
    }

    static class DummyClassWithUnwrappingNameTransformer {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        @JsonSerialize(using = JsonEmptyBeanSerializer.class)
        String unwrappingNameTransformerProperty;

        public String getUnwrappingNameTransformerProperty() {
            return unwrappingNameTransformerProperty;
        }

        public DummyClassWithUnwrappingNameTransformer(String unwrappingNameTransformerProperty) {
            this.unwrappingNameTransformerProperty = "bar";
        }
    }
}

class JsonEmptyBeanSerializer extends StdSerializer<Object> {
    public JsonEmptyBeanSerializer() {
        this(null);
    }

    public JsonEmptyBeanSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        }
    }
}