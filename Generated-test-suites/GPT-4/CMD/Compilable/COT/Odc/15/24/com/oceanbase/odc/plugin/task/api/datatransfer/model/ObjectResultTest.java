
package com.oceanbase.odc.plugin.task.api.datatransfer.model.test;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    // Test constructor with valid input 
    @Test
    public void constructorTest() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        Assert.assertEquals("schema1", result.getSchema());
        Assert.assertEquals("name1", result.getName());
        Assert.assertEquals("type1", result.getType());
    }

    // Test 'of' method with valid input
    @Test
    public void ofTest() {
        ObjectStatus status = new ObjectStatus("name1", new AtomicLong(0), new AtomicLong(0), null);
        status.setSchema("schema1");
        status.setType("type1");
        ObjectResult result = ObjectResult.of(status);
        Assert.assertEquals(result, status);
    }

    // Test 'getSummary' method
    @Test
    public void getSummaryTest() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        Assert.assertEquals("schema1.name1[type1]", result.getSummary());
    }

    // Test case for 'of' method : 'that' parameter is null
    @Test(expected = NullPointerException.class)
    public void ofTestWithNullParameter() {
        ObjectResult.of(null);
    }

    // Test case for 'getSummary' method: schema is null
    @Test
    public void getSummaryWithNullSchema() {
        ObjectResult result = new ObjectResult(null, "name1", "type1");
        Assert.assertEquals("null.name1[type1]", result.getSummary());
    }

    // Test case for 'getSummary' method: name is null
    @Test
    public void getSummaryWithNullName() {
        ObjectResult result = new ObjectResult("schema1", null, "type1");
        Assert.assertEquals("schema1.null[type1]", result.getSummary());
    }

    // Test case for 'getSummary' method: type is null
    @Test
    public void getSummaryWithNullType() {
        ObjectResult result = new ObjectResult("schema1", "name1", null);
        Assert.assertEquals("schema1.name1[null]", result.getSummary());
    }
}
