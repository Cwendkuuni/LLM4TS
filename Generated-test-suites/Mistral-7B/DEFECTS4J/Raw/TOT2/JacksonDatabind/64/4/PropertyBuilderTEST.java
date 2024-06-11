import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private PropertyBuilder builder;

 @Before
 public void setup() throws JsonMappingException {
 config = new SerializationConfig();
 ObjectMapper mapper = new ObjectMapper();
 beanDesc = mapper.getTypeFactory().constructType(PropertyBuilder.class);
 builder = new PropertyBuilder(config, beanDesc);
 }

 @Test
 public void testGetClassAnnotations() {
 Annotations actual = builder.getClassAnnotations();
 Annotations expected = beanDesc.getClassAnnotations();
 assertEquals(expected, actual);
 }

 @Test
 public void testBuildWriter_NullSer() {
 assertNull(builder.buildWriter(null, null, null, null, null, null, (AnnotatedMember) null, false));
 }

 @Test
 public void testBuildWriter_NullPropDef() {
 assertNull(builder.buildWriter(new SerializerProvider(), null, null, null, null, null, (AnnotatedMember) null, false));
 }

 @Test
 public void testBuildWriter_NullDeclaredType() {
 assertNull(builder.buildWriter(new SerializerProvider(), null, null, null, null, null, (AnnotatedMember) null, false));
 }

 @Test
 public void testBuildWriter_NullSerialized() {
 SerializerProvider prov = new SerializerProvider();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition();
 JavaType declaredType = TypeFactory.defaultInstance();
 assertNull(builder.buildWriter(prov, propDef, declaredType, null, null, null, (AnnotatedMember) null, false));
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 SerializerProvider prov = new SerializerProvider();
 BeanPropertyDefinition propDef = new BeanPropertyDefinition();
 JavaType declaredType = TypeFactory.defaultInstance();
 JsonSerializer<?> ser = mock(JsonSerializer.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = mock(TypeSerializer.class);
 AnnotatedMember am = mock(AnnotatedMember.class);
 assertNotNull(builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false));
 }

 @Test
 public void testFindSerializationType_Annotated() throws JsonMappingException {
 Annotated am = mock(Annotated.class);
 JavaType declaredType = TypeFactory.defaultInstance();
 assertNull(builder.findSerializationType(am, false, declaredType));
 }

 @Test
 public void testFindSerializationType_StaticTyping() throws JsonMappingException {
 Annotated am = mock(Annotated.class);
 JavaType declaredType = TypeFactory.constructType(Integer.class);
 assertNull(builder.findSerializationType(am, true, declaredType));
 }

 @Test
 public void testFindSerializationType_DifferentTypes() throws JsonMappingException {
 Annotated am = mock(Annotated.class);
 JavaType declaredType = TypeFactory.constructType(Integer.class);
 JavaType secondary = TypeFactory.constructType(String.class);
 when(am.getAnnotated()).thenReturn(secondary.getRawClass());
 assertNull(builder.findSerializationType(am, false, declaredType));
 }

 @Test
 public void testFindSerializationType_SameType() throws JsonMappingException {
 Annotated am = mock(Annotated.class);
 JavaType declaredType = TypeFactory.constructType(Integer.class);
 JavaType secondary = TypeFactory.constructType(Integer.class);
 when(am.getAnnotated()).thenReturn(secondary.getRawClass());
 assertEquals(declaredType, builder.findSerializationType(am, false, declaredType));
 }

 @Test
 public void testFindSerializationType_DifferentSuperTypes() throws JsonMappingException {
 Annotated am = mock(Annotated.class);
 JavaType declaredType = TypeFactory.constructType(Integer.class);
 JavaType secondary = TypeFactory.constructType(Number.class);
 when(am.getAnnotated()).thenReturn(secondary.getRawClass());
 assertNotNull(builder.findSerializationType(am, false, declaredType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationType_IllegalType() throws JsonMappingException {
 Annotated am = mock(Annotated.class);
 JavaType declaredType = TypeFactory.constructType(Integer.class);
 JavaType secondary = TypeFactory.constructType(Float.class);
 when(am.getAnnotated()).thenReturn(secondary.getRawClass());
 builder.findSerializationType(am, false, declaredType);
 }

 @Test
 public void testFindSerializationType_StaticTyping_ClassAnnotations() throws JsonMappingException {
 Annotated am = mock(Annotated.class);
 JavaType declaredType = TypeFactory.constructType(Integer.class);
 when(am.getAnnotated()).thenReturn(declaredType.getRawClass());
 when(_annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(TypeFactory.constructType(String.class));
 when(_annotationIntrospector.findSerializationTyping(am)).thenReturn(JsonSerialize.Typing.STATIC);
 assertNull(builder.findSerializationType(am, true, declaredType));
 }

 @Test
 public void testFindSerializationType_StaticTyping_AnnotationTyping() throws JsonMappingException {
 Annotated am = mock(Annotated.class);
 JavaType declaredType = TypeFactory.constructType(Integer.class);
 when(am.getAnnotated()).thenReturn(declaredType.getRawClass());
 when(_annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(TypeFactory.constructType(String.class));
 when(_annotationIntrospector.findSerializationTyping(am)).thenReturn(JsonSerialize.Typing.DEFAULT_TYPING);
 assertEquals(declaredType, builder.findSerializationType(am, true, declaredType));
 }

 @Test
 public void testGetPropertyDefaultValue() {
 String propName = "testProp";
 AnnotatedMember member = mock(AnnotatedMember.class);
 JavaType type = TypeFactory.constructType(Integer.class);
 Object defaultBean = new Object();
 assertEquals(0, builder.getPropertyDefaultValue(propName, member, type));
 }

 @Test
 public void testGetDefaultBean() {
 Object defaultBean = new Object();
 assertEquals(defaultBean, builder.getDefaultBean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDefaultBean_NullBeanDesc() {
 PropertyBuilder builder = new PropertyBuilder(null, null);
 assertNull(builder.getDefaultBean());
 }

 @Test
 public void testGetDefaultValue_Primitive() {
 JavaType type = TypeFactory.constructType(int.class);
 assertEquals(0, builder.getDefaultValue(type));
 }

 @Test
 public void testGetDefaultValue_WrapperTypeForPrimitive() {
 JavaType type = TypeFactory.constructType(Integer.class);
 assertEquals(0, builder.getDefaultValue(type));
 }

 @Test
 public void testGetDefaultValue_String() {
 JavaType type = TypeFactory.constructType(String.class);
 assertEquals("", builder.getDefaultValue(type));
 }

 @Test
 public void testGetDefaultValue_ContainerType() {
 JavaType type = TypeFactory.constructType(List.class);
 assertEquals(JsonInclude.Include.NON_EMPTY, builder.getDefaultValue(type));
 }

 @Test
 public void testGetDefaultValue_ReferenceType() {
 JavaType type = TypeFactory.constructType(String[].class);
 assertEquals(JsonInclude.Include.NON_EMPTY, builder.getDefaultValue(type));
 }

 @Test
 public void testThrowWrapped() {
 Exception e = new Exception("Test Exception");
 String propName = "testProp";
 Object defaultBean = new Object();
 assertEquals("Failed to get property 'testProp' of default "+defaultBean.getClass().getName()+" instance", e.getMessage(), builder._throwWrapped(e, propName, defaultBean).getMessage());
 }
}