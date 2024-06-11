package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode_ObjectNotNull() {
        // Arrange
        Object object = new Object();
        
        // Act
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        
        // Assert
        assertNotNull(hashCode);
    }

    @Test
    public void testReflectionHashCode_ObjectNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            HashCodeBuilder.reflectionHashCode(null, false);
        });
    }

    @Test
    public void testAppendBoolean() {
        // Arrange
        HashCodeBuilder builder = new HashCodeBuilder();
        
        // Act
        HashCodeBuilder result = builder.append(true);
        
        // Assert
        assertNotNull(result);
    }

    // Additional test cases for other methods follow...

}