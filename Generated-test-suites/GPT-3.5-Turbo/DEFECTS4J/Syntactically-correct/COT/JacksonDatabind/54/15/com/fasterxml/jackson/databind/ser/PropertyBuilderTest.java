import org.junit.Test;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.SerializationConfig;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector;

public class PropertyBuilderTest {
    
    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = null; // provide a valid SerializationConfig object
        BeanDescription beanDesc = null; // provide a valid BeanDescription object
        
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        
        Annotations annotations = propertyBuilder.getClassAnnotations();
        
        // Add assertions to test the functionality of getClassAnnotations() method
    }
    
    @Test
    public void testBuildWriter() {
        SerializerProvider prov = null; // provide a valid SerializerProvider object
        BeanPropertyDefinition propDef = null; // provide a valid BeanPropertyDefinition object
        JavaType declaredType = null; // provide a valid JavaType object
        JsonSerializer<?> ser = null; // provide a valid JsonSerializer object
        TypeSerializer typeSer = null; // provide a valid TypeSerializer object
        TypeSerializer contentTypeSer = null; // provide a valid TypeSerializer object
        AnnotatedMember am = null; // provide a valid AnnotatedMember object
        boolean defaultUseStaticTyping = true; // provide a valid defaultUseStaticTyping value
        
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        
        // Add assertions to test the functionality of buildWriter() method
    }
    
    @Test
    public void testFindSerializationType() {
        Annotated a = null; // provide a valid Annotated object
        boolean useStaticTyping = true; // provide a valid useStaticTyping value
        JavaType declaredType = null; // provide a valid JavaType object
        
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        
        // Add assertions to test the functionality of findSerializationType() method
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        String name = null; // provide a valid name
        AnnotatedMember member = null; // provide a valid AnnotatedMember object
        JavaType type = null; // provide a valid JavaType object
        
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        
        // Add assertions to test the functionality of getPropertyDefaultValue() method
    }
    
    @Test
    public void testGetDefaultValue() {
        JavaType type = null; // provide a valid JavaType object
        
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        
        // Add assertions to test the functionality of getDefaultValue() method
    }
    
    @Test
    public void testThrowWrapped() {
        Exception e = null; // provide a valid Exception object
        String propName = null; // provide a valid propName
        Object defaultBean = null; // provide a valid defaultBean
        
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        
        Object result = propertyBuilder._throwWrapped(e, propName, defaultBean);
        
        // Add assertions to test the functionality of _throwWrapped() method
    }
}