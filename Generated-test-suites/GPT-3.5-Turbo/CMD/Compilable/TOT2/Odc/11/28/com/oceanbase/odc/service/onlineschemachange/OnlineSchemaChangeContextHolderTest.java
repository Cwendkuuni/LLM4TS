package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongParams() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        // Add assertions here to check the functionality of the method
    }
    
}