package com.oceanbase.odc.service.flow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlowableAdaptorImplTest {

    @Test
    void testSetProcessInstanceId() {
        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        try {
            flowableAdaptorImpl.setProcessInstanceId(Long.valueOf(1), "ProcessId123456");
        } catch (Exception e) {
            fail("Exception is not expected!");
        }
    }

    @Test
    void testGetFlowInstanceIdByProcessInstanceId() {
        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("InvalidProcessId");
        assertFalse(result.isPresent());
    }
    //... other tests
}