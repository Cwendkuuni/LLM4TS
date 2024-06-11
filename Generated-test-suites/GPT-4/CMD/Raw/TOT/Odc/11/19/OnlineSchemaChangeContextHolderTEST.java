import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest{

    @Before
    public void setUp(){
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown(){
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithString(){
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithLong(){
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace(){
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "321");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "654");
        context.put(OdcConstants.ORGANIZATION_ID, "987");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("321", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("654", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("987", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear(){
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidString(){
        OnlineSchemaChangeContextHolder.trace("invalid", "456", "789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceWithNullString(){
        OnlineSchemaChangeContextHolder.trace(null, "456", "789");
    }
}