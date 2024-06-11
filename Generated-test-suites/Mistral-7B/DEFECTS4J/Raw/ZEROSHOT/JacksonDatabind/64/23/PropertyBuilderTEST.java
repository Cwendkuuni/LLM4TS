import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectNode testNode;
 private ObjectMapper objectMapper;

 @BeforeMethod
 public void setup() {
 objectMapper = new ObjectMapper();
 testNode = objectMapper.createObjectNode();
 }

 @Test
 public void testClassAnnotations() throws JsonProcessingException, IOException {
 // Given
 BeanDescription beanDescription = mock(BeanDescription.class);
 SerializationConfig serializationConfig = mock(SerializationConfig.class);
 when(beanDescription.getClassAnnotations()).thenReturn(mock(Annotations.class));

 // When
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

 // Then
 Annotations classAnnotations = propertyBuilder.getClassAnnotations();
 assertEquals(classAnnotations, mock(Annotations.class));
 }

 @Test
 public void testBuildWriter() throws JsonProcessingException, IOException {
 // Given
 BeanDescription beanDescription = mock(BeanDescription.class);
 SerializationConfig serializationConfig = mock(SerializationConfig.class);
 Object bean = mock(Object.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mock(JavaType.class);
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(beanDescription.findPropertyDefinition(eq("testProperty"))).thenReturn(propDef);
 when(beanDescription.getType()).thenReturn(declaredType);
 when(bean.getClass()).thenReturn(bean.getClass());
 when(am.getGenericType()).thenReturn(declaredType);
 when(am.getName()).thenReturn("testProperty");
 when(am.getMember()).thenReturn(bean);
 when(serializationConfig.getDefaultPropertyInclusion(declaredType.getRawClass(), _defaultInclusion)).thenReturn(_defaultInclusion);
 when(_defaultInclusion.getValueInclusion()).thenReturn(JsonInclude.Include.NON_DEFAULT);
 when(_config.getDefaultPropertyInclusion()).thenReturn(_defaultInclusion);

 // When
 Object defaultBean = new Object();
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

 // Then
 assertNotNull(beanPropertyWriter);
 verify(beanDescription).getClassAnnotations();
 verify(beanDescription).findPropertyDefinition(eq("testProperty"));
 verify(beanDescription).getType();
 verify(bean).getClass();
 verify(am).getName();
 verify(am).getGenericType();
 verify(serializationConfig).getDefaultPropertyInclusion(declaredType.getRawClass(), _defaultInclusion);
 verifyZeroInteractions(ser, typeSer, contentTypeSer, am);
 }

 @Test
 public void testFindSerializationType() throws JsonProcessingException, IOException {
 // Given
 BeanDescription beanDescription = mock(BeanDescription.class);
 SerializationConfig serializationConfig = mock(SerializationConfig.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 JavaType declaredType = mock(JavaType.class);
 when(beanDescription.findPropertyDefinition(eq("testProperty"))).thenReturn(mock(BeanPropertyDefinition.class));
 when(beanDescription.getType()).thenReturn(declaredType);
 when(am.getGenericType()).thenReturn(declaredType);
 when(am.getName()).thenReturn("testProperty");
 when(serializationConfig.getDefaultPropertyInclusion(declaredType.getRawClass(), _defaultInclusion)).thenReturn(_defaultInclusion);
 when(_defaultInclusion.getValueInclusion()).thenReturn(JsonInclude.Include.NON_DEFAULT);
 when(_config.getDefaultPropertyInclusion()).thenReturn(_defaultInclusion);
 when(serializerProvider.findValueSerializer(eq(declaredType), _beanDesc, _annotationIntrospector)).thenReturn(mock(JsonSerializer.class));

 // When
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);

 // Then
 assertEquals(serializationType, declaredType);
 }

 @Test
 public void testGetPropertyDefaultValue() throws JsonProcessingException, IOException {
 // Given
 BeanDescription beanDescription = mock(BeanDescription.class);
 SerializationConfig serializationConfig = mock(SerializationConfig.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 Object defaultBean = new Object();
 when(beanDescription.instantiateBean(true)).thenReturn(defaultBean);
 when(beanDescription.getClassInfo()).thenReturn(mock(ClassInfo.class));
 when(beanDescription.findPropertyDefinition(eq("testProperty"))).thenReturn(mock(BeanPropertyDefinition.class));
 when(am.getValue(defaultBean)).thenReturn("defaultValue");
 when(am.getName()).thenReturn("testProperty");
 when(am.getMember()).thenReturn(defaultBean);
 when(am.getGenericType()).thenReturn(mock(JavaType.class));

 // When
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", am, mock(JavaType.class));

 // Then
 assertEquals(propertyDefaultValue, "defaultValue");
 }

 @Test(expectedExceptions = IllegalStateException.class)
 public void testGetPropertyDefaultValueWithNoDefaultBean() throws JsonProcessingException, IOException {
 // Given
 BeanDescription beanDescription = mock(BeanDescription.class);
 SerializationConfig serializationConfig = mock(SerializationConfig.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(beanDescription.instantiateBean(true)).thenReturn(null);
 when(beanDescription.getClassInfo()).thenReturn(mock(ClassInfo.class));
 when(beanDescription.findPropertyDefinition(eq("testProperty"))).thenReturn(mock(BeanPropertyDefinition.class));
 when(am.getValue(null)).thenThrow(new IllegalStateException());
 when(am.getName()).thenReturn("testProperty");
 when(am.getMember()).thenReturn(null);
 when(am.getGenericType()).thenReturn(mock(JavaType.class));

 // When
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 propertyBuilder.getPropertyDefaultValue("testProperty", am, mock(JavaType.class));

 // Then
 // Exception is already thrown
 }

 @Test(expectedExceptions = IllegalArgumentException.class)
 public void testGetPropertyDefaultValueWithNonExistingProperty() throws JsonProcessingException, IOException {
 // Given
 BeanDescription beanDescription = mock(BeanDescription.class);
 SerializationConfig serializationConfig = mock(SerializationConfig.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 when(beanDescription.getClassInfo()).thenReturn(mock(ClassInfo.class));
 when(beanDescription.findPropertyDefinition(eq("testProperty"))).thenReturn(null);
 when(am.getName()).thenReturn("testProperty");
 when(am.getMember()).thenReturn(null);
 when(am.getGenericType()).thenReturn(mock(JavaType.class));

 // When
 PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
 propertyBuilder.getPropertyDefaultValue("testProperty", am, mock(JavaType.class));

 // Then
 // Exception is already thrown
 }
}