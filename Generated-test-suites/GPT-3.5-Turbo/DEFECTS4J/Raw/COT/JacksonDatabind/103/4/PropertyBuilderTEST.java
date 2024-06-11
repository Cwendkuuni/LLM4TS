import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {
  
  @Test
  public void testBuildWriter() {
    // Create a new PropertyBuilder instance
    SerializationConfig config = new SerializationConfig();
    BeanDescription beanDesc = new BeanDescription();
    PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
    
    // Perform the test
    try {
      propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
      fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Exception was correctly thrown
    } catch (Exception e) {
      fail("Expected NullPointerException to be thrown");
    }
  }

  @Test
  public void testFindSerializationType() {
    // Create a new PropertyBuilder instance
    SerializationConfig config = new SerializationConfig();
    BeanDescription beanDesc = new BeanDescription();
    PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
    
    // Perform the test
    try {
      propertyBuilder.findSerializationType(null, false, null);
      fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Exception was correctly thrown
    } catch (Exception e) {
      fail("Expected NullPointerException to be thrown");
    }
  }
}