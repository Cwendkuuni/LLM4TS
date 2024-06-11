
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {
    
    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTaskOption;

    @Mock
    private DlmTask taskUnit;
    
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }
    
    @Test
    public void testExecute() {
        List<DlmTask> taskUnits= new ArrayList<DlmTask>();
        taskUnits.add(taskUnit);
        when(dataArchiveTaskOption.get()).thenReturn(taskEntity);
        when(taskEntity.getResultJson()).thenReturn(taskUnits);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveRollbackJob.execute(context);
        
        verify(taskEntity).setId(any(Long.class));
        verify(taskEntity).setSourceDatabaseId(any(Long.class));
        verify(taskEntity).setTargetDatabaseId(any(Long.class));
        verify(taskEntity).setJobType(JobType.ROLLBACK);
        verify(taskEntity).setStatus(TaskStatus.DONE);
    }
    
    @Test
    public void testExecuteNoDataArchiveTask() {
        when(dataArchiveTaskOption.get()).thenReturn(null);
        
        dataArchiveRollbackJob.execute(context);
        
        verify(taskEntity).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
    }
}
