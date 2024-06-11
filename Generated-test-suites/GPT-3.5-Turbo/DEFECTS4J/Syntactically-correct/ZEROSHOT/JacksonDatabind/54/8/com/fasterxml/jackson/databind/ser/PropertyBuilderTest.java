import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.TypeFactory;

import static org.mockito.Mockito.when;

public class PropertyBuilderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    @Mock
    private JsonInclude.Value defaultInclusion;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Mock
    private AnnotatedMember am;

    @Mock
    private JavaType declaredType;

    @Mock
    private TypeSerializer contentTypeSer;
    
    @Mock
    private BeanPropertyDefinition propDef;

    @Mock
    private JsonSerializer<?> ser;

    @InjectMocks
    private PropertyBuilder propertyBuilder;

    @Test
    public void testBuildWriter() throws JsonMappingException {
        MockitoAnnotations.initMocks(this);

        when(propertyBuilder.buildWriter(config, propDef, declaredType, ser, null, contentTypeSer, am, false)).thenReturn(null);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(config, propDef, declaredType, ser, null, contentTypeSer, am, false);

        assert(writer == null);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        MockitoAnnotations.initMocks(this);

        when(annotationIntrospector.refineSerializationType(config, am, declaredType)).thenReturn(null);

        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);

        assert(serializationType == null);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        MockitoAnnotations.initMocks(this);

        Object defaultBean = new Object();
        when(propertyBuilder.getDefaultBean()).thenReturn(defaultBean);
        when(am.getValue(defaultBean)).thenReturn(new Object());

        Object value = propertyBuilder.getPropertyDefaultValue("name", am, declaredType);

        assert(value != null);
    }

    @Test
    public void testGetDefaultValue() {
        MockitoAnnotations.initMocks(this);

        Class<?> cls = String.class;
        JavaType type = new JavaType(cls);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assert(defaultValue.equals(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        MockitoAnnotations.initMocks(this);

        Object defaultBean = new Object();
        Exception e = new Exception("Test Exception");

        propertyBuilder._throwWrapped(e, "name", defaultBean);
    }
}