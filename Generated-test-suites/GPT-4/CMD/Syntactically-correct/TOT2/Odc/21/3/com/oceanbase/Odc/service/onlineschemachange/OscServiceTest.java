
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 123L;
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(anyLong())).thenReturn(database);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        verify(databaseRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 123L;
        Optional<ScheduleTaskEntity> taskEntity = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(taskEntity);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleEntity.getJobParametersJson()).thenReturn("");
        
        when(taskEntity.get().getResultJson()).thenReturn("");
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        when(result.isManualSwapTableEnabled()).thenReturn(true);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertNotNull(result);
        verify(scheduleTaskRepository, times(1)).findById(anyLong());
    }
}
