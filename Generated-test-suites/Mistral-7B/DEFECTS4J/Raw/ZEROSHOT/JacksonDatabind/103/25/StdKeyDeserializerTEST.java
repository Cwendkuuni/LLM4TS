import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.TokenBuffer;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.EnumResolver;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumUtil;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.TokenBufferBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testStringKeyDeserializer() throws Exception {
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        String key = "test";
        Object result = stringKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(key, result);
    }

    @Test
    public void testObjectKeyDeserializer() throws Exception {
        StdKeyDeserializer objectKD = StdKeyDeserializer.StringKD.forType(Object.class);
        String key = "test";
        Object result = objectKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(key, result);
    }

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        String keyTrue = "true";
        Object resultTrue = booleanKD.deserializeKey(keyTrue, objectMapper.getDeserializationContext());
        Assert.assertEquals(Boolean.TRUE, resultTrue);

        String keyFalse = "false";
        Object resultFalse = booleanKD.deserializeKey(keyFalse, objectMapper.getDeserializationContext());
        Assert.assertEquals(Boolean.FALSE, resultFalse);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
        String key = "123";
        Object result = byteKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals((byte) 123, result);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
        String key = "123";
        Object result = shortKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals((short) 123, result);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
        String key = "A";
        Object result = charKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals('A', result);
    }

    @Test
    public void testIntegerKeyDeserializer() throws Exception {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        String key = "123";
        Object result = intKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(123, result);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        String key = "12345678901234567890";
        Object result = longKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(Long.parseLong(key), result);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
        String key = "3.14159265";
        Object result = floatKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(Float.parseFloat(key), result);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        String key = "3.14159265358979323846264338327950288416";
        Object result = doubleKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(Double.parseDouble(key), result);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        String key = "en_US";
        Object result = localeKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
        String key = "2022-01-01";
        Object result = dateKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(new Date(0), result);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parseDate = dateFormat.parse(key);
        Assert.assertEquals(parseDate, result);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);
        String key = "2022-01-01";
        Object result = calendarKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof GregorianCalendar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parseDate = dateFormat.parse(key);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(2022, Calendar.JANUARY, 1);
        gregorianCalendar.setTime(parseDate);
        Assert.assertEquals(gregorianCalendar, result);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);
        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object result = uuidKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(UUID.fromString(key), result);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        String key = "https://www.example.com";
        Object result = uriKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(new URI(key), result);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        String key = "https://www.example.com";
        Object result = urlKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertEquals(new URL(key), result);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(String[].class);
        String key = "[Ljava.lang.String;";
        Object result = classKD.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertArrayEquals(new String[]{"test"}, (String[]) result);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        Enum<?> enumValue = MyEnum.VALUE_1;
        AnnotatedClass<MyEnum> annotatedClass = objectMapper.getAnnotationIntrospector().findAnnotation(MyEnum.class);
        BeanDescription beanDescription = objectMapper.findBeanDescription(annotatedClass.getRawClass());
        AnnotatedMethod annotatedMethod = beanDescription.findMethod("valueOf", String.class);
        Constructor<MyEnum> constructor = annotatedClass.getRawClass().getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        StdKeyDeserializer enumKD = new StdKeyDeserializer(StdKeyDeserializer.Kind.ENUM, MyEnum.class);
        enumKD._byNameResolver = new EnumResolver(MyEnum.class);
        enumKD._factory = annotatedMethod;
        Object result = enumKD.deserializeKey("VALUE_1", objectMapper.getDeserializationContext());
        Assert.assertEquals(enumValue, result);

        Assert.assertThrows(IllegalArgumentException.class, () -> enumKD.deserializeKey("INVALID_VALUE", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testFromStringDeserializerKeyDeserializer() throws Exception {
        StdKeyDeserializer fromStringDeserializerKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.Kind.FROM_STRING_DESERIALIZER, FromStringDeserializer.class);
        String key = "en_US";
        Object result = fromStringDeserializerKeyDeserializer.deserializeKey(key, objectMapper.getDeserializationContext());
        Assert.assertTrue(result instanceof Locale);
        Assert.assertEquals(Locale.forLanguageTag(key), result);

        key = "EUR";
        Assert.assertThrows(IllegalArgumentException.class, () -> fromStringDeserializerKeyDeserializer.deserializeKey(key, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testByteArrayKeyDeserializer() throws Exception {
        StdKeyDeserializer byteArrayKD = StdKeyDeserializer.forType(byte[].class);
        String key = "Rm9udCAoKSk=";
        Object result = byteArrayKD.deserializeKey(key, objectMapper.getDeserializationContext());
        byte[] expected = Base64Variants.base64Standard.decode(key);
        Assert.assertArrayEquals(expected, result);
    }
}