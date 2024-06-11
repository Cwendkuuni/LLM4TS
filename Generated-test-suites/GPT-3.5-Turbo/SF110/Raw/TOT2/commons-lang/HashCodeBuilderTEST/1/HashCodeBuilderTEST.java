import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        int expectedHashCode = 123456789;
        Object testObject = new Object();
        
        int actualHashCode = HashCodeBuilder.reflectionHashCode(17, 37, testObject);
        
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object testObject = new Object();
        int expectedHashCode = testObject.hashCode();

        HashCodeBuilder actualBuilder = builder.append(testObject);
        int actualHashCode = actualBuilder.toHashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    // Add more test cases for other public methods

}