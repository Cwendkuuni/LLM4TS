import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDefaultCreator() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 assertEquals("Default Creator test", instantiator.createUsingDefault(mapper.getDeserializationContext()));
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingDefaultCreator() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(CustomClass.class);
 instantiator.createUsingDefault(mapper.getDeserializationContext());
 }

 @Test
 public void testCreateFromString() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 assertEquals("String Test", instantiator.createFromString(mapper.getDeserializationContext(), "String Test"));
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidString() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.createFromString(mapper.getDeserializationContext(), "Invalid");
 }

 @Test
 public void testCreateFromInt() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
 assertEquals(1, instantiator.createFromInt(mapper.getDeserializationContext(), 1));
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidInt() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
 instantiator.createFromInt(mapper.getDeserializationContext(), 0); // should throw exception
 }

 @Test
 public void testCreateFromBoolean() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
 assertEquals(true, instantiator.createFromBoolean(mapper.getDeserializationContext(), true));
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidBoolean() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
 instantiator.createFromBoolean(mapper.getDeserializationContext(), false); // should throw exception
 }

 @Test
 public void testCreateFromObjectWith() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(MyClassWithProperties.class);
 MyClassWithProperties obj = new MyClassWithProperties("Test", 1);
 SettableBeanProperty[] properties = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("prop1", String.class),
 new SettableBeanProperty.Std("prop2", Integer.class)
 };
 instantiator.createFromObjectWith(mapper.getDeserializationContext(), properties, new PropertyValueBuffer());
 assertEquals("Test", instantiator.createFromObjectWith(mapper.getDeserializationContext(), obj).getProp1());
 assertEquals(1, instantiator.createFromObjectWith(mapper.getDeserializationContext(), obj).getProp2());
 }

 @Test
 public void testMissingProperty() throws JsonProcessingException {
 ValueInstantiator instantiator = mapper.findValueInstantiator(MyClassWithProperties.class);
 SettableBeanProperty[] properties = new SettableBeanProperty[] {
 new SettableBeanProperty.Std("prop1", String.class)
 };
 instantiator.createFromObjectWith(mapper.getDeserializationContext(), properties, new PropertyValueBuffer());
 assertNull(instantiator.createFromObjectWith(mapper.getDeserializationContext(), new MyClassWithProperties()).getProp2());
 }
}