import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

public class StdValueInstantiatorTest {

 private DeserializationContext ctxt;
 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 ctxt = new TestDeserializationContext();
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testDefaultCreator() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, String.class);
 assertEquals("Default Creator", instantiator.createUsingDefault(ctxt));
 }

 @Test
 public void testDelegateCreator() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, String.class);
 instantiator.configureFromDelegateSettings(
 new AnnotatedWithParams(MyDelegateClass.class),
 new SettableBeanProperty[]{new SettableBeanPropertyBuilder().setName("delegate").build()},
 null, null);
 assertEquals("Delegate Creator", instantiator.createUsingDelegate(ctxt, new MyDelegateClass()));
 }

 @Test
 public void testArrayDelegateCreator() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, int[].class);
 instantiator.configureFromArraySettings(
 new AnnotatedWithParams(MyArrayDelegateClass.class),
 new JavaType(int[].class),
 new SettableBeanProperty[]{new SettableBeanPropertyBuilder().setName("delegate").build()});
 int[] result = instantiator.createUsingArrayDelegate(ctxt, new MyArrayDelegateClass());
 assertNotNull(result);
 assertEquals(5, result.length);
 }

 @Test
 public void testFromStringCreator() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, MyStringClass.class);
 instantiator.configureFromStringCreator(new AnnotatedWithParams(MyStringCreatorClass.class));
 assertEquals("From String Creator", instantiator.createFromString(ctxt, "From String Creator"));
 }

 @Test
 public void testFromIntCreator() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, Integer.class);
 instantiator.configureFromIntCreator(new AnnotatedWithParams(MyIntCreatorClass.class));
 assertEquals(42, instantiator.createFromInt(ctxt, 42));
 }

 @Test
 public void testFromLongCreator() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, Long.class);
 instantiator.configureFromLongCreator(new AnnotatedWithParams(MyLongCreatorClass.class));
 assertEquals(42L, instantiator.createFromLong(ctxt, 42L));
 }

 @Test
 public void testFromDoubleCreator() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, Double.class);
 instantiator.configureFromDoubleCreator(new AnnotatedWithParams(MyDoubleCreatorClass.class));
 assertEquals(3.14, instantiator.createFromDouble(ctxt, 3.14), 0.001);
 }

 @Test
 public void testFromBooleanCreator() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, Boolean.class);
 instantiator.configureFromBooleanCreator(new AnnotatedWithParams(MyBooleanCreatorClass.class));
 assertEquals(true, instantiator.createFromBoolean(ctxt, true));
 }

 @Test
 public void testNullValue() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, String.class);
 assertNull(instantiator.createUsingDefault(ctxt));
 }

 @Test
 public void testIncompleteParameter() throws JsonMappingException, IOException {
 StdValueInstantiator instantiator = new StdValueInstantiator(ctxt, MyIncompleteClass.class);
 instantiator.configureIncompleteParameter(new AnnotatedParameter(MyIncompleteClass.class.getMethod("method", int.class), 0));
 assertThrows(JsonMappingException.class, () -> instantiator.createUsingDefault(ctxt));
 }
}

class TestDeserializationContext extends DeserializationContextBase {
 @Override
 public JsonProcessingException instantiationException(Class<?> valueType, Throwable cause) {
 throw new JsonMappingException(cause);
 }
}

class MyDelegateClass {
 public MyDelegateClass() {
 }

 public String method() {
 return "Delegate Creator";
 }
}

class MyArrayDelegateClass {
 public MyArrayDelegateClass() {
 }

 public int[] method() {
 int[] result = new int[5];
 return result;
 }
}

class MyStringClass {
 public MyStringClass() {
 }

 public String method() {
 return "From String Creator";
 }
}

class MyIntCreatorClass {
 public static int create(int value) {
 return value;
 }
}

class MyLongCreatorClass {
 public static long create(long value) {
 return value;
 }
}

class MyDoubleCreatorClass {
 public static double create(double value) {
 return value;
 }
}

class MyBooleanCreatorClass {
 public static boolean create(boolean value) {
 return value;
 }
}

class MyIncompleteClass {
 public static int method(int i) {
 throw new RuntimeException("Incomplete");
 }
}