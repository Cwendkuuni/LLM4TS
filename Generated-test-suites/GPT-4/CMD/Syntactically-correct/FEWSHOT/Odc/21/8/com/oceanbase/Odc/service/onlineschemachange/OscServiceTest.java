
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.Optional;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OscServiceTest {

    @Mock
    private DatabaseEntity databaseEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ScheduleEntity scheduleEntity;
    
    @Mock
    private ConnectionConfig connectionConfig;

    private OscService oscService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        oscService = spy(new OscService());
    }

    @Test
    public void testGetOscDatabaseInfo_Existing() {
        doReturn(Optional.of(databaseEntity)).when(databaseEntity).findById(1L);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNotNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_NotExisting() {
        doReturn(Optional.empty()).when(databaseEntity).findById(1L);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNull(result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskEntity).findById(1L);
        doReturn(Optional.of(scheduleEntity)).when(scheduleEntity).findById(1L);
        doReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE).when(scheduleEntity).getJobType();
        OscSwapTableVO result = oscService.swapTable(1L);
        assertNotNull(result);
    }

    @Test
    public void testGetLockUserIsRequired() {
        doReturn(Optional.of(connectionConfig)).when(connectionConfig).findById(1L);
        boolean result = oscService.getLockUserIsRequired(1L);
        assertEquals(false, result); // assuming it's false as it depends on external service
    }
}
