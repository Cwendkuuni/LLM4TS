package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

 private StdValueInstantiator instantiator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 Class<String> stringClass = String.class;
 JavaType stringType = new SimpleTypeReference<String>(String.class) {};
 StdValueInstantiator instantiator = new StdValueInstantiator(null, stringType);
 }

 @Test
 public void testCreateUsingDefault() throws Exception {
 Object result = instantiator.createUsingDefault(context());
 assertNotNull(result);
 assertTrue(result instanceof String);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDefaultWithNullValueType() throws Exception {
 instantiator = new StdValueInstantiator(null, null);
 Object result = instantiator.createUsingDefault(context());
 fail("Should not be able to create using default with null value type");
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 String delegate = "Test Delegate";
 instantiator.configureFromDelegateSettings(context().findValueInjector(), stringClass, null);
 Object result = instantiator.createUsingDelegate(context(), delegate);
 assertNotNull(result);
 assertTrue(result instanceof String);
 assertEquals(delegate, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDelegateWithNullDelegate() throws Exception {
 instantiator.configureFromDelegateSettings(context().findValueInjector(), stringClass, null);
 Object result = instantiator.createUsingDelegate(context(), null);
 fail("Should not be able to create using delegate with null delegate");
 }

 @Test
 public void testCreateUsingDelegateWithValueInjector() throws Exception {
 JavaType delegateType = new SimpleTypeReference<MyDelegateClass>(MyDelegateClass.class) {};
 MyDelegateClass delegate = new MyDelegateClass();
 instantiator.configureFromDelegateSettings(context().findValueInjector(), delegateType, null);
 Object result = instantiator.createUsingDelegate(context(), delegate);
 assertNotNull(result);
 assertTrue(result instanceof String);
 assertEquals(delegate.getValue(), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingDelegateWithNullDelegateType() throws Exception {
 instantiator.configureFromDelegateSettings(context().findValueInjector(), null, null);
 Object result = instantiator.createUsingDelegate(context(), new Object());
 fail("Should not be able to create using delegate with null delegate type");
 }

 @Test
 public void testCreateUsingArrayDelegate() throws Exception {
 int[] array = new int[] {1, 2, 3};
 instantiator.configureFromArraySettings(context().findValueInjector(), stringType, null);
 Object result = instantiator.createUsingArrayDelegate(context(), array);
 assertNotNull(result);
 assertTrue(result instanceof String[]);
 assertArrayEquals(new String[] {"1", "2", "3"}, (String[]) result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingArrayDelegateWithNullArray() throws Exception {
 instantiator.configureFromArraySettings(context().findValueInjector(), stringType, null);
 Object result = instantiator.createUsingArrayDelegate(context(), null);
 fail("Should not be able to create using array delegate with null array");
 }

 @Test
 public void testCreateFromString() throws Exception {
 String stringValue = "Test String";
 Object result = instantiator.createFromString(context(), stringValue);
 assertNotNull(result);
 assertTrue(result instanceof String);
 assertEquals(stringValue, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromStringWithNullValue() throws Exception {
 Object result = instantiator.createFromString(context(), null);
 fail("Should not be able to create from string with null value");
 }

 @Test
 public void testCreateFromInt() throws Exception {
 int intValue = 42;
 Object result = instantiator.createFromInt(context(), intValue);
 assertNotNull(result);
 assertTrue(result instanceof String);
 assertEquals(Integer.toString(intValue), result);
 }

 @Test
 public void testCreateFromLong() throws Exception {
 long longValue = 12345L;
 Object result = instantiator.createFromLong(context(), longValue);
 assertNotNull(result);
 assertTrue(result instanceof String);
 assertEquals(Long.toString(longValue), result);
 }

 @Test
 public void testCreateFromDouble() throws Exception {
 double doubleValue = 3.141592653589793;
 Object result = instantiator.createFromDouble(context(), doubleValue);
 assertNotNull(result);
 assertTrue(result instanceof String);
 assertEquals(Double.toString(doubleValue), result);
 }

 @Test
 public void testCreateFromBoolean() throws Exception {
 boolean booleanValue = true;
 Object result = instantiator.createFromBoolean(context(), booleanValue);
 assertNotNull(result);
 assertTrue(result instanceof String);
 assertEquals(Boolean.toString(booleanValue), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromBooleanWithNullValue() throws Exception {
 Object result = instantiator.createFromBoolean(context(), null);
 fail("Should not be able to create from boolean with null value");
 }

 @Test
 public void testCreateUsingObjectWith() throws Exception {
 String[] constructorArguments = {"Argument 1", "Argument 2"};
 instantiator.configureFromObjectSettings(context().findValueInjector(), null, null, constructorArguments);
 Object result = instantiator.createFromObjectWith(context(), new Object[] {new Object[] {"Argument 1", "Argument 2"}});
 assertNotNull(result);
 assertTrue(result instanceof String);
 //TODO: Implement proper assertion here
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingObjectWithNullArguments() throws Exception {
 instantiator.configureFromObjectSettings(context().findValueInjector(), null, null, null);
 Object result = instantiator.createFromObjectWith(context(), null);
 fail("Should not be able to create using object with null arguments");
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateUsingObjectWithNullCreator() throws Exception {
 instantiator.configureFromObjectSettings(context().findValueInjector(), null, null, null);
 Object result = instantiator.createFromObjectWith(context(), new Object[] {new Object[] {"Argument 1", "Argument 2"}});
 fail("Should not be able to create using object with null creator");
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 String[] constructorArguments = {"Argument 1", "Argument 2"};
 MyObject object = new MyObject("Argument 1", "Argument 2");
 instantiator.configureFromObjectSettings(context().findValueInjector(), null, null, constructorArguments);
 Object result = instantiator.createFromObjectWith(context(), new Object[] {object});
 assertNotNull(result);
 assertTrue(result instanceof String);
 assertEquals("Argument 1Argument 2", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromObjectWithNullCreatorType() throws Exception {
 instantiator.configureFromObjectSettings(context().findValueInjector(), null, null, null);
 Object result = instantiator.createFromObjectWith(context(), new Object[] {new Object[] {"Argument 1", "Argument 2"}});
 fail("Should not be able to create using object with null creator type");
 }

 @Test(expected = JsonMappingException.class)
 public void testCreateFromObjectWithNullObject() throws Exception {
 String[] constructorArguments = {"Argument 1", "Argument 2"};
 instantiator.configureFromObjectSettings(context().findValueInjector(), null, null, constructorArguments);
 Object result = instantiator.createFromObjectWith(context(), null);
 fail("Should not be able to create using object with null object");
 }

 private DeserializationContext context() {
 return new TestDeserializationContext();
 }

 @Test
 public void testCreateFromStringWithNullContext() throws Exception {
 Object result = instantiator.createFromString(null, "Test String");
 fail("Should not be able to create from string with null context");
 }

 @Test
 public void testCreateFromIntWithNullContext() throws Exception {
 Object result = instantiator.createFromInt(null, 42);
 fail("Should not be able to create from int with null context");
 }

 @Test
 public void testCreateFromLongWithNullContext() throws Exception {
 Object result = instantiator.createFromLong(null, 12345L);
 fail("Should not be able to create from long with null context");
 }

 @Test
 public void testCreateFromDoubleWithNullContext() throws Exception {
 Object result = instantiator.createFromDouble(null, 3.141592653589793);
 fail("Should not be able to create from double with null context");
 }

 @Test
 public void testCreateFromBooleanWithNullContext() throws Exception {
 Object result = instantiator.createFromBoolean(null, true);
 fail("Should not be able to create from boolean with null context");
 }

 @Test
 public void testCreateUsingDefaultWithNullContext() throws Exception {
 Object result = instantiator.createUsingDefault(null);
 fail("Should not be able to create using default with null context");
 }

 @Test
 public void testCreateUsingDelegateWithNullContext() throws Exception {
 Object delegate = new Object();
 Object result = instantiator.createUsingDelegate(null, delegate);
 fail("Should not be able to create using delegate with null context");
 }

 @Test
 public void testCreateUsingArrayDelegateWithNullContext() throws Exception {
 int[] array = new int[] {1, 2, 3};
 Object result = instantiator.createUsingArrayDelegate(null, array);
 fail("Should not be able to create using array delegate with null context");
 }

 @Test
 public void testCreateFromObjectWithNullContext() throws Exception {
 String[] constructorArguments = {"Argument 1", "Argument 2"};
 MyObject object = new MyObject("Argument 1", "Argument 2");
 Object result = instantiator.createFromObjectWith(null, new Object[] {object});
 fail("Should not be able to create using object with null context");
 }
}

class MyDelegateClass {
 private String value;

 public MyDelegateClass() {}

 public MyDelegateClass(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
}

class MyObject {
 private String arg1;
 private String arg2;

 public MyObject(String arg1, String arg2) {
 this.arg1 = arg1;
 this.arg2 = arg2;
 }

 @Override
 public String toString() {
 return arg1 + arg2;
 }
}

class TestDeserializationContext extends BaseTestDeserializerProvider {
 @Override
 public Object instantiateValue(JavaType valueType, AnnotationMetadata annotations, Object ctxt) throws IOException {
 return new StdValueInstantiator();
 }

 @Override
 public DeserializationConfig getConfig(MapperConfig<?> config, BeanDescription beanDesc) {
 return new DeserializationConfig(config, beanDesc);
 }

 @Override
 public Object findValueInjector() {
 return new ValueInjector() {};
 }
}