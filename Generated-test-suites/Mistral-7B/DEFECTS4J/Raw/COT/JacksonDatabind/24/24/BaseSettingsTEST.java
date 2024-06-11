import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.cfg.BaseSettings;

public class BaseSettingsTest {

 private BaseSettings settings;

 @Test
 public void testConstructor() {
 settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
 assertNotNull(settings);
 }

 @Test
 public void testWithClassIntrospector() {
 ClassIntrospector ci = Mockito.mock(ClassIntrospector.class);
 settings = settings.withClassIntrospector(ci);
 Assert.assertEquals(ci, settings._classIntrospector);
 }

 @Test
 public void testWithAnnotationIntrospector() {
 AnnotationIntrospector ai = Mockito.mock(AnnotationIntrospector.class);
 settings = settings.withAnnotationIntrospector(ai);
 Assert.assertEquals(ai, settings._annotationIntrospector);
 }

 @Test
 public void testWithVisibilityChecker() {
 VisibilityChecker<?> vc = Mockito.mock(VisibilityChecker.class);
 settings = settings.withVisibilityChecker(vc);
 Assert.assertEquals(vc, settings._visibilityChecker);
 }

 @Test
 public void testWithPropertyNamingStrategy() {
 PropertyNamingStrategy pns = Mockito.mock(PropertyNamingStrategy.class);
 settings = settings.withPropertyNamingStrategy(pns);
 Assert.assertEquals(pns, settings._propertyNamingStrategy);
 }

 @Test
 public void testWithTypeFactory() {
 TypeFactory tf = Mockito.mock(TypeFactory.class);
 settings = settings.withTypeFactory(tf);
 Assert.assertEquals(tf, settings._typeFactory);
 }

 @Test
 public void testWithTypeResolverBuilder() {
 TypeResolverBuilder<?> typer = Mockito.mock(TypeResolverBuilder.class);
 settings = settings.withTypeResolverBuilder(typer);
 Assert.assertEquals(typer, settings._typeResolverBuilder);
 }

 @Test
 public void testWithDateFormat() {
 DateFormat df = Mockito.mock(DateFormat.class);
 settings = settings.withDateFormat(df);
 Assert.assertEquals(df, settings._dateFormat);
 }

 @Test
 public void testWithHandlerInstantiator() {
 HandlerInstantiator hi = Mockito.mock(HandlerInstantiator.class);
 settings = settings.withHandlerInstantiator(hi);
 Assert.assertEquals(hi, settings._handlerInstantiator);
 }

 @Test
 public void testWithLocale() {
 Locale locale = Locale.US;
 settings = settings.with(locale);
 Assert.assertEquals(locale, settings._locale);
 }

 @Test
 public void testWithTimeZone() {
 TimeZone tz = TimeZone.getTimeZone("UTC");
 settings = settings.with(tz);
 Assert.assertEquals(tz, settings._timeZone);
 }

 @Test
 public void testWithBase64Variant() {
 Base64Variant base64 = Base64Variant.BASE64_STANDARD;
 settings = settings.with(base64);
 Assert.assertEquals(base64, settings._defaultBase64);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTimeZone() {
 settings = settings.with(null);
 fail("Should have thrown IllegalArgumentException");
 }
}