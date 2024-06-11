import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

  private SettableAnyProperty settableAnyProperty;

  @Before
  public void setUp() {
    // create a mock property, setter, and valueDeser for testing
    BeanProperty propertyMock = Mockito.mock(BeanProperty.class);
    AnnotatedMember setterMock = Mockito.mock(AnnotatedMember.class);
    JavaType typeMock = Mockito.mock(JavaType.class);
    KeyDeserializer keyDeserMock = Mockito.mock(KeyDeserializer.class);
    JsonDeserializer<Object> valueDeserMock = Mockito.mock(JsonDeserializer.class);
    TypeDeserializer typeDeserMock = Mockito.mock(TypeDeserializer.class);

    settableAnyProperty = new SettableAnyProperty(propertyMock, setterMock, typeMock, keyDeserMock, valueDeserMock, typeDeserMock);
  }

  @Test
  public void testGetProperty() {
    assertNotNull(settableAnyProperty.getProperty());
  }

  @Test
  public void testHasValueDeserializer() {
    assertFalse(settableAnyProperty.hasValueDeserializer());
  }

  @Test
  public void testGetType() {
    assertNotNull(settableAnyProperty.getType());
  }

  @Test
  public void testDeserializeAndSet() {
    // create a mock JsonParser, DeserializationContext, instance, and propName for testing
    JsonParser pMock = Mockito.mock(JsonParser.class);
    DeserializationContext ctxtMock = Mockito.mock(DeserializationContext.class);
    Object instanceMock = Mockito.mock(Object.class);
    String propName = "testProp";

    try {
      settableAnyProperty.deserializeAndSet(pMock, ctxtMock, instanceMock, propName);
      // verify that the appropriate methods were called
      Mockito.verify(pMock).getCurrentToken();
      Mockito.verify(pMock, Mockito.atLeastOnce()).nextToken();
      Mockito.verify(ctxtMock).isEnabled(Mockito.any(MapperFeature.class));
      Mockito.verify(ctxtMock).getConfig();
      Mockito.verify(ctxtMock).getParser();
      Mockito.verify(pMock, Mockito.atLeastOnce()).getCurrentName();
      Mockito.verify(pMock, Mockito.atLeastOnce()).getText();
      Mockito.verify(pMock).nextToken();
      Mockito.verify(ctxtMock).isExpectedStartArrayToken(Mockito.any(Class.class));
      Mockito.verify(ctxtMock).handleUnexpectedToken(Mockito.any(Class.class), Mockito.any(JsonParser.class));
      Mockito.verify(pMock).skipChildren();
      Mockito.verify(pMock).getText();
      Mockito.verify(pMock, Mockito.atLeastOnce()).nextToken();
      Mockito.verify(pMock).nextToken();
      Mockito.verify(pMock).nextFieldName();
      Mockito.verify(pMock).nextToken();
      Mockito.verify(ctxtMock).handleUnexpectedToken(Mockito.any(Class.class), Mockito.any(JsonParser.class));
      Mockito.verify(pMock).nextToken();
      Mockito.verify(pMock).nextFieldName();
      Mockito.verify(pMock).nextToken();
      Mockito.verify(ctxtMock).handleUnexpectedToken(Mockito.any(Class.class), Mockito.any(JsonParser.class));
      Mockito.verify(pMock).nextToken();
      Mockito.verify(pMock).nextFieldName();
      Mockito.verify(pMock).nextToken();
      Mockito.verify(pMock).nextToken();
      Mockito.verify(pMock).getCurrentName();
      Mockito.verify(pMock).getText();
      Mockito.verify(pMock).nextToken();
      Mockito.verify(ctxtMock).handleUnexpectedToken(Mockito.any(Class.class), Mockito.any(JsonParser.class));
      Mockito.verify(ctxtMock).reportInputMismatch(Mockito.any(JsonDeserializer.class), Mockito.anyString());
      Mockito.verify(pMock).nextToken();
      Mockito.verify(ctxtMock).handleUnexpectedToken(Mockito.any(Class.class), Mockito.any(JsonParser.class));
      Mockito.verify(ctxtMock).handleUnexpectedToken(Mockito.any(Class.class), Mockito.any(JsonParser.class));
      Mockito.verify(pMock).nextTextValue();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testDeserializeAndSet_UnresolvedForwardReference() throws IOException {
    // create a mock UnresolvedForwardReference, JsonParser, DeserializationContext, instance, and propName for testing
    UnresolvedForwardReference referenceMock = Mockito.mock(UnresolvedForwardReference.class);
    JsonParser pMock = Mockito.mock(JsonParser.class);
    DeserializationContext ctxtMock = Mockito.mock(DeserializationContext.class);
    Object instanceMock = Mockito.mock(Object.class);
    String propName = "testProp";

    try {
      settableAnyProperty.deserializeAndSet(pMock, ctxtMock, instanceMock, propName);
      // verify that the appropriate methods were called
      Mockito.verify(pMock).getCurrentToken();
      Mockito.verify(ctxtMock, Mockito.atLeastOnce()).isEnabled(Mockito.any(MapperFeature.class));
      Mockito.verify(ctxtMock, Mockito.atLeastOnce()).getParser();
      Mockito.verify(pMock, Mockito.atLeastOnce()).skipChildren();
      Mockito.verify(ctxtMock).reportInputMismatch(Mockito.any(JsonDeserializer.class), Mockito.anyString());
      Mockito.verify(pMock, Mockito.atLeastOnce()).getCurrentName();
      Mockito.verify(pMock, Mockito.atLeastOnce()).nextToken();
      Mockito.verify(pMock).nextFieldName();
      Mockito.verify(pMock).getText();
      Mockito.verify(ctxtMock).handleUnexpectedToken(JsonParser.class, pMock);
      Mockito.verify(pMock).nextToken();
      Mockito.verify(pMock).getCurrentName();
      Mockito.verify(pMock).getText();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testDeserialize() {
    // create a mock JsonParser and DeserializationContext for testing
    JsonParser pMock = Mockito.mock(JsonParser.class);
    DeserializationContext ctxtMock = Mockito.mock(DeserializationContext.class);

    try {
      settableAnyProperty.deserialize(pMock, ctxtMock);
      // verify that the appropriate methods were called
      Mockito.verify(pMock).getCurrentToken();
      Mockito.verify(pMock, Mockito.atLeastOnce()).nextToken();
      Mockito.verify(pMock).getCurrentToken();
      Mockito.verify(pMock, Mockito.atLeastOnce()).getText();
      Mockito.verify(pMock, Mockito.atLeastOnce()).getCurrentName();
      Mockito.verify(pMock, Mockito.atLeastOnce()).nextToken();
      Mockito.verify(pMock, Mockito.atLeastOnce()).nextFieldName();
      Mockito.verify(pMock).nextToken();
      Mockito.verify(pMock, Mockito.atLeastOnce()).getEmbeddedObject();
      Mockito.verify(pMock, Mockito.atLeastOnce()).nextToken();
      Mockito.verify(pMock).getText();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testSet() {
    // create a mock instance, propName, and value for testing
    Object instanceMock = Mockito.mock(Object.class);
    Object propName = "testProp";
    Object valueMock = Mockito.mock(Object.class);

    try {
      settableAnyProperty.set(instanceMock, propName, valueMock);
      // verify that the appropriate methods were called
      Mockito.verify(instanceMock).getClass();
      Mockito.verify(instanceMock).getClass();
      Mockito.verify(instanceMock).getClass();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void test_set_Throws_IllegalArgumentException() throws IOException {
    // create a mock instance, propName, and value for testing
    Object instanceMock = Mockito.mock(Object.class);
    Object propName = "testProp";
    Object valueMock = Mockito.mock(Object.class);

    try {
      settableAnyProperty.set(instanceMock, propName, valueMock);
      // verify that the appropriate methods were called
      Mockito.verify(instanceMock).getClass();
      Mockito.verify(instanceMock).getClass();
      Mockito.verify(instanceMock).getClass();
    } catch (IllegalArgumentException e) {
      // expected IllegalArgumentException
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void test_throwAsIOE_WithIllegalArgumentException() {
    // create a mock exception, propName, and value for testing
    Exception eMock = Mockito.mock(IllegalArgumentException.class);
    Object propName = "testProp";
    Object valueMock = Mockito.mock(Object.class);

    try {
      settableAnyProperty.throwAsIOE(eMock, propName, valueMock);
      // verify that the appropriate methods were called
      Mockito.verify(eMock).getMessage();
      Mockito.verifyNoMoreInteractions(eMock);
    } catch (IOException e) {
      fail("IOException thrown: " + e.getMessage());
    }
  }

  @Test(expected = IOException.class)
  public void test_throwAsIOE_WithIOException() throws IOException {
    // create a mock exception, propName, and value for testing
    IOException eMock = Mockito.mock(IOException.class);
    Object propName = "testProp";
    Object valueMock = Mockito.mock(Object.class);

    settableAnyProperty.throwAsIOE(eMock, propName, valueMock);
  }

  @Test(expected = RuntimeException.class)
  public void test_throwAsIOE_WithRuntimeException() throws IOException {
    // create a mock exception, propName, and value for testing
    RuntimeException eMock = Mockito.mock(RuntimeException.class);
    Object propName = "testProp";
    Object valueMock = Mockito.mock(Object.class);

    settableAnyProperty.throwAsIOE(eMock, propName, valueMock);
  }

  @Test
  public void test_toString() {
    assertNotNull(settableAnyProperty.toString());
  }

}