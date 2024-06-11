import org.junit.*;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    // Test cases for append() methods
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(1231, builder.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArrayNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((boolean[]) null);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendBooleanArrayEmpty() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[0]);
        assertEquals(17, builder.toHashCode());
    }
    
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        assertEquals(54, builder.toHashCode());
    }
    
    // ... more test cases for append() methods
    
    // Test cases for reflectionHashCode() methods
    
    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeNullObject() {
        HashCodeBuilder.reflectionHashCode(17, 37, null);
    }
    
    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object object = new CustomClass();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object, false, null, "field2");
        assertEquals(512, hashCode);
    }
    
    // ... more test cases for reflectionHashCode() methods
    
}