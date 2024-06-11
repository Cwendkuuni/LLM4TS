import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.reflect.*;
import java.util.*;

public class CreatorCollectorTest {

 private CreatorCollector _cc;
 private MapperConfig<?> _config;

 @Before
 public void setup() throws Exception {
 _config = new MapperFactory().getMapper().getConfig();
 _cc = new CreatorCollector(_config.introspect().findBeanDescription(String.class), _config);
 }

 @Test
 public void testDefaultCreator() throws Exception {
 // Create a default CreatorCollector instance
 _cc.setDefaultCreator(_config.introspect().findConstructors(String.class)[0]);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 Object created = inst.createValue(null, String.class, null);
 assertEquals("", created.toString());
 }

 @Test
 public void testStringCreator() throws Exception {
 // Create a String creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, String.class);
 _cc.addStringCreator(creator, true);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 Object created = inst.createValue(null, String.class, "Test");
 assertEquals("Test", created.toString());
 }

 @Test
 public void testIntCreator() throws Exception {
 // Create an Int creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, int.class);
 _cc.addIntCreator(creator, true);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 Object created = inst.createValue(null, String.class, 42);
 assertEquals("42", created.toString());
 }

 @Test
 public void testLongCreator() throws Exception {
 // Create a Long creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, long.class);
 _cc.addLongCreator(creator, true);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 Object created = inst.createValue(null, String.class, 42L);
 assertEquals("42", created.toString());
 }

 @Test
 public void testDoubleCreator() throws Exception {
 // Create a Double creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, double.class);
 _cc.addDoubleCreator(creator, true);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 Object created = inst.createValue(null, String.class, 3.14);
 assertEquals("3.14", created.toString());
 }

 @Test
 public void testBooleanCreator() throws Exception {
 // Create a Boolean creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, boolean.class);
 _cc.addBooleanCreator(creator, true);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 Object created = inst.createValue(null, String.class, true);
 assertEquals("true", created.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 // Create a String creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, String.class);
 _cc.addStringCreator(creator, true);
 // Try to add the same creator again
 _cc.addStringCreator(creator, true);
 }

 @Test
 public void testDelegateCreator() throws Exception {
 // Create a delegate creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, String[].class);
 SettableBeanProperty[] injectables = new SettableBeanProperty[1];
 injectables[0] = mock(SettableBeanProperty.class);
 when(injectables[0].getName()).thenReturn("delegate");
 _cc.addDelegatingCreator(creator, true, injectables);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 String[] args = new String[]{"delegate"};
 Object[] delegates = inst.createValue(null, String[].class, args);
 assertEquals("delegate", delegates[0].toString());
 assertEquals("delegate", injectables.get(0).getValue().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateDelegateInjectable() throws Exception {
 // Create a delegate creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, String[].class);
 SettableBeanProperty[] injectables = new SettableBeanProperty[1];
 injectables[0] = mock(SettableBeanProperty.class);
 when(injectables[0].getName()).thenReturn("delegate");
 _cc.addDelegatingCreator(creator, true, injectables);
 // Try to add the same injectable again
 SettableBeanProperty newInjectable = mock(SettableBeanProperty.class);
 when(newInjectable.getName()).thenReturn("delegate");
 _cc.addDelegatingCreator(creator, true, newInjectable);
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 // Create a property-based creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, Map.class);
 SettableBeanProperty[] properties = new SettableBeanProperty[2];
 properties[0] = mock(SettableBeanProperty.class);
 properties[1] = mock(SettableBeanProperty.class);
 when(properties[0].getName()).thenReturn("property1");
 when(properties[1].getName()).thenReturn("property2");
 _cc.addPropertyCreator(creator, true, properties);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 Map<String,Object> props = new HashMap<>();
 props.put("property1", "property1Value");
 props.put("property2", "property2Value");
 Object created = inst.createUsingObject(null, props);
 assertEquals("{property1Value=property1Value, property2=property2Value}", created.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicatePropertyInjectable() throws Exception {
 // Create a property-based creator and add it to the CreatorCollector
 AnnotatedWithParams creator = _config.introspect().findConstructor(String.class, Map.class);
 SettableBeanProperty[] properties = new SettableBeanProperty[2];
 properties[0] = mock(SettableBeanProperty.class);
 when(properties[0].getName()).thenReturn("property1");
 _cc.addPropertyCreator(creator, true, properties);
 // Try to add the same injectable again
 SettableBeanProperty newInjectable = mock(SettableBeanProperty.class);
 when(newInjectable.getName()).thenReturn("property1");
 _cc.addPropertyCreator(creator, true, properties);
 }

 @Test
 public void testIncompleteParameter() throws Exception {
 // Create a CreatorCollector and add an incomplete parameter
 AnnotatedParameter incompleteParam = _config.introspect().getParameterAnnotations(String.class.getDeclaredConstructor());
 AnnotatedParameter incompleteParamAnnot = _config.introspect().findParameter(String.class.getDeclaredConstructor(), 0);
 _cc.addIncompleteParameter(incompleteParamAnnot);
 ValueInstantiator inst = _cc.constructValueInstantiator(_config.getDefaultDeserializationConfig());
 Object created = inst.createValue(null, String.class, null);
 assertNull(created);
 }
}