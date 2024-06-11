import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testCreateUsingDefault() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 assertEquals("DEFAULT_STRING", instantiator.createUsingDefault(mapper.getDeserializationContext()));
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureFromObjectSettings(
 new AnnotatedWithParams(PropertyName.construct("constructorWithOneArg"), String[].class, "constructorArg"),
 null, null, null, null, null
 );
 Object[] args = new Object[]{"constructorArgValue"};
 assertEquals("constructorArgValue", instantiator.createFromObjectWith(mapper.getDeserializationContext(), args));
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 StdValueInstantiator delegateInstantiator = mapper.findValueInstantiator(Integer.class);
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureDelegate(delegateInstantiator);
 assertEquals("delegateInstance", instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegateInstantiator));
 }

 @Test
 public void testCreateUsingArrayDelegate() throws Exception {
 StdValueInstantiator arrayDelegateInstantiator = mapper.findValueInstantiator(Integer[].class);
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureArrayDelegate(arrayDelegateInstantiator);
 Object[] arrayDelegateArguments = new Object[]{new Integer[]{1, 2}};
 assertEquals("delegateInstance", instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), arrayDelegateInstantiator));
 }

 @Test
 public void testCreateFromString() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureFromStringCreator(
 new AnnotatedWithParams(PropertyName.construct("fromStringCreator"), String.class)
 );
 assertEquals("fromStringValue", instantiator.createFromString(mapper.getDeserializationContext(), "fromStringValue"));
 }

 @Test
 public void testCreateFromInt() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
 instantiator.configureFromIntCreator(
 new AnnotatedWithParams(PropertyName.construct("fromIntCreator"), Int.TYPE)
 );
 assertEquals(42, instantiator.createFromInt(mapper.getDeserializationContext(), 42));
 }

 @Test
 public void testCreateFromLong() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Long.class);
 instantiator.configureFromLongCreator(
 new AnnotatedWithParams(PropertyName.construct("fromLongCreator"), Long.TYPE)
 );
 assertEquals(42L, instantiator.createFromLong(mapper.getDeserializationContext(), 42L));
 }

 @Test
 public void testCreateFromDouble() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Double.class);
 instantiator.configureFromDoubleCreator(
 new AnnotatedWithParams(PropertyName.construct("fromDoubleCreator"), Double.TYPE)
 );
 assertEquals(3.14, instantiator.createFromDouble(mapper.getDeserializationContext(), 3.14), 0.001);
 }

 @Test
 public void testCreateFromBoolean() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
 instantiator.configureFromBooleanCreator(
 new AnnotatedWithParams(PropertyName.construct("fromBooleanCreator"), Boolean.TYPE)
 );
 assertTrue(instantiator.createFromBoolean(mapper.getDeserializationContext(), true));
 assertFalse(instantiator.createFromBoolean(mapper.getDeserializationContext(), false));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDefault_Null() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 assertEquals(null, instantiator.createUsingDefault(mapper.getDeserializationContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromObjectWith_NullArguments() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureFromObjectSettings(
 new AnnotatedWithParams(PropertyName.construct("constructorWithOneArg"), String[].class, "constructorArg"),
 null, null, null, null, null
 );
 Object[] args = new Object[]{null};
 try {
 instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDelegate_NullDelegate() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 StdValueInstantiator delegateInstantiator = mapper.findValueInstantiator(Integer.class);
 instantiator.configureDelegate(delegateInstantiator);
 assertEquals("delegateInstance", instantiator.createUsingDelegate(mapper.getDeserializationContext(), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingArrayDelegate_NullArrayDelegate() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 StdValueInstantiator arrayDelegateInstantiator = mapper.findValueInstantiator(Integer[].class);
 instantiator.configureArrayDelegate(arrayDelegateInstantiator);
 Object[] arrayDelegateArguments = new Object[]{null};
 try {
 instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), arrayDelegateInstantiator);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromString_NullString() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
 instantiator.configureFromStringCreator(
 new AnnotatedWithParams(PropertyName.construct("fromStringCreator"), String.class)
 );
 try {
 instantiator.createFromString(mapper.getDeserializationContext(), null);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromInt_NegativeNumber() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
 instantiator.configureFromIntCreator(
 new AnnotatedWithParams(PropertyName.construct("fromIntCreator"), Int.TYPE)
 );
 try {
 instantiator.createFromInt(mapper.getDeserializationContext(), -1);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromLong_NegativeNumber() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Long.class);
 instantiator.configureFromLongCreator(
 new AnnotatedWithParams(PropertyName.construct("fromLongCreator"), Long.TYPE)
 );
 try {
 instantiator.createFromLong(mapper.getDeserializationContext(), -1L);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromDouble_NonNumber() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Double.class);
 instantiator.configureFromDoubleCreator(
 new AnnotatedWithParams(PropertyName.construct("fromDoubleCreator"), Double.TYPE)
 );
 try {
 instantiator.createFromDouble(mapper.getDeserializationContext(), "notANumber");
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromBoolean_NotBoolean() throws Exception {
 StdValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
 instantiator.configureFromBooleanCreator(
 new AnnotatedWithParams(PropertyName.construct("fromBooleanCreator"), Boolean.TYPE)
 );
 try {
 instantiator.createFromBoolean(mapper.getDeserializationContext(), 'x');
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }
}