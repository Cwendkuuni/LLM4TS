import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TypeDeserializerBaseTest {
    
    @Test
    public void testForProperty() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public JavaType baseType() {
                return null;
            }

            @Override
            public String propertyName() {
                return null;
            }

            @Override    
            public TypeIdResolver typeIdResolver() {
                return null;
            }
            
            @Override    
            public Class<?> defaultImpl() {
                return null;
            }

        };
        BeanProperty prop = null;
        TypeDeserializerBase result = typeDeserializer.forProperty(prop);
        assertNull(result.getPropertyName());
        assertNull(result.getDefaultImpl());
    }
    
    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public JavaType baseType() {
                return null;
            }

            @Override
            public String propertyName() {
                return null;
            }

            @Override    
            public TypeIdResolver typeIdResolver() {
                return null;
            }
            
            @Override    
            public Class<?> defaultImpl() {
                return null;
            }

        };
        JsonTypeInfo.As result = typeDeserializer.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, result);
    }
    
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public JavaType baseType() {
                return null;
            }

            @Override
            public String propertyName() {
                return null;
            }

            @Override    
            public TypeIdResolver typeIdResolver() {
                return null;
            }
            
            @Override    
            public Class<?> defaultImpl() {
                return null;
            }

        };
        String result = typeDeserializer.baseTypeName();
        assertEquals("", result);
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public JavaType baseType() {
                return null;
            }

            @Override
            public String propertyName() {
                return null;
            }

            @Override    
            public TypeIdResolver typeIdResolver() {
                return null;
            }
            
            @Override    
            public Class<?> defaultImpl() {
                return null;
            }

        };
        String result = typeDeserializer.getPropertyName();
        assertEquals("", result);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public JavaType baseType() {
                return null;
            }

            @Override
            public String propertyName() {
                return null;
            }

            @Override    
            public TypeIdResolver typeIdResolver() {
                return null;
            }
            
            @Override    
            public Class<?> defaultImpl() {
                return null;
            }

        };
        TypeIdResolver result = typeDeserializer.getTypeIdResolver();
        assertNull(result);
    }
    
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public JavaType baseType() {
                return null;
            }

            @Override
            public String propertyName() {
                return null;
            }

            @Override    
            public TypeIdResolver typeIdResolver() {
                return null;
            }
            
            @Override    
            public Class<?> defaultImpl() {
                return null;
            }

        };
        Class<?> result = typeDeserializer.getDefaultImpl();
        assertNull(result);
    }
    
    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public JavaType baseType() {
                return null;
            }

            @Override
            public String propertyName() {
                return null;
            }

            @Override    
            public TypeIdResolver typeIdResolver() {
                return null;
            }
            
            @Override    
            public Class<?> defaultImpl() {
                return null;
            }

        };
        String result = typeDeserializer.toString();
        assertEquals("[TypeDeserializerBase; base-type:null; id-resolver: null]", result);
    }

}