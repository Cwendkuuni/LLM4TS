import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;

public class ValueInstantiatorTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testValueInstantiator_getValueClass() {
 ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
 assertEquals(String.class, valueInstantiator.getValueClass());
 }

 @Test
 public void testValueInstantiator_getValueTypeDesc() {
 ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
 assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
 }

 @Test
 public void testValueInstantiator_canInstantiate() throws Exception {
 ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);
 assertTrue(valueInstantiator.canInstantiate());
 }

 @Test
 public void testValueInstantiator_canCreateFromObjectWith_NullArguments() {
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return new SettableBeanProperty[0];
 }

 @Override
 public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
 return null;
 }
 };
 assertNull(valueInstantiator.createFromObjectWith(null, (Object[]) null));
 }

 @Test
 public void testValueInstantiator_canCreateFromObjectWith_InvalidArguments() throws Exception {
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return new SettableBeanProperty[1];
 }

 @Override
 public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
 return null;
 }
 };
 assertNull(valueInstantiator.createFromObjectWith(null, new Object[1]));
 }

 @Test
 public void testValueInstantiator_createFromObjectWith_ValidArguments() throws Exception {
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return new SettableBeanProperty[] { new PropertyName("name"), new PropertyName("age") };
 }

 @Override
 public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
 String name = (String) args[0];
 int age = (int) args[1];
 return new Person(name, age);
 }
 };
 JsonFactory jsonFactory = new JsonFactory();
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("name", "John Doe");
 node.put("age", 30);
 Object value = valueInstantiator.createFromObjectWith(ctxt, node.fields());
 assertEquals("John Doe", value.getClass().getMethod("getName").invoke(value));
 assertEquals(30, value.getClass().getMethod("getAge").invoke(value));
 }

 @Test
 public void testValueInstantiator_createFromString() throws Exception {
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
 return mapper.readValue(value, String.class);
 }
 };
 assertEquals("Hello World", valueInstantiator.createFromString(null, "Hello World"));
 }

 @Test(expected = IOException.class)
 public void testValueInstantiator_createFromString_NullValue() throws Exception {
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
 return mapper.readValue(value, String.class);
 }
 };
 valueInstantiator.createFromString(null, null);
 }

 @Test
 public void testValueInstantiator_createFromInt() throws Exception {
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
 return value;
 }
 };
 assertEquals(42, valueInstantiator.createFromInt(null, 42));
 }

 @Test(expected = IOException.class)
 public void testValueInstantiator_createFromInt_InvalidValue() throws Exception {
 ValueInstantiator valueInstantiator = new ValueInstantiator() {
 @Override
 public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
 return value;
 }
 };
 valueInstantiator.createFromInt(null, Integer.MIN_VALUE);
 }
}