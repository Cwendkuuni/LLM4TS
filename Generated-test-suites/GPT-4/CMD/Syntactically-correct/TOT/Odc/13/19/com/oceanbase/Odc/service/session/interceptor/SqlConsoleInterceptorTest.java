package com.oceanbase.odc.service.session.interceptor;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ExampleTest {
    Example example = new Example();

    @Test
    public void testAdd() {
        assertEquals("Test failed!", 3, example.add(1, 2));
    }

    @Test
    public void testSubtract() {
        assertEquals("Test failed!", 1, example.subtract(3, 2));
    }
}