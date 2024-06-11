import org.junit.Test;

import static org.junit.Assert.*;

public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = new DatabindContextImplementation();
        assertNotNull(context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = new DatabindContextImplementation();
        assertNotNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = new DatabindContextImplementation();
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_FIELDS));
        assertTrue(context.isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = new DatabindContextImplementation();
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = new DatabindContextImplementation();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = new DatabindContextImplementation();
        assertNotNull(context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = new DatabindContextImplementation();
        assertNotNull(context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = new DatabindContextImplementation();
        assertNotNull(context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = new DatabindContextImplementation();
        assertNull(context.getAttribute("key"));
    }

    @Test
    public void testSetAttribute() {
        DatabindContext context = new DatabindContextImplementation();
        assertNull(context.getAttribute("key"));
        context.setAttribute("key", "value");
        assertEquals("value", context.getAttribute("key"));
    }

    @Test
    public void testConstructType() {
        DatabindContext context = new DatabindContextImplementation();
        assertNotNull(context.constructType(String.class));
        assertNull(context.constructType(null));
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = new DatabindContextImplementation();
        assertNotNull(context.constructSpecializedType(
                context.constructType(Object.class), String.class));
    }

    @Test
    public void testResolveSubType() {
        DatabindContext context = new DatabindContextImplementation();
        assertNotNull(context.resolveSubType(
                context.constructType(Object.class), "java.lang.String"));
    }

    @Test
    public void testInvalidTypeIdException() {
        DatabindContext context = new DatabindContextImplementation();
        assertThrows(JsonMappingException.class, () ->
                context.invalidTypeIdException(context.constructType(String.class),
                        "subtype_id", "Invalid subtype"));
    }

    @Test
    public void testReportBadDefinition() {
        DatabindContext context = new DatabindContextImplementation();
        assertThrows(JsonMappingException.class, () ->
                context.reportBadDefinition(context.constructType(String.class),
                        "Bad definition"));
    }

    private static class DatabindContextImplementation extends DatabindContext {
        @Override
        public MapperConfig<?> getConfig() {
            return new MapperConfigImplementation();
        }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return new AnnotationIntrospectorImplementation();
        }

        @Override
        public boolean isEnabled(MapperFeature feature) {
            return feature == MapperFeature.SORT_PROPERTIES_ALPHABETICALLY;
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
            return Locale.US;
        }

        @Override
        public TimeZone getTimeZone() {
            return TimeZone.getDefault();
        }

        @Override
        public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
            return new JsonFormat.Value();
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
        public JsonMappingException invalidTypeIdException(JavaType baseType, String typeId,
                                                           String extraDesc) {
            return new JsonMappingException(typeId);
        }

        @Override
        public TypeFactory getTypeFactory() {
            return new TypeFactoryImplementation();
        }

        @Override
        public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
            throw new JsonMappingException(msg);
        }
    }

    private static class MapperConfigImplementation extends MapperConfig<Object> {
        @Override
        public boolean canOverrideAccessModifiers() {
            return true;
        }
    }

    private static class AnnotationIntrospectorImplementation extends AnnotationIntrospector {
    }

    private static class TypeFactoryImplementation extends TypeFactory {
    }
}