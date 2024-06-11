package com.oceanbase.odc.service.schedule.job;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class DataArchiveRollbackJobTest {
    
    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;
            
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        scheduleTaskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute(){
        //given
        scheduleTaskEntity.setId(1);
        given(scheduleTaskRepository.findById(1)).willReturn(Optional.of(scheduleTaskEntity));
        
        //when
        dataArchiveRollbackJob.execute(context);

        //then
        verify(scheduleTaskRepository).findById(1);
       
    }

    @Test
    public void testExecuteWithNullId(){
        //given
        scheduleTaskEntity.setId(null);
        given(scheduleTaskRepository.findById(null)).willReturn(Optional.empty());

        //when
        dataArchiveRollbackJob.execute(context);

        //then
        verify(scheduleTaskRepository, never()).findById(null);
    }
   
}