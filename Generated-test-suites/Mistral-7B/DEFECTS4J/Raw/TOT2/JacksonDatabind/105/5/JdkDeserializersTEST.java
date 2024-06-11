import org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

  // Test case 1 for method find
  @Test
  public void testFindDeserializer() {
    Class<String> stringClass = String.class;
    String clsName = "java.lang.String";
    JsonDeserializer<?> expectedDeserializer = new StringDeserializer(); // Assuming StringDeserializer is a mock deserializer
    JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(stringClass, clsName);
    assertSame(expectedDeserializer, actualDeserializer);
  }

  // Test case 1: test find with null rawType
  @Test(expected = NullPointerException.class)
  public void testFindWithNullRawType() {
    String clsName = "java.lang.String";
    JdkDeserializers.find(null, clsName);
  }

  // Test case 2: test find with null clsName
  @Test(expected = NullPointerException.class)
  public void testFindWithNullClassName() {
    Class<String> stringClass = String.class;
    JdkDeserializers.find(stringClass, null);
  }

  // Test case 3: test find with empty string as clsName
  @Test(expected = IllegalArgumentException.class)
  public void testFindWithEmptyClassName() {
    Class<String> stringClass = String.class;
    JdkDeserializers.find(stringClass, "");
  }

  // Test case 4: test find with non-existent class name
  @Test(expected = IllegalStateException.class)
  public void testFindWithNonExistentClassName() {
    Class<String> stringClass = String.class;
    JdkDeserializers.find(stringClass, "com.nonexistent.Class");
  }

  // ... other test cases for other methods will be added here
}