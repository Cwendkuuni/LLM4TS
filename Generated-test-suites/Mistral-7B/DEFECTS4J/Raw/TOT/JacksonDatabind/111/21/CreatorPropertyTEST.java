import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private Object testInstance;
 private Object injectableValue;
 private DeserializationContext context;
 private JsonParser parser;

 @Before
 public void setup() throws JsonProcessingException {
 testInstance = new Object();
 injectableValue = new Object();
 context = new TestingDeserializationContext();
 parser = context.getParser();
 }

 @Test
 public void testConstructorWithAnnotatedParameter() throws JsonProcessingException {
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("propertyName"),
 JavaType.construct(Object.class),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(new Annotation[]{}, "paramName", Object.class, 0, null),
 0,
 null,
 PropertyMetadata.empty()
 );

 Object result = property.deserializeAndSet(parser, context, testInstance);
 assertEquals(injectableValue, testInstance);
 }

 @Test(expected = JsonMappingException.class)
 public void testConstructorWithoutInjectableValueId() throws JsonProcessingException {
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("propertyName"),
 JavaType.construct(Object.class),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(new Annotation[]{}, "paramName", Object.class, 0, null),
 0,
 null,
 PropertyMetadata.empty()
 );
 context.reportBadDefinition(Object.class, "Property 'propertyName' has no injectable value id configured");
 property.findInjectableValue(context, testInstance);
 }

 @Test
 public void testInjectValue() throws JsonProcessingException {
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("propertyName"),
 JavaType.construct(Object.class),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(new Annotation[]{}, "paramName", Object.class, 0, null),
 0,
 null,
 PropertyMetadata.empty()
 );
 property.markAsIgnorable();
 property.inject(context, testInstance);
 assertTrue(property.isIgnorable());
 }

 @Test(expected = IOException.class)
 public void testSetValueWithoutFallbackSetter() throws JsonProcessingException {
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("propertyName"),
 JavaType.construct(Object.class),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(new Annotation[]{}, "paramName", Object.class, 0, null),
 0,
 null,
 PropertyMetadata.empty()
 );
 property.setFallbackSetter(null);
 parser.nextToken(); // Consume START_OBJECT token
 property.set(testInstance, parser.getValueAsToken());
 }

 @Test
 public void testSetValue() throws JsonProcessingException {
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("propertyName"),
 JavaType.construct(Object.class),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(new Annotation[]{}, "paramName", Object.class, 0, null),
 0,
 null,
 PropertyMetadata.empty()
 );
 SettableBeanProperty fallbackSetter = new SimpleBeanProperty();
 property.setFallbackSetter(fallbackSetter);
 parser.nextToken(); // Consume START_OBJECT token
 property.set(testInstance, parser.getValueAsToken());
 assertEquals(parser.getValueAsToken(), fallbackSetter.get(testInstance));
 }

 @Test
 public void testFindInjectableValue() throws JsonProcessingException {
 CreatorProperty property = new CreatorProperty(
 PropertyName.construct("propertyName"),
 JavaType.construct(Object.class),
 PropertyName.construct("wrapperName"),
 null,
 Annotations.empty(),
 AnnotatedParameter.construct(new Annotation[]{}, "paramName", Object.class, 0, null),
 0,
 null,
 PropertyMetadata.empty()
 );
 property.injectableValueId = "injectValueId";
 Object result = property.findInjectableValue(context, testInstance);
 assertEquals(injectableValue, result);
 }

 @After
 public void teardown() {
 context = null;
 parser = null;
 testInstance = null;
 injectableValue = null;
 }
}