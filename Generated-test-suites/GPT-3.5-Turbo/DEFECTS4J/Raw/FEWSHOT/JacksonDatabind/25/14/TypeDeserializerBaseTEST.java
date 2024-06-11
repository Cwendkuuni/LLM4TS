import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    private TypeDeserializerBase typeDeserializer;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;
    private BeanProperty property;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = String.class;
        property = mock(BeanProperty.class);
        
        typeDeserializer = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase result = typeDeserializer.forProperty(property);
        assertEquals(property, result._property);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As result = typeDeserializer.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.PROPERTY, result);
    }

    @Test
    public void testBaseTypeName() {
        String result = typeDeserializer.baseTypeName();
        assertEquals("", result);
    }

    @Test
    public void testGetPropertyName() {
        String result = typeDeserializer.getPropertyName();
        assertEquals("type", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = typeDeserializer.getTypeIdResolver();
        assertEquals(idResolver, result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> result = typeDeserializer.getDefaultImpl();
        assertEquals(String.class, result);
    }

    @Test
    public void testToString() {
        String result = typeDeserializer.toString();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl; base-type:null; id-resolver: null]", result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);
        assertNull(result);
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {
        private static final long serialVersionUID = 1L;

        protected TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        protected TypeDeserializerBaseImpl(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseImpl(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }
    }
}