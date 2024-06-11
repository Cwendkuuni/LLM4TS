
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        HashCodeBuilder result = hcb.append(true);
        assertEquals(37, result.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        HashCodeBuilder result = hcb.append(array);
        assertEquals(1189, result.toHashCode());
    }

    // Other test cases for different methods...

}
