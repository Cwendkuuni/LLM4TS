package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test class for BaseSettings
 */
public class BaseSettingsTest {

    private BaseSettings _baseSettings;
    private ObjectMapper _objectMapper;

    @Before
    public void setup() {
        _objectMapper = new ObjectMapper();
        _baseSettings = new BaseSettings(_objectMapper.getClassIntrospector(), _objectMapper.getAnnotationIntrospector(),
                _objectMapper.getVisibilityChecker(), _objectMapper.getDefaultPropertyNaming(), _objectMapper.getTypeFactory(),
                _objectMapper.getTypeResolverBuilder(), new StdDateFormat(), _objectMapper.getHandlerInstantiator(),
                Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
    }

    @Test
    public void testBaseSettingsWithClassIntrospector() {
        BaseSettings settings = _baseSettings.withClassIntrospector(_objectMapper.getClassIntrospector());
        assertEquals(_baseSettings.getClassIntrospector(), settings.getClassIntrospector());
    }

    @Test
    public void testBaseSettingsWithAnnotationIntrospector() {
        BaseSettings settings = _baseSettings.withAnnotationIntrospector(_objectMapper.getAnnotationIntrospector());
        assertEquals(_baseSettings.getAnnotationIntrospector(), settings.getAnnotationIntrospector());
    }

    @Test
    public void testBaseSettingsWithVisibilityChecker() throws JsonMappingException {
        BaseSettings settings = _baseSettings.withVisibilityChecker(_objectMapper.getVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY, "privateField"));
        assertNotEquals(_baseSettings.getVisibilityChecker(), settings.getVisibilityChecker());
    }

    @Test
    public void testBaseSettingsWithPropertyNamingStrategy() {
        PropertyNamingStrategy strategy = new PropertyNamingStrategy.SnakeCaseStrategy();
        BaseSettings settings = _baseSettings.withPropertyNamingStrategy(strategy);
        assertEquals(strategy, settings.getPropertyNamingStrategy());
    }

    @Test
    public void testBaseSettingsWithTypeFactory() {
        TypeFactory typeFactory = _objectMapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {});
        BaseSettings settings = _baseSettings.withTypeFactory(_objectMapper.getTypeFactory().overrideActivatedTypes(typeFactory));
        assertEquals(typeFactory, settings.getTypeFactory());
    }

    @Test
    public void testBaseSettingsWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typeResolverBuilder = _objectMapper.getTypeResolverBuilder();
        JavaType javaType = _objectMapper.getTypeFactory().constructType(new TypeReference<Map<String, Object>>() {});
        BaseSettings settings = _baseSettings.withTypeResolverBuilder(typeResolverBuilder.findTypeDeserializer(javaType));
        assertEquals(typeResolverBuilder, settings.getTypeResolverBuilder());
    }

    @Test
    public void testBaseSettingsWithDateFormat() {
        DateFormat dateFormat = new StdDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        BaseSettings settings = _baseSettings.withDateFormat(dateFormat);
        assertEquals(dateFormat, settings.getDateFormat());
    }

    @Test
    public void testBaseSettingsWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = _objectMapper.getHandlerInstantiator();
        BaseSettings settings = _baseSettings.withHandlerInstantiator(handlerInstantiator.withAdditionalHandler(new StringDeserializer()));
        assertEquals(handlerInstantiator, settings.getHandlerInstantiator());
    }

    @Test
    public void testBaseSettingsWithLocale() {
        Locale locale = new Locale("fr_FR");
        BaseSettings settings = _baseSettings.with(locale);
        assertEquals(locale, settings.getLocale());
    }

    @Test
    public void testBaseSettingsWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"));
        BaseSettings settings = _baseSettings.with(timeZone);
        assertEquals(timeZone, settings.getTimeZone());
    }

    @Test
    public void testBaseSettingsWithBase64Variant() {
        BaseSettings settings = _baseSettings.with(Base64Variant.BASE64_URL);
        assertNotEquals(_baseSettings.getBase64Variant(), settings.getBase64Variant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSettingsWithNullTimeZone() {
        _baseSettings.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSettingsWithNullLocale() {
        _baseSettings.with(null);
    }

    @Test
    public void testSerializationWithCustomDateFormat() throws Exception {
        _objectMapper.setDateFormat(new StdDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z"));
        ObjectMapper mapper = new ObjectMapper(_baseSettings);
        String json = mapper.writeValueAsString(new MyDate("2023-03-20 12:00:00.123Z"));
        assertEquals("\"2023-03-20T12:00:00.123Z\"", json);
    }

    static class MyDate {
        private final Date _date;

        public MyDate(Date date) {
            _date = date;
        }

        public Date getDate() {
            return _date;
        }

        @Override
        public String toString() {
            try {
                return new StdDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").format(_date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}