// Your test classes should extend TestCase or Test instead of the
// AbstractTestSuite class.
// The following code snippet shows an example of a simple test case.

import static org.junit.Assert.*;
import org.junit.Test;

public class YourTestClass extends TestCase {

  @Test
  public void testMethodUnderTest() {
    // Set up your objects under test here.

    // Call the method under test.
    int result = yourMethodUnderTest();

    // Assert that the method returned the expected result.
    assertEquals(expectedResult, result);
  }
}

// Test cases for other methods in the class can be added in a similar way.