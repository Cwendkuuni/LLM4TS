import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

 private CreatorProperty property;
 private ObjectBean objectBean;
 private DeserializationContext context;
 private JsonParser parser;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 context = mapper.getDeserializationContext();
 parser = mapper.getFactory().createParser("[{\"name\": \"test\", \"value\": 123}]");
 objectBean = new ObjectBean();
 }

 @Test
 public void testNormalDeserialization() throws Exception {
 property = new CreatorProperty(
 PropertyName.construct("test"),
 JavaType.constructRawType(Object.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.std()
 );
 property.setFallbackSetter(new CreatorProperty.SettableBeanProperty(PropertyName.construct("testValue"), JavaType.constructRawType(Integer.class), null, null, null, null, -1, null, PropertyMetadata.std()));
 property.deserializeAndSet(parser, context, objectBean);
 assertEquals(123, objectBean.getTestValue());
 }

 @Test
 public void testNullDeserialization() throws Exception {
 property = new CreatorProperty(
 PropertyName.construct("test"),
 JavaType.constructRawType(Object.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.std()
 );
 property.setFallbackSetter(new CreatorProperty.SettableBeanProperty(PropertyName.construct("testValue"), JavaType.constructRawType(Integer.class), null, null, null, null, -1, null, PropertyMetadata.std()));
 property.deserializeAndSet(parser, context, objectBean);
 assertNull(objectBean.getTestValue());
 }

 @Test(expected = IOException.class)
 public void testDeserializationFailure() throws Exception {
 property = new CreatorProperty(
 PropertyName.construct("test"),
 JavaType.constructRawType(Object.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.std()
 );
 property.setFallbackSetter(new CreatorProperty.SettableBeanProperty(PropertyName.construct("testValue"), JavaType.constructRawType(Integer.class), null, null, null, null, -1, null, PropertyMetadata.std()));
 property.deserializeAndSet(parser, context, objectBean);
 parser.nextToken(); // consume valid token to make sure parser is not empty
 parser.nextValue(); // consume valid value to make sure deserialization is attempted
 parser.nextToken(); // consume error token
 parser.nextValue(); // consume error value
 fail("Deserialization should have failed");
 }

 @Test
 public void testInjectValue() throws Exception {
 property = new CreatorProperty(
 PropertyName.construct("test"),
 JavaType.constructRawType(Object.class),
 null,
 null,
 null,
 null,
 -1,
 "testInjectId",
 PropertyMetadata.std()
 );
 property.setFallbackSetter(new CreatorProperty.SettableBeanProperty(PropertyName.construct("testValue"), JavaType.constructRawType(Integer.class), null, null, null, null, -1, null, PropertyMetadata.std()));
 context.injectValue("testInjectId", 456);
 property.inject(context, objectBean);
 assertEquals(456, objectBean.getTestValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingInjectValueId() throws Exception {
 property = new CreatorProperty(
 PropertyName.construct("test"),
 JavaType.constructRawType(Object.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.std()
 );
 property.setFallbackSetter(new CreatorProperty.SettableBeanProperty(PropertyName.construct("testValue"), JavaType.constructRawType(Integer.class), null, null, null, null, -1, null, PropertyMetadata.std()));
 context.reportBadDefinition(Object.class, "Property 'test' (type Object) has no injectable value id configured");
 property.inject(context, objectBean);
 fail("InjectValue should have thrown JsonMappingException");
 }

 @Test
 public void testSetterSetAfterConstruction() throws Exception {
 property = new CreatorProperty(
 PropertyName.construct("test"),
 JavaType.constructRawType(Object.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.std()
 );
 property.setFallbackSetter(new CreatorProperty.SettableBeanProperty(PropertyName.construct("testValue"), JavaType.constructRawType(Integer.class), null, null, null, null, -1, null, PropertyMetadata.std()));
 property.setFallbackSetter(new CreatorProperty.SettableBeanProperty(PropertyName.construct("testValue"), JavaType.constructRawType(String.class), null, null, null, null, -1, null, PropertyMetadata.std()));
 property.set(objectBean, "testValue");
 assertEquals("testValue", objectBean.getTestValue());
 }

 @Test(expected = IOException.class)
 public void testSetterSetBeforeConstruction() throws Exception {
 property = new CreatorProperty(
 PropertyName.construct("test"),
 JavaType.constructRawType(Object.class),
 null,
 null,
 null,
 null,
 -1,
 null,
 PropertyMetadata.std()
 );
 property.setFallbackSetter(new CreatorProperty.SettableBeanProperty(PropertyName.construct("testValue"), JavaType.constructRawType(Integer.class), null, null, null, null, -1, null, PropertyMetadata.std()));
 property.set(objectBean, 123);
 fail("Setter should not be able to be set before construction");
 }

}