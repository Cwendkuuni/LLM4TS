package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

  private JsonWriteContext jsonWriteContext;
  
  @Test
  public void testWriteFieldName_expectValue() throws JsonGenerationException {
    jsonWriteContext = JsonWriteContext.createRootContext();
    Assert.assertEquals(4, jsonWriteContext.writeFieldName("name"));
  }
  
  @Test
  public void testWriteFieldName_notExpectValue() throws JsonGenerationException {
    jsonWriteContext = JsonWriteContext.createRootContext();
    jsonWriteContext.writeFieldName("name");
    Assert.assertEquals(1, jsonWriteContext.writeFieldName("value"));
  }
  
  @Test
  public void testWriteValue_inObjectStatusOkAfterColon() {
    jsonWriteContext = JsonWriteContext.createRootContext();
    jsonWriteContext.writeFieldName("name");
    Assert.assertEquals(2, jsonWriteContext.writeValue());
  }
  
  @Test
  public void testWriteValue_inObjectStatusOkAsIs() {
    jsonWriteContext = JsonWriteContext.createRootContext();
    Assert.assertEquals(0, jsonWriteContext.writeValue());
  }

  @Test
  public void testWriteValue_inArrayStatusOkAfterComma() {
    jsonWriteContext = JsonWriteContext.createRootContext();
    Assert.assertEquals(1, jsonWriteContext.writeValue());
  }
  
  @Test
  public void testWriteValue_inArrayStatusOkAsIs() {
    jsonWriteContext = JsonWriteContext.createRootContext();
    jsonWriteContext.writeValue();
    Assert.assertEquals(0, jsonWriteContext.writeValue());
  }
  
  @Test
  public void testWriteValue_inRootContextStatusOkAfterSpace() {
    jsonWriteContext = JsonWriteContext.createRootContext();
    jsonWriteContext.writeValue();
    jsonWriteContext.writeValue();
    Assert.assertEquals(3, jsonWriteContext.writeValue());
  }
  
  @Test
  public void testWriteValue_inRootContextStatusOkAsIs() {
    jsonWriteContext = JsonWriteContext.createRootContext();
    Assert.assertEquals(0, jsonWriteContext.writeValue());
  }
  
  @Test
  public void testWriteValue_getCurrentIndex() {
    jsonWriteContext = JsonWriteContext.createRootContext();
    jsonWriteContext.writeValue();
    Assert.assertEquals(0, jsonWriteContext.getCurrentIndex());
  }
}