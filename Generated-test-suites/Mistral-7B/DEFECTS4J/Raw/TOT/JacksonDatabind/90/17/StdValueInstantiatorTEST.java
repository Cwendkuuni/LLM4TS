import org.junit.Test;
import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

 private DeserializationContext createContext() {
 return new TestNameContext(getClass(), "test");
 }

 @Test
 public void testCreateUsingDefault() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
 Object value = instantiator.createUsingDefault(createContext());
 assertEquals("Default value created using StdValueInstantiator", value);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDefaultWithNullValueType() {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
 instantiator.createUsingDefault(createContext());
 fail("CreateUsingDefault should have thrown JsonMappingException when valueType is null");
 }

 @Test
 public void testCreateUsingDelegate() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
 instantiator.configureFromObjectSettings(
 new AnnotatedWithParams(MyDelegateClass.class),
 null,
 null,
 null,
 null,
 null
 );
 Object value = instantiator.createUsingDelegate(createContext(), new Object());
 assertEquals("Value created using delegate", value);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDelegateWithNullDelegate() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
 instantiator.configureFromObjectSettings(
 null,
 null,
 null,
 null,
 null,
 null
 );
 instantiator.createUsingDelegate(createContext(), new Object());
 fail("CreateUsingDelegate should have thrown JsonMappingException when delegate is null");
 }

 @Test
 public void testCreateUsingArrayDelegate() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, int[].class);
 instantiator.configureFromArraySettings(
 new AnnotatedWithParams(MyArrayDelegateClass.class),
 int[].class,
 new SettableBeanProperty[]{new BeanProperty.Standardifier()}
 );
 Object value = instantiator.createUsingArrayDelegate(createContext(), new Object());
 assertArrayEquals(new int[]{1, 2, 3}, value);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingArrayDelegateWithNullArrayDelegate() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, int[].class);
 instantiator.configureFromArraySettings(
 null,
 null,
 null
 );
 instantiator.createUsingArrayDelegate(createContext(), new Object());
 fail("CreateUsingArrayDelegate should have thrown JsonMappingException when arrayDelegate is null");
 }

 @Test
 public void testCreateFromString() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
 instantiator.configureFromStringCreator(new AnnotatedWithParams(ToStringCreator.class));
 Object value = instantiator.createFromString(createContext(), "Hello World");
 assertEquals("Hello World", value);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromStringWithNullFromStringCreator() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
 instantiator.createFromString(createContext(), "Hello World");
 fail("CreateFromString should have thrown JsonMappingException when fromStringCreator is null");
 }

 @Test
 public void testCreateFromInt() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, Integer.class);
 instantiator.configureFromIntCreator(new AnnotatedWithParams(IntCreator.class));
 Object value = instantiator.createFromInt(createContext(), 42);
 assertEquals(42, value);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromIntWithNullFromIntCreator() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, Integer.class);
 instantiator.createFromInt(createContext(), 42);
 fail("CreateFromInt should have thrown JsonMappingException when fromIntCreator is null");
 }

 @Test
 public void testCreateFromBoolean() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, Boolean.class);
 instantiator.configureFromBooleanCreator(new AnnotatedWithParams(BooleanCreator.class));
 Object value = instantiator.createFromBoolean(createContext(), true);
 assertEquals(true, value);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromBooleanWithNullFromBooleanCreator() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, Boolean.class);
 instantiator.createFromBoolean(createContext(), true);
 fail("CreateFromBoolean should have thrown JsonMappingException when fromBooleanCreator is null");
 }
}