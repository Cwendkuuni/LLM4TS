import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class DatabindContextTest {

    private DatabindContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MapperConfig<ObjectMapper> config = new MapperFactory().configure(MapperConfig.StandardConfig.builder()
                .build());
        this.context = new BaseDatabindContext(config);
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Test method for {@link DatabindContext#getConfig()}.
     */
    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    /**
     * Test method for {@link DatabindContext#getAnnotationIntrospector()}.
     */
    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    /**
     * Test method for {@link DatabindContext#isEnabled(MapperFeature)}.
     */
    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_USE_ANNOTATIONS));
        assertFalse(context.isEnabled(MapperFeature.DEFAULT_USE_STATIC_TYPES));
    }

    /**
     * Test method for {@link DatabindContext#getActiveView()}.
     */
    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    /**
     * Test method for {@link DatabindContext#getLocale()}.
     */
    @Test
    public void testGetLocale() {
        Locale locale = context.getLocale();
        assertEquals(Locale.getDefault(), locale);
    }

    /**
     * Test method for {@link DatabindContext#getTimeZone()}.
     */
    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = context.getTimeZone();
        assertEquals(TimeZone.getDefault(), timeZone);
    }

    /**
     * Test method for {@link DatabindContext#getDefaultPropertyFormat(Class)}.
     */
    @Test
    public void testgetDefaultPropertyFormat() throws JsonMappingException {
        JsonFormat.Value format = context.getDefaultPropertyFormat(String.class);
        assertNotNull(format);
    }

    /**
     * Test method for {@link DatabindContext#getAttribute(Object)}.
     */
    @Test
    public void testGetAttribute() {
        assertNull(context.getAttribute("testKey"));
    }

    /**
     * Test method for {@link DatabindContext#setAttribute(Object, Object)}.
     */
    @Test
    public void testSetAttribute() {
        context.setAttribute("testKey", "testValue");
        assertEquals("testValue", context.getAttribute("testKey"));
    }

    /**
     * Test method for {@link DatabindContext#constructType(Type)}.
     */
    @Test
    public void testConstructType() {
        JavaType javaType = context.constructType(String.class);
        assertNotNull(javaType);
    }

    /**
     * Test method for {@link DatabindContext#constructSpecializedType(JavaType, Class)}.
     */
    @Test
    public void testConstructSpecializedType() {
        JavaType javaType = context.constructType(String.class);
        JavaType javaTypeSubclass = context.constructSpecializedType(javaType, Substring.class);
        assertNotNull(javaTypeSubclass);
        assertEquals(Substring.class, javaTypeSubclass.getRawClass());
    }

    /**
     * Test method for {@link DatabindContext#resolveSubType(JavaType, String)}.
     */
    @Test
    public void testResolveSubType() {
        JavaType javaType = context.constructType(String.class);
        JavaType javaTypeSubclass = context.resolveSubType(javaType, "java.lang.StringBuilder");
        assertNotNull(javaTypeSubclass);
        assertEquals(StringBuilder.class, javaTypeSubclass.getRawClass());
    }

    /**
     * Test method for {@link DatabindContext#invalidTypeIdException(JavaType, String, String)}.
     */
    @Test
    public void testInvalidTypeIdException() {
        assertThrows(JsonMappingException.class, () -> context.invalidTypeIdException(JavaType.construct(int.class), "int", ""));
    }

    /**
     * Test method for {@link DatabindContext#getTypeFactory()}.
     */
    @Test
    public void testgetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    /**
     * Test method for {@link DatabindContext#objectIdGeneratorInstance(Annotated, ObjectIdInfo)}.
     */
    @Test
    public void testObjectIdGeneratorInstance() {
        assertNull(context.objectIdGeneratorInstance(null, null));
    }

    /**
     * Test method for {@link DatabindContext#objectIdResolverInstance(Annotated, ObjectIdInfo)}.
     */
    @Test
    public void testObjectIdResolverInstance() {
        assertNotNull(context.objectIdResolverInstance(null, null));
    }

    /**
     * Test method for {@link DatabindContext#converterInstance(Annotated, Object)}.
     */
    @Test
    public void testConverterInstance() {
        assertNull(context.converterInstance(null, null));
    }

    /**
     * Test method for {@link DatabindContext#reportBadDefinition(JavaType, String)}.
     */
    @Test
    public void testReportBadDefinition() {
        assertThrows(JsonMappingException.class, () -> context.reportBadDefinition(JavaType.construct(int[].class), ""));
    }

    /**
     * Test method for {@link DatabindContext#reportBadDefinition(Class, String)}.
     */
    @Test
    public void testReportBadDefinitionClass() {
        assertThrows(JsonMappingException.class, () -> context.reportBadDefinition(int[].class, ""));
    }

    /**
     * Test method for {@link DatabindContext#_format(String, Object...)}.
     */
    @Test
    public void testFormat() {
        String formattedMessage = context._format("%s is an %s with %d %s", "John Doe", "employee", 5, "records");
        assertEquals("John Doe is an employee with 5 records", formattedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_truncate(String)}.
     */
    @Test
    public void testTruncate() {
        String truncatedMessage = context._truncate("This is a very long message that should be truncated");
        assertTrue(truncatedMessage.endsWith("...[" + "This is a very long message that should be truncated".substring(truncatedMessage.length() - 20) + "]"));
    }

    /**
     * Test method for {@link DatabindContext#_quotedString(String)}.
     */
    @Test
    public void testQuotedString() {
        String quotedMessage = context._quotedString("This is a message with 'quotes' in it");
        assertTrue(quotedMessage.startsWith("\"This is a message with 'quotes' in it\"") && quotedMessage.endsWith("\""));
    }

    /**
     * Test method for {@link DatabindContext#_colonConcat(String, String)}.
     */
    @Test
    public void testColonConcat() {
        String colonConcatenatedMessage = context._colonConcat("Error: ", "Invalid data format");
        assertEquals("Error: Invalid data format", colonConcatenatedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_desc(String)}.
     */
    @Test
    public void testDesc() {
        String describedMessage = context._desc("This is a message that should be described");
        assertTrue(describedMessage.startsWith("[N/A]") || describedMessage.endsWith("]...") || describedMessage.equals("This is a message that should be described"));
    }

    /**
     * Test method for {@link DatabindContext#_format(String, Object...)}.
     */
    @Test
    public void testFormatWithException() {
        assertThrows(JsonProcessingException.class, () -> objectMapper.writeValueAsString(new Object()));
    }

    /**
     * Test method for {@link DatabindContext#_format(String, Object...)}.
     */
    @Test
    public void testFormatWithNullMessage() {
        String formattedMessage = context._format(null, "John Doe", "employee", 5, "records");
        assertEquals("", formattedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_format(String, Object...)}.
     */
    @Test
    public void testFormatWithNullMessageArgs() {
        String formattedMessage = context._format("%s is an %s with %d %s", null, "employee", 5, "records");
        assertEquals("", formattedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_format(String, Object...)}.
     */
    @Test
    public void testFormatWithNullMessageFormat() {
        String formattedMessage = context._format(null, "John Doe", "employee", 5, "records");
        assertEquals("", formattedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_truncate(String)}.
     */
    @Test
    public void testTruncateWithNullDescription() {
        String truncatedMessage = context._truncate(null);
        assertEquals("", truncatedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_quotedString(String)}.
     */
    @Test
    public void testQuotedStringWithNullDescription() {
        String quotedMessage = context._quotedString(null);
        assertEquals("[N/A]", quotedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_quotedString(String)}.
     */
    @Test
    public void testQuotedStringWithEmptyDescription() {
        String quotedMessage = context._quotedString("");
        assertEquals("\"\"", quotedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_quotedString(String)}.
     */
    @Test
    public void testQuotedStringWithDescriptionWithQuotes() {
        String quotedMessage = context._quotedString("'This is a message with 'quotes' in it'");
        assertTrue(quotedMessage.startsWith("\"\'This is a message with 'quotes' in it\'\"") && quotedMessage.endsWith("\""));
    }

    /**
     * Test method for {@link DatabindContext#_colonConcat(String, String)}.
     */
    @Test
    public void testColonConcatWithNullMessage() {
        String colonConcatenatedMessage = context._colonConcat(null, "Invalid data format");
        assertEquals("Invalid data format", colonConcatenatedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_desc(String)}.
     */
    @Test
    public void testDescWithNullDescription() {
        String describedMessage = context._desc(null);
        assertEquals("[N/A]", describedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_desc(String)}.
     */
    @Test
    public void testDescWithEmptyDescription() {
        String describedMessage = context._desc("");
        assertEquals("", describedMessage);
    }

    /**
     * Test method for {@link DatabindContext#_desc(String)}.
     */
    @Test
    public void testDescWithDescriptionWithLengthGreaterThanMaxErrorStringLength() {
        String describedMessage = context._desc("This is a very long description that should be truncated");
        assertTrue(describedMessage.endsWith("...[" + "This is a very long description that should be truncated".substring(describedMessage.length() - 20) + "]"));
    }
}