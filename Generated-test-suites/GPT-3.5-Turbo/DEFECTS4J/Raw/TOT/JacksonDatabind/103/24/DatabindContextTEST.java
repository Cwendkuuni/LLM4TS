import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private DatabindContext databindContext;

    @Before
    public void setUp() {
        databindContext = new DatabindContext() {
            // In this test, we will use anonymous class to instantiate DatabindContext
            // since DatabindContext is an abstract class.
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
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId,
                                                                  String extraDesc) {
                return null;
            }

            @Override
            public TypeFactory getTypeFactory() {
                return null;
            }

            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                return null;
            }
        };
    }

    @Test
    public void testConstructType() {
        // Test when type is null
        assertNull(databindContext.constructType(null));

        // Test when type is not null
        Type type = new Type() {};
        assertNotNull(databindContext.constructType(type));
    }

    @Test
    public void testConstructSpecializedType() {
        // Test when baseType's raw class is equal to subclass
        JavaType baseType = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return String.class;
            }
        };
        Class<?> subclass = String.class;
        assertEquals(baseType, databindContext.constructSpecializedType(baseType, subclass));

        // Test when baseType's raw class is not equal to subclass
        JavaType baseType2 = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return String.class;
            }
        };
        Class<?> subclass2 = Integer.class;
        assertNotEquals(baseType2, databindContext.constructSpecializedType(baseType2, subclass2));
        assertEquals(Integer.class, databindContext.constructSpecializedType(baseType2, subclass2).getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return Object.class;
            }
        };

        // Test when subClass is valid
        String subClass = "java.lang.Integer";
        assertEquals(Integer.class, databindContext.resolveSubType(baseType, subClass).getRawClass());

        // Test when subClass is invalid
        try {
            subClass = "InvalidClass";
            databindContext.resolveSubType(baseType, subClass);
        } catch (JsonMappingException e) {
            assertEquals("Not a subtype", e.getMessage());
        }
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        // Test when converterDef is null
        assertNull(databindContext.converterInstance(null, null));

        // Test when converterDef is already a Converter
        Converter<Object, Object> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };
        assertEquals(converter, databindContext.converterInstance(null, converter));

        // Test when converterDef is a Class
        Class<?> converterClass = Converter.None.class;
        assertNull(databindContext.converterInstance(null, converterClass));

        converterClass = String.class;
        try {
            databindContext.converterInstance(null, converterClass);
        } catch (IllegalStateException e) {
            assertEquals("AnnotationIntrospector returned Class java.lang.String; expected Class<Converter>", e.getMessage());
        }
    }
}