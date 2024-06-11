import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int result = builder.toHashCode();
        assertEquals(17, result);
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int result = builder.toHashCode();
        assertEquals(37, result);
    }
    
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[] {true, false});
        int result = builder.toHashCode();
        assertEquals(1353, result);
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 10);
        int result = builder.toHashCode();
        assertEquals(359, result);
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[] {1, 2, 3});
        int result = builder.toHashCode();
        assertEquals(7126, result);
    }
    
    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int result = builder.toHashCode();
        assertEquals(907, result);
    }
    
    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[] {'a', 'b', 'c'});
        int result = builder.toHashCode();
        assertEquals(41537, result);
    }
    
    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(3.14);
        int result = builder.toHashCode();
        assertEquals(3039, result);
    }
    
    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[] {1.1, 2.2, 3.3});
        int result = builder.toHashCode();
        assertEquals(4076391, result);
    }
    
    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.23f);
        int result = builder.toHashCode();
        assertEquals(47771, result);
    }
    
    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[] {1.1f, 2.2f, 3.3f});
        int result = builder.toHashCode();
        assertEquals(6074413, result);
    }
    
    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(100);
        int result = builder.toHashCode();
        assertEquals(3767, result);
    }
    
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[] {1, 2, 3});
        int result = builder.toHashCode();
        assertEquals(7126, result);
    }
    
    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1000000000L);
        int result = builder.toHashCode();
        assertEquals(5704946, result);
    }
    
    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[] {1000000000L, 2000000000L, 3000000000L});
        int result = builder.toHashCode();
        assertEquals(1738433870, result);
    }
    
    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object());
        int result = builder.toHashCode();
        assertEquals(17, result);
    }
    
    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[] {1, 2, 3});
        int result = builder.toHashCode();
        assertEquals(7126, result);
    }
    
    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 10);
        int result = builder.toHashCode();
        assertEquals(367, result);
    }
    
    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[] {1, 2, 3});
        int result = builder.toHashCode();
        assertEquals(7126, result);
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(12345);
        int result = builder.toHashCode();
        assertEquals(443874, result);
    }
    
    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer result = builder.build();
        assertEquals(17, result.intValue());
    }
    
    @Test
    public void testHashCodeEquals() {
        HashCodeBuilder builder1 = new HashCodeBuilder();
        HashCodeBuilder builder2 = new HashCodeBuilder();
        
        builder1.append(1);
        builder2.append(1);
        
        int hashCode1 = builder1.toHashCode();
        int hashCode2 = builder2.toHashCode();
        
        assertTrue(builder1.equals(builder2));
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCodeNotEquals() {
        HashCodeBuilder builder1 = new HashCodeBuilder();
        HashCodeBuilder builder2 = new HashCodeBuilder();
        
        builder1.append(1);
        builder2.append(2);
        
        int hashCode1 = builder1.toHashCode();
        int hashCode2 = builder2.toHashCode();
        
        assertFalse(builder1.equals(builder2));
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testReflectionHashCode() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(obj);
        assertEquals(obj.hashCode(), hashCode);
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(obj, new String[] {"field1", "field2"});
        assertEquals(obj.hashCode(), hashCode);
    }
    
    @Test
    public void testReflectionHashCodeWithReflectUpToClass() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(obj, Object.class);
        assertEquals(obj.hashCode(), hashCode);
    }
    
    @Test
    public void testReflectionHashCodeWithTestTransients() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(obj, true);
        assertEquals(obj.hashCode(), hashCode);
    }
    
    @Test
    public void testIsRegistered() {
        Object obj = new Object();
        boolean isRegistered = HashCodeBuilder.isRegistered(obj);
        assertFalse(isRegistered);
    }
    
    @Test
    public void testRegister() {
        Object obj = new Object();
        HashCodeBuilder.register(obj);
        boolean isRegistered = HashCodeBuilder.isRegistered(obj);
        assertTrue(isRegistered);
    }
    
    @Test
    public void testUnregister() {
        Object obj = new Object();
        HashCodeBuilder.unregister(obj);
        boolean isRegistered = HashCodeBuilder.isRegistered(obj);
        assertFalse(isRegistered);
    }
    
}