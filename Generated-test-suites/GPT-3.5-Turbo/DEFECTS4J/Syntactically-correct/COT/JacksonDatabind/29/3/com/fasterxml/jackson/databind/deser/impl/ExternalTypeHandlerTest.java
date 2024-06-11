import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "propName";
        Object bean = null;

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler externalTypeHandler = builder.build();

        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);

        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "propName";
        Object bean = null;

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler externalTypeHandler = builder.build();

        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);

        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler externalTypeHandler = builder.build();

        Object result = externalTypeHandler.complete(p, ctxt, bean);

        assertNull(result);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler externalTypeHandler = builder.build();

        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);

        assertNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 1;
        String typeId = "typeId";

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("propName", index);
        properties[0] = new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializerStub());
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);

        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);

        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 1;
        String typeId = "typeId";

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("propName", index);
        properties[0] = new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializerStub());
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, null, null);

        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
    
    private class TypeDeserializerStub extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public boolean isTypedScalar() {
            return false;
        }

        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt, TypeDeserializer defaultValueDeserializer) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt, TypeDeserializer elementTypeDeserializer) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt, TypeDeserializer propertyNameDeserializer) throws IOException {
            return null;
        }

        @Override
        public Object deserializeTypedFromScalar(JsonParser p, DeserializationContext ctxt, TypeDeserializer defaultImpl) throws IOException {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public String getPropertyName() {
            return "typeProperty";
        }

        @Override
        public TypeIdResolver getTypeIdResolver() {
            return null;
        }

        @Override
        public Class<?> getDefaultImpl() {
            return null;
        }
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();

        assertNotNull(builder);

        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializerStub();
        builder.addExternal(property, typeDeser);
    }
}