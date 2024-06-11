
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void getClassAnnotations_Test() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void buildWriter_Test() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, true);
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail("JsonMappingException occurred");
        }
    }

    @Test
    public void findSerializationType_Test() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(null, true, null);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail("JsonMappingException occurred");
        }
    }

    @Test
    public void getDefaultBean_Test() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void getPropertyDefaultValue_Test() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getPropertyDefaultValue("test", null, null));
    }

    @Test
    public void getDefaultValue_Test() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultValue(null));
    }

    @Test
    public void _throwWrapped_Test() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder._throwWrapped(null, null, null));
    }
}

