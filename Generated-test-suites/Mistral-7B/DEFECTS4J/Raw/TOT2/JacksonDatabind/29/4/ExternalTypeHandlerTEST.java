import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new TestBean().getClass().getDeclaredField("prop1"), new StdKeyDeserializer(String.class))
 .addExternal(new TestBean().getClass().getDeclaredField("prop2"), new StdKeyDeserializer(Integer.class))
 .build());
 }

 @Test
 public void testNormalCase() throws IOException {
 ObjectNode jsonObject = mapper.createObjectNode();
 jsonObject.put("prop1", "test1");
 jsonObject.put("prop2", 1);
 TestBean testBean = mapper.reader(TestBean.class).readValue(jsonObject);

 Assert.assertEquals("test1", testBean.getProp1());
 Assert.assertEquals(1, testBean.getProp2());
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 ObjectNode jsonObject = mapper.createObjectNode();
 jsonObject.put("prop1", "TestType1");
 TestBean testBean = new TestBean();

 JsonParser jp = mapper.parserForObject(jsonObject);
 handler.handleTypePropertyValue(jp, null, "prop1", testBean);

 Assert.assertEquals("TestType1", testBean.getTypeId1());
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 ObjectNode jsonObject = mapper.createObjectNode();
 jsonObject.put("prop1", "test1");
 jsonObject.put("prop2", 1);
 TestBean testBean = new TestBean();

 JsonParser jp = mapper.parserForObject(jsonObject);
 handler.handlePropertyValue(jp, null, "prop1", testBean);

 Assert.assertEquals("test1", testBean.getProp1());

 JsonParser jp2 = mapper.parserForObject(jsonObject);
 handler.handlePropertyValue(jp2, null, "prop2", testBean);

 Assert.assertEquals(1, testBean.getProp2());
 }

 @Test(expected = IOException.class)
 public void testMissingProperty() throws IOException {
 ObjectNode jsonObject = mapper.createObjectNode();
 jsonObject.put("missingProp", "test1");
 TestBean testBean = new TestBean();

 handler.handlePropertyValue(mapper.parserForObject(jsonObject), null, "missingProp", testBean);
 }

 @Test(expected = IOException.class)
 public void testMissingTypeId() throws IOException {
 ObjectNode jsonObject = mapper.createObjectNode();
 jsonObject.put("prop1", "TestType1");
 TestBean testBean = new TestBean();

 JsonParser jp = mapper.parserForObject(jsonObject);
 handler.handlePropertyValue(jp, null, "prop1", testBean);
 jp.nextToken(); // consume type id token

 Assert.assertFalse(testBean.isTypeIdSet());
 }

 @Test(expected = IOException.class)
 public void testMissingTypeAndProperty() throws IOException {
 ObjectNode jsonObject = mapper.createObjectNode();
 TestBean testBean = new TestBean();

 JsonParser jp = mapper.parserForObject(jsonObject);
 handler.handlePropertyValue(jp, null, "missingProp", testBean);
 }

 @Test
 public void testComplete() throws IOException {
 ObjectNode jsonObject = mapper.createObjectNode();
 jsonObject.put("prop1", "TestType1");
 jsonObject.put("prop2", 1);
 TestBean testBean = new TestBean();

 handler.handlePropertyValue(mapper.parserForObject(jsonObject), null, "prop1", testBean);
 handler.handlePropertyValue(mapper.parserForObject(jsonObject), null, "prop2", testBean);
 Object completedBean = handler.complete(mapper.parserForObject(jsonObject), null, testBean);

 Assert.assertEquals("TestType1", completedBean.getClass().getDeclaredField("typeId1").get(completedBean));
 Assert.assertEquals(1, completedBean.getClass().getDeclaredField("prop2").get(completedBean));
 }

 @Test
 public void testCompleteWithNaturalType() throws IOException {
 ObjectNode jsonObject = mapper.createObjectNode();
 jsonObject.put("prop1", "test1");
 TestBean testBean = new TestBean();

 handler.handlePropertyValue(mapper.parserForObject(jsonObject), null, "prop1", testBean);
 Object completedBean = handler.complete(mapper.parserForObject(jsonObject), null, null, null);

 Assert.assertEquals("test1", completedBean.getClass().getDeclaredField("prop1").get(completedBean));
 }
}

class TestBean {
 private String typeId1;
 private String prop1;
 private Integer prop2;
 private Integer typeId2;

 public String getTypeId1() { return typeId1; }
 public void setTypeId1(String typeId1) { this.typeId1 = typeId1; }
 public String getProp1() { return prop1; }
 public void setProp1(String prop1) { this.prop1 = prop1; }
 public Integer getProp2() { return prop2; }
 public void setProp2(Integer prop2) { this.prop2 = prop2; }
 public boolean isTypeIdSet() { return typeId1 != null; }
}