import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler testHandler;
 private JsonParser testParser;
 private Object testBean;
 private DeserializationContext testContext;
 private final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 testParser = mapper.getFactory().createParser("{\"property1\": \"value1\", \"property2\": \"value2\"}");
 testBean = new Object();
 testContext = mapper.getDeserializationContext();
 }

 @Test
 public void testHandleTypePropertyValue() throws Exception {
 String propName = "property1";
 String typeId = "StringTypeId";
 ExtTypedProperty testProperty = new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class));
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] { testProperty }, null, null, null);
 testHandler.start();
 testHandler.handleTypePropertyValue(testParser, testContext, propName, testBean);
 assertEquals("value1", testBean.getClass().getField("property1").get(testBean));
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueWithMissingProperty() throws Exception {
 String propName = "missingProperty";
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] {}, null, null, null);
 testHandler.start();
 testHandler.handleTypePropertyValue(testParser, testContext, propName, testBean);
 fail("should have thrown IOException");
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueWithMissingTypeId() throws Exception {
 String propName = "property1";
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] { new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class)) }, null, null, null);
 testHandler.start();
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume property1
 testParser.nextToken(); // consume "value1"
 testHandler.handleTypePropertyValue(testParser, testContext, propName, testBean);
 fail("should have thrown IOException");
 }

 @Test
 public void testHandlePropertyValue() throws Exception {
 String propName = "property1";
 String typeId = "StringTypeId";
 ExtTypedProperty testProperty = new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class));
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] { testProperty }, null, null, null);
 testHandler.start();
 testHandler.handlePropertyValue(testParser, testContext, propName, testBean);
 assertEquals("value1", testBean.getClass().getField("property1").get(testBean));
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithoutTypeId() throws Exception {
 String propName = "property1";
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] { new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class)) }, null, null, null);
 testHandler.start();
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume property1
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume end of property value
 testHandler.handlePropertyValue(testParser, testContext, propName, testBean);
 fail("should have thrown IOException");
 }

 @Test
 public void testHandlePropertyValueWithNullBean() throws Exception {
 String propName = "property1";
 String typeId = "StringTypeId";
 ExtTypedProperty testProperty = new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class));
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] { testProperty }, null, null, null);
 testHandler.start();
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume property1
 testParser.nextToken(); // consume "value1"
 testHandler.handlePropertyValue(testParser, testContext, propName, null);
 fail("should have thrown IOException");
 }

 @Test
 public void testHandlePropertyValueWithNullTokens() throws Exception {
 String propName = "property1";
 String typeId = "StringTypeId";
 ExtTypedProperty testProperty = new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class));
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] { testProperty }, null, null, null);
 testHandler.start();
 testHandler.handlePropertyValue(testParser, testContext, propName, testBean);
 testHandler.handlePropertyValue(testParser, testContext, propName, testBean);
 assertEquals("value1", testBean.getClass().getField("property1").get(testBean));
 }

 @Test
 public void testComplete() throws Exception {
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class)),
 new ExtTypedProperty(new SettableBeanProperty("property2", Integer.class), new SimpleTypeDeserializer(Integer.class)) }, null, null, null);
 testHandler.start();
 testHandler.handlePropertyValue(testParser, testContext, "property1", testBean);
 testHandler.handlePropertyValue(testParser, testContext, "property2", testBean);
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume START_ARRAY
 testParser.nextToken(); // consume "StringTypeId"
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume "property1"
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume "IntegerTypeId"
 testParser.nextToken(); // consume "2"
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume END_ARRAY
 testHandler.complete(testParser, testContext, testBean);
 assertEquals("value1", testBean.getClass().getField("property1").get(testBean));
 assertEquals(2, testBean.getClass().getField("property2").get(testBean));
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingTypeIds() throws Exception {
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class)),
 new ExtTypedProperty(new SettableBeanProperty("property2", Integer.class), new SimpleTypeDeserializer(Integer.class)) }, null, null, null);
 testHandler.start();
 testHandler.handlePropertyValue(testParser, testContext, "property1", testBean);
 testHandler.handlePropertyValue(testParser, testContext, "property2", testBean);
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume START_ARRAY
 testParser.nextToken(); // consume "StringTypeId"
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume "property1"
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume END_ARRAY
 testHandler.complete(testParser, testContext, testBean);
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume property2
 testParser.nextToken(); // consume "value2"
 testParser.nextToken(); // consume END_OBJECT
 fail("should have thrown IOException");
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingProperties() throws Exception {
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class)),
 new ExtTypedProperty(new SettableBeanProperty("property2", Integer.class), new SimpleTypeDeserializer(Integer.class)) }, null, null, null);
 testHandler.start();
 testHandler.handlePropertyValue(testParser, testContext, "property1", testBean);
 testHandler.handlePropertyValue(testParser, testContext, "property2", testBean);
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume START_ARRAY
 testParser.nextToken(); // consume "StringTypeId"
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume "property1"
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume "IntegerTypeId"
 testParser.nextToken(); // consume null
 testParser.nextToken(); // consume END_ARRAY
 testHandler.complete(testParser, testContext, testBean);
 fail("should have thrown IOException");
 }

 @Test
 public void testCompleteWithDefaultValues() throws Exception {
 testHandler = new ExternalTypeHandler(new ExtTypedProperty[] {
 new ExtTypedProperty(new SettableBeanProperty("property1", String.class), new SimpleTypeDeserializer(String.class)),
 new ExtTypedProperty(new SettableBeanProperty("property2", Integer.class), new SimpleTypeDeserializer(Integer.class)) }, null, null, null);
 testHandler.start();
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume property1
 testParser.nextToken(); // consume "value1"
 testHandler.handlePropertyValue(testParser, testContext, "property1", testBean);
 testParser.nextToken(); // consume property2
 testParser.nextToken(); // consume null
 testHandler.handlePropertyValue(testParser, testContext, "property2", testBean);
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume START_ARRAY
 testParser.nextToken(); // consume "StringTypeId"
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume START_OBJECT
 testParser.nextToken(); // consume "property1"
 testParser.nextToken(); // consume "value1"
 testParser.nextToken(); // consume END_OBJECT
 testParser.nextToken(); // consume END_ARRAY
 testHandler.complete(testParser, testContext, testBean);
 assertEquals("value1", testBean.getClass().getField("property1").get(testBean));
 assertEquals(0, testBean.getClass().getField("property2").get(testBean));
 }

 @Test
 public void testBuilder() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
 SettableBeanProperty property1 = new SettableBeanProperty("property1", String.class);
 SettableBeanProperty property2 = new SettableBeanProperty("property2", Integer.class);
 builder.addExternal(property1, new SimpleTypeDeserializer(String.class));
 builder.addExternal(property2, new SimpleTypeDeserializer(Integer.class));
 ExternalTypeHandler handler = builder.build();
 assertEquals("property1", handler.getProperties()[0].getProperty().getName());
 assertEquals("property2", handler.getProperties()[1].getProperty().getName());
 }
}