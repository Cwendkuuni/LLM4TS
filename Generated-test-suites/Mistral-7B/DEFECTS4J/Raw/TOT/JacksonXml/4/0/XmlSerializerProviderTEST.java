import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.*;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider serializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper(new XMLFactory());
 serializerProvider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 // Given
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 // When
 serializerProvider.serializeValue(xmlGenerator, null);
 // Then
 xmlGenerator.flush();
 ObjectNode result = objectMapper.readTree(xmlGenerator.getWriter().asString());
 assertEquals("null", result.getName());
 }

 @Test
 public void testSerializeEmptyList() throws JsonProcessingException {
 // Given
 ObjectNode node = objectMapper.createObjectNode();
 node.put("elements", objectMapper.createArrayNode());
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 // When
 serializerProvider.serializeValue(xmlGenerator, node);
 // Then
 xmlGenerator.flush();
 String xml = xmlGenerator.getWriter().asString();
 assertTrue(xml.contains("<elements/>"));
 }

 @Test
 public void testSerializeEmptyMap() throws JsonProcessingException {
 // Given
 ObjectNode node = objectMapper.createObjectNode();
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 // When
 serializerProvider.serializeValue(xmlGenerator, node);
 // Then
 xmlGenerator.flush();
 String xml = xmlGenerator.getWriter().asString();
 assertTrue(xml.contains("<propertyElementName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"mapType\"></propertyElementName>"));
 }

 @Test
 public void testSerializeNonEmptyList() throws JsonProcessingException {
 // Given
 ObjectNode node = objectMapper.createObjectNode();
 node.put("elements", objectMapper.createArrayNode().elementsAdd(1));
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 // When
 serializerProvider.serializeValue(xmlGenerator, node);
 // Then
 xmlGenerator.flush();
 String xml = xmlGenerator.getWriter().asString();
 assertTrue(xml.contains("<elements><item>1</item></elements>"));
 }

 @Test
 public void testSerializeNonEmptyMap() throws JsonProcessingException {
 // Given
 ObjectNode node = objectMapper.createObjectNode();
 node.put("propertyElementName", objectMapper.createObjectNode().put("value", 1));
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 // When
 serializerProvider.serializeValue(xmlGenerator, node);
 // Then
 xmlGenerator.flush();
 String xml = xmlGenerator.getWriter().asString();
 assertTrue(xml.contains("<propertyElementName><value>1</value></propertyElementName>"));
 }

 @Test
 public void testSerializeBean() throws JsonProcessingException {
 // Given
 MyBean bean = new MyBean("test");
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
 // When
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setSerializerProvider(serializerProvider)
 .setBeanPropertyFilter(filter);
 serializeValue(xmlGenerator, objectMapper.writeValueAsString(bean));
 // Then
 xmlGenerator.flush();
 String xml = xmlGenerator.getWriter().asString();
 assertTrue(xml.contains("<MyBean><name>test</name></MyBean>"));
 }

 @Test
 public void testSerializeBeanWithCustomSerializer() throws JsonProcessingException {
 // Given
 MyCustomBean bean = new MyCustomBean(1);
 ObjectNode node = objectMapper.createObjectNode();
 node.put("myCustomBean", bean);
 ToXmlGenerator xmlGenerator = StaxUtil.createXmlGenerator();
 // When
 StdSerializer<MyCustomBean> serializer = new StdSerializer<MyCustomBean>(MyCustomBean.class) {
 protected void serializeChildren(MyCustomBean bean, ToXmlGenerator xgen) throws IOException, XMLStreamException {
 xgen.writeNumber(bean.getValue());
 }
 };
 ObjectMapper customObjectMapper = new ObjectMapper();
 customObjectMapper.setSerializerProvider(serializerProvider)
 .registerSerializer(MyCustomBean.class, serializer);
 ObjectNode result = customObjectMapper.readTree(serializerProvider.serializeValueAsString(node, MyCustomBean.class));
 // Then
 assertEquals(1, result.get("myCustomBean").asInt());
 }
}

class MyBean {

 private String name;

 public MyBean(String name) {
 this.name = name;
 }

 public String getName() {
 return name;
 }
}

class MyCustomBean {

 private int value;

 public MyCustomBean(int value) {
 this.value = value;
 }

 public int getValue() {
 return value;
 }
}