package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.WithDeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

@RunWith(MockitoJUnitRunner.class)
public class JsonNodeDeserializerTest {

    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private JsonNodeFactory nodeFactory;
    
    @Mock
    private TypeDeserializer typeDeserializer;

    private JsonNodeDeserializer jsonNodeDeserializer;

     public JsonNodeDeserializerTest() {
        MockitoAnnotations.initMocks(this);
        jsonNodeDeserializer = new JsonNodeDeserializer();
    }

  @Test
    public void testGetNullValue_ValidJsonParser_ReturnNullNode() throws Exception {
        Mockito.when(jsonParser.getText()).thenReturn("");
        JsonNode expectedNode = NullNode.getInstance();
        JsonNode actualNode = jsonNodeDeserializer.getNullValue(deserializationContext);
        assertEquals(expectedNode, actualNode);
    }

  @Test
    public void testGetNullValue_InvalidJsonParser_ReturnNullNode() throws Exception {
        Mockito.when(jsonParser.getText()).thenReturn("");
        JsonNode expectedNode = NullNode.getInstance();
        JsonNode actualNode = jsonNodeDeserializer.getNullValue();
        assertEquals(expectedNode, actualNode);
    }

  @Test
    public void testDeserialize_ValidJsonParser_ReturnJsonNode() throws Exception {
        Mockito.when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        JsonNode expectedNode = new ObjectNode(null,null);
        JsonNode actualNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(expectedNode, actualNode);
    }

  @Test
    public void testDeserialize_InvalidJsonParser_ReturnJsonNode() throws Exception {
        Mockito.when(jsonParser.getCurrentTokenId()).thenReturn(null);
        JsonNode expectedNode = new ObjectNode(null,null);
        JsonNode actualNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(expectedNode, actualNode);
    }
  
  @Test
  public void testObjectDeserializer_DeserializeObject_ReturnObjectNode() throws Exception {
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        ObjectNode expectedNode = new ObjectNode(null,null);
        ObjectNode actualNode = jsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(jsonParser, deserializationContext);
        assertEquals(expectedNode, actualNode);
  }

  @Test
  public void testArrayDeserializer_DeserializeArray_ReturnArrayNode() throws Exception {
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        ArrayNode expectedNode = new ArrayNode(null,null);
        ArrayNode actualNode = jsonNodeDeserializer.ArrayDeserializer.getInstance().deserialize(jsonParser, deserializationContext);
        assertEquals(expectedNode, actualNode);
  }

}