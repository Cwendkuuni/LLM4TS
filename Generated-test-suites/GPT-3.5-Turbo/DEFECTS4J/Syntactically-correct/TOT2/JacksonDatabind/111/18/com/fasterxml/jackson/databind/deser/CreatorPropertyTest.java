import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class CreatorPropertyTest {

    @Test
    public void testCreatorPropertyConstructor() {
        // Test all parameters are set correctly in the constructor
        PropertyName name = PropertyName.construct("propertyName");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 1;
        Object injectableValueId = new Object();
        Object metadata = new Object();
        
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName,
        		typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
    }

    @Test
    public void testFindInjectableValue_WithNullId_ThrowsException() throws JsonMappingException {
        CreatorProperty creatorProperty = new CreatorProperty(PropertyName.construct("test"), TypeFactory.defaultInstance().constructType(String.class),
                null, null, null, null, 0, null, null);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object beanInstance = new Object();

        Exception exception = assertThrows(JsonMappingException.class, () -> {
            creatorProperty.findInjectableValue(context, beanInstance);
        });

        assertEquals("Property 'test' (type java.lang.String) has no injectable value id configured",
                exception.getMessage());
    }

    // More test cases for the CreatorProperty class methods...

}