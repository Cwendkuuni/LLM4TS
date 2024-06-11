import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void appendTo_withAppendableAndIterable_shouldReturnAppendableObjectWithPartsAppended() {
        // Arrange
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        // Act
        try {
            joiner.appendTo(builder, Arrays.asList("apple", "banana", "grape"));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Assert
        assertEquals("apple,banana,grape", builder.toString());
    }

    @Test
    public void appendTo_withAppendableAndIterator_shouldReturnAppendableObjectWithPartsAppended() {
        // Arrange
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        // Act
        try {
            joiner.appendTo(builder, Arrays.asList("apple", "banana", "grape").iterator());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Assert
        assertEquals("apple,banana,grape", builder.toString());
    }
    
    @Test
    public void appendTo_withAppendableAndObjectArray_shouldReturnAppendableObjectWithPartsAppended() {
        // Arrange
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        // Act
        try {
            joiner.appendTo(builder, new Object[]{"apple", "banana", "grape"});
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Assert
        assertEquals("apple,banana,grape", builder.toString());
    }
    
    @Test
    public void appendTo_withAppendableAndObjects_shouldReturnAppendableObjectWithPartsAppended() {
        // Arrange
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        // Act
        try {
            joiner.appendTo(builder, "apple", "banana", "grape");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Assert
        assertEquals("apple,banana,grape", builder.toString());
    }
    
    @Test
    public void appendTo_withStringBuilderAndIterable_shouldReturnStringBuilderWithPartsAppended() {
        // Arrange
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        // Act
        joiner.appendTo(builder, Arrays.asList("apple", "banana", "grape"));

        // Assert
        assertEquals("apple,banana,grape", builder.toString());
    }
    
    @Test
    public void appendTo_withStringBuilderAndIterator_shouldReturnStringBuilderWithPartsAppended() {
        // Arrange
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        // Act
        joiner.appendTo(builder, Arrays.asList("apple", "banana", "grape").iterator());

        // Assert
        assertEquals("apple,banana,grape", builder.toString());
    }
    
    @Test
    public void appendTo_withStringBuilderAndObjectArray_shouldReturnStringBuilderWithPartsAppended() {
        // Arrange
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        // Act
        joiner.appendTo(builder, new Object[]{"apple", "banana", "grape"});

        // Assert
        assertEquals("apple,banana,grape", builder.toString());
    }
    
    @Test
    public void appendTo_withStringBuilderAndObjects_shouldReturnStringBuilderWithPartsAppended() {
        // Arrange
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        // Act
        joiner.appendTo(builder, "apple", "banana", "grape");

        // Assert
        assertEquals("apple,banana,grape", builder.toString());
    }
    
    @Test
    public void join_withIterable_shouldReturnJoinedString() {
        // Arrange
        Joiner joiner = Joiner.on(",");

        // Act
        String result = joiner.join(Arrays.asList("apple", "banana", "grape"));

        // Assert
        assertEquals("apple,banana,grape", result);
    }
    
    @Test
    public void join_withIterator_shouldReturnJoinedString() {
        // Arrange
        Joiner joiner = Joiner.on(",");

        // Act
        String result = joiner.join(Arrays.asList("apple", "banana", "grape").iterator());

        // Assert
        assertEquals("apple,banana,grape", result);
    }
    
    @Test
    public void join_withObjectArray_shouldReturnJoinedString() {
        // Arrange
        Joiner joiner = Joiner.on(",");

        // Act
        String result = joiner.join(new Object[]{"apple", "banana", "grape"});

        // Assert
        assertEquals("apple,banana,grape", result);
    }
    
    @Test
    public void join_withObjects_shouldReturnJoinedString() {
        // Arrange
        Joiner joiner = Joiner.on(",");

        // Act
        String result = joiner.join("apple", "banana", "grape");

        // Assert
        assertEquals("apple,banana,grape", result);
    }
    
    @Test
    public void useForNull_shouldReturnJoinerWithChangedNullText() {
        // Arrange
        Joiner joiner = Joiner.on(",").useForNull("NULL");

        // Act
        String result = joiner.join("apple", null, "banana", "grape");

        // Assert
        assertEquals("apple,NULL,banana,grape", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void useForNull_calledTwice_shouldThrowUnsupportedOperationException() {
        // Arrange
        Joiner joiner = Joiner.on(",").useForNull("NULL");

        // Act
        joiner.useForNull("dummy");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void skipNulls_calledTwice_shouldThrowUnsupportedOperationException() {
        // Arrange
        Joiner joiner = Joiner.on(",").skipNulls();

        // Act
        joiner.skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void withKeyValueSeparator_calledWithSkipNulls_shouldThrowUnsupportedOperationException() {
        // Arrange
        Joiner joiner = Joiner.on(",").skipNulls();

        // Act
        joiner.withKeyValueSeparator(":");
    }
    
    // Missing tests: MapJoiner

}