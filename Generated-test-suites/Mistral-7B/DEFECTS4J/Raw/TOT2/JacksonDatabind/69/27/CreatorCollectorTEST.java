import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import java.io.IOException;
import java.lang.reflect.*;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private MapperFactory mapperFactory;

 @Before
 public void setup() throws Exception {
 MapperFactory factory = new MapperFactory();
 MapperConfig config = factory.getConfig();
 Object intGraph = new IntGraph();
 ObjectMap mapper = factory.createObjectMapper();
 BeanDescription beanDesc = mapper.constructType(IntGraph.class);
 collector = new CreatorCollector(beanDesc, config);
 mapperFactory = factory;
 }

 @Test
 public void testDefaultCreator() {
 ValueInstantiator inst = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig());
 Object o = inst.createValueInstance(null, IntGraph.class);
 assertInstanceOf(IntGraph.class, o);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefaultCreatorWithExplicitCreator() {
 collector.setDefaultCreator(null);
 collector.addIntCreator(new AnnotatedWithParams(new AnnotatedMethod(IntGraph.class, "test", new Class[0]), null), true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig());
 inst.createValueInstance(null, IntGraph.class);
 }

 @Test
 public void testStringCreator() throws IOException {
 Object o = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig())
 .createValueInstance(new ObjectValue("testStringCreator"), IntGraph.class);
 assertEquals("testStringCreator", ((IntGraph) o).getName());
 }

 @Test
 public void testIntCreator() throws IOException {
 Object o = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig())
 .createValueInstance(new NumberValue(42), IntGraph.class);
 assertEquals(42, ((IntGraph) o).getNumber());
 }

 @Test
 public void testLongCreator() throws IOException {
 Object o = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig())
 .createValueInstance(new NumberValue(42L), IntGraph.class);
 assertEquals(42L, ((IntGraph) o).getLongNumber());
 }

 @Test
 public void testDoubleCreator() throws IOException {
 Object o = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig())
 .createValueInstance(new NumberValue(42.0), IntGraph.class);
 assertEquals(42.0, ((IntGraph) o).getDoubleNumber(), 0.0);
 }

 @Test
 public void testBooleanCreator() throws IOException {
 Object o = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig())
 .createValueInstance(new BooleanValue(true), IntGraph.class);
 assertTrue(((IntGraph) o).isBoolean());
 }

 @Test
 public void testDelegateCreator() throws IOException {
 Object arg = new Object();
 SettableBeanProperty delegateArg = new SettableBeanProperty("delegateArg", arg.getClass(), arg);
 collector.addDelegatingCreator(new AnnotatedWithParams(new AnnotatedMethod(IntGraph.class, "testDelegate", new Class[0]), null), false, new SettableBeanProperty[] { delegateArg });
 Object o = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig())
 .createValueInstance(null, IntGraph.class);
 assertEquals(arg, o.getDelegateArg());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDelegateCreatorWithNullDelegateArg() {
 collector.addDelegatingCreator(new AnnotatedWithParams(new AnnotatedMethod(IntGraph.class, "testDelegate", new Class[0]), null), false, new SettableBeanProperty[0]);
 ValueInstantiator inst = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig());
 inst.createValueInstance(null, IntGraph.class);
 }

 @Test
 public void testPropertyBasedCreator() throws IOException {
 Object arg1 = new Object();
 Object arg2 = new Object();
 SettableBeanProperty propertyBasedArg1 = new SettableBeanProperty("propertyBasedArg1", arg1.getClass(), arg1);
 SettableBeanProperty propertyBasedArg2 = new SettableBeanProperty("propertyBasedArg2", arg2.getClass(), arg2);
 collector.addPropertyCreator(new AnnotatedWithParams(new AnnotatedMethod(IntGraph.class, "testPropertyBased", new Class[0]), null), false, new SettableBeanProperty[] { propertyBasedArg1, propertyBasedArg2 });
 Object o = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig())
 .createValueInstance(null, IntGraph.class);
 assertEquals(arg1, o.getPropertyBasedArg1());
 assertEquals(arg2, o.getPropertyBasedArg2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPropertyBasedCreatorWithNullPropertyBasedArg() {
 SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
 propertyBasedArgs[0] = new SettableBeanProperty("propertyBasedArg1", null, null);
 collector.addPropertyCreator(new AnnotatedWithParams(new AnnotatedMethod(IntGraph.class, "testPropertyBased", new Class[0]), null), false, propertyBasedArgs);
 ValueInstantiator inst = collector.constructValueInstantiator(mapperFactory.getDeserializationConfig());
 inst.createValueInstance(null, IntGraph.class);
 }

}