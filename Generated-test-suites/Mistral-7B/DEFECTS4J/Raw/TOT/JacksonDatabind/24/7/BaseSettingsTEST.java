import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseSettingsTest {

 private BaseSettings baseSettings;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 ClassIntrospector ci = mapper.getConfiguration().getClassIntrospector();
 AnnotationIntrospector ai = mapper.getConfiguration().getDefaultIntrospector();
 VisibilityChecker<?> vc = mapper.getConfiguration().getDefaultVisibilityChecker();
 PropertyNamingStrategy pns = PropertyNamingStrategy.STANDARD_ENGLISH_LOW_CAMEL_CASE;
 TypeFactory tf = mapper.getTypeFactory();
 TypeResolverBuilder<?> typer = mapper.getTypeResolverBuilder();
 DateFormat df = new StdDateFormat();
 HandlerInstantiator hi = mapper.getDefaultInstanceCreator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, timeZone, null);
 }

 @Test
 public void testWithClassIntrospector() {
 ClassIntrospector ci1 = mapper.getConfiguration().getClassIntrospector();
 BaseSettings settings1 = baseSettings.withClassIntrospector(ci1);
 assertEquals(ci1, settings1.getClassIntrospector());
 }

 @Test
 public void testWithAnnotationIntrospector() {
 AnnotationIntrospector ai1 = new CustomAnnotationIntrospector();
 BaseSettings settings2 = baseSettings.withAnnotationIntrospector(ai1);
 assertEquals(ai1, settings2.getAnnotationIntrospector());
 }

 @Test
 public void testWithVisibilityChecker() {
 VisibilityChecker<?> vc1 = new CustomVisibilityChecker();
 BaseSettings settings3 = baseSettings.withVisibilityChecker(vc1);
 assertEquals(vc1, settings3.getVisibilityChecker());
 }

 @Test
 public void testWithPropertyNamingStrategy() {
 PropertyNamingStrategy pns1 = PropertyNamingStrategy.CAMEL_CASE_TO_UNDERSCORES_WITH_DASHES;
 BaseSettings settings4 = baseSettings.withPropertyNamingStrategy(pns1);
 assertEquals(pns1, settings4.getPropertyNamingStrategy());
 }

 @Test
 public void testWithTypeFactory() {
 TypeFactory tf1 = new CustomTypeFactory();
 BaseSettings settings5 = baseSettings.withTypeFactory(tf1);
 assertEquals(tf1, settings5.getTypeFactory());
 }

 @Test
 public void testWithTypeResolverBuilder() {
 TypeResolverBuilder<?> typer1 = new CustomTypeResolverBuilder();
 BaseSettings settings6 = baseSettings.withTypeResolverBuilder(typer1);
 assertEquals(typer1, settings6.getTypeResolverBuilder());
 }

 @Test
 public void testWithDateFormat() {
 DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
 BaseSettings settings7 = baseSettings.withDateFormat(df1);
 assertNotNull(settings7.getDateFormat());
 }

 @Test
 public void testWithHandlerInstantiator() {
 HandlerInstantiator hi1 = new CustomHandlerInstantiator();
 BaseSettings settings8 = baseSettings.withHandlerInstantiator(hi1);
 assertEquals(hi1, settings8.getHandlerInstantiator());
 }

 @Test
 public void testWithLocale() {
 Locale locale1 = Locale.CHINA;
 BaseSettings settings9 = baseSettings.with(locale1);
 assertEquals(locale1, settings9.getLocale());
 }

 @Test
 public void testWithTimeZone() {
 TimeZone timeZone1 = TimeZone.getTimeZone("Asia/Tokyo");
 BaseSettings settings10 = baseSettings.with(timeZone1);
 assertEquals(timeZone1, settings10.getTimeZone());
 }

 @Test
 public void testWithBase64Variant() {
 Base64Variant base64Variant = Base64Variants.BASE64_STANDARD;
 BaseSettings settings11 = baseSettings.with(base64Variant);
 assertEquals(base64Variant, settings11.getBase64Variant());
 }

 @Test
 public void testSerialization() throws JsonProcessingException {
 ObjectWriter writer = mapper.writerWithRootValue(baseSettings);
 String json = writer.writeValueAsString(baseSettings);
 assertNotNull(json);
 }

 @Test
 public void testDeserialization() throws JsonProcessingException {
 String json = "{\"classIntrospector\":{\"implClass\":\"com.fasterxml.jackson.databind.introspect.ClassIntrospectorImpl\"},\"annotationIntrospector\":" +
 "{\"implClass\":\"com.fasterxml.jackson.databind.jsontype.ClassIdResolverBase\"},\"visibilityChecker\":{" +
 "\"implClass\":\"com.fasterxml.jackson.databind.cfg.DefaultVisibilityCheckerForSerialization\"" +
 "},\"propertyNamingStrategy\":{" +
 "\"implClass\":\"com.fasterxml.jackson.databind.cfg.PropertyNamingStrategy$StandardEnglish$LcIdentifierStrategy\"" +
 "},\"typeFactory\":{\"implClass\":\"com.fasterxml.jackson.core.type.TypeFactory\"" +
 "},\"typeResolverBuilder\":{" +
 "\"implClass\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeResolverBuilderBase\"" +
 "},\"dateFormat\":{},\"handlerInstantiator\":{" +
 "\"implClass\":\"com.fasterxml.jackson.databind.jsontype.impl.JacksonAnnotationIntrospector\"" +
 "},\"locale\":{\"country\":\"US\",\"language\":\"en\",\"variant\":null},\"timeZone\":{" +
 "\"id\":\"America/Los_Angeles\",\"offset\":-21600000,\"dstSavings\":3600000\"" +
 "},\"defaultBase64\":null}";
 BaseSettings settings12 = mapper.readerForType(new TypeReference<BaseSettings>() {}.getType()).readValue(json);
 assertEquals(baseSettings, settings12);
 }
}