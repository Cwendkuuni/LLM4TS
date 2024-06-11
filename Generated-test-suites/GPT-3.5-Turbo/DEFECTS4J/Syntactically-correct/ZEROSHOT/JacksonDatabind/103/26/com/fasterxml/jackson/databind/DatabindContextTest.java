package com.fasterxml.jackson.databind;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class DatabindContextTest {

    private DatabindContext context;

    @Before
    public void setUp() {
        context = new DatabindContext() {
            @Override
            public MapperConfig<?> getConfig() {
                return null;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }

            @Override
            public boolean isEnabled(MapperFeature feature) {
                return false;
            }

            @Override
            public boolean canOverrideAccessModifiers() {
                return false;
            }

            @Override
            public Class<?> getActiveView() {
                return null;
            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public TimeZone getTimeZone() {
                return null;
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
                return null;
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
            public JavaType resolveSubType(JavaType baseType, String subClass) throws JsonMappingException {
                return null;
            }

            @Override
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId,
                    String extraDesc) {
                return null;
            }

            @Override
            public TypeFactory getTypeFactory() {
                return null;
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

            @Override
            public String _format(String msg, Object... msgArgs) {
                return null;
            }

            @Override
            public String _truncate(String desc) {
                return null;
            }

            @Override
            public String _quotedString(String desc) {
                return null;
            }

            @Override
            protected String _colonConcat(String msgBase, String extra) {
                return null;
            }

            @Override
            protected String _desc(String desc) {
                return null;
            }
        };
    }

    @Test
    public void testGetConfig() {
        Assert.assertNull(context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        Assert.assertNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        Assert.assertFalse(context.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        Assert.assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        Assert.assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        Assert.assertNull(context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        Assert.assertNull(context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        Assert.assertNull(context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testGetAttribute() {
        Assert.assertNull(context.getAttribute("key"));
    }

    @Test
    public void testSetAttribute() {
        Assert.assertNull(context.setAttribute("key", "value"));
    }

    @Test
    public void testConstructType() {
        Assert.assertNull(context.constructType(String.class));
    }

    @Test
    public void testConstructSpecializedType() {
        Assert.assertNull(context.constructSpecializedType(JavaType.construct(String.class), String.class));
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        Assert.assertNull(context.resolveSubType(JavaType.construct(String.class), "SubClass"));
    }

    @Test
    public void testInvalidTypeIdException() {
        Assert.assertNull(context.invalidTypeIdException(JavaType.construct(String.class), "TypeId", "ExtraDesc"));
    }

    @Test
    public void testGetObjectType() {
        Assert.assertNull(context.getTypeFactory());
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Assert.assertNull(context.objectIdGeneratorInstance(null, null));
    }

    @Test
    public void testObjectIdResolverInstance() {
        Assert.assertNull(context.objectIdResolverInstance(null, null));
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Assert.assertNull(context.converterInstance(null, null));
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        Assert.assertNull(context.reportBadDefinition(JavaType.construct(String.class), "Invalid Definition"));
    }

    @Test
    public void testFormat() {
        Assert.assertNull(context._format("Message"));
    }

    @Test
    public void testTruncate() {
        Assert.assertNull(context._truncate("Description"));
    }

    @Test
    public void testQuotedString() {
        Assert.assertNull(context._quotedString("Description"));
    }

    @Test
    public void testColonConcat() {
        Assert.assertNull(context._colonConcat("Message", "Extra"));
    }

    @Test
    public void testDesc() {
        Assert.assertNull(context._desc("Description"));
    }
}