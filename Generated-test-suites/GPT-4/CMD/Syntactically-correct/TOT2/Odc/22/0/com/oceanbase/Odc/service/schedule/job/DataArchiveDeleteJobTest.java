package com.oceanbase.odc.service.schedule.job;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.schedule.job.*;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.quartz.JobExecutionContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    private ScheduleTaskEntity mockedEntity;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    public void testExecute() {
        when(mockedEntity.getParametersJson()).thenReturn("jsonData");

        when(context.getResult()).thenReturn(mockedEntity);
        
        // Assume that json parsing will produce this result
        when(JsonUtils.fromJson(any(String.class), eq(DataArchiveClearParameters.class))).thenReturn(dataArchiveClearParameters);

        // Trying to run the method
        dataArchiveDeleteJob.execute(context);
        
        // Now checking if json parsing was attempted
        verify(JsonUtils, times(1)).fromJson(any(String.class), eq(DataArchiveClearParameters.class));
        
        // and if getting the result in the context did occur
        verify(context, times(1)).getResult();       
    }
}