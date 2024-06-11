import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        // Test when lhs and rhs are equal
        assertEquals(0, CompareToBuilder.reflectionCompare("test", "test"));
        
        // Test when lhs and rhs are different
        assertEquals(1, CompareToBuilder.reflectionCompare("test1", "test2"));
    }
    
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        assertEquals(5, builder.toComparison());
    }
    
    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        
        // Test when lhs and rhs are equal
        builder.append("test", "test");
        assertEquals(0, builder.toComparison());
        
        // Test when lhs is null
        builder.append(null, "test");
        assertEquals(-1, builder.toComparison());
    }
    
    @Test
    public void testAppendLong() {
        CompareToBuilder builder = new CompareToBuilder();
        
        // Test when lhs is less than rhs
        builder.append(5L, 10L);
        assertEquals(-1, builder.toComparison());
        
        // Test when lhs is greater than rhs
        builder.append(10L, 5L);
        assertEquals(1, builder.toComparison());
    }
    
    // Additional test cases for other append methods can be added here
    
}