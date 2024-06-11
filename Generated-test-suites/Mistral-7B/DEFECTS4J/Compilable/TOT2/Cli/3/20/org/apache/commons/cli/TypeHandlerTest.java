package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.TypeHandler;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "testString";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }
}