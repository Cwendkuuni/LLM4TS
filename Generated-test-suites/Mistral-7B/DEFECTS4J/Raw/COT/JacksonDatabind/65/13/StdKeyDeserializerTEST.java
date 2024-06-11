import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeKey_StringAsString_ReturnsInput() throws Exception {
        StdKeyDeserializer stringKD = StdKeyDeserializer.forType(String.class);
        KeyDeserializer deserializer = objectMapper.getConfig().getKeyDeserializer();
        assertSame(stringKD, deserializer);

        String input = "test";
        String key = stringKD.deserializeKey(input, objectMapper.getDeserializationContext());
        assertEquals(input, key);
    }

    @Test
    public void testDeserializeKey_Boolean_ValidValues() throws Exception {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);

        String trueInput = "true";
        assertEquals(Boolean.TRUE, booleanKD.deserializeKey(trueInput, objectMapper.getDeserializationContext()));

        String falseInput = "false";
        assertEquals(Boolean.FALSE, booleanKD.deserializeKey(falseInput, objectMapper.getDeserializationContext()));

        String invalidBooleanInput = "invalid";
        assertNull(booleanKD.deserializeKey(invalidBooleanInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Byte_ValidValues() throws Exception {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);

        String validByteInput1 = "10";
        assertEquals((byte) 10, byteKD.deserializeKey(validByteInput1, objectMapper.getDeserializationContext()).byteValue());

        String validByteInput2 = "50";
        assertEquals((byte) 50, byteKD.deserializeKey(validByteInput2, objectMapper.getDeserializationContext()).byteValue());

        String invalidByteInput = "123";
        assertNull(byteKD.deserializeKey(invalidByteInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Short_ValidValues() throws Exception {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);

        String validShortInput1 = "20";
        assertEquals((short) 20, shortKD.deserializeKey(validShortInput1, objectMapper.getDeserializationContext()).shortValue());

        String validShortInput2 = "30";
        assertEquals((short) 30, shortKD.deserializeKey(validShortInput2, objectMapper.getDeserializationContext()).shortValue());

        String invalidShortInput = "456";
        assertNull(shortKD.deserializeKey(invalidShortInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Char_SingleCharacter() throws Exception {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);

        String validCharInput = "A";
        assertEquals(Character.valueOf('A'), charKD.deserializeKey(validCharInput, objectMapper.getDeserializationContext()));

        String invalidCharInput = "AB";
        assertNull(charKD.deserializeKey(invalidCharInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Int_ValidValues() throws Exception {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);

        String validIntInput1 = "100";
        assertEquals(100, intKD.deserializeKey(validIntInput1, objectMapper.getDeserializationContext()));

        String validIntInput2 = "200";
        assertEquals(200, intKD.deserializeKey(validIntInput2, objectMapper.getDeserializationContext()));

        String invalidIntInput = "300";
        assertNull(intKD.deserializeKey(invalidIntInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Long_ValidValues() throws Exception {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);

        String validLongInput1 = "1000";
        assertEquals(1000L, longKD.deserializeKey(validLongInput1, objectMapper.getDeserializationContext()));

        String validLongInput2 = "2000";
        assertEquals(2000L, longKD.deserializeKey(validLongInput2, objectMapper.getDeserializationContext()));

        String invalidLongInput = "3000";
        assertNull(longKD.deserializeKey(invalidLongInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Float_ValidValues() throws Exception {
        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);

        String validFloatInput1 = "1.5";
        assertEquals((float) 1.5F, floatKD.deserializeKey(validFloatInput1, objectMapper.getDeserializationContext()));

        String validFloatInput2 = "2.5";
        assertEquals((float) 2.5F, floatKD.deserializeKey(validFloatInput2, objectMapper.getDeserializationContext()));

        String invalidFloatInput = "3.5";
        assertNull(floatKD.deserializeKey(invalidFloatInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Double_ValidValues() throws Exception {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);

        String validDoubleInput1 = "1.5";
        assertEquals(1.5D, doubleKD.deserializeKey(validDoubleInput1, objectMapper.getDeserializationContext()));

        String validDoubleInput2 = "2.5";
        assertEquals(2.5D, doubleKD.deserializeKey(validDoubleInput2, objectMapper.getDeserializationContext()));

        String invalidDoubleInput = "3.5";
        assertNull(doubleKD.deserializeKey(invalidDoubleInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Locale_ValidValues() throws Exception {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);

        String validLocaleInput1 = "en_US";
        assertEquals(Locale.US, localeKD.deserializeKey(validLocaleInput1, objectMapper.getDeserializationContext()));

        String validLocaleInput2 = "fr_FR";
        assertEquals(Locale.FRANCE, localeKD.deserializeKey(validLocaleInput2, objectMapper.getDeserializationContext()));

        String invalidLocaleInput = "invalid";
        assertNull(localeKD.deserializeKey(invalidLocaleInput, objectMapper.getDeserializationContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_Date_InvalidDateFormat() throws Exception {
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);

        String invalidDateInput = "InvalidDateFormat";
        dateKD.deserializeKey(invalidDateInput, objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeKey_Calendar_ValidValues() throws Exception {
        StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);

        String validCalendarInput1 = "2022-01-01";
        assertEquals(Calendar.getInstance().setTime(objectMapper.readValue(new ByteArrayInputStream(validCalendarInput1.getBytes()), new SimpleDateFormat("yyyy-MM-dd"))),
                calendarKD.deserializeKey(validCalendarInput1, objectMapper.getDeserializationContext()));

        String validCalendarInput2 = "2022-01-31";
        assertEquals(Calendar.getInstance().setTime(objectMapper.readValue(new ByteArrayInputStream(validCalendarInput2.getBytes()), new SimpleDateFormat("yyyy-MM-dd"))),
                calendarKD.deserializeKey(validCalendarInput2, objectMapper.getDeserializationContext()));

        String invalidCalendarInput = "InvalidCalendarFormat";
        assertNull(calendarKD.deserializeKey(invalidCalendarInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_UUID_ValidValues() throws Exception {
        StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);

        String validUuidInput1 = "00000000-0000-0000-0000-000000000001";
        assertEquals(UUID.fromString(validUuidInput1), uuidKD.deserializeKey(validUuidInput1, objectMapper.getDeserializationContext()));

        String validUuidInput2 = "00000000-0000-0000-0000-000000000002";
        assertEquals(UUID.fromString(validUuidInput2), uuidKD.deserializeKey(validUuidInput2, objectMapper.getDeserializationContext()));

        String invalidUuidInput = "InvalidUuidFormat";
        assertNull(uuidKD.deserializeKey(invalidUuidInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_URI_ValidValues() throws Exception {
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);

        String validUriInput = "http://example.com";
        assertEquals(new URI(validUriInput), uriKD.deserializeKey(validUriInput, objectMapper.getDeserializationContext()));

        String invalidUriInput = "InvalidUriFormat";
        assertNull(uriKD.deserializeKey(invalidUriInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_URL_ValidValues() throws Exception {
        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);

        String validUrlInput = "http://example.com";
        assertEquals(new URL(validUrlInput), urlKD.deserializeKey(validUrlInput, objectMapper.getDeserializationContext()));

        String invalidUrlInput = "InvalidUrlFormat";
        assertNull(urlKD.deserializeKey(invalidUrlInput, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Class_ValidValues() throws Exception {
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(String[].class);

        String validClassInput1 = "java.lang.String[]";
        assertEquals(new String[0].getClass(), classKD.deserializeKey(validClassInput1, objectMapper.getDeserializationContext()));

        String validClassInput2 = "java.util.ArrayList";
        AnnotatedClass annotatedClass = objectMapper.getAnnotationIntrospector().findClassAnnotations(new ClassName("java.util.ArrayList"));
        AnnotatedField annotatedField = annotatedClass.getAnnotation(JsonTypeName.class);
        TypeReference typeReference = new TypeReference<List<String>>() {};
        assertEquals(annotatedField.getValue().toString(), classKD.deserializeKey(validClassInput2, objectMapper.getDeserializationContext()).getType().getCanonicalName());

        String invalidClassInput = "InvalidClassFormat";
        assertNull(classKD.deserializeKey(invalidClassInput, objectMapper.getDeserializationContext()));
    }
}