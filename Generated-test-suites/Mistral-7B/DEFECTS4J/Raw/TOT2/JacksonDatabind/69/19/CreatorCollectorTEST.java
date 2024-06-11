import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 private static MapperConfig mapperConfig = new MapperConfig(new AnnotationIntrospector());

 @Test
 public void testDefaultCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 Object result = instantiator.createValueInstance(null);
 assertNotNull(result);
 }

 @Test
 public void testStringCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 AnnotatedWithParams stringCreator = new AnnotatedWithParams(null, String.class, null, new AnnotatedMember[]{null}, null, null, null);
 collector.setStringCreator(stringCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 String input = "test";
 Object result = instantiator.createValueInstance(null);
 assertEquals(input, result);
 }

 @Test
 public void testIntCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 AnnotatedWithParams intCreator = new AnnotatedWithParams(null, int.class, null, new AnnotatedMember[]{null}, null, null, null);
 collector.setIntCreator(intCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 int input = 5;
 Object result = instantiator.createValueInstance(null);
 assertEquals(input, result);
 }

 @Test
 public void testLongCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 AnnotatedWithParams longCreator = new AnnotatedWithParams(null, long.class, null, new AnnotatedMember[]{null}, null, null, null);
 collector.setLongCreator(longCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 long input = 10L;
 Object result = instantiator.createValueInstance(null);
 assertEquals(input, result);
 }

 @Test
 public void testDoubleCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 AnnotatedWithParams doubleCreator = new AnnotatedWithParams(null, double.class, null, new AnnotatedMember[]{null}, null, null, null);
 collector.setDoubleCreator(doubleCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 double input = 3.14;
 Object result = instantiator.createValueInstance(null);
 assertEquals(input, result, 0.01);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 AnnotatedWithParams booleanCreator = new AnnotatedWithParams(null, boolean.class, null, new AnnotatedMember[]{null}, null, null, null);
 collector.setBooleanCreator(booleanCreator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 boolean input = true;
 Object result = instantiator.createValueInstance(null);
 assertEquals(input, result);
 }

 @Test
 public void testDelegateCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 AnnotatedWithParams delegateCreator = new AnnotatedWithParams(null, String.class, null, null, null, null, null);
 SettableBeanProperty[] delegateArgs = new SettableBeanProperty[]{new SettableBeanProperty(null, "name", null, null, null)};
 collector.addDelegatingCreator(delegateCreator, true, delegateArgs);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 Object result = instantiator.createValueInstance(null);
 assertEquals("", result.getClass().getMethod("getName").invoke(result));
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 AnnotatedWithParams propertyBasedCreator = new AnnotatedWithParams(null, String.class, null, null, null, null, new AnnotatedMember[]{new AnnotatedMember(null, "prop", null, null, null, null)});
 SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[]{new SettableBeanProperty(null, "prop", null, null, null)};
 collector.addPropertyCreator(propertyBasedCreator, true, propertyBasedArgs);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 Object result = instantiator.createValueInstance(null);
 assertEquals("", result.getClass().getDeclaredField("prop").get(result));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 AnnotatedWithParams creator = new AnnotatedWithParams(null, String.class, null, null, null, null, null);
 collector.setStringCreator(creator, true);
 collector.setStringCreator(creator, true);
 }

 @Test
 public void testNullInput() throws Exception {
 CreatorCollector collector = new CreatorCollector(null, mapperConfig);
 ValueInstantiator instantiator = collector.constructValueInstantiator(null);
 Object result = instantiator.createValueInstance(null);
 assertNull(result);
 }
}