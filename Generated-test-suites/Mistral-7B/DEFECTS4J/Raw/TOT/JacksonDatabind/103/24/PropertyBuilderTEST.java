package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;
import org.junit.runners.*;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PropertyBuilderTest {

 private static final Object NO_DEFAULT_MARKER = Boolean.FALSE;

 @Parameters
 public static Iterable<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Test case 1: Simple POJO with default values
 new Object[]{new SimplePojo(), null},
 // Test case 2: POJO with custom null serializer
 new Object[]{new CustomNullPojo(), CustomNullSerializer.class},
 // Test case 3: POJO with custom serialization type
 new Object[]{new CustomSerializationPojo(), CustomSerializationType.class},
 // Test case 4: POJO with empty lists and arrays
 new Object[]{new EmptyListsAndArraysPojo(), null},
 // Test case 5: POJO with NON_DEFAULT inclusion annotation
 new Object[]{new NonDefaultPojo(), null}
 });
 }

 private final Object objectToTest;
 private final Class<? extends JsonSerializer<?>> serializerClass;

 @Before
 public void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
 SerializationConfig config = objectMapper.getSerializationConfig();
 config.setAnnotationIntrospector(new DefaultAnnotationIntrospector());
 BeanFactory beanFactory = new BeanFactory();
 this.objectToTest = (this.serializerClass == null) ? beanFactory.getBeanInstance(this.objectToTest.getClass()) : beanFactory.getBeanInstance(this.objectToTest.getClass(), this.serializerClass);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 PropertyBuilder propertyBuilder = new PropertyBuilder(new Configuration(),
 beanFactory.getBeanDescription(this.objectToTest.getClass()));
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(new TestSerializerProvider(), null, this.objectToTest.getClass(), null, null, null, null, false);
 assertNotNull(beanPropertyWriter);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 when(am.getRawType()).thenReturn(this.objectToTest.getClass());
 when(am.getGenericType()).thenReturn(new SimpleType(this.objectToTest.getClass()));
 JavaType declaredType = Mockito.mock(JavaType.class);
 JavaType actualType = Mockito.mock(JavaType.class);
 when(actualType.getRawClass()).thenReturn(this.objectToTest.getClass());
 when(actualType.getContentType()).thenReturn(null);
 when(declaredType.getContentType()).thenReturn(null);
 when(propertyBuilder.findSerializationType(am, false, declaredType)).thenReturn(actualType);
 assertEquals(this.objectToTest.getClass(), propertyBuilder.findSerializationType(am, false, null).getRawClass());
 }

 @Test
 public void testGetDefaultBean() {
 assertNull(propertyBuilder.getDefaultBean());
 propertyBuilder.setDefaultBean(new SimplePojo());
 assertSame(new SimplePojo(), propertyBuilder.getDefaultBean());
 }

 @Test
 public void testGetPropertyDefaultValue() {
 String name = "name";
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 when(member.getName()).thenReturn(name);
 when(member.getValue(Mockito.any())).thenReturn("default value");
 when(propertyBuilder.getPropertyDefaultValue(name, member, Mockito.mock(JavaType.class))).thenReturn("default value");
 assertEquals("default value", propertyBuilder.getPropertyDefaultValue("name", member, new SimpleType(String.class)));
 }

 @Test
 public void testGetDefaultValue() {
 assertEquals("", propertyBuilder.getDefaultValue(new SimpleType(String.class)));
 assertEquals(0, propertyBuilder.getDefaultValue(new SimpleType(Integer.class)).intValue());
 assertEquals(false, propertyBuilder.getDefaultValue(new SimpleType(Boolean.class)).booleanValue());
 }

 @Test
 public void testThrowWrapped() {
 Exception e = new RuntimeException("Test exception");
 Throwable cause = new IllegalArgumentException("Caused exception");
 cause.initCause(e);
 assertThrows(IllegalArgumentException.class, () -> propertyBuilder._throwWrapped(cause, "test", new Object()));
 }

}