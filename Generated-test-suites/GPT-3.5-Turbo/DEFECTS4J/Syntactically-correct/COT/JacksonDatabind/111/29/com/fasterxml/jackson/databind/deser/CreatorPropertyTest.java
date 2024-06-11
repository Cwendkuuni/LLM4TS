import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {
  
  private CreatorProperty creatorProperty;
  
  @Before
  public void setUp() {
    creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
  }
  
  @Test
  public void withNameTest() {
    PropertyName newName = new PropertyName("");
    SettableBeanProperty result = creatorProperty.withName(newName);
    assertEquals(newName, result.getName());
  }
  
  @Test
  public void withValueDeserializerTest() {
    JsonDeserializer<?> deser = null;
    SettableBeanProperty result = creatorProperty.withValueDeserializer(deser);
    assertEquals(deser, result.getValueDeserializer());
  }
  
  @Test
  public void withNullProviderTest() {
    NullValueProvider nva = null;
    SettableBeanProperty result = creatorProperty.withNullProvider(nva);
    assertEquals(nva, result.getNullValueProvider());
  }
  
  @Test
  public void fixAccessTest() {
    DeserializationConfig config = null;
    creatorProperty.fixAccess(config);
    //Should not throw any exception
  }
  
  @Test
  public void setFallbackSetterTest() {
    SettableBeanProperty fallbackSetter = null;
    creatorProperty.setFallbackSetter(fallbackSetter);
    //Should not throw any exception
  }
  
  @Test
  public void markAsIgnorableTest() {
    creatorProperty.markAsIgnorable();
    assertTrue(creatorProperty.isIgnorable());
  }
  
  @Test
  public void isIgnorableTest() {
    assertFalse(creatorProperty.isIgnorable());
  }
  
  @Test(expected=InvalidDefinitionException.class)
  public void findInjectableValueTest() throws JsonMappingException {
    DeserializationContext context = null;
    Object beanInstance = null;
    creatorProperty.findInjectableValue(context, beanInstance);
  }
  
  @Test(expected=IOException.class)
  public void injectTest() throws IOException {
    DeserializationContext context = null;
    Object beanInstance = null;
    creatorProperty.inject(context, beanInstance);
  }
  
  @Test
  public void getAnnotationTest() {
    Class<A> acls = new Class<A>();
    A result = creatorProperty.getAnnotation(acls);
    assertNull(result);
  }
  
  @Test
  public void getMemberTest() {
    assertNull(creatorProperty.getMember());
  }
  
  @Test
  public void getCreatorIndexTest() {
    assertEquals(0, creatorProperty.getCreatorIndex());
  }
  
  @Test(expected=IOException.class)
  public void deserializeAndSetTest() throws IOException {
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Object instance = null;
    creatorProperty.deserializeAndSet(p, ctxt, instance);
  }
  
  @Test(expected=IOException.class)
  public void deserializeSetAndReturnTest() throws IOException {
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Object instance = null;
    creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
  }
  
  @Test(expected=IOException.class)
  public void setTest() throws IOException {
    Object instance = null;
    Object value = null;
    creatorProperty.set(instance, value);
  }
  
  @Test(expected=IOException.class)
  public void setAndReturnTest() throws IOException {
    Object instance = null;
    Object value = null;
    creatorProperty.setAndReturn(instance, value);
  }
  
  @Test
  public void getInjectableValueIdTest() {
    assertNull(creatorProperty.getInjectableValueId());
  }
  
  @Test
  public void toStringTest() {
    String result = creatorProperty.toString();
    assertNotNull(result);
  }
  
  @Test(expected=IOException.class)
  public void verifySetterTest() throws IOException {
    creatorProperty.setFallbackSetter(null);
    creatorProperty.deserializeAndSet(null, null, null);
  }
  
  @Test(expected=IOException.class)
  public void reportMissingSetterTest() throws IOException {
    creatorProperty._reportMissingSetter(null, null);
  }
}