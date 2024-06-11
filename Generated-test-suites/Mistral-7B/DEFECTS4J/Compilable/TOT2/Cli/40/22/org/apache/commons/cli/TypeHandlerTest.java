package org.apache.commons.cli;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() throws ParseException {
        Object obj = new Object();
        String str = "test";
        Object actual = TypeHandler.createValue(str, obj);
        Object expected = str; // Assuming the argument object is not modified in this test
        assertEquals(expected, actual);
    }
}