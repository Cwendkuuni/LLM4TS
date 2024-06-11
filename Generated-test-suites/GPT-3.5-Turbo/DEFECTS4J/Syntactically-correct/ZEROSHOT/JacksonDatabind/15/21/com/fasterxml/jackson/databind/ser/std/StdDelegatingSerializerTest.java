import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ser.std.*;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testResolve() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        try {
            SerializerProvider provider = new SerializerProvider();
            
            serializer.resolve(provider);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateContextual() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        try {
            SerializerProvider provider = new SerializerProvider();
            BeanProperty property = new BeanProperty();
            
            serializer.createContextual(provider, property);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerialize() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        try {
            JsonGenerator gen = new JsonGenerator();
            SerializerProvider provider = new SerializerProvider();
            serializer.serialize(null, gen, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerializeWithType() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        try {
            JsonGenerator gen = new JsonGenerator();
            SerializerProvider provider = new SerializerProvider();
            TypeSerializer typeSer = new TypeSerializer();
            serializer.serializeWithType(null, gen, provider, typeSer);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testIsEmpty1() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty2() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        try {
            SerializerProvider prov = new SerializerProvider();
            assertTrue(serializer.isEmpty(prov, null));
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetSchema1() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        try {
            SerializerProvider provider = new SerializerProvider();
            Type typeHint = new Type();
            JsonNode result = serializer.getSchema(provider, typeHint);
            assertNull(result);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetSchema2() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        try {
            SerializerProvider provider = new SerializerProvider();
            Type typeHint = new Type();
            boolean isOptional = false;
            JsonNode result = serializer.getSchema(provider, typeHint, isOptional);
            assertNull(result);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        try {
            JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
            JavaType typeHint = new JavaType();
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testConvertValue() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        Object value = null;
        Object result = serializer.convertValue(value);
        assertNull(result);
    }

    @Test
    public void testGetDelegatee() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }
        });
        
        assertNull(serializer.getDelegatee());
    }
}