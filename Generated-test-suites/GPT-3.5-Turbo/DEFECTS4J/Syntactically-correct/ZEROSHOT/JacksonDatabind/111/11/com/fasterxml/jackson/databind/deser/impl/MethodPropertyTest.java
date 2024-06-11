import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MethodPropertyTest {
  
  @Test
  public void testConstructor() {
    BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
    JavaType type = Mockito.mock(JavaType.class);
    TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
    Annotations contextAnnotations = Mockito.mock(Annotations.class);
    AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
    
    MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    
    assertNotNull(methodProperty);
    assertEquals(propDef, methodProperty.getPropertyDefinition());
    assertEquals(type, methodProperty.getType());
    assertEquals(typeDeser, methodProperty.getTypeDeserializer());
    assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
    assertEquals(method, methodProperty.getMember());
  }
  
  @Test
  public void testWithDeserialzer() {
    JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
    
    MethodProperty methodProperty = Mockito.mock(MethodProperty.class);
    MethodProperty newMethodProperty = methodProperty.withValueDeserializer(deser);
    
    assertNotNull(newMethodProperty);
    assertNotEquals(methodProperty, newMethodProperty);
    assertEquals(deser, newMethodProperty.getValueDeserializer());
  }
  
  @Test
  public void testWithNullProvider() {
    NullValueProvider nva = Mockito.mock(NullValueProvider.class);
    
    MethodProperty methodProperty = Mockito.mock(MethodProperty.class);
    MethodProperty newMethodProperty = methodProperty.withNullProvider(nva);
    
    assertNotNull(newMethodProperty);
    assertNotEquals(methodProperty, newMethodProperty);
    assertEquals(nva, newMethodProperty.getNullProvider());
  }
  
  @Test
  public void testFixAccess() {
    DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
    
    AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
    Mockito.doNothing().when(annotatedMethod).fixAccess(Mockito.anyBoolean());
    
    MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class),
                                                      Mockito.mock(JavaType.class),
                                                      Mockito.mock(TypeDeserializer.class),
                                                      Mockito.mock(Annotations.class),
                                                      annotatedMethod);
    
    methodProperty.fixAccess(config);
    
    Mockito.verify(annotatedMethod, Mockito.times(1)).fixAccess(Mockito.anyBoolean());
  }
  
  @Test
  public void testGetAnnotation() {
    AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
    Mockito.when(annotatedMethod.getAnnotation(Mockito.any(Class.class))).thenReturn(Mockito.mock(Annotation.class));
    
    MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class),
                                                      Mockito.mock(JavaType.class),
                                                      Mockito.mock(TypeDeserializer.class),
                                                      Mockito.mock(Annotations.class),
                                                      annotatedMethod);
    
    Annotation annotation = methodProperty.getAnnotation(Annotation.class);
    
    assertNotNull(annotation);
    Mockito.verify(annotatedMethod, Mockito.times(1)).getAnnotation(Mockito.any(Class.class));
  }
  
  @Test
  public void testDeserializeAndSet() throws IOException {
    JsonParser p = Mockito.mock(JsonParser.class);
    DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
    Object instance = new Object();
    Object value = Mockito.mock(Object.class);
    
    NullValueProvider nullProvider = Mockito.mock(NullValueProvider.class);
    Mockito.when(nullProvider.getNullValue(Mockito.any(DeserializationContext.class))).thenReturn(Mockito.mock(Object.class));
    
    Method setterMethod = Mockito.mock(Method.class);
    try {
      Mockito.when(setterMethod.invoke(Mockito.any(Object.class), Mockito.any(Object.class))).thenReturn(null);
      Mockito.when(setterMethod.invoke(Mockito.any(Object.class), Mockito.isNull())).thenThrow(new RuntimeException());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
    Mockito.when(annotatedMethod.getAnnotated()).thenReturn(setterMethod);
    
    MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class),
                                                      Mockito.mock(JavaType.class),
                                                      Mockito.mock(TypeDeserializer.class),
                                                      Mockito.mock(Annotations.class),
                                                      annotatedMethod);
    methodProperty._setter=setterMethod;
    methodProperty._nullProvider = nullProvider;
    
    methodProperty.deserializeAndSet(p, ctxt, instance);
    
    Mockito.verify(p, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
    Mockito.verify(nullProvider, Mockito.times(1)).getNullValue(Mockito.any(DeserializationContext.class));
    Mockito.verify(setterMethod, Mockito.times(1)).invoke(Mockito.any(Object.class), Mockito.any(Object.class));
    Mockito.verify(setterMethod, Mockito.times(1)).invoke(Mockito.any(Object.class), Mockito.isNull());
  }
  
  @Test
  public void testDeserializeSetAndReturn() throws IOException {
    JsonParser p = Mockito.mock(JsonParser.class);
    DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
    Object instance = new Object();
    Object value = Mockito.mock(Object.class);
    
    NullValueProvider nullProvider = Mockito.mock(NullValueProvider.class);
    Mockito.when(nullProvider.getNullValue(Mockito.any(DeserializationContext.class))).thenReturn(Mockito.mock(Object.class));
    
    Method setterMethod = Mockito.mock(Method.class);
    try {
      Mockito.when(setterMethod.invoke(Mockito.any(Object.class), Mockito.any(Object.class))).thenReturn(null);
      Mockito.when(setterMethod.invoke(Mockito.any(Object.class), Mockito.isNull())).thenThrow(new RuntimeException());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
    Mockito.when(annotatedMethod.getAnnotated()).thenReturn(setterMethod);
    
    MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class),
                                                      Mockito.mock(JavaType.class),
                                                      Mockito.mock(TypeDeserializer.class),
                                                      Mockito.mock(Annotations.class),
                                                      annotatedMethod);
    methodProperty._setter=setterMethod;
    methodProperty._nullProvider = nullProvider;
    
    methodProperty.deserializeSetAndReturn(p, ctxt, instance);
    
    Mockito.verify(p, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
    Mockito.verify(nullProvider, Mockito.times(1)).getNullValue(Mockito.any(DeserializationContext.class));
    Mockito.verify(setterMethod, Mockito.times(1)).invoke(Mockito.any(Object.class), Mockito.any(Object.class));
    Mockito.verify(setterMethod, Mockito.times(1)).invoke(Mockito.any(Object.class), Mockito.isNull());
  }
  
  @Test
  public void testSet() throws IOException {
    Object instance = new Object();
    Object value = Mockito.mock(Object.class);
    
    Method setterMethod = Mockito.mock(Method.class);
    try {
      Mockito.when(setterMethod.invoke(Mockito.any(Object.class), Mockito.any(Object.class))).thenReturn(null);
      Mockito.when(setterMethod.invoke(Mockito.any(Object.class), Mockito.isNull())).thenThrow(new RuntimeException());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
    Mockito.when(annotatedMethod.getAnnotated()).thenReturn(setterMethod);
    
    MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class),
                                                      Mockito.mock(JavaType.class),
                                                      Mockito.mock(TypeDeserializer.class),
                                                      Mockito.mock(Annotations.class),
                                                      annotatedMethod);
    methodProperty._setter=setterMethod;
    
    methodProperty.set(instance, value);
    
    Mockito.verify(setterMethod, Mockito.times(1)).invoke(Mockito.any(Object.class), Mockito.any(Object.class));
  }
  
  @Test
  public void testSetAndReturn() throws IOException {
    Object instance = new Object();
    Object value = Mockito.mock(Object.class);
    
    Method setterMethod = Mockito.mock(Method.class);
    try {
      Mockito.when(setterMethod.invoke(Mockito.any(Object.class), Mockito.any(Object.class))).thenReturn(instance);
      Mockito.when(setterMethod.invoke(Mockito.any(Object.class), Mockito.isNull())).thenThrow(new RuntimeException());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
    Mockito.when(annotatedMethod.getAnnotated()).thenReturn(setterMethod);
    
    MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class),
                                                      Mockito.mock(JavaType.class),
                                                      Mockito.mock(TypeDeserializer.class),
                                                      Mockito.mock(Annotations.class),
                                                      annotatedMethod);
    methodProperty._setter=setterMethod;
    
    methodProperty.setAndReturn(instance, value);
    
    Mockito.verify(setterMethod, Mockito.times(1)).invoke(Mockito.any(Object.class), Mockito.any(Object.class));
  }
}