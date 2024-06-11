import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    private HashCodeBuilder hashCodeBuilder;

    @Before
    public void setUp() {
        hashCodeBuilder = new HashCodeBuilder();
    }

    @Test
    public void testAppendBoolean_WhenTrue_ExpectIncreasedTotal() {
        // Arrange
        boolean value = true;
        int expectedTotal = hashCodeBuilder.toHashCode() * hashCodeBuilder.getConstant() + 0;

        // Act
        hashCodeBuilder.append(value);

        // Assert
        assertEquals(expectedTotal, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendBoolean_WhenFalse_ExpectIncreasedTotal() {
        // Arrange
        boolean value = false;
        int expectedTotal = hashCodeBuilder.toHashCode() * hashCodeBuilder.getConstant() + 1;

        // Act
        hashCodeBuilder.append(value);

        // Assert
        assertEquals(expectedTotal, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray_WhenNull_ExpectIncreasedTotal() {
        // Arrange
        boolean[] array = null;
        int expectedTotal = hashCodeBuilder.toHashCode() * hashCodeBuilder.getConstant();

        // Act
        hashCodeBuilder.append(array);

        // Assert
        assertEquals(expectedTotal, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray_WhenEmpty_ExpectIncreasedTotal() {
        // Arrange
        boolean[] array = {};
        int expectedTotal = hashCodeBuilder.toHashCode() * hashCodeBuilder.getConstant();

        // Act
        hashCodeBuilder.append(array);

        // Assert
        assertEquals(expectedTotal, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray_WhenNotEmpty_ExpectIncreasedTotal() {
        // Arrange
        boolean[] array = {true, false, true};
        int expectedTotal = (hashCodeBuilder.toHashCode() * hashCodeBuilder.getConstant() + 0) * hashCodeBuilder.getConstant() + 1;
        expectedTotal = (expectedTotal * hashCodeBuilder.getConstant() + 0) * hashCodeBuilder.getConstant() + 1;

        // Act
        hashCodeBuilder.append(array);

        // Assert
        assertEquals(expectedTotal, hashCodeBuilder.toHashCode());
    }

    // Repeat similar tests for other append methods

}