import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CreatorPropertyTest {

 private CreatorProperty property;
 private ObjectBeanContext context;
 private Object beanInstance;

 @Before
 public void setup() throws Exception {
 JsonFactory factory = new JsonFactory();
 JsonParser parser = factory.getParser();
 ObjectMapper mapper = new ObjectMapper();
 context = new ObjectBeanContext();
 property = new CreatorProperty(
 new PropertyName("testProperty"),
 new BasicValueType(String.class),
 null,
 null,
 new Annotation[0],
 null,
 -1,
 null,
 PropertyMetadata.empty()
 );
 beanInstance = new TestBean();
 }

 @Test
 public void testBasicFunctionality() throws Exception {
 String testValue = "test value";
 property.setValueDeserializer(mapper.findValueDeserializer(String.class, null));
 property.inject(context, beanInstance);
 assertEquals(testValue, property.getValue(beanInstance));
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectableValueId() throws Exception {
 property._injectableValueId = null;
 property.inject(context, beanInstance);
 }

 @Test
 public void testFindInjectableValue() throws Exception {
 String testValue = "test value";
 context.setInjectableValues(new Object[]{testValue});
 assertEquals(testValue, property.findInjectableValue(context, beanInstance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 String testValue = "test value";
 Object result = property.setAndReturn(beanInstance, testValue);
 assertEquals(testValue, result);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String testValue = "test value";
 JsonParser p = context.getParserForObject(beanInstance.getClass());
 p.nextTokenId(JsonParser.CurrentName);
 p.nextValue("test value");
 property.deserializeAndSet(p, context, beanInstance);
 assertEquals(testValue, property.getValue(beanInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 String testValue = "test value";
 Object result = property.deserializeSetAndReturn(parser, context, beanInstance);
 assertEquals(testValue, result);
 }

 @Test
 public void testSet() throws Exception {
 String testValue = "test value";
 property.set(beanInstance, testValue);
 assertEquals(testValue, property.getValue(beanInstance));
 }

 @Test
 public void testIsIgnorable() {
 property.markAsIgnorable();
 assertTrue(property.isIgnorable());
 }

 @Test
 public void testGetCreatorIndex() {
 assertEquals(-1, property.getCreatorIndex());
 }

 @Test
 public void testGetAnnotation() {
 Class<Annotation> annotationType = Annotation.class;
 assertNull(property.getAnnotation(annotationType));
 }
}

class TestBean {
 // empty class for testing
}