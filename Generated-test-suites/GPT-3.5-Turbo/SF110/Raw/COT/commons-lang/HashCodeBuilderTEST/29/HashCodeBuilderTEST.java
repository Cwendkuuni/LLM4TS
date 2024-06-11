import static org.junit.Assert.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode_1() {
        //Generate test case for reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object)
        //with all parameters set to 0
        int actual = HashCodeBuilder.reflectionHashCode(0, 0, null);
        int expected = 17;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReflectionHashCode_2() {
        //Generate test case for reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, 
        //boolean testTransients) with all parameters set to 0
        int actual = HashCodeBuilder.reflectionHashCode(0, 0, null, false);
        int expected = 17;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReflectionHashCode_3() {
        //Generate test case for reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object,
        //boolean testTransients, Class<? super T> reflectUpToClass, String... excludeFields) with all parameters set to 0
        int actual = HashCodeBuilder.reflectionHashCode(0, 0, null, false, null, new String[0]);
        int expected = 17;
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCode_4() {
        //Generate test case for reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, 
        //boolean testTransients) with null object
        HashCodeBuilder.reflectionHashCode(17, 37, null, false);
    }
    
    @Test
    public void testReflectionHashCode_5() {
        //Generate test case for reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, 
        //boolean testTransients) with object of type Integer
        Integer obj = new Integer(42);
        int actual = HashCodeBuilder.reflectionHashCode(17, 37, obj, false);
        int expected = 37 * 17 + obj.hashCode();
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilder_1() {
        //Generate test case for HashCodeBuilder(int initialOddNumber, int multiplierOddNumber) with initialOddNumber = 0
        new HashCodeBuilder(0, 37);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilder_2() {
        //Generate test case for HashCodeBuilder(int initialOddNumber, int multiplierOddNumber) with multiplierOddNumber = 0
        new HashCodeBuilder(17, 0);
    }
    
    @Test
    public void testAppend_1() {
        //Generate test case for append(boolean value) with value = true
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder actual = builder.append(true);
        HashCodeBuilder expected = new HashCodeBuilder(37, 17).append(0);
        assertEquals(expected.toHashCode(), actual.toHashCode());
    }
    
    @Test
    public void testAppend_2() {
        //Generate test case for append(boolean value) with value = false
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder actual = builder.append(false);
        HashCodeBuilder expected = new HashCodeBuilder(37, 17).append(1);
        assertEquals(expected.toHashCode(), actual.toHashCode());
    }
    
    @Test
    public void testAppend_3() {
        //Generate test case for append(boolean[] array) with array = null
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder actual = builder.append((boolean[]) null);
        HashCodeBuilder expected = new HashCodeBuilder(37, 17);
        assertEquals(expected.toHashCode(), actual.toHashCode());
    }
    
    @Test
    public void testAppend_4() {
        //Generate test case for append(boolean[] array) with array = {true, false}
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder actual = builder.append(new boolean[] {true, false});
        HashCodeBuilder expected = new HashCodeBuilder(37, 17).append(true).append(false);
        assertEquals(expected.toHashCode(), actual.toHashCode());
    }
    
    @Test
    public void testAppend_5() {
        //Generate test case for append(byte value) with value = 42
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder actual = builder.append((byte) 42);
        HashCodeBuilder expected = new HashCodeBuilder(37, 17).append(42);
        assertEquals(expected.toHashCode(), actual.toHashCode());
    }
    
    @Test
    public void testAppend_6() {
        //Generate test case for append(byte[] array) with array = null
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder actual = builder.append((byte[]) null);
        HashCodeBuilder expected = new HashCodeBuilder(37, 17);
        assertEquals(expected.toHashCode(), actual.toHashCode());
    }
    
    @Test
    public void testAppend_7() {
        //Generate test case for append(byte[] array) with array = {1, 2, 3}
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder actual = builder.append(new byte[] {1, 2, 3});
        HashCodeBuilder expected = new HashCodeBuilder(37, 17).append((byte) 1).append((byte) 2).append((byte) 3);
        assertEquals(expected.toHashCode(), actual.toHashCode());
    }
    
    //Continue generating test cases for all methods
    
}