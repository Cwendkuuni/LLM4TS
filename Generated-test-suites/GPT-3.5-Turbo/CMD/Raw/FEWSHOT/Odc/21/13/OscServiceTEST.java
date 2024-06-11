package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class OscServiceTest {

    private OscService oscService;

    @Before
    public void setUp() {
        oscService = new OscService();
        oscService.databaseRepository = mock(DatabaseRepository.class);
        oscService.connectionService = mock(ConnectionService.class);
        oscService.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        oscService.scheduleRepository = mock(ScheduleRepository.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(oscService.databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertNotNull(oscDatabaseInfo);
        assertEquals(1L, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(oscService.scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");
        when(oscService.scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(oscService.scheduleTaskRepository.updateTaskResult(scheduleTaskId, JsonUtils.toJson(result))).thenAnswer(returnsFirstArg());

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);
        assertNotNull(oscSwapTableVO);
        assertEquals(scheduleTaskId, oscSwapTableVO.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        oscService.connectionService = mock(ConnectionService.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(oscService.connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

        OscDBUserUtil oscDBUserUtil = mock(OscDBUserUtil.class);
        when(oscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), any())).thenReturn(true);

        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);
        assertTrue(lockUserRequired);
    }
}