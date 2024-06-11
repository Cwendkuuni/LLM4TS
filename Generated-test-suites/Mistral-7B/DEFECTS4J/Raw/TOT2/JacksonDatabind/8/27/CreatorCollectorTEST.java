import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private DeserializationConfig config;

 @Before
 public void setUp() {
 BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(CreatorCollector.class));
 collector = new CreatorCollector(beanDesc, true);
 config = new DeserializationConfig();
 }

 @Test
 public void testDefaultCreator() throws Exception {
 collector.setDefaultCreator(new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredConstructor())));
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNotNull(inst);
 }

 @Test
 public void testStringCreator() throws Exception {
 String name = "test";
 AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("addStringCreator", String.class)));
 collector.addStringCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNotNull(inst);
 inst.createUsingDefault(config);
 assertEquals(name, inst.createValue(config, String.class, (Object[]) null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringCreatorDuplicate() throws Exception {
 String name = "test";
 AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("addStringCreator", String.class)));
 collector.addStringCreator(creator, true);
 collector.addStringCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testIntCreator() throws Exception {
 int value = 1;
 AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("addIntCreator", int.class)));
 collector.addIntCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNotNull(inst);
 inst.createUsingDefault(config);
 assertEquals(value, inst.createValue(config, int.class, (Object[]) null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntCreatorDuplicate() throws Exception {
 int value = 1;
 AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("addIntCreator", int.class)));
 collector.addIntCreator(creator, true);
 collector.addIntCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testDelegateCreator() throws Exception {
 Class<?> delegateType = String.class;
 AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("addDelegatingCreator", AnnotatedWithParams.class, CreatorProperty[].class)));
 CreatorProperty[] injectables = new CreatorProperty[1];
 injectables[0] = new CreatorProperty(new AnnotatedField(CreatorCollector.class.getDeclaredField("_beanDesc")));
 collector.addDelegatingCreator(creator, true, injectables);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNotNull(inst);
 JavaType delegateType2 = TypeBindings.constructType(CreatorCollector.class, Object.class, delegateType);
 inst.configureFromDelegateCreator(new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("constructValueInstantiator", DeserializationConfig.class))), delegateType2, injectables);
 Object obj = inst.createUsingDefault(config);
 assertTrue(obj instanceof CreatorCollector);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDelegateCreatorDuplicate() throws Exception {
 Class<?> delegateType = String.class;AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("addDelegatingCreator", AnnotatedWithParams.class, CreatorProperty[].class)));
 CreatorProperty[] injectables = new CreatorProperty[1];
 injectables[0] = new CreatorProperty(new AnnotatedField(CreatorCollector.class.getDeclaredField("_beanDesc")));
 collector.addDelegatingCreator(creator, true, injectables);
 collector.addDelegatingCreator(creator, true, injectables);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 String propertyName = "test";
 CreatorProperty creatorProperty = new CreatorProperty(new AnnotatedField(CreatorCollector.class.getDeclaredField("_beanDesc")));
 creatorProperty.setName(propertyName);
 AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("addPropertyCreator", AnnotatedWithParams.class, CreatorProperty[].class)));
 CreatorProperty[] properties = new CreatorProperty[1];
 properties[0] = creatorProperty;
 collector.addPropertyCreator(creator, true, properties);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNotNull(inst);
 inst.configureFromPropertyBasedCreator(new AnnotatedWithParams(new AnnotatedField(CreatorCollector.class.getDeclaredField("_beanDesc")), creatorProperty));
 Object obj = inst.createUsingDefault(config);
 assertTrue(obj instanceof CreatorCollector);
 assertEquals(propertyName, obj.getClass().getField("_beanDesc").get(obj).getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPropertyBasedCreatorDuplicate() throws Exception {
 String propertyName = "test";
 CreatorProperty creatorProperty = new CreatorProperty(new AnnotatedField(CreatorCollector.class.getDeclaredField("_beanDesc")));
 creatorProperty.setName(propertyName);
 AnnotatedWithParams creator = new AnnotatedWithParams(new AnnotatedMethod(CreatorCollector.class.getDeclaredMethod("addPropertyCreator", AnnotatedWithParams.class, CreatorProperty[].class)));
 CreatorProperty[] properties = new CreatorProperty[1];
 properties[0] = creatorProperty;
 collector.addPropertyCreator(creator, true, properties);
 collector.addPropertyCreator(creator, true, properties);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIncompleteParameter() throws Exception {
 AnnotatedParameter incompleteParameter = new AnnotatedParameter(new AnnotatedField(CreatorCollector.class.getDeclaredField("_incompleteParameter")));
 collector.addIncompleteParameter(incompleteParameter);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 fail("Should have thrown IllegalArgumentException");
 }
}