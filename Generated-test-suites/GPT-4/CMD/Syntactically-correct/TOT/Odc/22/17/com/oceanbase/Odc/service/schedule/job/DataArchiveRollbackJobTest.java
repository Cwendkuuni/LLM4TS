package com.oceanbase.odc.service.schedule.job;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test(expected = AnyException.class)
    public void testExecute(){
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        try{
            dataArchiveRollbackJob.execute(context);
        } catch (AnyException e){
            // handle exception
        }
        Mockito.verify(context).getResult();
    }
}