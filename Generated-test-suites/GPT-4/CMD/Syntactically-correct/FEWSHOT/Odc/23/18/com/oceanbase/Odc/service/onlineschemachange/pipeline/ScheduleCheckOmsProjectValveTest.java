package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {
    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testInvoke() {
        // TODO: Add implementation for testInvoke
    }

    @Test
    public void testHandleOmsProjectStepResult(){
        // TODO: Add implementation for testHandleOmsProjectStepResult
    }

    @Test
    public void testContinueHandleProjectStepResult(){
        // TODO: Add implementation for testContinueHandleProjectStepResult
    }

    @Test
    public void testListProjectFullVerifyResult(){
        // TODO: Add implementation for testListProjectFullVerifyResult
    }

    @Test
    public void testAdaptResult() {
        // TODO: Add implementation for testAdaptResult
    }

    @Test
    public void testGetProjectRequest() {
        // TODO: Add implementation for testGetProjectRequest
    }

    @Test
    public void testRecordCurrentProgress() {
        // TODO: Add implementation for testRecordCurrentProgress
    }

    @Test
    public void testGetHeader() {
        // TODO: Add implementation for testGetHeader
    }

}