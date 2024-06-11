package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.MDC;
import java.util.HashMap;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OnlineSchemaChangeContextHolderTest {

    private final Long userIdLong = 123L;
    private final Long taskIdLong = 456L;
    private final Long orgIdLong = 789L;
  
    private final String userIdString = "123";
    private final String taskIdString = "456";
    private final String orgIdString = "789";

    @Before
    public void setUp() {
        this.clearMDCCtx();
    }

    @After
    public void tearDown() {
        this.clearMDCCtx();
    }

    @Test
    public void test01_TraceWithLongParams() {
        OnlineSchemaChangeContextHolder.trace(userIdLong, taskIdLong, orgIdLong);
        assertEquals(userIdLong.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdLong.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgIdLong.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void test02_TraceWithStringParams() {
        OnlineSchemaChangeContextHolder.trace(userIdString, taskIdString, orgIdString);
        assertEquals(userIdString, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdString, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgIdString, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void test03_Retrace() {
        HashMap<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userIdString);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskIdString);
        context.put(OdcConstants.ORGANIZATION_ID, orgIdString);
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(userIdString, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdString, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgIdString, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void test04_Clear() {
        OnlineSchemaChangeContextHolder.trace(userIdLong, taskIdLong, orgIdLong);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    private void clearMDCCtx() {
        MDC.clear();
    }
}
