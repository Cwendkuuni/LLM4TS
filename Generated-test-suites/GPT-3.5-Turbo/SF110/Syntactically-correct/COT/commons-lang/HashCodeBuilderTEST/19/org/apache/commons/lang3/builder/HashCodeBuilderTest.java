
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean_Null() {
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder result = builder.append((boolean) null);
        int hashCode = result.toHashCode();
        assertEquals(builder, result);
        //Assert the returned hashCode is valid
        //e.g. Call other methods to calculate the expected result and compare it to hashCode
    }
    // Implement other test methods for each public method in the HashCodeBuilder class

}