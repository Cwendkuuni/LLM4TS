package org.apache.commons.lang3.builder;
import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(0, CompareToBuilder.reflectionCompare(obj1, obj1));
        assertNotEquals(0, CompareToBuilder.reflectionCompare(obj1, obj2));
    }

    @Test
    public void testReflectionCompareWithTransients() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(0, CompareToBuilder.reflectionCompare(obj1, obj1, true));
        assertNotEquals(0, CompareToBuilder.reflectionCompare(obj1, obj2, true));
    }

    // Other test methods for reflectionCompare with different parameters

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        assertEquals(0, builder.appendSuper(5).toComparison());
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        assertEquals(0, builder.append(10, 10).toComparison());
        assertEquals(1, builder.append(10, 5).toComparison());
        assertEquals(-1, builder.append(5, 10).toComparison());
    }

    // Other test methods for different append scenarios

}