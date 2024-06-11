import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {
    
    @Test
    public void testHandleTypePropertyValue() throws JsonParseException, IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.createParser("{\"testProperty\":\"testValue\"}");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        
        TestProperty property = new TestProperty();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(jsonParser, deserializationContext, property);
        TypeDeserializer typeDeserializer = new TypeDeserializer(jsonParser, objectMapper.getTypeFactory(), null);
        
        builder.addExternal(settableBeanProperty, typeDeserializer);
        ExternalTypeHandler externalTypeHandler = builder.build();
        
        boolean result = externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, "testProperty", property);
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testHandlePropertyValue() throws JsonParseException, IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.createParser("{\"testProperty\":\"testValue\"}");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        
        TestProperty property = new TestProperty();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(jsonParser, deserializationContext, property);
        TypeDeserializer typeDeserializer = new TypeDeserializer(jsonParser, objectMapper.getTypeFactory(), null);
        
        builder.addExternal(settableBeanProperty, typeDeserializer);
        ExternalTypeHandler externalTypeHandler = builder.build();
        
        boolean result = externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, "testProperty", property);
        
        Assert.assertEquals(false, result);
    }
    
    @Test
    public void testComplete() throws JsonParseException, IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.createParser("{\"testProperty\":\"testValue\"}");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        
        TestProperty property = new TestProperty();
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(jsonParser, deserializationContext, property);
        TypeDeserializer typeDeserializer = new TypeDeserializer(jsonParser, objectMapper.getTypeFactory(), null);
        
        builder.addExternal(settableBeanProperty, typeDeserializer);
        ExternalTypeHandler externalTypeHandler = builder.build();
        
        Object result = externalTypeHandler.complete(jsonParser, deserializationContext, property);
        
        Assert.assertEquals(property, result);
    }
    
    class TestProperty {
        private String testProperty;
        
        public String getTestProperty() {
            return testProperty;
        }
        
        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }
    }
}