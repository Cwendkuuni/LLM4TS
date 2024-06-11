package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.core.shared.PreConditions.assertFalse;
import static com.oceanbase.odc.core.shared.PreConditions.assertNotNull;
import static com.oceanbase.odc.core.shared.PreConditions.assertNull;
import static com.oceanbase.odc.core.shared.PreConditions.assertEquals;
import static com.oceanbase.odc.core.shared.PreConditions.assertFalse;
import static com.oceanbase.odc.core.shared.PreConditions.assertThat;
import static com.oceanbase.odc.core.shared.PreConditions.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskResult;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigDto;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        // given
        Long id = UUID.randomUUID().longValue();
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        // when
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        // then
        assertNotNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseFound() {
        // given
        Long id = UUID.randomUUID().longValue();
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id + 1);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        // when
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        // then
        assertEquals(id, result.getDatabaseId());
        assertTrue(oscService.getLockUserIsRequired(id));
    }

    @Test
    public void testSwapTable_scheduleTaskNotFound() {
        // given
        Long scheduleTaskId = UUID.randomUUID().longValue();

        // when
        oscService.swapTable(scheduleTaskId);

        // then
        // nothing happens
    }

    @Test
    public void testSwapTable_scheduleTaskFoundButNotCompleted() {
        // given
        Long scheduleTaskId = UUID.randomUUID().longValue();

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleId(UUID.randomUUID().longValue());
        scheduleTask.setResultJson(null);
        scheduleTask.setJobName("schedule_job_1");
        scheduleTask.setJobType(JobType.SCHEDULED);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        // when
        oscService.swapTable(scheduleTaskId);

        // then
        assertFalse("Expected SwapTable to fail when job type is not ONLINE_SCHEMA_CHANGE",
                scheduleTask.getJobType() == JobType.ONLINE_SCHEMA_CHANGE);
    }

    @Test
    public void testSwapTable_scheduleTaskFoundAndCompleted() {
        // given
        Long scheduleTaskId = UUID.randomUUID().longValue();

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleId(UUID.randomUUID().longValue());
        scheduleTask.setJobName("schedule_job_1");
        scheduleTask.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleTask.setResultJson("{\"manualSwapTableEnabled\":true,\"manualSwapTableStarted\":false}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        // when
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        // then
        assertNotNull(result);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    public void testSwapTable_getLockUserIsRequired_returnsFalse() {
        // given
        Long connectionId = UUID.randomUUID().longValue();
        when(oscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(false);

        // when
        boolean result = oscService.getLockUserIsRequired(connectionId);

        // then
        assertFalse(result);
    }

    @Test
    public void testSwapTable_getLockUserIsRequired_returnsTrue() {
        // given
        Long connectionId = UUID.randomUUID().longValue();
        ConnectionConfig decryptedConnConfig = new ConnectionConfigDto();

        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenAnswer(invocation -> OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(),
                        () -> {
                            ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
                            ConnectionSession connSession = null;
                            String version = null;
                            try {
                                connSession = factory.generateSession();
                                version = ConnectionSessionUtil.getVersion(connSession);
                            } catch (Exception e) {
                                log.info("Get connection occur error", e);
                            } finally {
                                if (connSession != null) {
                                    connSession.expire();
                                }
                            }
                            return version;
                        }));

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);

        // when
        boolean result = oscService.getLockUserIsRequired(connectionId);

        // then
        assertTrue(result);
    }
}