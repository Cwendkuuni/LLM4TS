
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

@RunWith(JUnit4.class)
public class CreatorPropertyTest {

    @Test
    public void testFindInjectableValue_WhenInjectableValueIdIsNull_ShouldThrowException() {
        CreatorProperty creatorProperty = createCreatorProperty(null);
        DeserializationContext context = createContext();
        Object beanInstance = new Object();

        try {
            creatorProperty.findInjectableValue(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (InvalidDefinitionException e) {
            assertEquals("Property 'testName' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testFindInjectableValue_WhenInjectableValueIdIsNotNull_ShouldReturnInjectableValue() {
        Object injectableValueId = new Object();
        CreatorProperty creatorProperty = createCreatorProperty(injectableValueId);
        DeserializationContext context = createContext();
        Object beanInstance = new Object();

        Object injectableValue = new Object();
        context.addInjectableValue(injectableValueId, injectableValue);

        Object result = creatorProperty.findInjectableValue(context, beanInstance);

        assertEquals(injectableValue, result);
    }

    @Test
    public void testInject_WhenInjectableValueIdIsNull_ShouldThrowException() {
        CreatorProperty creatorProperty = createCreatorProperty(null);
        DeserializationContext context = createContext();
        Object beanInstance = new Object();
        Object injectableValue = new Object();

        try {
            creatorProperty.inject(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (InvalidDefinitionException e) {
            assertEquals("Property 'testName' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testInject_WhenInjectableValueIdIsNotNull_ShouldInjectValue() throws Exception {
        Object injectableValueId = new Object();
        CreatorProperty creatorProperty = createCreatorProperty(injectableValueId);
        DeserializationContext context = createContext();
        Object beanInstance = new Object();
        Object injectableValue = new Object();

        context.addInjectableValue(injectableValueId, injectableValue);

        creatorProperty.inject(context, beanInstance);

        assertEquals(injectableValue, creatorProperty.get(beanInstance));
    }

    @Test
    public void testDeserializeAndSet_ShouldSetFallbackSetter_WhenFallbackSetterIsNotNull() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext context = createContext();
        Object instance = new Object();

        creatorProperty.deserializeAndSet(parser, context, instance);

        assertNotNull(creatorProperty.get(instance));
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeAndSet_ShouldThrowException_WhenFallbackSetterIsNull() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext context = createContext();
        Object instance = new Object();
        creatorProperty.setFallbackSetter(null);

        creatorProperty.deserializeAndSet(parser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn_ShouldReturnFallbackSetterValue_WhenFallbackSetterIsNotNull() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext context = createContext();
        Object instance = new Object();

        Object result = creatorProperty.deserializeSetAndReturn(parser, context, instance);

        assertNotNull(result);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeSetAndReturn_ShouldThrowException_WhenFallbackSetterIsNull() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext context = createContext();
        Object instance = new Object();
        creatorProperty.setFallbackSetter(null);

        creatorProperty.deserializeSetAndReturn(parser, context, instance);
    }

    @Test
    public void testSet_ShouldSetFallbackSetter_WhenFallbackSetterIsNotNull() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        Object instance = new Object();

        creatorProperty.set(instance, new Object());

        assertNotNull(creatorProperty.get(instance));
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testSet_ShouldThrowException_WhenFallbackSetterIsNull() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        Object instance = new Object();
        creatorProperty.setFallbackSetter(null);

        creatorProperty.set(instance, new Object());
    }

    @Test
    public void testSetAndReturn_ShouldReturnFallbackSetterValue_WhenFallbackSetterIsNotNull() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        Object instance = new Object();

        Object result = creatorProperty.setAndReturn(instance, new Object());

        assertNotNull(result);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testSetAndReturn_ShouldThrowException_WhenFallbackSetterIsNull() throws Exception {
        CreatorProperty creatorProperty = createCreatorProperty();
        Object instance = new Object();
        creatorProperty.setFallbackSetter(null);

        creatorProperty.setAndReturn(instance, new Object());
    }

    private CreatorProperty createCreatorProperty() {
        PropertyName name = new PropertyName("testName");
        Annotations contextAnnotations = new Annotations(new AnnotatedMember() {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }
        });
        AnnotatedParameter param = new AnnotatedParameter(null, null, 0);
        Object injectableValueId = new Object();
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED_OR_OPTIONAL;
        return new CreatorProperty(name, null, null, null, contextAnnotations, param, 0, injectableValueId, metadata);
    }

    private CreatorProperty createCreatorProperty(Object injectableValueId) {
        PropertyName name = new PropertyName("testName");
        Annotations contextAnnotations = new Annotations(new AnnotatedMember() {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }
        });
        AnnotatedParameter param = new AnnotatedParameter(null, null, 0);
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED_OR_OPTIONAL;
        return new CreatorProperty(name, null, null, null, contextAnnotations, param, 0, injectableValueId, metadata);
    }

    private DeserializationContext createContext() {
        return new ObjectMapper().reader().createDeserializationContext(null, null);
    }
}
