import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanDescription;

public class PropertyBuilderTest {
    
    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;
    private Annotations classAnnotations;
    private JsonSerializer<?> serializer;
    private TypeSerializer typeSerializer;
    private TypeSerializer contentTypeSerializer;
    private AnnotatedMember annotatedMember;
    private JavaType declaredType;
    private ValueInclusion defaultInclusion;
    
    @Before
    public void setup() {
        config = Mockito.mock(SerializationConfig.class);
        beanDesc = Mockito.mock(BeanDescription.class);
        classAnnotations = Mockito.mock(Annotations.class);
        serializer = Mockito.mock(JsonSerializer.class);
        typeSerializer = Mockito.mock(TypeSerializer.class);
        contentTypeSerializer = Mockito.mock(TypeSerializer.class);
        annotatedMember = Mockito.mock(AnnotatedMember.class);
        declaredType = Mockito.mock(JavaType.class);
        defaultInclusion = Mockito.mock(ValueInclusion.class);
        
        Mockito.when(beanDesc.findPropertyInclusion(Mockito.any(ValueInclusion.class))).thenReturn(defaultInclusion);
        Mockito.when(defaultInclusion.getValueInclusion()).thenReturn(ValueInclusion.ALWAYS);
        
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }
    
    @Test
    public void testBuildWriter() throws JsonMappingException
    {
        //given
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;
        
        //when
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, annotatedMember, defaultUseStaticTyping);
        
        //then
         //Add your assertions here
    }
}