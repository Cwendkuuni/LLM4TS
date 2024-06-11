import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

 private DefaultDeserializationContext context;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 context = mapper.getDeserializationContext();
 }

 // Test for findObjectId method
 @Test
 public void testFindObjectId() {
 Object id = new Object();
 ObjectIdGenerator gen = new StandardObjectIdGenerator();
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 ReadableObjectId expected = context.findObjectId(id, gen, resolver);
 ObjectIdGenerator.IdKey key = gen.key(id);
 assertEquals(key, expected.getKey());
 }

 // Test for findObjectId method with null id
 @Test
 public void testFindObjectIdWithNullId() {
 ObjectIdGenerator gen = new StandardObjectIdGenerator();
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 ReadableObjectId actual = context.findObjectId(null, gen, resolver);
 assertNull(actual);
 }

 // Test for findObjectId method with null generator
 @Test(expected = NullPointerException.class)
 public void testFindObjectIdWithNullGenerator() {
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 context.findObjectId(new Object(), null, resolver);
 }

 // Test for findObjectId method with null object id resolver
 @Test(expected = NullPointerException.class)
 public void testFindObjectIdWithNullResolver() {
 ObjectIdGenerator gen = new StandardObjectIdGenerator();

 context.findObjectId(new Object(), gen, null);
 }

 // Test for checkUnresolvedObjectId method
 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 ObjectIdGenerator gen = new StandardObjectIdGenerator();
 Object id1 = new Object();
 Object id2 = new Object();
 context._objectIds = new LinkedHashMap<>();
 context._objectIdResolvers = new ArrayList<>();

 context.findObjectId(id1, gen, new SimpleObjectIdResolver());
 context.findObjectId(id2, gen, new SimpleObjectIdResolver());

 context.checkUnresolvedObjectId();
 }

 // Test for deserializerInstance method
 @Test
 public void testDeserializerInstance() throws JsonMappingException {
 Object testClass = TestClass.class;
 Annotated ann = Mockito.mock(Annotated.class);
 JsonDeserializer<TestClass> deserializer = new TestClassDeserializer();
 when(ann.getType()).thenReturn(testClass);

 JsonDeserializer<TestClass> actual = context.deserializerInstance(ann, deserializer);
 assertEquals(deserializer, actual);
 }

 // Test for deserializerInstance method with null annotated
 @Test(expected = NullPointerException.class)
 public void testDeserializerInstanceWithNullAnnotated() {
 JsonDeserializer<Object> deserializer = new TestClassDeserializer();

 context.deserializerInstance(null, deserializer);
 }

 // Test for deserializerInstance method with null deserializer
 @Test(expected = NullPointerException.class)
 public void testDeserializerInstanceWithNullDeserializer() {
 Annotated ann = Mockito.mock(Annotated.class);

 context.deserializerInstance(ann, null);
 }

 // Test for keyDeserializerInstance method
 @Test
 public void testKeyDeserializerInstance() throws JsonMappingException {
 Object testClass = TestClassKey.class;
 Annotated ann = Mockito.mock(Annotated.class);
 KeyDeserializer deserializer = new TestClassKeyDeserializer();
 when(ann.getType()).thenReturn(testClass);

 KeyDeserializer actual = context.keyDeserializerInstance(ann, deserializer);
 assertEquals(deserializer, actual);
 }

 // Test for keyDeserializerInstance method with null annotated
 @Test(expected = NullPointerException.class)
 public void testKeyDeserializerInstanceWithNullAnnotated() {
 KeyDeserializer deserializer = new TestClassKeyDeserializer();

 context.keyDeserializerInstance(null, deserializer);
 }

 // Test for keyDeserializerInstance method with null key deserializer
 @Test(expected = NullPointerException.class)
 public void testKeyDeserializerInstanceWithNullKeyDeserializer() {
 Annotated ann = Mockito.mock(Annotated.class);

 context.keyDeserializerInstance(ann, null);
 }

 // Test for with method
 @Test
 public void testWith() {
 DeserializerFactory factory = Mockito.mock(DeserializerFactory.class);
 DefaultDeserializationContext expected = new DefaultDeserializationContext(new Impl(), factory);
 DefaultDeserializationContext actual = context.with(factory);
 assertEquals(expected, actual);
 }

 // Test for with method with null factory
 @Test(expected = NullPointerException.class)
 public void testWithWithNullFactory() {
 DefaultDeserializationContext actual = context.with(null);
 assertNull(actual);
 }

 // Test for createInstance method
 @Test
 public void testCreateInstance() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 DefaultDeserializationContext expected = mapper.getDeserializationContext();
 DefaultDeserializationContext actual = context.createInstance(mapper.getDeserializationConfig(), Mockito.mock(JsonParser.class), Mockito.mock(InjectableValues.class));
 assertEquals(expected, actual);
 }

}

class TestClass {
}

class TestClassDeserializer extends StdDeserializer<TestClass> {
 protected TestClassDeserializer() { this(null); }

 TestClassDeserializer(Class<?> vc) { super(vc); }

 @Override
 public TestClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return new TestClass();
 }
}

class TestClassKey {
}

class TestClassKeyDeserializer extends KeyDeserializer {
 @Override
 public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return new TestClassKey();
 }
}