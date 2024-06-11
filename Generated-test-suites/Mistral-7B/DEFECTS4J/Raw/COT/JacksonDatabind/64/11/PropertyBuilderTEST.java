import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilderConstructor() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

}