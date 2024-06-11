package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        oscService.databaseRepository = Mockito.mock(DatabaseRepository.class);

        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        Mockito.when(oscService.databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        assertEquals(databaseEntity.getDatabaseId(), oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    @Transactional
    public void testSwapTable() {
        OscService oscService = new OscService();
        oscService.scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        oscService.scheduleRepository = Mockito.mock(ScheduleRepository.class);

        long scheduleTaskId = 1L;
        OscSwapTableVO expectedOscSwapTable = new OscSwapTableVO();
        expectedOscSwapTable.setScheduleTaskId(scheduleTaskId);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("1");
        Mockito.when(oscService.scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        Mockito.when(oscService.scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        Mockito.when(oscService.scheduleTaskRepository.updateTaskResult(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);

        assertEquals(expectedOscSwapTable.getScheduleTaskId(), oscSwapTableVO.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }
}