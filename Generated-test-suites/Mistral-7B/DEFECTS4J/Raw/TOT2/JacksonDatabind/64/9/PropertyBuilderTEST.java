import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder propertyBuilder;
 private Object defaultBean;

 @Before
 public void setup() throws JsonMappingException {
 config = mock(SerializationConfig.class);
 beanDesc = mock(BeanDescription.class);
 when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
 when(beanDesc.getType()).thenReturn(Object.class);
 when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.empty());
 when(config.getDefaultPropertyInclusion(Object.class, JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.empty());
 when(config.getDefaultPropertyInclusion()).thenReturn(JsonInclude.Value.empty());
 when(config.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)).thenReturn(true);
 propertyBuilder = new PropertyBuilder(config, beanDesc);
 defaultBean = propertyBuilder.getDefaultBean();
 }

 @Test
 public void testGetClassAnnotations() {
 assertEquals(new Annotations(), propertyBuilder.getClassAnnotations());
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mock(JavaType.class);
 when(propDef.getName()).thenReturn("testProperty");
 when(propDef.findInclusion()).thenReturn(JsonInclude.Include.NON_DEFAULT);
 when(declaredType.getRawClass()).thenReturn(String.class);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, null, true);
 assertEquals(writer.getPropertyName(), "testProperty");
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithInvalidSerializationType() throws JsonMappingException {
 JsonSerializer<?> serializer = mock(JsonSerializer.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType declaredType = mock(JavaType.class);
 when(propDef.getName()).thenReturn("testProperty");
 when(propDef.findInclusion()).thenReturn(JsonInclude.Include.NON_DEFAULT);
 when(declaredType.getRawClass()).thenReturn(Object.class);
 when(declaredType.isContainerType()).thenReturn(true);
 when(declaredType.getContentType()).thenReturn(null);
 BeanPropertyWriter writer = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, null, true);
 assertNull(writer);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember am = mock(AnnotatedMember.class);
 JavaType declaredType = mock(JavaType.class);
 when(am.getName()).thenReturn("testProperty");
 when(am.getGenericType()).thenReturn(IntType.class);
when(am.getRawType()).thenReturn(Integer.class);
when(am.getDeclaringClass()).thenReturn(Object.class);
JavaType secondary = mock(JavaType.class);
when(secondary.getRawClass()).thenReturn(Integer.class);
when(propertyBuilder.findSerializationType(am, false, declaredType)).thenReturn(secondary);
assertEquals(secondary, propertyBuilder.findSerializationType(am, true, declaredType));
 }

 @Test
 public void testGetDefaultBean() {
 assertNull(propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetPropertyDefaultValue() throws JsonMappingException {
 String name = "testProperty";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getValue(defaultBean)).thenReturn("testValue");
 JavaType type = IntType.class;
 Object value = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertEquals("testValue", value);
 }

 @Test
 public void testGetPropertyDefaultValueForReferenceType() throws JsonMappingException {
 String name = "testProperty";
 AnnotatedMember member = mock(AnnotatedMember.class);
 when(member.getValue(defaultBean)).thenReturn(new Object());
 JavaType type = Object.class;
 Object value = propertyBuilder.getPropertyDefaultValue(name, member, type);
 assertEquals(JsonInclude.Include.NON_EMPTY, value);
 }

 @Test
 public void testGetDefaultValue() {
 assertEquals(Integer.TYPE, propertyBuilder.getDefaultValue(IntType.class).getClass());
 }

 @Test
 public void testGetDefaultValueForStringType() {
 assertEquals("", propertyBuilder.getDefaultValue(StringType.class));
 }

 @Test
 public void testGetDefaultValueForContainerType() {
 assertNull(propertyBuilder.getDefaultValue(ListType.class));
 }

 @Test
 public void testThrowWrappedException() {
 Exception e = new IllegalArgumentException("Test exception");
 String propName = "testProperty";
 Object defaultBean = new Object();
 PropertyBuilder.PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 Object result = propertyBuilder._throwWrapped(e, propName, defaultBean);
 assertEquals(result.getClass(), IllegalArgumentException.class);
 }
}