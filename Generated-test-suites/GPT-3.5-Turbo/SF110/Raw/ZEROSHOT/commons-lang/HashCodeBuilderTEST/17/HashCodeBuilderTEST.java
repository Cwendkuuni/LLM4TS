import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {
  
  @Test
  public void testHashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    
    // Test initial hash code
    int expected = 17;
    int actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append boolean
    builder.append(true);
    expected = 53;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append boolean array
    builder.append(new boolean[] {true, false});
    expected = 97;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append byte
    builder.append((byte) 5);
    expected = 3705;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append byte array
    builder.append(new byte[] {1, 2, 3});
    expected = 981533625;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append char
    builder.append('a');
    expected = 35313232;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append char array
    builder.append(new char[] {'a', 'b', 'c'});
    expected = 996763137;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append double
    builder.append(3.14);
    expected = 83325413;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append double array
    builder.append(new double[] {3.14, 1.23});
    expected = -1905589855;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append float
    builder.append(1.23f);
    expected = 48634256;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append float array
    builder.append(new float[] {3.14f, 1.23f});
    expected = -642685639;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append int
    builder.append(10);
    expected = 18178328;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append int array
    builder.append(new int[] {1, 2, 3});
    expected = -468987698;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append long
    builder.append(1000000000L);
    expected = 157037097;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append long array
    builder.append(new long[] {100000000L, 200000000L});
    expected = -140497245;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append Object (null)
    builder.append((Object) null);
    expected = -140497245;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append Object array
    builder.append(new Object[] {null, "Hello", 3.14});
    expected = 794369147;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append short
    builder.append((short) 20);
    expected = -235678383;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test append short array
    builder.append(new short[] {10, 20, 30});
    expected = -66105413;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
    
    // Test appendSuper
    builder.appendSuper(123456789);
    expected = -1261235830;
    actual = builder.toHashCode();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testBuilder() {
    HashCodeBuilder builder = new HashCodeBuilder();
    
    int expected = builder.toHashCode();
    int actual = builder.build();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testReflectionHashCode() {
    int expected = HashCodeBuilder.reflectionHashCode(123, 456, new Object());
    int actual = HashCodeBuilder.reflectionHashCode(123, 456, new Object(), false);
    assertEquals(expected, actual);
    
    expected = HashCodeBuilder.reflectionHashCode(new Object(), true);
    actual = HashCodeBuilder.reflectionHashCode(new Object(), true, null);
    assertEquals(expected, actual);
    
    expected = HashCodeBuilder.reflectionHashCode(new Object(), Arrays.asList("field1", "field2"));
    actual = HashCodeBuilder.reflectionHashCode(new Object(), "field1", "field2");
    assertEquals(expected, actual);
  }
}