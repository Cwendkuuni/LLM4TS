import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class HashCodeBuilderTest {

  private HashCodeBuilder hashCodeBuilder;

  @Before
  public void setUp() {
    hashCodeBuilder = new HashCodeBuilder();
  }

  @Test
  public void testAppendBoolean() {
    boolean value = true;
    int result = hashCodeBuilder.append(value).toHashCode();
    assertEquals(37, result);
  }

  @Test
  public void testAppendBooleanArray() {
    boolean[] array = {true, false, true};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(70, result);
  }

  @Test
  public void testAppendByte() {
    byte value = 5;
    int result = hashCodeBuilder.append(value).toHashCode();
    assertEquals(47, result);
  }

  @Test
  public void testAppendByteArray() {
    byte[] array = {2, 4, 6};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(42, result);
  }

  @Test
  public void testAppendChar() {
    char value = 'a';
    int result = hashCodeBuilder.append(value).toHashCode();
    assertEquals(30, result);
  }

  @Test
  public void testAppendCharArray() {
    char[] array = {'a', 'b', 'c'};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(130, result);
  }

  @Test
  public void testAppendDouble() {
    double value = 1.5;
    int result = hashCodeBuilder.append(value).toHashCode();
    assertEquals(18, result);
  }

  @Test
  public void testAppendDoubleArray() {
    double[] array = {2.4, 3.7, 5.1};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(144, result);
  }

  @Test
  public void testAppendFloat() {
    float value = 2.5f;
    int result = hashCodeBuilder.append(value).toHashCode();
    assertEquals(47, result);
  }

  @Test
  public void testAppendFloatArray() {
    float[] array = {1.5f, 2.5f, 3.5f};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(167, result);
  }

  @Test
  public void testAppendInt() {
    int value = 10;
    int result = hashCodeBuilder.append(value).toHashCode();
    assertEquals(373, result);
  }

  @Test
  public void testAppendIntArray() {
    int[] array = {1, 2, 3};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(365, result);
  }

  @Test
  public void testAppendLong() {
    long value = 1000L;
    int result = hashCodeBuilder.append(value).toHashCode();
    assertEquals(937, result);
  }

  @Test
  public void testAppendLongArray() {
    long[] array = {1000L, 2000L, 3000L};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(127, result);
  }

  @Test
  public void testAppendNullObject() {
    Object object = null;
    int result = hashCodeBuilder.append(object).toHashCode();
    assertEquals(0, result);
  }

  @Test
  public void testAppendObjectArray() {
    Object[] array = {1, 2, 3};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(113, result);
  }

  @Test
  public void testAppendShort() {
    short value = 5;
    int result = hashCodeBuilder.append(value).toHashCode();
    assertEquals(42, result);
  }

  @Test
  public void testAppendShortArray() {
    short[] array = {1, 2, 3};
    int result = hashCodeBuilder.append(array).toHashCode();
    assertEquals(98, result);
  }

  @Test
  public void testAppendSuper() {
    int superHashCode = 20;
    int result = hashCodeBuilder.appendSuper(superHashCode).toHashCode();
    assertEquals(737, result);
  }

  @Test
  public void testToHashCode() {
    int result = hashCodeBuilder.toHashCode();
    assertEquals(17, result);
  }

  @Test
  public void testBuild() {
    Integer result = hashCodeBuilder.build();
    assertEquals(17, result.intValue());
  }
}