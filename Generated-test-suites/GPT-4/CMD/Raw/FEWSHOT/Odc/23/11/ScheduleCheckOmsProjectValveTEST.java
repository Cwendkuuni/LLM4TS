
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for ScheduleCheckOmsProjectValve.
 */
@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ProjectStepResult projectStepResult;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private Table table;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        // Set up a mock projectStepResult
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);

        // Set up a mock context
        OscValveContext context = mock(OscValveContext.class);

        // Invoke the test method
        scheduleCheckOmsProjectValve.invoke(context);
    }

    @Test
    public void testAdaptResult() {
        // Set up a mock projectStepResult
        when(projectStepResult.getFullTransferEstimatedCount()).thenReturn(100);
        when(projectStepResult.getFullTransferFinishedCount()).thenReturn(50);
        when(projectStepResult.getFullTransferProgressPercentage()).thenReturn(50.0);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(projectStepResult.getCurrentStep()).thenReturn(OmsStepName.PRE_CHECK.toString());
        when(projectStepResult.getCurrentStepStatus()).thenReturn(TaskStatus.RUNNING.name());

        // Set up a mock result
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);

        // Invoke the test method
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        // Verify the results
        assertEquals(TaskStatus.RUNNING.name(), result.getCurrentStepStatus());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Set up a mock table
        DefaultTableFactory defaultTableFactory = mock(DefaultTableFactory.class);
        when(defaultTableFactory.generateTable(5, null, Lists.newArrayList("1", "PRE_CHECK", "RUNNING", "50", "CONSISTENT"))).thenReturn(table);

        // Invoke the test method
        scheduleCheckOmsProjectValve.recordCurrentProgress("1", result);

        // Verify the results
        assertEquals("\n" + table.render().toString() + "\n", log.info());
    }
}
