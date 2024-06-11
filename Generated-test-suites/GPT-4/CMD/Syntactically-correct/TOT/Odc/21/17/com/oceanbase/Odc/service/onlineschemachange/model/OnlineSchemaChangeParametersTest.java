package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void isContinueOnErrorTest() {
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(oscp.isContinueOnError());

        oscp.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(oscp.isContinueOnError());

        // Consider to add more tests to cover edge cases 
    }


    @Test
    public void generateSubTaskParametersTest() {
        // Given due to complexity of this method and dependencies with external factors such as ConnectionConfig,
        // it is not feasible to generate a detailed unit test without appropriate mocking resources and 
        // further knowledge about the particular project details.

        // The main purpose of this test would be to verify the correct generation of sub-task parameters, 
        // the correct handling of different SQL commands, and the correct handling of exceptions.
    }
}