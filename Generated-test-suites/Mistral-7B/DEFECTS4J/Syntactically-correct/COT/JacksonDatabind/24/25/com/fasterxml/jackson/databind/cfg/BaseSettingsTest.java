import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseSettingsTest {

 private BaseSettings settings;

 @Test
 public void testConstructor() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Std();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.getDefault();
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = null;

 settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

 Assert.assertNotNull(settings.getClassIntrospector());
 Assert.assertNotNull(settings.getAnnotationIntrospector());
 Assert.assertNotNull(settings.getVisibilityChecker());
 Assert.assertNotNull(settings.getPropertyNamingStrategy());
 Assert.assertNotNull(settings.getTypeFactory());
 Assert.assertNotNull(settings.getTypeResolverBuilder());
 Assert.assertNotNull(settings.getDateFormat());
 Assert.assertNotNull(settings.getHandlerInstantiator());
 Assert.assertNotNull(settings.getLocale());
 Assert.assertNotNull(settings.getTimeZone());
 Assert.assertNull(settings.getBase64Variant());
 }

 @Test
 public void testWithClassIntrospector() {
 ClassIntrospector oldClassIntrospector = settings.getClassIntrospector();
 ClassIntrospector newClassIntrospector = new ClassIntrospector();

 settings = settings.withClassIntrospector(newClassIntrospector);

 Assert.assertNotSame(oldClassIntrospector, settings.getClassIntrospector());
 Assert.assertEquals(newClassIntrospector, settings.getClassIntrospector());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullAnnotationIntrospector() {
 // This test case should throw an IllegalArgumentException
 settings.withAnnotationIntrospector(null);
 }

 @Test
 public void testWithAnnotationIntrospector() {
 AnnotationIntrospector oldAnnotationIntrospector = settings.getAnnotationIntrospector();
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();

 settings = settings.withAnnotationIntrospector(newAnnotationIntrospector);

 Assert.assertNotSame(oldAnnotationIntrospector, settings.getAnnotationIntrospector());
 Assert.assertEquals(newAnnotationIntrospector, settings.getAnnotationIntrospector());
 }

 @Test
 public void testWithInsertedAnnotationIntrospector() {
 AnnotationIntrospector oldAnnotationIntrospector = settings.getAnnotationIntrospector();
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();

 settings = settings.withInsertedAnnotationIntrospector(newAnnotationIntrospector);

 Assert.assertNotSame(oldAnnotationIntrospector, settings.getAnnotationIntrospector());
 Assert.assertEquals(newAnnotationIntrospector, settings.getAnnotationIntrospector());
 Assert.assertEquals(oldAnnotationIntrospector, settings.getAnnotationIntrospector().getAnnotationIntrospector());
 }

 @Test
 public void testWithAppendedAnnotationIntrospector() {
 AnnotationIntrospector oldAnnotationIntrospector = settings.getAnnotationIntrospector();
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();

 settings = settings.withAppendedAnnotationIntrospector(newAnnotationIntrospector);

 Assert.assertNotSame(oldAnnotationIntrospector, settings.getAnnotationIntrospector());
 Assert.assertEquals(newAnnotationIntrospector, settings.getAnnotationIntrospector().getAnnotationIntrospector());
 Assert.assertEquals(oldAnnotationIntrospector, settings.getAnnotationIntrospector());
 }

 @Test
 public void testWithVisibilityChecker() {
 VisibilityChecker<?> oldVisibilityChecker = settings.getVisibilityChecker();
 VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker.Std();

 settings = settings.withVisibilityChecker(newVisibilityChecker);

 Assert.assertNotSame(oldVisibilityChecker, settings.getVisibilityChecker());
 Assert.assertEquals(newVisibilityChecker, settings.getVisibilityChecker());
 }

 @Test
 public void testWithVisibility() {
 PropertyAccessor forMethod = new PropertyAccessorImpl();
 JsonAutoDetect.Visibility oldVisibility = settings.getVisibilityChecker().withVisibility(forMethod, JsonAutoDetect.Visibility.ANY);
 JsonAutoDetect.Visibility newVisibility = JsonAutoDetect.Visibility.NONE;

 settings = settings.withVisibility(forMethod, newVisibility);

 Assert.assertNotSame(oldVisibility, settings.getVisibilityChecker().withVisibility(forMethod, JsonAutoDetect.Visibility.ANY));
 Assert.assertEquals(newVisibility, settings.getVisibilityChecker().withVisibility(forMethod, JsonAutoDetect.Visibility.ANY));
 }

 @Test
 public void testWithPropertyNamingStrategy() {
 PropertyNamingStrategy oldPropertyNamingStrategy = settings.getPropertyNamingStrategy();
 PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy.CamelCaseStrategy();

 settings = settings.withPropertyNamingStrategy(newPropertyNamingStrategy);

 Assert.assertNotSame(oldPropertyNamingStrategy, settings.getPropertyNamingStrategy());
 Assert.assertEquals(newPropertyNamingStrategy, settings.getPropertyNamingStrategy());
 }

 @Test
 public void testWithTypeFactory() {
 TypeFactory oldTypeFactory = settings.getTypeFactory();
 TypeFactory newTypeFactory = new TypeFactory();

 settings = settings.withTypeFactory(newTypeFactory);

 Assert.assertNotSame(oldTypeFactory, settings.getTypeFactory());
 Assert.assertEquals(newTypeFactory, settings.getTypeFactory());
 }

 @Test
 public void testWithTypeResolverBuilder() {
 TypeResolverBuilder<?> oldTypeResolverBuilder = settings.getTypeResolverBuilder();
 TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilderDefault();

 settings = settings.withTypeResolverBuilder(newTypeResolverBuilder);

 Assert.assertNotSame(oldTypeResolverBuilder, settings.getTypeResolverBuilder());
 Assert.assertEquals(newTypeResolverBuilder, settings.getTypeResolverBuilder());
 }

 @Test
 public void testWithDateFormat() {
 DateFormat oldDateFormat = settings.getDateFormat();
 DateFormat newDateFormat = new StdDateFormat();

 settings = settings.withDateFormat(newDateFormat);

 Assert.assertNotSame(oldDateFormat, settings.getDateFormat());
 Assert.assertEquals(newDateFormat, settings.getDateFormat());
 }

 @Test
 public void testWithHandlerInstantiator() {
 HandlerInstantiator oldHandlerInstantiator = settings.getHandlerInstantiator();
 HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();

 settings = settings.withHandlerInstantiator(newHandlerInstantiator);

 Assert.assertNotSame(oldHandlerInstantiator, settings.getHandlerInstantiator());
 Assert.assertEquals(newHandlerInstantiator, settings.getHandlerInstantiator());
 }

 @Test
 public void testWith() {
 Locale oldLocale = settings.getLocale();
 Locale newLocale = new Locale("fr", "FR");

 settings = settings.with(newLocale);

 Assert.assertNotSame(oldLocale, settings.getLocale());
 Assert.assertEquals(newLocale, settings.getLocale());
 }

 @Test
 public void testWithTimeZone() {
 TimeZone oldTimeZone = settings.getTimeZone();
 TimeZone newTimeZone = new TimeZone.Builder().setID("Europe/Paris").build();

 settings = settings.with(newTimeZone);

 Assert.assertNotSame(oldTimeZone, settings.getTimeZone());
 Assert.assertEquals(newTimeZone, settings.getTimeZone());
 }

 @Test
 public void testWithBase64Variant() {
 Base64Variant oldBase64Variant = settings.getBase64Variant();
 Base64Variant newBase64Variant = Base64Variant.STANDARD;

 settings = settings.with(newBase64Variant);

 Assert.assertNotSame(oldBase64Variant, settings.getBase64Variant());
 Assert.assertEquals(newBase64Variant, settings.getBase64Variant());
 }
}