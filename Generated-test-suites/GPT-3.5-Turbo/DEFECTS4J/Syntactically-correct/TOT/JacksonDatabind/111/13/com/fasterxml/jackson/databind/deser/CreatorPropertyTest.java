import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;
    private DeserializationContext deserializationContext;
    private Object beanInstance;
    
    @Before
    public void setUp() {
        PropertyName propertyName = new PropertyName("test");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = null;
        TypeDeserializer typeDeserializer = null;
        Annotations contextAnnotations = new Annotations[] {};
        AnnotatedParameter annotatedParameter = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata propertyMetadata = null;
        creatorProperty = new CreatorProperty(propertyName, type, wrapperName,
                typeDeserializer, contextAnnotations, annotatedParameter,
                index, injectableValueId, propertyMetadata);
        
        deserializationContext = new DeserializationContext(null, null);
        beanInstance = new Object();
    }
    
    @Test
    public void testFindInjectableValue() {
        try {
            assertNull(creatorProperty.findInjectableValue(deserializationContext, beanInstance));
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }
    
    @Test
    public void testInject() {
        try {
            creatorProperty.inject(deserializationContext, beanInstance);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(creatorProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(creatorProperty.getMember());
    }
    
    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser parser = null;
        try {
            creatorProperty.deserializeAndSet(parser, deserializationContext, beanInstance);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser parser = null;
        try {
            assertNull(creatorProperty.deserializeSetAndReturn(parser, deserializationContext, beanInstance));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSet() {
        try {
            creatorProperty.set(beanInstance, "value");
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSetAndReturn() {
        try {
            assertEquals("value", creatorProperty.setAndReturn(beanInstance, "value"));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }
    
    @Test
    public void testToString() {
        assertEquals("[creator property, name 'test'; inject id 'null']", creatorProperty.toString());
    }
    
    @Test(expected = IOException.class)
    public void testVerifySetter() throws IOException {
        creatorProperty._fallbackSetter = null;
        creatorProperty._verifySetter();
    }
    
    @Test(expected = IOException.class)
    public void testReportMissingSetter() throws IOException {
        creatorProperty._reportMissingSetter(null, null);
    }
}