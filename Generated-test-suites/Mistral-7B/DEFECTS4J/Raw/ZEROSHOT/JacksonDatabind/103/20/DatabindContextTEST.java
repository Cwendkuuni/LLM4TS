package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabindContextTest {

    private DatabindContext context;
    private MapperConfig<ObjectMapper.DefaultConfig> config;
    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        config = mock(MapperConfig.class);
        typeFactory = mock(TypeFactory.class);
        context = new TestDatabindContext(config, typeFactory);
    }

    @Test
    public void testGetConfig() {
        assertEquals(config, context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        when(config.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
        assertEquals(annotationIntrospector, context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        when(config.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)).thenReturn(true);
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));

        when(config.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)).thenReturn(false);
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        when(config.canOverrideAccessModifiers()).thenReturn(true);
        assertTrue(context.canOverrideAccessModifiers());

        when(config.canOverrideAccessModifiers()).thenReturn(false);
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        // This test is not comprehensive as it depends on the implementation of the getDefaultPropertyFormat method.
        // However, we can check if the method returns a valid instance.
        assertNotNull(context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testGetAttribute() {
        Object attribute = new Object();
        when(config.setAttribute(Object.class, attribute)).thenReturn(context);

        assertEquals(attribute, context.getAttribute(Object.class));
        assertNull(context.getAttribute(null));
    }

    @Test
    public void testSetAttribute() {
        Object attribute = new Object();

        context.setAttribute(Object.class, attribute);
        assertEquals(attribute, context.getAttribute(Object.class));
    }

    @Test
    public void testConstructType() {
        Type type = Object.class;
        JavaType javaType = context.constructType(type);
        assertEquals(type, javaType.getType());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        Class<?> subclass = String[].class;

        JavaType specializedType = context.constructSpecializedType(baseType, subclass);
        assertEquals(subclass, specializedType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeFailure() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subClass = "NonExistentClass";

        context.resolveSubType(baseType, subClass);
    }

    @Test
    public void testInvalidTypeIdException() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "NonStringType";

        assertNotNull(context.invalidTypeIdException(baseType, typeId, null));
    }

    @Test
    public void testGetTypeFactory() {
        assertEquals(typeFactory, context.getTypeFactory());
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        // This test is not comprehensive as it depends on the implementation of the objectIdGeneratorInstance method.
        // However, we can check if the method returns a valid instance.
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);

        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);

        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        Object converterDef = new Converter<Integer, Integer>() {};

        Converter<Object, Object> converter = context.converterInstance(annotatedMember, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType(String.class);
        String msg = "Unexpected message";

        context.reportBadDefinition(type, msg);
    }

    @Test
    public void testFormat() {
        String msg = "Message{0}";
        String[] msgArgs = {"Argument"};

        assertEquals(_format(msg, msgArgs), "MessageArgument");
    }

    @Test
    public void testTruncate() {
        String desc = "Long description";

        assertEquals(_truncate(desc), "Long description]...");
    }

    @Test
    public void testQuotedString() {
        String desc = "\"Short description\"";

        assertEquals(_quotedString(desc), "\"Short description\"");
    }

    @Test
    public void testColonConcat() {
        String msgBase = "Base message";
        String extra = "Extra message";

        assertEquals(_colonConcat(msgBase, extra), "Base message: Extra message");
    }

    @Test
    public void testDesc() {
        String desc = "Short description";

        assertEquals(_desc(desc), "Short description");
    }
}

class TestDatabindContext extends DatabindContext {

    public TestDatabindContext(MapperConfig<?> config, TypeFactory typeFactory) {
        super();
        this.config = config;
        this.typeFactory = typeFactory;
    }

    @Override
    public MapperConfig<?> getConfig() {
        return config;
    }

    @Override
    public AnnotationIntrospector getAnnotationIntrospector() {
        return null;
    }

    @Override
    public boolean isEnabled(MapperFeature feature) {
        return true;
    }

    @Override
    public boolean canOverrideAccessModifiers() {
        return true;
    }

    @Override
    public Class<?> getActiveView() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    @Override
    public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
        return null;
    }

    @Override
    public Object getAttribute(Object key) {
        return null;
    }

    @Override
    public DatabindContext setAttribute(Object key, Object value) {
        return this;
    }

    @Override
    public JavaType constructType(Type type) {
        return null;
    }

    @Override
    public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
        return null;
    }

    @Override
    public JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
        throw new JsonMappingException(JsonMappingException.Reason.INVALID_TYPE_ID, baseType, typeId, extraDesc);
    }

    @Override
    public TypeFactory getTypeFactory() {
        return typeFactory;
    }

    @Override
    public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
            throws JsonMappingException {
        return null;
    }

    @Override
    public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) {
        return null;
    }

    @Override
    public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef)
            throws JsonMappingException {
        return null;
    }

    @Override
    public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
        return null;
    }
}