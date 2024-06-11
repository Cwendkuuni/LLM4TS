
package com.fasterxml.jackson.databind.node;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TreeTraversingParserTest {
  
  @Test
  public void testNextToken_ArrayNode() throws IOException {
    // Arrange
    JsonNode node = createArrayNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    JsonToken token = parser.nextToken();
    
    // Assert
    Assert.assertEquals(JsonToken.START_ARRAY, token);
  }
  
  @Test
  public void testNextToken_ObjectNode() throws IOException {
    // Arrange
    JsonNode node = createObjectNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    JsonToken token = parser.nextToken();
    
    // Assert
    Assert.assertEquals(JsonToken.START_OBJECT, token);
  }
  
  @Test
  public void testNextToken_RootNode() throws IOException {
    // Arrange
    JsonNode node = createRootNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    JsonToken token = parser.nextToken();
    
    // Assert
    Assert.assertNull(token);
  }
  
  @Test
  public void testNextToken_TextValueNode() throws IOException {
    // Arrange
    JsonNode node = createTextValueNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    JsonToken token = parser.nextToken();
    
    // Assert
    Assert.assertEquals(JsonToken.VALUE_STRING, token);
  }
  
  @Test
  public void testNextToken_NullValueNode() throws IOException {
    // Arrange
    JsonNode node = createNullValueNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    JsonToken token = parser.nextToken();
    
    // Assert
    Assert.assertEquals(JsonToken.VALUE_NULL, token);
  }
  
  @Test
  public void testSkipChildren_ArrayNode() throws IOException {
    // Arrange
    JsonNode node = createArrayNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    JsonToken token = parser.skipChildren().nextToken();
    
    // Assert
    Assert.assertEquals(JsonToken.END_ARRAY, token);
  }
  
  @Test
  public void testSkipChildren_ObjectNode() throws IOException {
    // Arrange
    JsonNode node = createObjectNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    JsonToken token = parser.skipChildren().nextToken();
    
    // Assert
    Assert.assertEquals(JsonToken.END_OBJECT, token);
  }
  
  @Test
  public void testGetIntValue() throws IOException {
    // Arrange
    JsonNode node = createNumericNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    int value = parser.getIntValue();
    
    // Assert
    Assert.assertEquals(123, value);
  }
  
  @Test(expected = JsonParseException.class)
  public void testGetIntValue_InvalidNode_ThrowsException() throws IOException {
    // Arrange
    JsonNode node = createTextValueNode();
    TreeTraversingParser parser = new TreeTraversingParser(node);
    
    // Act
    parser.getIntValue();
  }
  
  private JsonNode createArrayNode() {
    ArrayNode node = new ArrayNode(null);
    node.add(new TextNode("item1"));
    node.add(new TextNode("item2"));
    return node;
  }
  
  private JsonNode createObjectNode() {
    ObjectNode node = new ObjectNode(null);
    node.set("property1", new TextNode("value1"));
    node.set("property2", new TextNode("value2"));
    return node;
  }
  
  private JsonNode createRootNode() {
    return new TextNode("root");
  }
  
  private JsonNode createTextValueNode() {
    return new TextNode("value");
  }
  
  private JsonNode createNullValueNode() {
    return NullNode.getInstance();
  }
  
  private JsonNode createNumericNode() {
    return new IntNode(123);
  }
}
