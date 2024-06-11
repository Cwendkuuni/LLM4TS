package org.apache.commons.cli;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() throws Exception {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(obj, result);
    }
}