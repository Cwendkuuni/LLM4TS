
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.annotation.Annotation;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.defaultType();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.emptyAnnotations();
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.EMPTY_METADATA;
        
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        
        Assert.assertEquals(name, creatorProperty.getName());
        Assert.assertEquals(type, creatorProperty.getType());
        Assert.assertEquals(wrapperName, creatorProperty.getWrapperName());
        Assert.assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        Assert.assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        Assert.assertEquals(param, creatorProperty.getMember());
        Assert.assertEquals(index, creatorProperty.getCreatorIndex());
        Assert.assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        Assert.assertEquals(metadata, creatorProperty.getMetadata());
    }

    @Test
    public void testInjectableValue() {
        CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("test"), JavaType.defaultType(), null, null,
                Annotations.emptyAnnotations(), null, 0, "injectableId",
                PropertyMetadata.EMPTY_METADATA);

        DeserializationContext context = new DeserializationContext(null, null, null, null, null);
        Object beanInstance = new Object();

        try {
            creatorProperty.findInjectableValue(context, beanInstance);
        } catch (JsonMappingException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("Property 'test' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured",
                    e.getMessage());
        }
    }

    @Test
    public void testDeserializationAndSet() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("test"), JavaType.defaultType(), null, null,
                Annotations.emptyAnnotations(), null, 0, "injectableId",
                PropertyMetadata.EMPTY_METADATA);

        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        try {
            creatorProperty.deserializeAndSet(p, ctxt, instance);
        } catch (InvalidDefinitionException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("No fallback setter/field defined for creator property 'test'",
                    e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("test"), JavaType.defaultType(), null, null,
                Annotations.emptyAnnotations(), null, 0, "injectableId",
                PropertyMetadata.EMPTY_METADATA);

        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        
        try {
            creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
        } catch (InvalidDefinitionException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("No fallback setter/field defined for creator property 'test'",
                    e.getMessage());
        }
    }
    
    @Test
    public void testSet() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("test"), JavaType.defaultType(), null, null,
                Annotations.emptyAnnotations(), null, 0, "injectableId",
                PropertyMetadata.EMPTY_METADATA);

        Object instance = new Object();
        Object value = new Object();
        
        try {
            creatorProperty.set(instance, value);
        } catch (InvalidDefinitionException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("No fallback setter/field defined for creator property 'test'",
                    e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("test"), JavaType.defaultType(), null, null,
                Annotations.emptyAnnotations(), null, 0, "injectableId",
                PropertyMetadata.EMPTY_METADATA);

        Object instance = new Object();
        Object value = new Object();
        
        try {
            creatorProperty.setAndReturn(instance, value);
        } catch (InvalidDefinitionException e) {
            Assert.assertNotNull(e);
            Assert.assertEquals("No fallback setter/field defined for creator property 'test'",
                    e.getMessage());
        }
    }
}

