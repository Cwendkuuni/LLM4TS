import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objResult = new ObjectResult("TestSchema", "TestName", "TestType");
        
        assertEquals("TestSchema", objResult.getSchema());
        assertEquals("TestName", objResult.getName());
        assertEquals("TestType", objResult.getType());
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectStatus objStatus = new ObjectStatus("TestName", new AtomicLong(5), new AtomicLong(10), "TestType");
        ObjectResult objResult = ObjectResult.of(objStatus);
        
        assertEquals("TestName", objResult.getName());
        assertEquals(5, objResult.getCount().get());
        assertEquals(10, objResult.getTotal().get());
        assertEquals("TestType", objResult.getType());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult("TestSchema", "TestName", "TestType");
        
        assertEquals("TestSchema.TestName[TestType]", objResult.getSummary());
    }
    
    @Test
    public void testConstructorEdgeCases() {
        // Null values for schema, name, and type
        ObjectResult objResult = new ObjectResult(null, null, null);
        
        assertNull(objResult.getSchema());
        assertNull(objResult.getName());
        assertNull(objResult.getType());
    }
    
    @Test
    public void testObjectResultOfNullObjectStatus() {
        ObjectResult objResult = ObjectResult.of(null);
        
        assertNull(objResult.getName());
        assertEquals(0, objResult.getCount().get());
        assertEquals(0, objResult.getTotal().get());
        assertNull(objResult.getType());
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetSummaryExceptionHandling() {
        ObjectResult objResult = new ObjectResult(null, null, null);
        objResult.getSummary();
    }
}