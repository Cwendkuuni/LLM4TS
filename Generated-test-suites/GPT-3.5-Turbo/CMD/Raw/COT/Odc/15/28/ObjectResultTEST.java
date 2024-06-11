
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objResult.getSchema());
        assertEquals("name", objResult.getName());
        assertEquals("type", objResult.getType());
        assertNotNull(objResult.getCount());
        assertNotNull(objResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult objResult = ObjectResult.of(objStatus);
       
        assertEquals("name", objResult.getName());
        assertNotNull(objResult.getCount());
        assertNotNull(objResult.getTotal());
        assertNotNull(objResult.getStatus());
        assertNotNull(objResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult();
        objResult.setSchema("schema");
        objResult.setName("name");
        objResult.setType("type");
        
        assertEquals("schema.name[type]", objResult.getSummary());
    }
}
