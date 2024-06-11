package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;


/**
 * @Author: Tester
 * @Date: 2023/7/1
 * @Description: Test Class
 */
@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {

        //mocking objects
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = mock(DataArchiveRollbackParameters.class);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        DlmTask taskUnit = mock(DlmTask.class);

        DataArchiveRollbackJob dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());

        //mock methods
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{'dataArchiveTaskId': '123'}");
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class))).thenReturn(rollbackParameters);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("123");
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById("123")).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("[{'sourceDatabaseId': '12', 'targetDatabaseId': '13', 'jobType': 'ARCHIVE', 'status': 'PREPARING'}]");
        when(JsonUtils.fromJson(anyString(), any())).thenReturn(Arrays.asList(taskUnit));
        when(taskUnit.getSourceDatabaseId()).thenReturn(12L);
        when(taskUnit.getTargetDatabaseId()).thenReturn(12L);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt())).thenReturn("111");

        //execute the method
        dataArchiveRollbackJob.execute(context);

        //verify if methods are executed
        verify(taskEntity, atLeastOnce()).getId();
        verify(dataArchiveRollbackJob.scheduleTaskRepository, atLeastOnce()).updateStatusById(anyLong(), any());
    }
}