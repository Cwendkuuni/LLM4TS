package org.apache.commons.cli;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        String validString = "exampleString";
        Object obj = new Object();
        Object expectedValue = "exampleString";

        Object actualValue = TypeHandler.createValue(validString, obj.getClass());
        assertEquals(expectedValue, actualValue);

        Class classType = String.class;
        String validString2 = "anotherExampleString";
        Object actualValue2 = TypeHandler.createValue(validString2, classType);
        assertEquals(validString2, actualValue2);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValueNullString() {
        String nullString = null;
        Object obj = new Object();

        TypeHandler.createValue(nullString, obj.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInvalidClass() {
        String validString = "exampleString";
        Object obj = new Object();

        TypeHandler.createValue(validString, Integer.class);
    }
}