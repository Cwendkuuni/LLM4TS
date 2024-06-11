import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

 private static final Object MAP_KEY = new Object();
 private static final Object MAP_VALUE = new Object();
 private static final String MAP_TYPE_ID = "MapTypeId";
 private static final String PROPERTY_NAME = "propertyName";
 private static final String TYPE_ID = "TypeId";

 private ExternalTypeHandler handler;
 private Map<String, Object> map;

 @Before
 public void setup() throws IOException {
 List<ExtTypedProperty> properties = new ArrayList<>();
 SettableBeanProperty prop1 = new SettableBeanProperty(MAP_KEY, String.class);
 SettableBeanProperty prop2 = new SettableBeanProperty(MAP_VALUE, Map.class);
 TypeDeserializer typeDeserializer = new MapTypeDeserializer(Map.class, String.class);
 ExtTypedProperty extProp1 = new ExtTypedProperty(prop1, typeDeserializer);
 ExtTypedProperty extProp2 = new ExtTypedProperty(prop2, null);
 extProp1.linkTypeProperty(extProp2);
 properties.add(extProp1);
 properties.add(extProp2);
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
 new HashMap<>(), null, null).start();
 map = new HashMap<>();
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 JsonParser p = createParser();
 p.nextToken(); // [START_OBJECT]
 p.nextToken(); // field1: TypeId
 p.value(MAP_TYPE_ID);
 p.nextToken(); // ,
 p.nextToken(); // field2: propertyName
 p.value(PROPERTY_NAME);
 p.nextToken(); // ,
 p.nextToken(); // value: Map<String,Object>
 handler.handleTypePropertyValue(p, null, PROPERTY_NAME, map);

 // Check if the external type id has been processed correctly
 Assert.assertEquals(MAP_TYPE_ID, handler.getTypeId(0));
 // Check if the value has been stored correctly
 Assert.assertEquals(MAP_VALUE, map.get(PROPERTY_NAME));
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 JsonParser p = createParser();
 p.nextToken(); // [START_OBJECT]
 p.nextToken(); // field1: propertyName
 p.value(PROPERTY_NAME);
 p.nextToken(); // : Map<String,Object>
 p.beginObject();
 p.nextToken(); // property1
 p.value("key1");
 p.nextToken(); // : Object
 p.value(MAP_VALUE);
 p.nextToken(); // property2
 p.value("key2");
 p.nextToken(); // : Object
 p.value(MAP_VALUE);
 p.endObject();
 handler.handlePropertyValue(p, null, PROPERTY_NAME, map);

 // Check if the external type id has been processed correctly
 Assert.assertEquals(MAP_TYPE_ID, handler.getTypeId(0));
 // Check if the properties have been stored correctly
 Assert.assertEquals(2, map.size());
 // Check if the values have been stored correctly
 Assert.assertEquals(MAP_VALUE, map.get("key1"));
 Assert.assertEquals(MAP_VALUE, map.get("key2"));
 }

 @Test(expected = IOException.class)
 public void testHandleTypePropertyValueWithMissingTypeId() throws IOException {
 JsonParser p = createParser();
 p.nextToken(); // [START_OBJECT]
 p.nextToken(); // field1: propertyName
 p.value(PROPERTY_NAME);
 p.nextToken(); // ,
 p.nextToken(); // value: Map<String,Object>
 p.beginObject();
 p.nextToken(); // property1
 p.value("key1");
 p.nextToken(); // : Object
 p.value(MAP_VALUE);
 p.nextToken(); // property2
 p.value("key2");
 p.nextToken(); // : Object
 p.endObject();
 handler.handleTypePropertyValue(p, null, PROPERTY_NAME, map);

 // This test should fail because the typeId for the second property is missing
 }

 @Test(expected = IOException.class)
 public void testHandlePropertyValueWithMissingTypeId() throws IOException {
 JsonParser p = createParser();
 p.nextToken(); // [START_OBJECT]
 p.nextToken(); // field1: propertyName
 p.value(PROPERTY_NAME);
 p.nextToken(); // : Map<String,Object>
 p.beginObject();
 p.nextToken(); // property1
 p.value("key1");
 p.nextToken(); // : Object
 p.value(MAP_VALUE);
 p.endObject();
 // Missing the typeId for the second property
 p.nextToken(); // field2
 p.value("key2");
 p.nextToken(); // : Object
 p.value(MAP_VALUE);
 handler.handlePropertyValue(p, null, PROPERTY_NAME, map);

 // This test should fail because the typeId for the second property is missing
 }

 @Test
 public void testComplete() throws IOException {
 JsonParser p = createParser();
 p.nextToken(); // [START_OBJECT]
 p.nextToken(); // field1: propertyName
 p.value(PROPERTY_NAME);
 p.nextToken(); // : Map<String,Object>
 p.beginObject();
 p.nextToken(); // property1
 p.value("key1");
 p.nextToken(); // : Object
 p.value(MAP_VALUE);
 p.endObject();
 p.nextToken(); // field2
 p.value("key2");
 p.nextToken(); // : Object
 p.value(MAP_VALUE);
 p.endObject();
 handler.complete(p, null, map);

 // Check if the external type ids have been processed correctly
 Assert.assertEquals(MAP_TYPE_ID, handler.getTypeId(0));
 Assert.assertEquals(MAP_TYPE_ID, handler.getTypeId(1));
 // Check if the properties have been stored correctly
 Assert.assertEquals(2, map.size());
 // Check if the values have been stored correctly
 Assert.assertEquals(MAP_VALUE, map.get("key1"));
 Assert.assertEquals(MAP_VALUE, map.get("key2"));
 }

 @Test
 public void testCompleteWithNullValues() throws IOException {
 JsonParser p = createParser();
 p.nextToken(); // [START_OBJECT]
 p.nextToken(); // field1: propertyName
 p.value(PROPERTY_NAME);
 p.nextToken(); // : Map<String,Object>
 p.beginObject();
 p.nextToken(); // property1
 p.value("key1");
 p.nextToken(); // : null
 p.endObject();
 p.nextToken(); // field2
 p.value("key2");
 p.nextToken(); // : null
 p.endObject();
 handler.complete(p, null, map);

 // Check if the external type ids have been processed correctly
 Assert.assertEquals(MAP_TYPE_ID, handler.getTypeId(0));
 Assert.assertEquals(MAP_TYPE_ID, handler.getTypeId(1));
 // Check if the properties have been stored correctly with null values
 Assert.assertEquals(2, map.size());
 // Check if the values have been stored correctly
 Assert.assertNull(map.get("key1"));
 Assert.assertNull(map.get("key2"));
 }

 private JsonParser createParser() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 return objectMapper.getFactory().createParser(getClass().getResource("/test.json"));
 }

}