import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testHashCodeBuilderConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        assertNotNull(builder);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(622, builder.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] arr = {true, false, true};
        builder.append(arr);
        assertEquals(1681314432, builder.toHashCode());
    }
    
    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        assertEquals(206, builder.toHashCode());
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] arr = {1, 2, 3};
        builder.append(arr);
        assertEquals(25813, builder.toHashCode());
    }
    
    // Add more test cases for other methods
    
}