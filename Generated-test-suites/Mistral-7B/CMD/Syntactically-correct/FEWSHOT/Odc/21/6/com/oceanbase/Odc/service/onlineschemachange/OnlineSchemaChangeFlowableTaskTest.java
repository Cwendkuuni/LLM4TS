package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.json.JsonFactory;
import org.apache.commons.collections4.json.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OnlineSchemaChangeFlowableTaskTest {

    @MockBean
    private OrganizationService organizationService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private ScheduleTaskService scheduleTaskService;

    @MockBean
    private QuartzJobService quartzJobService;

    @MockBean
    private OnlineSchemaChangeTaskHandler taskHandler;

    @MockBean
    private OscTaskCompleteHandler completeHandler;

    private OnlineSchemaChangeFlowableTask testee;

    @Before
    public void setUp() {
        testee = new OnlineSchemaChangeFlowableTask();
        ReflectionTestUtils.setFieldValue(testee, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setFieldValue(testee, "scheduleService", scheduleService);
        ReflectionTestUtils.setFieldValue(testee, "scheduleTaskService", scheduleTaskService);
        ReflectionTestUtils.setFieldValue(testee, "quartzJobService", quartzJobService);
        ReflectionTestUtils.setFieldValue(testee, "taskHandler", taskHandler);
        ReflectionTestUtils.setFieldValue(testee, "completeHandler", completeHandler);
        ReflectionTestUtils.setFieldValue(testee, "organizationService", organizationService);
    }

    @Test
    public void testCreateScheduleEntity() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        ScheduleEntity expectedScheduleEntity = new ScheduleEntity();
        expectedScheduleEntity.setConnectionId(1L);
        expectedScheduleEntity.setDatabaseName(schema);
        expectedScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        expectedScheduleEntity.setStatus(ScheduleStatus.ENABLED);
        expectedScheduleEntity.setAllowConcurrent(false);
        expectedScheduleEntity.setCreatorId(1L);
        expectedScheduleEntity.setOrganizationId(1L);
        expectedScheduleEntity.setProjectId(1L);
        expectedScheduleEntity.setDatabaseId(1L);
        expectedScheduleEntity.setModifierId(1L);
        TriggerConfig triggerConfig = new TriggerConfig();
        triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
        triggerConfig.setCronExpression("0/10 * * * * ?");
        expectedScheduleEntity.setTriggerConfigJson(JsonFactory.getJsonObject().toString(triggerConfig));
        expectedScheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
        expectedScheduleEntity.setJobParametersJson(JsonFactory.getJsonObject().toString(parameter));

        ScheduleEntity actualScheduleEntity = testee.createScheduleEntity(connectionConfig, parameter, schema);

        assertEquals(expectedScheduleEntity, actualScheduleEntity);
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        Long scheduleId = 1L;
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity expectedScheduleTaskEntity = new ScheduleTaskEntity();
        expectedScheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        expectedScheduleTaskEntity.setJobName(scheduleId.toString());
        expectedScheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        expectedScheduleTaskEntity.setProgressPercentage(0.0);
        expectedScheduleTaskEntity.setParametersJson(JsonFactory.getJsonObject().toString(param));
        expectedScheduleTaskEntity.setResultJson(JsonFactory.getJsonObject().toString(new OnlineSchemaChangeScheduleTaskResult(param)));
        expectedScheduleTaskEntity.setFireTime(new Date());

        ScheduleTaskEntity actualScheduleTaskEntity = testee.createScheduleTaskEntity(scheduleId, param);

        assertEquals(expectedScheduleTaskEntity, actualScheduleTaskEntity);
    }

    @Test
    public void testStart() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        String schema = "testSchema";

        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        Mockito.when(scheduleEntity.getId()).thenReturn(1L);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersList = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters subTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        subTaskParameters.setId(1L);
        subTaskParametersList.add(subTaskParameters);

        Mockito.when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(scheduleEntity);

        Mockito.when(taskHandler.start(scheduleId, subTaskParameters.getId())).thenReturn(true);

        testee.start(1L, null, null);

        Mockito.verify(scheduleService, Mockito.times(1)).create(scheduleEntity);
        Mockito.verify(taskHandler, Mockito.times(1)).start(1L, 1L);
    }

    @Test
    public void testIsSuccessful() {
        testee.status = TaskStatus.DONE;
        assertTrue(testee.isSuccessful());

        testee.status = TaskStatus.FAILED;
        assertFalse(testee.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        testee.status = TaskStatus.DONE;
        assertFalse(testee.isFailure());

        testee.status = TaskStatus.FAILED;
        assertTrue(testee.isFailure());

        testee.status = TaskStatus.CANCELED;
        assertTrue(testee.isFailure());
    }

    @Test
    public void testOnFailure() throws Exception {
        testee.start(1L, null, null);
        testee.onFailure(1L, null);

        Mockito.verify(taskHandler, Mockito.times(1)).terminate(1L, 1L);
    }

    @Test
    public void testOnSuccessful() throws Exception {
        testee.start(1L, null, null);
        testee.onSuccessful(1L, null);

        Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testOnTimeout() throws Exception {
        testee.start(1L, null, null);
        testee.onTimeout(1L, null);

        Mockito.verify(taskService, Mockito.times(1)).cancel(1L);
    }

    @Test
    public void testOnProgressUpdate() {
        Long scheduleId = 1L;
        List<ScheduleTaskEntity> tasks = new ArrayList<>();

        Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(Mockito.mock(Page.class));

        testee.progressStatusUpdate(tasks);

        Mockito.verify(taskService, Mockito.times(0)).update(Mockito.any(TaskEntity.class));

        testee.percentage = 50.0;
        testee.onProgressUpdate(1L, null);

        Mockito.verify(taskService, Mockito.times(1)).update(Mockito.any(TaskEntity.class));

        TaskEntity taskEntity = Mockito.mock(TaskEntity.class);
        Mockito.when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        Mockito.when(taskService.detail(taskId)).thenReturn(taskEntity);

        JsonObject expectedResultJson = JsonFactory.getJsonObject();
        expectedResultJson.put("tasks", ListUtils.toJson(tasks));
        ReflectionTestUtils.setFieldValue(testee, "status", TaskStatus.DONE);

        testee.onProgressUpdate(1L, null);

        TaskEntity actualTaskEntity = Mockito.mock(TaskEntity.class);
        Mockito.when(actualTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        Mockito.when(actualTaskEntity.getResultJson()).thenReturn(expectedResultJson.toString());
        Mockito.when(taskService.update(actualTaskEntity)).thenReturn(actualTaskEntity);

        ReflectionTestUtils.setFieldValue(testee, "status", TaskStatus.RUNNING);
        ReflectionTestUtils.setFieldValue(testee, "percentage", 50.0);

        testee.onProgressUpdate(1L, null);

        Mockito.verify(taskService, Mockito.times(2)).update(Mockito.any(TaskEntity.class));
    }

    @Test
    public void testCancel() throws Exception {
        testee.start(1L, null, null);
        testee.cancel(true, 1L, null);

        Mockito.verify(taskHandler, Mockito.times(1)).terminate(1L, 1L);
        Mockito.verify(taskService, Mockito.times(1)).cancel(1L);
    }

    @Test
    public void testIsCancelled() {
        testee.status = TaskStatus.CANCELED;
        assertTrue(testee.isCancelled());

        testee.status = TaskStatus.DONE;
        assertFalse(testee.isCancelled());
    }
}