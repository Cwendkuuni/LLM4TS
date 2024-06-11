package com.oceanbase.odc.service.flow.task;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ClassNameTest {
    @Test
    public void testAdd() {
        ClassName object = new ClassName();
        assertEquals(5, object.add(2, 3));
        assertEquals(-1, object.add(-2, 1));
        assertEquals(0, object.add(2, -2));
        assertEquals(-5, object.add(-2, -3));
    }
}