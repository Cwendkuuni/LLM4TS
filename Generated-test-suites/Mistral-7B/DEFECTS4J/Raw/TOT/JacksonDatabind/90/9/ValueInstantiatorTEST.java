package com.fasterxml.jackson.databind.deser.test;

 import com.fasterxml.jackson.annotation.JsonCreator;
 import com.fasterxml.jackson.core.type.TypeReference;
 import com.fasterxml.jackson.databind.*;
 import com.fasterxml.jackson.databind.deser.ValueInstantiator;
 import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
 import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
 import org.junit.Test;
 import static org.junit.Assert.*;

 public class ValueInstantiatorTest {

 private static class DummyValueInstantiator extends ValueInstantiator {
 private static final long serialVersionUID = 1L;

 @Override
 public Class<?> getValueClass() {
 return Dummy.class;
 }

 @Override
 public boolean canCreateUsingDefault() {
 return true;
 }

 @Override
 public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
 return new Dummy();
 }

 @Override
 public boolean canCreateFromString() {
 return false;
 }

 @Override
 public boolean canCreateFromInt() {
 return false;
 }

 @Override
 public boolean canCreateFromLong() {
 return false;
 }

 @Override
 public boolean canCreateFromDouble() {
 return false;
 }

 @Override
 public boolean canCreateFromBoolean() {
 return false;
 }

 @JsonCreator
 public static Dummy createFromString(String value) {
 return new Dummy(value);
 }
 }

 @Test
 public void testDummyValueInstantiator_canInstantiate_defaultConstructor() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ValueInstantiator instantiator = new DummyValueInstantiator();
 Object result = instantiator.createUsingDefault(mapper.getDeserializationContext());
 assertEquals(Dummy.class, result.getClass());
 }

 @Test(expected = IOException.class)
 public void testDummyValueInstantiator_cannotCreateFromString() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 ValueInstantiator instantiator = new DummyValueInstantiator();
 AnnotatedClass ac = mapper.getTypeFactory().constructType(new TypeReference<ValueInstantiator>() {});
 instantiator = mapper.findValueInstantiator(ac);
 String input = "{\"string\":\"test\"}";
 JsonParser parser = mapper.getFactory().createParser(input);
 assertNull(instantiator.createFromObjectWith(parser, new Object[] {}));
 }

 @Test
 public void testDummyValueInstantiator_canCreateUsingDelegate() throws Exception {
 Class<Dummy> dummyClass = Dummy.class;
 ValueInstantiator instantiator = new StdValueInstantiator(dummyClass);
 instantiator = (ValueInstantiator) instantiator.deserializeFromObject(new ObjectMapper(), new Object());
 Object result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), instantiator);
 assertEquals(Dummy.class, result.getClass());
 }

 @Test(expected = IOException.class)
 public void testDummyValueInstantiator_cannotCreateUsingDelegate() throws Exception {
 Class<Dummy> dummyClass = Dummy.class;
 ValueInstantiator instantiator = new StdValueInstantiator(dummyClass);
 instantiator = (ValueInstantiator) instantiator.deserializeFromObject(new ObjectMapper(), new Object());
 instantiator.canCreateUsingDelegate();
 }

 @Test(expected = IOException.class)
 public void testDummyValueInstantiator_cannotCreateUsingDefault_noDefaultConstructor() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 AnnotatedClass ac = mapper.getTypeFactory().constructType(new TypeReference<ValueInstantiator>() {});
 instantiator = mapper.findValueInstantiator(ac);
 assertNotNull(instantiator);
 assertFalse(instantiator.canInstantiate());
 }
}