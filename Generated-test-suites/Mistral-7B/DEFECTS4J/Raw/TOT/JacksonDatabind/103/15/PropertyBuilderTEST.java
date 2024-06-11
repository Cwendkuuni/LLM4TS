import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

 private ObjectNode objectNode;
 private ObjectMapper objectMapper;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 objectNode = objectMapper.createObjectNode();
 propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(),
 new BeanDescription(MyClass.class));
 }

 @Test
 public void testPropertyBuilderWithSimpleType() throws JsonMappingException {
 MyClass myClass = new MyClass("simple value");
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClass.class.getType(), null, null, null, null, false).writeValue(objectNode, myClass);
 assertEquals("simple value", objectNode.get("property").asText());
 }

 @Test
 public void testPropertyBuilderWithContainerType() throws JsonMappingException {
 MyClassWithList myClass = new MyClassWithList();
 myClass.getList().add("container value 1");
 myClass.getList().add("container value 2");
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClassWithList.class.getType(), null, null, null, null, false).writeValue(objectNode, myClass);
 assertEquals("[container value 1, container value 2]", objectNode.get("list").asText());
 }

 @Test
 public void testPropertyBuilderWithNullValue() throws JsonMappingException {
 MyClassWithNull myClass = new MyClassWithNull();
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClassWithNull.class.getType(), null, null, null, null, false).writeValue(objectNode, myClass);
 assertNull(objectNode.get("property"));
 }

 @Test
 public void testPropertyBuilderWithNonDefaultInclude() throws JsonMappingException {
 MyClassWithDefaultValue myClass = new MyClassWithDefaultValue();
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClassWithDefaultValue.class.getType(), null, null, null, null, true);
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNull(defaultBean); // since no default bean is created
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClassWithDefaultValue.class.getType(), null, null, null, null, true).writeValue(objectNode, myClass);
 assertNull(objectNode.get("property"));
 }

 @Test
 public void testPropertyBuilderWithCustomInclude() throws JsonMappingException {
 MyClassWithCustomInclude myClass = new MyClassWithCustomInclude();
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClassWithCustomInclude.class.getType(), null, null, null, null, false)
 .withValueFilter(new ValueInclusionFilter())
 .writeValue(objectNode, myClass);
 assertNull(objectNode.get("property"));
 }

 @Test
 public void testPropertyBuilderWithNonEmptyInclude() throws JsonMappingException {
 MyClassWithNonEmpty myClass = new MyClassWithNonEmpty();
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClassWithNonEmpty.class.getType(), null, null, null, null, false).writeValue(objectNode, myClass);
 assertNull(objectNode.get("property"));
 }

 @Test(expected = JsonMappingException.class)
 public void testPropertyBuilderWithNullSerializer() throws JsonMappingException {
 MyClassWithNullSerializer myClass = new MyClassWithNullSerializer();
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClassWithNullSerializer.class.getType(), null, null, null, null, false)
 .assignNullSerializer(null);
 }

 @Test
 public void testPropertyBuilderWithUnwrappingWriter() throws JsonMappingException {
 MyClassWithWrapper myClass = new MyClassWithWrapper("wrapped value");
 propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
 MyClassWithWrapper.class.getType(), null, null, null, null, false)
 .unwrappingWriter(new NameTransformer() {
 @Override public String transformDeserializationPropertyName(String name) {
 return "wrappedProperty";
 }
 })
 .writeValue(objectNode, myClass);
 assertEquals("wrapped value", objectNode.get("wrappedProperty").asText());
 }

 @Test
 public void testPropertyBuilderWithCustomNullSerializer() throws JsonMappingException {
 MyClassWithCustomNullSerializer myClass = new MyClassWithCustomNullSerializer();
 JavaType type = MyClassWithCustomNullSerializer.class.getType();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 BeanPropertyWriter writer = propertyBuilder.buildWriter(serializerProvider, null, type, null, null, null, null, false);
 writer.assignNullSerializer(new MyCustomNullSerializer());
 ObjectWriter objectWriter = serializerProvider.getWriterFactory()
 .withView(new StaticTypingSerializerView(type))
 .withSerializer(writer);
 ObjectWriter writerForValue = objectWriter.withRootValueSerializer(new MyRootValueSerializer());
 StringWriter stringWriter = new StringWriter();
 writerForValue.writeValue(stringWriter, myClass);
 assertEquals("custom null value", stringWriter.toString());
 }

 @Test
 public void testPropertyBuilderWithStaticTyping() throws JsonMappingException {
 MyClassWithStaticTyping myClass = new MyClassWithStaticTyping();
 JavaType type = MyClassWithStaticTyping.class.getType();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 BeanPropertyWriter writer = propertyBuilder.buildWriter(serializerProvider, null, type, null, null, null, null, true);
 ObjectWriter objectWriter = serializerProvider.getWriterFactory()
 .withView(new StaticTypingSerializerView(type))
 .withSerializer(writer);
 StringWriter stringWriter = new StringWriter();
 objectWriter.writeValue(stringWriter, myClass);
 assertEquals("MyClassWithStaticTyping", stringWriter.toString());
 }
}

class MyClass {
 String property;

 public MyClass(String property) {
 this.property = property;
 }
}

class MyClassWithList {
 List<String> list = new ArrayList<>();

 public MyClassWithList() {
 }

 public List<String> getList() {
 return list;
 }
}

class MyClassWithNull {
 @JsonSerialize(nulls = JsonSerialize.Null.USE_EMPTY)
 String property;

 public MyClassWithNull() {
 }
}

class MyClassWithCustomInclude {
 @JsonInclude(JsonInclude.Include.NON_EMPTY)
 String property;

 public MyClassWithCustomInclude() {
 }
}

class MyClassWithEmpty {
 @JsonInclude(JsonInclude.Include.NON_EMPTY)
 String property;

 public MyClassWithEmpty() {
 }
}

class MyClassWithNullSerializer {
 @JsonSerialize(using = MyNullSerializer.class)
 String property;

 public MyClassWithNullSerializer() {
 }
}

class MyClassWithCustomNullSerializer {
 @JsonSerialize(using = MyCustomNullSerializer.class)
 String property;

 public MyClassWithCustomNullSerializer() {
 }
}

class MyClassWithWrapper {
 @JsonSerialize(using = MyWrapperSerializer.class)
 String wrappedProperty;

 public MyClassWithWrapper(String wrappedProperty) {
 this.wrappedProperty = wrappedProperty;
 }
}

class MyRootValueSerializer extends StdSerializer<Object> {

 public MyRootValueSerializer() {
 this(null);
 }

 protected MyRootValueSerializer(Class<Object> t) {
 super(t);
 }

 @Override public void serializeValues(Object[] values, JsonGenerator jg) throws IOException {
 jg.writeValue(values[0].toString());
 }
}

class MyCustomNullSerializer extends JsonSerializer<Object> {

 @Override public void serialize(Object value, JsonGenerator jg, SerializerProvider p)
 throws IOException {
 jg.writeValue("custom null value");
 }
}

class MyNullSerializer extends JsonSerializer<Object> {

 @Override public void serialize(Object value, JsonGenerator jg, SerializerProvider p)
 throws IOException {
 // empty implementation
 }
}

class MyWrapperSerializer extends JsonSerializer<MyClassWithWrapper> {

 @Override public void serialize(MyClassWithWrapper value, JsonGenerator jg, SerializerProvider p)
 throws IOException {
 jg.writeFieldName("wrappedProperty");
 jg.writeValue(value.getWrappedProperty());
 }
}