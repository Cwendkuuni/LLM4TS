import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {
    
    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
    }
    
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        assertEquals(5, builder.toComparison());
    }
    
    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(3, 5);
        assertEquals(-1, builder.toComparison());
    }
    
    @Test
    public void testAppendLong() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10L, 5L);
        assertEquals(1, builder.toComparison());
    }
    
    @Test
    public void testAppendShort() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((short)2, (short)5);
        assertEquals(-1, builder.toComparison());
    }
    
    @Test
    public void testAppendChar() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append('a', 'b');
        assertEquals(-1, builder.toComparison());
    }
    
    @Test
    public void testAppendByte() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append((byte)3, (byte)3);
        assertEquals(0, builder.toComparison());
    }
    
    @Test
    public void testAppendDouble() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(3.5, 5.2);
        assertEquals(-1, builder.toComparison());
    }
    
    @Test
    public void testAppendFloat() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(3.5f, 5.2f);
        assertEquals(-1, builder.toComparison());
    }
    
    @Test
    public void testAppendBoolean() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(true, false);
        assertEquals(1, builder.toComparison());
    }
    
    @Test
    public void testAppendObjectArray() {
        Object[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(arr1, arr2);
        assertEquals(-1, builder.toComparison());
    }
    
    @Test
    public void testAppendLongArray() {
        long[] arr1 = {1, 2, 3};
        long[] arr2 = {1, 2, 4};
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(arr1, arr2);
        assertEquals(-1, builder.toComparison());
    }

    // Add more test cases for other methods
  
}