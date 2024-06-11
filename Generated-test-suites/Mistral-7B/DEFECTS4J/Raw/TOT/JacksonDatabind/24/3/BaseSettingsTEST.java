import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class BaseSettingsTest {

 @Test
 public void testInstanceCreation() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Default();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new SimpleTypeResolverBuilder();
 DateFormat dateFormat = StdDateFormat.getDefaultInstance();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.getDefault();
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy,
 typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

 assertNotNull(settings);
 }

 @Test
 public void testGetters() {
 BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
 new VisibilityChecker.Default(), new PropertyNamingStrategy.Default(), new TypeFactory(),
 new SimpleTypeResolverBuilder(), StdDateFormat.getDefaultInstance(), new HandlerInstantiator(),
 Locale.getDefault(), TimeZone.getDefault(), Base64Variants.STANDARD);

 ClassIntrospector classIntrospector = settings.getClassIntrospector();
 AnnotationIntrospector annotationIntrospector = settings.getAnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = settings.getVisibilityChecker();
 PropertyNamingStrategy propertyNamingStrategy = settings.getPropertyNamingStrategy();
 TypeFactory typeFactory = settings.getTypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = settings.getTypeResolverBuilder();
 DateFormat dateFormat = settings.getDateFormat();
 HandlerInstantiator handlerInstantiator = settings.getHandlerInstantiator();
 Locale locale = settings.getLocale();
 TimeZone timeZone = settings.getTimeZone();
 Base64Variant base64Variant = settings.getBase64Variant();

 assertTrue(classIntrospector instanceof ClassIntrospector);
 assertTrue(annotationIntrospector instanceof AnnotationIntrospector);
 assertTrue(visibilityChecker instanceof VisibilityChecker.Default);
 assertTrue(propertyNamingStrategy instanceof PropertyNamingStrategy.Default);
 assertTrue(typeFactory instanceof TypeFactory);
 assertTrue(typeResolverBuilder instanceof SimpleTypeResolverBuilder);
 assertTrue(dateFormat instanceof StdDateFormat);
 assertTrue(handlerInstantiator instanceof HandlerInstantiator);
 assertTrue(locale.equals(Locale.getDefault()));
 assertTrue(timeZone.equals(TimeZone.getDefault()));
 assertTrue(base64Variant.equals(Base64Variants.STANDARD));
 }

 @Test
 public void testSerializationAndDeserialization() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForId("com.fasterxml.jackson.databind.cfg.BaseSettings");

 BaseSettings settings1 = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
 new VisibilityChecker.Default(), new PropertyNamingStrategy.Default(), new TypeFactory(),
 new SimpleTypeResolverBuilder(), StdDateFormat.getDefaultInstance(), new HandlerInstantiator(),
 Locale.getDefault(), TimeZone.getDefault(), Base64Variants.STANDARD);
 String json = mapper.writerWithTypeValue(new TypeReference<BaseSettings>() {}.getType())
 .writeValueAsString(settings1);
 BaseSettings settings2 = typeDeserializer.deserialize(json, BaseSettings.class);

 assertTrue(settings1.getClassIntrospector() == settings2.getClassIntrospector());
 assertTrue(settings1.getAnnotationIntrospector() == settings2.getAnnotationIntrospector());
 assertTrue(settings1.getVisibilityChecker() == settings2.getVisibilityChecker());
 assertTrue(settings1.getPropertyNamingStrategy() == settings2.getPropertyNamingStrategy());
 assertTrue(settings1.getTypeFactory() == settings2.getTypeFactory());
 assertTrue(settings1.getTypeResolverBuilder() == settings2.getTypeResolverBuilder());
 assertTrue(settings1.getDateFormat() == settings2.getDateFormat());
 assertTrue(settings1.getHandlerInstantiator() == settings2.getHandlerInstantiator());
 assertTrue(settings1.getLocale() == settings2.getLocale());
 assertTrue(settings1.getTimeZone() == settings2.getTimeZone());
 assertTrue(settings1.getBase64Variant() == settings2.getBase64Variant());
 }

}