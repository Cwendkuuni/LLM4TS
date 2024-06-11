package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {
    
    @Test
    public void testReflectionHashCodeWithNullObject() {
        int hashCode = HashCodeBuilder.reflectionHashCode(0, 0, null);
        assertEquals(0, hashCode);
    }
    
    @Test
    public void testAppendBooleanValue() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertEquals(37, hashCode);
    }
    
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = {1, 2, 3};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(1110596, hashCode);
    }
    
    // Add more test cases for other methods and edge cases
    
    @Test
    public void testAppendWithNullObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((Object)null);
        int hashCode = builder.toHashCode();
        assertEquals(37, hashCode);
    }
    
    @Test
    public void testAppendWithArrayContainingNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = {1, null, 3};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(5015, hashCode);
    }
    
    
    
    // Include all the individual test cases here
    
    
}