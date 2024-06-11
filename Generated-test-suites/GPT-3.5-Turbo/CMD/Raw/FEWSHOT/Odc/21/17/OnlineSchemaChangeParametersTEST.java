package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params1 = new OnlineSchemaChangeParameters();
        params1.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params1.isContinueOnError());

        OnlineSchemaChangeParameters params2 = new OnlineSchemaChangeParameters();
        params2.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params2.isContinueOnError());
    }

}