package org.apache.commons.cli;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object expectedObject = new String("Hello");
        Object actualObject = TypeHandler.createValue("Hello", String.class);
        assertEquals(expectedObject, actualObject);
    }
}