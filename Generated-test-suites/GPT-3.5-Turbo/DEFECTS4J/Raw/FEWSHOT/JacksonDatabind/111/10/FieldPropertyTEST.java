import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FieldPropertyTest {
    
    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null);
        JavaType type = new JavaType(null, 0, null, null);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        assertEquals(propDef, fieldProperty.getMember());
    }
    
    @Test
    public void testConstructor_withNullValueProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null);
        JavaType type = new JavaType(null, 0, null, null);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        assertNull(fieldProperty.getNullProvider());
    }
    
    @Test
    public void testWithNullProvider() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null);
        JavaType type = new JavaType(null, 0, null, null);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        fieldProperty = fieldProperty.withNullProvider(null);
        
        assertNull(fieldProperty.getNullProvider());
    }
    
    @Test
    public void testDeserializeAndSet() {
        //Not implemented
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        //Not implemented
    }
    
    @Test
    public void testSet() {
        //Not implemented
    }
    
    @Test
    public void testSetAndReturn() {
        //Not implemented
    }
    
    @Test
    public void testReadResolve() {
        //Not implemented
    }
}