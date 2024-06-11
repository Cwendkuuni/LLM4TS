package org.apache.commons.cli;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object expected = new String("test");
        Object actual = TypeHandler.createValue("test", String.class);
        assertEquals(expected, actual);
    }
}