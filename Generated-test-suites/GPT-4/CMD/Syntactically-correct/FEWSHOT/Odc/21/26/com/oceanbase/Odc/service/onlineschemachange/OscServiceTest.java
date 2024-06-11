package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import java.util.Optional;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseRepository mockedDatabaseRepository = mock(DatabaseRepository.class);
        ConnectionService mockedConnectionService = mock(ConnectionService.class);

        OscService underTest = new OscService();
        underTest.setDatabaseRepository(mockedDatabaseRepository);
        underTest.setConnectionService(mockedConnectionService);

        DatabaseEntity mockedEntity = mock(DatabaseEntity.class);
        when(mockedDatabaseRepository.findById(1L)).thenReturn(Optional.ofNullable(mockedEntity));
        when(mockedEntity.getConnectionId()).thenReturn(1L);

        OscLockDatabaseUserInfo result = underTest.getOscDatabaseInfo(1L);
        assertNotNull(result);
        assertEquals(0L, result.getDatabaseId().longValue());
        assertFalse(result.getLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_NotPresent() {
        DatabaseRepository mockedDatabaseRepository = mock(DatabaseRepository.class);
        ConnectionService mockedConnectionService = mock(ConnectionService.class);

        OscService underTest = new OscService();
        underTest.setDatabaseRepository(mockedDatabaseRepository);
        underTest.setConnectionService(mockedConnectionService);

        when(mockedDatabaseRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        OscLockDatabaseUserInfo result = underTest.getOscDatabaseInfo(1L);
        assertNotNull(result);
    }

    @Test
    public void testSwapTable() {
        // Test needs to create mocks for various methods and inject those into the service for testing
    }

    @Test
    public void getLockUserIsRequired() {
      OscService oscService = new OscService();
      oscService.setConnectionService(new ConnectionService());
      encServiceUnderTest.setConnectionService(mockedConnectionService);

      ConnectionConfig mockedConnectionConfig = mock(ConnectionConfig.class);
      when(mockedConnectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(mockedConnectionConfig);
      when(mockedConnectionConfig.getDialectType()).thenReturn(anyString());
     
      boolean result = oscService.getLockUserIsRequired(100L);
      assertNotNull(result);
    }
}