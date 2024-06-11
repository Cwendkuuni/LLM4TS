import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        TypeDeserializer expResult = null;
        TypeDeserializer result = instance.forProperty(prop);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTypeName() {
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String expResult = "";
        String result = instance.getPropertyName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        TypeIdResolver expResult = null;
        TypeIdResolver result = instance.getTypeIdResolver();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        Class<?> expResult = null;
        Class<?> result = instance.getDefaultImpl();
        assertEquals(expResult, result);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String expResult = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testDeserializedWithNativeTypeId() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }
        };
        Object expResult = null;
        Object result = instance._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertEquals(expResult, result);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        DeserializationContext ctxt = null;
        String typeId = "";
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        JsonDeserializer<Object> expResult = null;
        JsonDeserializer<Object> result = instance._findDeserializer(ctxt, typeId);
        assertEquals(expResult, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext ctxt = null;
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        JsonDeserializer<Object> expResult = null;
        JsonDeserializer<Object> result = instance._findDefaultImplDeserializer(ctxt);
        assertEquals(expResult, result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = null;
        String typeId = "";
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        TypeDeserializerBase instance = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
        JavaType expResult = null;
        JavaType result = instance._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertEquals(expResult, result);
    }

}