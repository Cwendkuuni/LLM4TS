import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 @Test
 public void testCreatorCollectorDefaultConstructor() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 assertNull(cc.getDefaultCreator());
 assertNull(cc.getDelegatingCreator());
 assertNull(cc.getPropertyBasedCreator());
 }

 @Test
 public void testCreatorCollectorDefaultConstructor_WithBeanDescription() throws Exception {
 BeanDescription beanDesc = new BeanDescription(null, null, null, null, null, null, null, null, null, null);
 CreatorCollector cc = new CreatorCollector(beanDesc, null);
 assertNotNull(cc);
 }

 @Test
 public void testCreatorCollectorDefaultCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Object defaultCreator = cc.constructValueInstantiator(null).getValueInstantiator();
 // Verify the default creator is an instance of Vanilla
 assertTrue(defaultCreator instanceof Vanilla);
 }

 @Test
 public void testCreatorCollectorAddStringCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Class<?> stringClass = String.class;
 AnnotatedWithParams stringCreator = new AnnotatedWithParams(null, null, stringClass, null);
 cc.addStringCreator(stringCreator, true);
 assertNotNull(cc.getDefaultCreator());
 assertNull(cc.getDelegatingCreator());
 assertNull(cc.getPropertyBasedCreator());

 ValueInstantiator valueInstantiator = cc.constructValueInstantiator(null);
 AnnotatedMember stringCreatorMember = valueInstantiator.getCreator(CreatorCollector.C_STRING);
 assertEquals(stringClass, stringCreatorMember.getRawParameterType(0));
 }

 @Test
 public void testCreatorCollectorAddStringCreator_WithExistingDefaultCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Object defaultCreator = cc.constructValueInstantiator(null).getValueInstantiator();
 // Verify the default creator is an instance of Vanilla
 assertTrue(defaultCreator instanceof Vanilla);

 Class<?> stringClass = String.class;
 AnnotatedWithParams stringCreator = new AnnotatedWithParams(null, null, stringClass, null);
 cc.addStringCreator(stringCreator, true);
 ValueInstantiator newValueInstantiator = cc.constructValueInstantiator(null);
 AnnotatedMember newStringCreatorMember = newValueInstantiator.getCreator(CreatorCollector.C_STRING);
 assertEquals(stringClass, newStringCreatorMember.getRawParameterType(0));

 // Verify the new String creator takes precedence over the default creator
 assertNull(defaultCreator);
 }

 @Test
 public void testCreatorCollectorAddIntCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Class<?> intClass = int.class;
 AnnotatedWithParams intCreator = new AnnotatedWithParams(null, null, intClass, null);
 cc.addIntCreator(intCreator, true);
 assertNotNull(cc.getDefaultCreator());
 assertNull(cc.getDelegatingCreator());
 assertNull(cc.getPropertyBasedCreator());

 ValueInstantiator valueInstantiator = cc.constructValueInstantiator(null);
 AnnotatedMember intCreatorMember = valueInstantiator.getCreator(CreatorCollector.C_INT);
 assertEquals(intClass, intCreatorMember.getRawParameterType(0));
 }

 @Test
 public void testCreatorCollectorAddIntCreator_WithExistingDefaultCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Object defaultCreator = cc.constructValueInstantiator(null).getValueInstantiator();
 // Verify the default creator is an instance of Vanilla
 assertTrue(defaultCreator instanceof Vanilla);

 Class<?> intClass = int.class;
 AnnotatedWithParams intCreator = new AnnotatedWithParams(null, null, intClass, null);
 cc.addIntCreator(intCreator, true);
 ValueInstantiator newValueInstantiator = cc.constructValueInstantiator(null);
 AnnotatedMember newIntCreatorMember = newValueInstantiator.getCreator(CreatorCollector.C_INT);
 assertEquals(intClass, newIntCreatorMember.getRawParameterType(0));

 // Verify the new Int creator takes precedence over the default creator
 assertNull(defaultCreator);
 }

 @Test
 public void testCreatorCollectorAddDelegatingCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Class<?> delegateClass = String[].class;
 AnnotatedWithParams delegatingCreator = new AnnotatedWithParams(null, null, null, null);
 SettableBeanProperty[] delegateArgs = new SettableBeanProperty[] { new SettableBeanProperty("arg1", String.class) };
 cc.addDelegatingCreator(delegatingCreator, true, delegateArgs);
 assertNotNull(cc.getDefaultCreator());
 assertNull(cc.getDelegatingCreator());
 assertNull(cc.getPropertyBasedCreator());

 ValueInstantiator valueInstantiator = cc.constructValueInstantiator(null);
 AnnotatedMember delegatingCreatorMember = valueInstantiator.getCreator(CreatorCollector.C_DELEGATE);
 assertNull(delegatingCreatorMember);
 }

 @Test
 public void testCreatorCollectorAddDelegatingCreator_WithExistingDefaultCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Object defaultCreator = cc.constructValueInstantiator(null).getValueInstantiator();
 // Verify the default creator is an instance of Vanilla
 assertTrue(defaultCreator instanceof Vanilla);

 Class<?> delegateClass = String[].class;
 AnnotatedWithParams delegatingCreator = new AnnotatedWithParams(null, null, null, null);
 SettableBeanProperty[] delegateArgs = new SettableBeanProperty[] { new SettableBeanProperty("arg1", String.class) };
 cc.addDelegatingCreator(delegatingCreator, true, delegateArgs);
 ValueInstantiator newValueInstantiator = cc.constructValueInstantiator(null);
 AnnotatedMember newDelegatingCreatorMember = newValueInstantiator.getCreator(CreatorCollector.C_DELEGATE);
 assertNull(newDelegatingCreatorMember);

 // Verify the new Delegating creator does not take precedence over the default creator
 assertNotNull(defaultCreator);
 }

 @Test
 public void testCreatorCollectorAddPropertyBasedCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 PropertyName pn1 = new PropertyName("property1");
 SettableBeanProperty property1 = new SettableBeanProperty("property1", String.class);
 SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] { property1 };
 AnnotatedWithParams propertyCreator = new AnnotatedWithParams(null, null, null, null);
 cc.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
 assertNotNull(cc.getDefaultCreator());
 assertNull(cc.getDelegatingCreator());
 assertNotNull(cc.getPropertyBasedCreator());

 ValueInstantiator valueInstantiator = cc.constructValueInstantiator(null);
 AnnotatedMember propertyCreatorMember = valueInstantiator.getCreator(CreatorCollector.C_PROPS);
 assertNotNull(propertyCreatorMember);

 SettableBeanProperty property = propertyCreatorMember.getParameterAnnotation(SettableBeanProperty.class);
 assertEquals("property1", property.getName());
 assertEquals(String.class, property.getValueType());
 }

 @Test
 public void testCreatorCollectorAddPropertyBasedCreator_WithExistingDefaultCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Object defaultCreator = cc.constructValueInstantiator(null).getValueInstantiator();
 // Verify the default creator is an instance of Vanilla
 assertTrue(defaultCreator instanceof Vanilla);

 PropertyName pn1 = new PropertyName("property1");
 SettableBeanProperty property1 = new SettableBeanProperty("property1", String.class);
 SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] { property1 };
 AnnotatedWithParams propertyCreator = new AnnotatedWithParams(null, null, null, null);
 cc.addPropertyCreator(propertyCreator, true, propertyBasedArgs);
 ValueInstantiator newValueInstantiator = cc.constructValueInstantiator(null);
 AnnotatedMember newPropertyCreatorMember = newValueInstantiator.getCreator(CreatorCollector.C_PROPS);
 assertNotNull(newPropertyCreatorMember);

 // Verify the new Property creator takes precedence over the default creator
 assertNull(defaultCreator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatorCollectorAddDuplicateCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 Class<?> stringClass = String.class;
 AnnotatedWithParams stringCreator = new AnnotatedWithParams(null, null, stringClass, null);
 cc.addStringCreator(stringCreator, true);

 Class<?> intClass = int.class;
 AnnotatedWithParams intCreator = new AnnotatedWithParams(null, null, intClass, null);
 cc.addIntCreator(intCreator, true);

 // Creating a duplicate String creator should throw an IllegalArgumentException
 cc.addStringCreator(stringCreator, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatorCollectorAddDuplicatePropertyBasedCreator() throws Exception {
 CreatorCollector cc = new CreatorCollector(null, null);
 PropertyName pn1 = new PropertyName("property1");
 SettableBeanProperty property1 = new SettableBeanProperty("property1", String.class);
 SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] { property1 };
 AnnotatedWithParams propertyCreator = new AnnotatedWithParams(null, null, null, null);
 cc.addPropertyCreator(propertyCreator, true, propertyBasedArgs);

 PropertyName pn2 = new PropertyName("property1");
 SettableBeanProperty property2 = new SettableBeanProperty("property1", String.class);
 SettableBeanProperty[] propertyBasedArgs2 = new SettableBeanProperty[] { property2 };
 cc.addPropertyCreator(propertyCreator, true, propertyBasedArgs2);
 }

}