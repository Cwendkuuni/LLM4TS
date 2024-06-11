import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize(new BigDecimal("12345.67890"), generator, null);
        generator.flush();
        Assert.assertEquals("12345.67890", generator.getOutputTarget().toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize(new BigInteger("1234567890"), generator, null);
        generator.flush();
        Assert.assertEquals("1234567890", generator.getOutputTarget().toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize(1234567890L, generator, null);
        generator.flush();
        Assert.assertEquals("1234567890", generator.getOutputTarget().toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize(123456.7890, generator, null);
        generator.flush();
        Assert.assertEquals("123456.7890", generator.getOutputTarget().toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize(123456.7890f, generator, null);
        generator.flush();
        Assert.assertEquals("123456.7890", generator.getOutputTarget().toString());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize(1234567890, generator, null);
        generator.flush();
        Assert.assertEquals("1234567890", generator.getOutputTarget().toString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize((byte) 123, generator, null);
        generator.flush();
        Assert.assertEquals("123", generator.getOutputTarget().toString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize((short) 12345, generator, null);
        generator.flush();
        Assert.assertEquals("12345", generator.getOutputTarget().toString());
    }

    @Test
    public void testSerializeUnknownType() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public BeanSerializer modifySerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanSerializer ser) {
                ser.resolve(new TypeResolverBuilder<Dummy>() {
                    private static final long serialVersionUID = 1L;
                });
                return ser;
            }
        });
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        numberSerializer.serialize(1234567890, generator, null);
        generator.flush();
        Assert.assertEquals("1234567890", generator.getOutputTarget().toString());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = new SerializerProvider();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JsonNode schema = numberSerializer.getSchema(provider, typeFactory.constructType(BigInteger.class));
        Assert.assertEquals("integer", schema.get("type").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitor() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}