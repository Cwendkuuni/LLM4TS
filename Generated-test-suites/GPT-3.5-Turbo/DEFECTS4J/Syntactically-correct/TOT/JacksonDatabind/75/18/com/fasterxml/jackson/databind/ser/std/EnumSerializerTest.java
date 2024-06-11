import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testSerialize_asIndexWithOrdinalValue() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        
        TestEnum testEnum = TestEnum.FOO;
        JsonFactory jsonFactory = new JsonFactory();
        SerializerProvider serializerProvider = new SerializerProvider(null, jsonFactory.getCodec(), null) {
            
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
                    throws JsonMappingException {
                return serializer;
            }
            
        };
        JsonGenerator generator;
        try {
            generator = jsonFactory.createGenerator(System.out);
            serializer.serialize(testEnum, generator, serializerProvider);
            generator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerialize_asIndexWithToStringValue() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        
        TestEnum testEnum = TestEnum.BAR;
        JsonFactory jsonFactory = new JsonFactory();
        SerializerProvider serializerProvider = new SerializerProvider(null, jsonFactory.getCodec(), null) {
            
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
                    throws JsonMappingException {
                return serializer;
            }
            
        };
        JsonGenerator generator;
        try {
            generator = jsonFactory.createGenerator(System.out);
            serializer.serialize(testEnum, generator, serializerProvider);
            generator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerialize_asStringValue() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        
        TestEnum testEnum = TestEnum.BAZ;
        JsonFactory jsonFactory = new JsonFactory();
        SerializerProvider serializerProvider = new SerializerProvider(null, jsonFactory.getCodec(), null) {
            
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
                    throws JsonMappingException {
                return serializer;
            }
            
        };
        JsonGenerator generator;
        try {
            generator = jsonFactory.createGenerator(System.out);
            serializer.serialize(testEnum, generator, serializerProvider);
            generator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSchema_asIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        
        TestEnum testEnum = TestEnum.FOO;
        JsonFactory jsonFactory = new JsonFactory();
        SerializerProvider serializerProvider = new SerializerProvider(null, jsonFactory.getCodec(), null) {
            
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
                    throws JsonMappingException {
                return serializer;
            }
            
        };
        try {
            ObjectNode schema = (ObjectNode) serializer.getSchema(serializerProvider, null);
            assertEquals("integer", schema.get("type").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSchema_asStringValue() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        
        TestEnum testEnum = TestEnum.BAZ;
        JsonFactory jsonFactory = new JsonFactory();
        SerializerProvider serializerProvider = new SerializerProvider(null, jsonFactory.getCodec(), null) {
            
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
                    throws JsonMappingException {
                return serializer;
            }
            
        };
        try {
            ObjectNode schema = (ObjectNode) serializer.getSchema(serializerProvider, null);
            assertEquals("string", schema.get("type").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor_asIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        
        TestEnum testEnum = TestEnum.FOO;
        JsonFactory jsonFactory = new JsonFactory();
        SerializerProvider serializerProvider = new SerializerProvider(null, jsonFactory.getCodec(), null) {
            
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
                    throws JsonMappingException {
                return serializer;
            }
            
        };
        try {
            JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(serializerProvider) {};
            serializer.acceptJsonFormatVisitor(visitor, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor_asStringValue() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), false);
        
        TestEnum testEnum = TestEnum.BAZ;
        JsonFactory jsonFactory = new JsonFactory();
        SerializerProvider serializerProvider = new SerializerProvider(null, jsonFactory.getCodec(), null) {
            
            @Override
            public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
                    throws JsonMappingException {
                return serializer;
            }
            
        };
        try {
            JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(serializerProvider) {
                
                @Override
                public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
                    return new JsonStringFormatVisitor.Base() {
                        
                        @Override
                        public void enumTypes(Set<String> enums) {
                            assertEquals(3, enums.size());
                            assertEquals("FOO", enums.iterator().next());
                        }
                        
                    };
                }
                
            };
            serializer.acceptJsonFormatVisitor(visitor, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private enum TestEnum {
        FOO, BAR, BAZ;
    }
}