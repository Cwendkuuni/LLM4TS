import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BaseSettingsTest {

 private ObjectMapper mapper;

 @Test
 public void testBaseSettingsCreation() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.WithDefault(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Std();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new StdDateFormat("dd-MM-yyyy HH:mm:ss zzz");
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

 assertNotNull(baseSettings);
 assertEquals(classIntrospector, baseSettings.getClassIntrospector());
 assertEquals(annotationIntrospector, baseSettings.getAnnotationIntrospector());
 assertEquals(visibilityChecker, baseSettings.getVisibilityChecker());
 assertEquals(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
 assertEquals(typeFactory, baseSettings.getTypeFactory());
 assertEquals(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
 assertEquals(dateFormat, baseSettings.getDateFormat());
 assertEquals(handlerInstantiator, baseSettings.getHandlerInstantiator());
 assertEquals(locale, baseSettings.getLocale());
 assertEquals(timeZone, baseSettings.getTimeZone());
 assertEquals(defaultBase64, baseSettings.getBase64Variant());
 }

 @Test
 public void testBaseSettingsSerializationAndDeserialization() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 BaseSettings original = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
 new VisibilityChecker.WithDefault(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE), new PropertyNamingStrategy.Std(),
 new TypeFactory(), new TypeResolverBuilderDefault(), new StdDateFormat("dd-MM-yyyy HH:mm:ss zzz"),
 new HandlerInstantiator(), Locale.US, TimeZone.getDefault(), Base64Variants.STANDARD);

 String json = mapper.writeValueAsString(original);

 BaseSettings deserialized = mapper.readValue(json, BaseSettings.class);

 assertNotNull(deserialized);
 assertNotNull(deserialized.getClassIntrospector());
 assertNotNull(deserialized.getAnnotationIntrospector());
 assertNotNull(deserialized.getVisibilityChecker());
 assertNotNull(deserialized.getPropertyNamingStrategy());
 assertNotNull(deserialized.getTypeFactory());
 assertNotNull(deserialized.getTypeResolverBuilder());
 assertNotNull(deserialized.getDateFormat());
 assertNotNull(deserialized.getHandlerInstantiator());
 assertNotNull(deserialized.getLocale());
 assertNotNull(deserialized.getTimeZone());
 assertNotNull(deserialized.getBase64Variant());

 assertEquals(original.getClassIntrospector(), deserialized.getClassIntrospector());
 assertEquals(original.getAnnotationIntrospector(), deserialized.getAnnotationIntrospector());
 assertEquals(original.getVisibilityChecker(), deserialized.getVisibilityChecker());
 assertEquals(original.getPropertyNamingStrategy(), deserialized.getPropertyNamingStrategy());
 assertEquals(original.getTypeFactory(), deserialized.getTypeFactory());
 assertEquals(original.getTypeResolverBuilder(), deserialized.getTypeResolverBuilder());
 assertEquals(original.getDateFormat(), deserialized.getDateFormat());
 assertEquals(original.getHandlerInstantiator(), deserialized.getHandlerInstantiator());
 assertEquals(original.getLocale(), deserialized.getLocale());
 assertEquals(original.getTimeZone(), deserialized.getTimeZone());
 assertEquals(original.getBase64Variant(), deserialized.getBase64Variant());
 }

 @Test(expected = JsonMappingException.class)
 public void testBaseSettingsDeserializationWithNullDateFormat() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "{\"classIntrospector\":{\"runtimeType\":\"java.lang.ClassIntrospector\"},\"annotationIntrospector\":{\"runtimeType\":\"com.fasterxml.jackson.databind.AnnotationIntrospector\"},\"visibilityChecker\":{\"runtimeType\":\"com.fasterxml.jackson.databind.VisibilityChecker\"},\"propertyNamingStrategy\":{\"runtimeType\":\"com.fasterxml.jackson.databind.PropertyNamingStrategy\"},\"typeFactory\":{\"runtimeType\":\"com.fasterxml.jackson.databind.TypeFactory\"},\"typeResolverBuilder\":{\"runtimeType\":\"com.fasterxml.jackson.databind.TypeResolverBuilder\"},\"dateFormat\":null,\"handlerInstantiator\":{\"runtimeType\":\"com.fasterxml.jackson.databind.HandlerInstantiator\"},\"locale\":{\"runtimeType\":\"java.util.Locale\"},\"timeZone\":{\"runtimeType\":\"java.util.TimeZone\"},\"base64Variant\":{\"runtimeType\":\"com.fasterxml.jackson.core.Base64Variant\"}}";
 mapper.readValue(json, BaseSettings.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testBaseSettingsDeserializationWithEmptyDateFormat() throws JsonMappingException {
 ObjectMapper objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 String json = "{\"classIntrospector\":{\"runtimeType\":\"java.lang.ClassIntrospector\"},\"annotationIntrospector\":{\"runtimeType\":\"com.fasterxml.jackson.databind.AnnotationIntrospector\"},\"visibilityChecker\":{\"runtimeType\":\"com.fasterxml.jackson.databind.VisibilityChecker\"},\"propertyNamingStrategy\":{\"runtimeType\":\"com.fasterxml.jackson.databind.PropertyNamingStrategy\"},\"typeFactory\":{\"runtimeType\":\"com.fasterxml.jackson.databind.TypeFactory\"},\"typeResolverBuilder\":{\"runtimeType\":\"com.fasterxml.jackson.databind.TypeResolverBuilder\"},\"dateFormat\":{},\"handlerInstantiator\":{\"runtimeType\":\"com.fasterxml.jackson.databind.HandlerInstantiator\"},\"locale\":{\"runtimeType\":\"java.util.Locale\"},\"timeZone\":{\"runtimeType\":\"java.util.TimeZone\"},\"base64Variant\":{\"runtimeType\":\"com.fasterxml.jackson.core.Base64Variant\"}}";
 mapper.readValue(json, BaseSettings.class);
 }
}

class TypeResolverBuilderDefault extends TypeResolverBuilder {
 @Override
 public JavaType resolveType(TypeReference baseType) {
 throw new UnsupportedOperationException();
 }
}