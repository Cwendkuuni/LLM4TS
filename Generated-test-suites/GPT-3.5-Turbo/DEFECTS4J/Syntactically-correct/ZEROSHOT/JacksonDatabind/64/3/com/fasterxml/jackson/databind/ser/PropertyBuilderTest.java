import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {
    
    private PropertyBuilder propertyBuilder;
    
    @Before
    public void setUp() {
        propertyBuilder = new PropertyBuilder();
    }
    
    @After
    public void tearDown() {
        propertyBuilder = null;
    }
    
    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }
    
    @Test
    public void testBuildWriter() {
        // TODO: Implement test
    }
    
    @Test
    public void testFindSerializationType() throws JsonMappingException {
        JavaType type = propertyBuilder.findSerializationType(annotated, useStaticTyping, declaredType);
        assertNotNull(type);
    }
    
    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNotNull(propertyDefaultValue);
    }
    
    @Test
    public void testGetDefaultValue() {
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNotNull(defaultValue);
    }
    
    @Test
    public void test_throwWrapped() {
        Exception exception = null;
        try {
            _throwWrapped(e, propName, defaultBean);
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception);
    }
}