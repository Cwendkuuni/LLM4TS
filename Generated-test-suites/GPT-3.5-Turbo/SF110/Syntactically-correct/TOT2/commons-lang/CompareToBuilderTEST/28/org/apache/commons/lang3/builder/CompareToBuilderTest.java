package org.apache.commons.lang3.builder;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class CompareToBuilderTest {
    
    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
    }
    
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.appendSuper(5);
        assertEquals(5, result.toComparison());
    }
    
    @Test
    public void testAppendObject() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.append("abc", "abc");
        assertEquals(0, result.toComparison());
    }
    
    @Test
    public void testAppendArrays() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        assertEquals(0, result.toComparison());
    }
    
    @Test
    public void testToComparison() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10, 5);
        int result = builder.toComparison();
        assertEquals(1, result);
    }
    
    // Add more test cases for other methods here
    
}