import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

public class StdValueInstantiatorTest {

 private static ObjectMap factory = new ObjectMapper().createObjectMapper().getFactory();

 @Test
 public void testStdValueInstantiatorDefaultConstructor() throws IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
 assertEquals("UNKNOWN TYPE", instantiator.getValueTypeDesc());
 assertNotNull(instantiator);
 }

 @Test
 public void testStdValueInstantiatorCustomConstructor() throws IOException {
 JavaType valueType = factory.constructType(String.class);
 StdValueInstantiator instantiator = new StdValueInstantiator(null, valueType);
 assertEquals("java.lang.String", instantiator.getValueTypeDesc());
 assertNotNull(instantiator);
 }

 @Test
 public void testStdValueInstantiatorDefaultCreator() throws IOException {
 DeserializationConfig config = new DeserializationConfig();
 StdValueInstantiator instantiator = new StdValueInstantiator(config, Object.class);
 assertNull(instantiator.getDefaultCreator());

 Object defaultInstance = instantiator.createUsingDefault(new TestContext());
 assertEquals("UNKNOWN TYPE", defaultInstance.getClass().getName());
 }

 @Test
 public void testStdValueInstantiatorWithArgsCreator() throws IOException {
 DeserializationConfig config = new DeserializationConfig();
 AnnotatedWithParams annotatedWithParams = new AnnotatedClass(String.class)
 .annnotatedFor(StdInstantiationStrategy.Standard.class)
 .getParameterTypes()
 .next();
 SettableBeanProperty[] constructorArguments = new SettableBeanProperty[] { new TestProperty("arg1"), new TestProperty("arg2") };
 instantiator.configureFromObjectSettings(annotatedWithParams, null, null, constructorArguments);

 Object instance = instantiator.createFromObjectWith(new TestContext(), new Object[] { "testArg1", "testArg2" });
 assertEquals("java.lang.String", instance.getClass().getName());
 assertEquals("testArg1", instance.toString());
 assertEquals("testArg2", instantiator.getFromObjectArguments(new TestContext())[0].getValue());
 }

 @Test
 public void testStdValueInstantiatorUsingDelegate() throws IOException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType delegateType = factory.constructType(Integer.class);
 AnnotatedWithParams delegateCreator = new AnnotatedClass(Integer.class)
 .annnotatedFor(StdInstantiationStrategy.Standard.class)
 .getParameterTypes()
 .next();
 SettableBeanProperty[] delegateArguments = new SettableBeanProperty[] { new TestProperty("delegateInstance") };
 instantiator.configureFromDelegateSettings(delegateType, delegateCreator, delegateArguments);
 Object delegateInstance = new Integer(10);
 Object instance = instantiator.createUsingDelegate(new TestContext(), delegateInstance);
 assertEquals(10, instance.intValue());
 }

 @Test
 public void testStdValueInstantiatorUsingArrayDelegate() throws IOException {
 DeserializationConfig config = new DeserializationConfig();
 JavaType arrayDelegateType = factory.constructType(int[].class);
 AnnotatedWithParams arrayDelegateCreator = new AnnotatedClass(int[].class)
 .annnotatedFor(StdInstantiationStrategy.Standard.class)
 .getParameterTypes()
 .next();
 SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[] { new TestProperty("arrayDelegateInstance") };
 instantiator.configureFromArraySettings(arrayDelegateCreator, arrayDelegateType, arrayDelegateArguments);
 int[] arrayDelegateInstance = { 10, 20 };
 Object instance = instantiator.createUsingArrayDelegate(new TestContext(), arrayDelegateInstance);
 assertArrayEquals(new int[] { 10, 20 }, (int[]) instance);
 }
}

class TestContext extends DeserializationContext {
 @Override
 public Object findValueInContext(String name) {
 return null;
 }

 @Override
 public Object findValueInContext(Class<?> clazz) {
 return null;
 }
}

class TestProperty extends PropertyName {
 public TestProperty(String name) {
 super(new PropertyName("testProperty"), new TypeReference<>() {});
 }
}