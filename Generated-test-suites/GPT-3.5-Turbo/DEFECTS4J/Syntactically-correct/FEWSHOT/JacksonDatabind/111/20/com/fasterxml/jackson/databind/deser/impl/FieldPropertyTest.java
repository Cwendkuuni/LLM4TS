import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class FieldPropertyTest {
    
    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedField field = null;
        
        FieldProperty property = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullValueProvider());
        assertNull(property.getName());
        assertNull(property.getInternalName());
        assertNull(property.getMember());
    }
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        
        FieldProperty property = new FieldProperty(null, null, null, null, null);
        SettableBeanProperty newProperty = property.withNullProvider(nva);
        
        assertEquals(nva, newProperty.getNullValueProvider());
    }
    
    @Test
    public void testWithNullValueDeserializer() {
        JsonDeserializer<?> deser = null;
        
        FieldProperty property = new FieldProperty(null, null, null, null, null);
        SettableBeanProperty newProperty = property.withValueDeserializer(deser);
        
        assertEquals(deser, newProperty.getValueDeserializer());
    }
    
    @Test
    public void testDeserializeAndSet() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        
        FieldProperty property = new FieldProperty(null, null, null, null, null);
        property.deserializeAndSet(p, ctxt, instance);
        
        // Add asserts if necessary
        
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        
        FieldProperty property = new FieldProperty(null, null, null, null, null);
        property.deserializeSetAndReturn(p, ctxt, instance);
        
        // Add asserts if necessary
        
    }
    
    @Test
    public void testSet() {
        Object instance = null;
        Object value = null;
        
        FieldProperty property = new FieldProperty(null, null, null, null, null);
        property.set(instance, value);
        
        // Add asserts if necessary
        
    }
    
    @Test
    public void testSetAndReturn() {
        Object instance = null;
        Object value = null;
        
        FieldProperty property = new FieldProperty(null, null, null, null, null);
        property.setAndReturn(instance, value);
        
        // Add asserts if necessary
        
    }
}