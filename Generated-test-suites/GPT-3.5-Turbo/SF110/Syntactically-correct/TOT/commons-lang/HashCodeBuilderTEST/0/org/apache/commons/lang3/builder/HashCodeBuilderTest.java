import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

  @Test
  public void testReflectionHashCode_validObject_returnsHashCode() {
    // Arrange
    String input = "Test String";

    // Act
    int result = HashCodeBuilder.reflectionHashCode(input, true);

    // Assert
    assertNotEquals(0, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReflectionHashCode_nullObject_throwsIllegalArgumentException() {
    // Arrange
    String input = null;

    // Act
    HashCodeBuilder.reflectionHashCode(input, true);

    // Assert
    fail("Expected IllegalArgumentException to be thrown");
  }

  @Test
  public void testReflectionHashCode_noExcludeFields_returnsHashCode() {
    // Arrange
    TestData testData = new TestData("Test", 123);

    // Act
    int result = HashCodeBuilder.reflectionHashCode(testData, false);

    // Assert
    assertNotEquals(0, result);
  }

  @Test
  public void testReflectionHashCode_withExcludeFields_returnsHashCode() {
    // Arrange
    TestData testData = new TestData("Test", 123);

    // Act
    int result = HashCodeBuilder.reflectionHashCode(testData, true, TestData.class, "id");

    // Assert
    assertNotEquals(0, result);
  }

  @Test
  public void testReflectionHashCode_withNoFields_returnsHashCode() {
    // Arrange
    TestDataNoFields testData = new TestDataNoFields();

    // Act
    int result = HashCodeBuilder.reflectionHashCode(testData, true);

    // Assert
    assertNotEquals(0, result);
  }

  @Test
  public void testAppend_booleanValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(true);

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_booleanArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new boolean[] {true, false});

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_byteValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append((byte) 127);

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_byteArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new byte[] {1, 2, 3});

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_charValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append('a');

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_charArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new char[] {'a', 'b', 'c'});

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_doubleValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(1.23);

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_doubleArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new double[] {1.23, 4.56});

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_floatValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(1.23f);

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_floatArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new float[] {1.23f, 4.56f});

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_intValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(123);

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_intArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new int[] {1, 2, 3});

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_longValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(1234567890L);

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_longArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new long[] {123456, 7890});

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_objectValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append("Test String");

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_objectArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new Object[] {"Test1", "Test2", "Test3"});

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_shortValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append((short) 123);

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testAppend_shortArrayValue_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.append(new short[] {1, 2, 3});

    // Assert
    assertNotEquals(builder, result);
}

  @Test
  public void testAppendSuper_superHashCode_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    HashCodeBuilder result = builder.appendSuper(123);

    // Assert
    assertNotEquals(builder, result);
  }

  @Test
  public void testToHashCode_validBuilder_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    int result = builder.toHashCode();

    // Assert
    assertNotEquals(0, result);
  }

  @Test
  public void testBuild_validBuilder_returnsHashCode() {
    // Arrange
    HashCodeBuilder builder = new HashCodeBuilder();

    // Act
    Integer result = builder.build();

    // Assert
    assertNotNull(result);
  }
}