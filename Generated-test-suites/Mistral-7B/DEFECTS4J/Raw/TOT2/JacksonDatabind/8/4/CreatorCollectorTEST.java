import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class CreatorCollectorTest {

 private ObjectMapper mapper;
 private CreatorCollector collector;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 try {
 collector = mapper.getFactory().createBeanDescription(String[].class)
 .getAnnotationIntrospector()
 .findCreatorCollector();
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize CreatorCollector");
 }
 }

 @Test
 public void testDefaultCreator() {
 JavaType type = mapper.getTypeFactory().constructType(List.class);
 collector.setDefaultCreator(mapper.getFactory().findDefaultCreator(type));
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 List list = inst.createValueInstance(mapper.getConfig());
 assertTrue(list instanceof ArrayList);
 }

 @Test
 public void testStringCreator() {
 String[] values = new String[]{"one", "two", "three"};
 AnnotatedClass ac = mapper.getFactory().findClassInstancifier(ArrayList.class);
 AnnotatedWithParams creator = ac.getAnnotationMembers()[0];
 collector.addStringCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 List list = inst.createValueInstance(mapper.getConfig(), "one,two,three");
 assertEquals(Arrays.asList(values), list);
 }

 @Test
 public void testIntCreator() {
 JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
 int[] values = new int[]{1, 2, 3};
 AnnotatedClass ac = mapper.getFactory().findClassInstancifier(ArrayList.class);
 AnnotatedWithParams creator = ac.getAnnotationMembers()[1];
 collector.addIntCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 List list = inst.createValueInstance(mapper.getConfig(), TypeDeserializer.intValue(1));
 assertEquals(Arrays.asList(values), list);
 }

 @Test
 public void testLongCreator() {
 JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
 long[] values = new long[]{1, 2, 3};
 AnnotatedClass ac = mapper.getFactory().findClassInstancifier(ArrayList.class);
 AnnotatedWithParams creator = ac.getAnnotationMembers()[2];
 collector.addLongCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 List list = inst.createValueInstance(mapper.getConfig(), TypeDeserializer.longValue(1));
 assertEquals(Arrays.asList(values), list);
 }

 @Test
 public void testDoubleCreator() {
 JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
 double[] values = new double[]{1.0, 2.0, 3.0};
 AnnotatedClass ac = mapper.getFactory().findClassInstancifier(ArrayList.class);
 AnnotatedWithParams creator = ac.getAnnotationMembers()[3];
 collector.addDoubleCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 List list = inst.createValueInstance(mapper.getConfig(), TypeDeserializer.doubleValue(1.0));
 assertEquals(Arrays.asList(values), list);
 }

 @Test
 public void testBooleanCreator() {
 JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
 boolean[] values = new boolean[]{true, false, true};
 AnnotatedClass ac = mapper.getFactory().findClassInstancifier(ArrayList.class);
 AnnotatedWithParams creator = ac.getAnnotationMembers()[4];
 collector.addBooleanCreator(creator, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 List list = inst.createValueInstance(mapper.getConfig(), TypeDeserializer.booleanValue(true));
 assertEquals(Arrays.asList(values), list);
 }

 @Test
 public void testDelegateCreator() {
 JavaType delegateType = mapper.getTypeFactory().constructType(String.class);
 AnnotatedClass ac = mapper.getFactory().findClassInstancifier(ArrayList.class);
 AnnotatedWithParams creator = ac.getAnnotationMembers()[5];
 CreatorProperty[] injectables = new CreatorProperty[1];
 injectables[0] = new CreatorProperty(AnnotatedMember.ClassMember.FIELD, mapper.getFactory().findPropertyOrMemberInstance("delegate", String.class));
 collector.addDelegatingCreator(creator, true, injectables);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 ArrayList list = (ArrayList) inst.createValueInstance(mapper.getConfig(), delegateType);
 assertEquals("delegate", list.get(0));
 }

 @Test
 public void testPropertyBasedCreator() {
 JavaType type = mapper.getTypeFactory().constructType(HashMap.class);
 Map map = new HashMap();
 map.put("one", "one");
 map.put("two", "two");
 map.put("three", "three");
 Map properties = new HashMap();
 properties.put("one", new AnnotatedWithParams(new CreatorProperty(AnnotatedMember.ClassMember.FIELD, new AnnotatedMember(map, "one"))));
 properties.put("two", new AnnotatedWithParams(new CreatorProperty(AnnotatedMember.ClassMember.FIELD, new AnnotatedMember(map, "two"))));
 properties.put("three", new AnnotatedWithParams(new CreatorProperty(AnnotatedMember.ClassMember.FIELD, new AnnotatedMember(map, "three"))));
 collector.addPropertyCreator(new AnnotatedClass(HashMap.class), properties, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 Map createdMap = inst.createValueInstance(mapper.getConfig());
 assertEquals(map, createdMap);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreators() {
 String[] values = new String[]{"one", "two", "three"};
 AnnotatedClass ac = mapper.getFactory().findClassInstancifier(ArrayList.class);
 AnnotatedWithParams creator1 = ac.getAnnotationMembers()[0];
 collector.addStringCreator(creator1, true);
 AnnotatedWithParams creator2 = ac.getAnnotationMembers()[0];
 collector.addStringCreator(creator2, true);
 ValueInstantiator inst = collector.constructValueInstantiator(mapper.getConfig());
 try {
 inst.createValueInstance(mapper.getConfig());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}