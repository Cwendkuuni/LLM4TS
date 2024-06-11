import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.databind.cfg.BaseSettings;

public class BaseSettingsTest {

 private BaseSettings baseSettings;

 @Test
 public void testBaseSettingsConstruction() {
 baseSettings = new BaseSettings(
 new ClassIntrospector(),
 new AnnotationIntrospector(),
 new VisibilityChecker<>(),
 new PropertyNamingStrategy(),
 new TypeFactory(),
 new TypeResolverBuilder<>(),
 new StdDateFormat(),
 new HandlerInstantiator(),
 Locale.getDefault(),
 TimeZone.getDefault(),
 Base64Variants.STANDARD
 );
 assertNotNull(baseSettings);
 }

 @Test
 public void testWithClassIntrospector() {
 baseSettings = new BaseSettings(baseSettings.getClassIntrospector());
 assertNotNull(baseSettings.getClassIntrospector());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullClassIntrospector() {
 baseSettings = new BaseSettings(null);
 }

 @Test
 public void testWithAnnotationIntrospector() {
 baseSettings = new BaseSettings(baseSettings.getAnnotationIntrospector());
 assertNotNull(baseSettings.getAnnotationIntrospector());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullAnnotationIntrospector() {
 baseSettings = new BaseSettings(null);
 }

 @Test
 public void testWithVisibilityChecker() {
 baseSettings = new BaseSettings(baseSettings.getVisibilityChecker(), PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
 assertNotNull(baseSettings.getVisibilityChecker());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullVisibilityChecker() {
 baseSettings = new BaseSettings(null, PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
 }

 @Test
 public void testWithPropertyNamingStrategy() {
 baseSettings = new BaseSettings(baseSettings.getPropertyNamingStrategy(), PropertyNamingStrategy.PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
 assertNotNull(baseSettings.getPropertyNamingStrategy());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullPropertyNamingStrategy() {
 baseSettings = new BaseSettings(null, PropertyNamingStrategy.PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
 }

 @Test
 public void testWithTypeFactory() {
 baseSettings = new BaseSettings(baseSettings.getTypeFactory());
 assertNotNull(baseSettings.getTypeFactory());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTypeFactory() {
 baseSettings = new BaseSettings(null);
 }

 @Test
 public void testWithTypeResolverBuilder() {
 baseSettings = new BaseSettings(baseSettings.getTypeResolverBuilder());
 assertNotNull(baseSettings.getTypeResolverBuilder());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTypeResolverBuilder() {
 baseSettings = new BaseSettings(null);
 }

 @Test
 public void testWithDateFormat() {
 baseSettings = new BaseSettings(new StdDateFormat());
 assertNotNull(baseSettings.getDateFormat());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullDateFormat() {
 baseSettings = new BaseSettings(null);
 }

 @Test
 public void testWithHandlerInstantiator() {
 baseSettings = new BaseSettings(new HandlerInstantiator());
 assertNotNull(baseSettings.getHandlerInstantiator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullHandlerInstantiator() {
 baseSettings = new BaseSettings(null);
 }

 @Test
 public void testWithLocale() {
 baseSettings = new BaseSettings(baseSettings.getLocale(), TimeZone.getDefault());
 assertNotNull(baseSettings.getLocale());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullLocale() {
 baseSettings = new BaseSettings(null, TimeZone.getDefault());
 }

 @Test
 public void testWithTimeZone() {
 baseSettings = new BaseSettings(baseSettings.getLocale(), new TimeZone("GMT+1"));
 assertNotNull(baseSettings.getTimeZone());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTimeZone() {
 baseSettings = new BaseSettings(baseSettings.getLocale(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullPropertyAccessor() {
 baseSettings = new BaseSettings(null, null);
 }

 @Test
 public void testWithJsonAutoDetectVisibility() {
 baseSettings = new BaseSettings(baseSettings.getVisibilityChecker(), PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
 assertNotNull(baseSettings.getVisibilityChecker());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithInvalidJsonAutoDetectVisibility() {
 baseSettings = new BaseSettings(baseSettings.getVisibilityChecker(), PropertyAccessor.FIELD, JsonAutoDetect.Visibility.INVALID);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullVisibility() {
 baseSettings = new BaseSettings(null, PropertyAccessor.FIELD, null);
 }
}