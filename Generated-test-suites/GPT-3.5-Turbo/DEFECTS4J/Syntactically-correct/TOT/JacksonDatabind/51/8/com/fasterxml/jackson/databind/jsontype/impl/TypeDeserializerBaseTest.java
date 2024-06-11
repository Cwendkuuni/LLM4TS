import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);

        typeDeserializer = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = Mockito.mock(BeanProperty.class);
        TypeDeserializer result = typeDeserializer.forProperty(prop);

        Assert.assertNotNull(result);
        Assert.assertEquals(prop, ((TypeDeserializerBaseImpl) result).getProperty());
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializer.As result = typeDeserializer.getTypeInclusion();

        Assert.assertEquals(TypeDeserializer.As.EXISTING_PROPERTY, result);
    }

    @Test
    public void testBaseTypeName() {
        String result = typeDeserializer.baseTypeName();

        Assert.assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl", result);
    }

    @Test
    public void testGetPropertyName() {
        String result = typeDeserializer.getPropertyName();

        Assert.assertEquals("type", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = typeDeserializer.getTypeIdResolver();

        Assert.assertNotNull(result);
        Assert.assertEquals(Mockito.mock(TypeIdResolver.class), result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> result = typeDeserializer.getDefaultImpl();

        Assert.assertNull(result);
    }

    @Test
    public void testToString() {
        String result = typeDeserializer.toString();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.startsWith("["));
        Assert.assertTrue(result.contains("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl"));
        Assert.assertTrue(result.contains("; base-type:"));
        Assert.assertTrue(result.contains("; id-resolver:"));
        Assert.assertTrue(result.endsWith("]"));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = NullifyingDeserializer.instance;

        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);

        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWhenTypeIdIsNull() throws IOException {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object typeId = null;
        JsonDeserializer<Object> deser = NullifyingDeserializer.instance;

        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);

        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWhenDeserializerIsNotFound() throws IOException {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object typeId = "exampleId";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"key\":\"value\"}");
        jp.readValueAsTree();
        jp.getTypeId();

        Mockito.when(typeDeserializer._findDeserializer(ctxt, typeId)).thenReturn(null);

        try {
            typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof JsonProcessingException);
            Assert.assertEquals("No (native) type id found when one was expected for polymorphic type handling", exception.getMessage());
        }
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String typeId = "exampleId";
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        String extraDesc = "known type ids = example";

        Mockito.when(idResolver instanceof TypeIdResolverBase).thenReturn(true);
        Mockito.when(((TypeIdResolverBase) idResolver).getDescForKnownTypeIds()).thenReturn(extraDesc);
        Mockito.when(ctxt.handleUnknownTypeId(baseType, typeId, idResolver, extraDesc)).thenReturn(baseType);

        JavaType result = typeDeserializer._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);

        Assert.assertNotNull(result);
        Assert.assertEquals(baseType, result);
    }

    private class TypeDeserializerBaseImpl extends TypeDeserializerBase {

        private final BeanProperty _property;

        public TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
            _property = null;
        }

        public TypeDeserializerBaseImpl(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
            _property = property;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseImpl(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        public BeanProperty getProperty() {
            return _property;
        }
    }
}