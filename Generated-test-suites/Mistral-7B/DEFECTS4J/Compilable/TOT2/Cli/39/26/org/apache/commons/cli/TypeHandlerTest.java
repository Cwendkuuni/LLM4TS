package org.apache.commons.cli;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() throws ParseException {
        String str = "example";
        Object obj = String.class;

        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    // Add test cases for edge cases and exceptions here
}