import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.BeanProperty;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;
    private TypeIdResolver idResolver;
    private JavaType baseType;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private BeanProperty property;

    @Before
    public void setUp() {
        baseType = TypeFactory.defaultInstance().constructType(Object.class);
        idResolver = new DefaultTypeIdResolver();
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = null;
        property = null;
        typeDeserializer = new TypeDeserializerBaseMock(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = new BeanPropertyMock("propertyName", TypeFactory.defaultInstance().constructType(String.class));
        TypeDeserializerBase newTypeDeserializer = typeDeserializer.forProperty(prop);
        assertTrue(newTypeDeserializer instanceof TypeDeserializerBase);
        assertTrue(newTypeDeserializer.getPropertyName().equals(typePropertyName));
    }

    @Test
    public void testGetTypeInclusion() {
        assertNotNull(typeDeserializer.getTypeInclusion());
        assertTrue(typeDeserializer.getTypeInclusion().equals(TypeDeserializer.As.WRAPPER_OBJECT));
    }

    @Test
    public void testBaseTypeName() {
        assertEquals(typeDeserializer.baseTypeName(), "java.lang.Object");
    }

    @Test
    public void testGetPropertyName() {
        assertEquals(typeDeserializer.getPropertyName(), "type");
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeDeserializer.getTypeIdResolver(), idResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertTrue(typeDeserializer.toString().startsWith("["));
        assertTrue(typeDeserializer.toString().endsWith("]"));
    }

    @Test
    public void testFindDefaultImplDeserializer() {
        try {
            assertNull(typeDeserializer._findDefaultImplDeserializer(null));
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() {
        try {
            assertNull(typeDeserializer._deserializeWithNativeTypeId(null, null));
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testHandleUnknownTypeId() {
        try {
            assertNull(typeDeserializer._handleUnknownTypeId(null, "UNKNOWN", idResolver, baseType));
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    private class TypeDeserializerBaseMock extends TypeDeserializerBase {

        protected TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes, String typePropertyName, 
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.WRAPPER_OBJECT;
        }
    }

    private class DefaultTypeIdResolver implements TypeIdResolver {

        @Override
        public void init(JavaType baseType) {}

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public String idFromBaseType() {
            return null;
        }

        @Override
        public JavaType typeFromId(String id) {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public Id getMechanism() {
            return null;
        }
    }

    private class BeanPropertyMock implements BeanProperty {

        private String name;
        private JavaType type;

        public BeanPropertyMock(String name, JavaType type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        @Override
        public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
            return null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public JavaType getType() {
            return type;
        }

        @Override
        public PropertyMetadata getMetadata() {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public TypeDeserializer findTypeDeserializer(DeserializationConfig config) throws JsonMappingException {
            return null;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls, boolean forSerialization) {
            return null;
        }
    }
}