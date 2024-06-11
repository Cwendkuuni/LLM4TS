import org.junit.Assert;
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
    public void append_boolean_true() {
        boolean value = true;
        int expectedHashCode = 37;
        hashCodeBuilder.append(value);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void append_boolean_false() {
        boolean value = false;
        int expectedHashCode = 38;
        hashCodeBuilder.append(value);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void append_boolean_array_null() {
        boolean[] array = null;
        int expectedHashCode = 0;
        hashCodeBuilder.append(array);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void append_boolean_array_empty() {
        boolean[] array = {};
        int expectedHashCode = 0;
        hashCodeBuilder.append(array);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void append_boolean_array_all_true() {
        boolean[] array = {true, true, true};
        int expectedHashCode = 39;
        hashCodeBuilder.append(array);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void append_boolean_array_all_false() {
        boolean[] array = {false, false, false};
        int expectedHashCode = 40;
        hashCodeBuilder.append(array);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void append_byte() {
        byte value = 127;
        int expectedHashCode = 3209;
        hashCodeBuilder.append(value);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    // More test cases for other methods...
    
    @Test
    public void appendSuper() {
        int superHashCode = 1234;
        int expectedHashCode = superHashCode * 37;
        hashCodeBuilder.appendSuper(superHashCode);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void toHashCode() {
        hashCodeBuilder.append(true).append(127);
        int expectedHashCode = ((37 + 1) * 37 + 127) * 37;
        int actualHashCode = hashCodeBuilder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void build() {
        Integer expectedHashCode = hashCodeBuilder.toHashCode();
        Integer actualHashCode = hashCodeBuilder.build();
        assertEquals(expectedHashCode, actualHashCode);
    }
}