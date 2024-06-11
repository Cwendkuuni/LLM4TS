import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PropertyBuilderTest {

 private SerializationConfig _config;
 private BeanDescription _beanDesc;
 private AnnotationIntrospector _annotationIntrospector;
 private Object _defaultBean;
 private JsonInclude.Value _defaultInclusion;
 private boolean _useRealPropertyDefaults;

 @BeforeAll
 public void setup() throws JsonMappingException {
 _config = Mockito.mock(SerializationConfig.class);
 _beanDesc = Mockito.mock(BeanDescription.class);
 _annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
 _defaultBean = null;
 _defaultInclusion = JsonInclude.Value.STANDARD_EXCLUDE;
 _useRealPropertyDefaults = false;
 }

 @Test
 public void testClassAnnotations() {
 PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
 Annotations actual = builder.getClassAnnotations();
 Annotations expected = Mockito.mock(Annotations.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 SerializerProvider prov = Mockito.mock(SerializerProvider.class);
 BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
 JavaType declaredType = Mockito.mock(JavaType.class);
 JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
 TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
 TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 boolean defaultUseStaticTyping = false;

 PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
 BeanPropertyWriter actual = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
 BeanPropertyWriter expected = Mockito.mock(BeanPropertyWriter.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 Annotated a = Mockito.mock(Annotated.class);
 JavaType declaredType = Mockito.mock(JavaType.class);
 boolean useStaticTyping = false;

 PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
 JavaType actual = builder.findSerializationType(a, useStaticTyping, declaredType);
 JavaType expected = Mockito.mock(JavaType.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDefaultBean() {
 PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
 Object actual = builder.getDefaultBean();
 Object expected = null;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetPropertyDefaultValue() {
 String name = "test";
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 JavaType type = Mockito.mock(JavaType.class);

 PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
 Object actual = builder.getPropertyDefaultValue(name, member, type);
 Object expected = null;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDefaultValue() {
 JavaType type = Mockito.mock(JavaType.class);
 PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
 Object actual = builder.getDefaultValue(type);
 Object expected = null;
 assertEquals(expected, actual);
 }

 @Test
 public void test_throwWrapped() {
 Exception e = new Exception("Test Exception");
 String propName = "test";
 Object defaultBean = new Object();
 PropertyBuilder builder = new PropertyBuilder(_config, _beanDesc);
 Object result = builder._throwWrapped(e, propName, defaultBean);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @AfterAll
 public void teardown() {
 // Cleanup mocks
 Mockito.verifyNoInteractions(_config, _beanDesc, _annotationIntrospector, _defaultBean, _defaultInclusion, _useRealPropertyDefaults);
 }

}