
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilderConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertEquals(37, hashCode);
    }

    @Test
    public void testAppendArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        builder.append(array);
        int hashCode = builder.toHashCode();
        assertEquals(2027045, hashCode);
    }

    // Additional test cases for other methods can be added here

}
