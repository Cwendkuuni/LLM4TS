import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext contextMock;
    private ScheduleTaskEntity taskEntityMock;
    
    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        
        contextMock = mock(JobExecutionContext.class);
        taskEntityMock = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute() {
        // Setup
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters("dataId");
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        
        when(contextMock.getResult()).thenReturn(taskEntityMock);
        when(taskEntityMock.getParametersJson()).thenReturn(parametersJson);

        // Test
        dataArchiveRollbackJob.execute(contextMock);

        // Verify
        verify(contextMock).getResult();
        verify(taskEntityMock).getParametersJson();
        // Add more verification here based on expected behavior
    }

    // Add more test cases for other public methods if needed

}