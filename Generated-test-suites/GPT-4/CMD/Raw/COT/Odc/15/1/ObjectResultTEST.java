import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objRslt = new ObjectResult("schema1", "name1", "type1");
        assertNotNull(objRslt);
        assertEquals("schema1", objRslt.getSchema());
        assertEquals("name1", objRslt.getName());
        assertEquals("type1", objRslt.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), "status1");
        objStatus.setType("type1");
        objStatus.setSchema("schema1");
        ObjectResult objRslt = ObjectResult.of(objStatus);
        assertNotNull(objRslt);
        assertEquals("schema1", objRslt.getSchema());
        assertEquals("name1", objRslt.getName());
        assertEquals("type1", objRslt.getType());
        assertEquals("status1", objRslt.getStatus());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objRslt = new ObjectResult("schema1", "name1", "type1");
        String summary = objRslt.getSummary();
        assertEquals("schema1.name1[type1]", summary);
    }

    @Test
    public void testObjectResultConstructorWithNull() {
        ObjectResult objRslt = new ObjectResult(null, null, null);
        assertNotNull(objRslt);
        assertNull(objRslt.getSchema());
        assertNull(objRslt.getName());
        assertNull(objRslt.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultOfWithNull() {
        ObjectResult.of(null);
    }

    @Test
    public void testGetSummaryWithNull() {
        ObjectResult objRslt = new ObjectResult(null, null, null);
        String summary = objRslt.getSummary();
        assertEquals(".[null]", summary);
    }
}