package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;


@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(any(Long.class))).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(Long.valueOf(1));
        assertNotNull(oscDatabase);
    }

    @Test(expected = PreConditions.EntityNotExistsException.class)
    public void testGetOscDatabaseInfo_EntityNotFoundException() {
        when(databaseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(Long.valueOf(1));
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testSwapTable() {
        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(any(Long.class))).thenReturn(Optional.of(new ScheduleEntity()));
        OscSwapTableVO oscSwapTable = oscService.swapTable(Long.valueOf(1));
        assertNotNull(oscSwapTable);
    }

    @Test(expected = PreConditions.EntityNotExistsException.class)
    @Transactional(rollbackFor = Exception.class)
    public void testSwapTable_EntityNotFoundException() {
        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        oscService.swapTable(Long.valueOf(1));
    }

    @Test
    public void testGetLockUserIsRequired() {
        when(connectionService.getForConnectionSkipPermissionCheck(any(Long.class))).thenReturn(new ConnectionConfig());
        boolean isRequired = oscService.getLockUserIsRequired(Long.valueOf(1));
        assertNotNull(isRequired);
    }
}
