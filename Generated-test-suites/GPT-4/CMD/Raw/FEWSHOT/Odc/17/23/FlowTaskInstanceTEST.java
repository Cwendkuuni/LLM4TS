package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {
    @Mock
    private RuntimeDelegateMapper mockConvertor;
    @Mock
    private FlowableAdaptor mockFlowableAdaptor;
    @Mock
    private TaskService mockTaskService;
    @Mock
    private ServiceTaskInstanceEntity mockEntity;
    @Mock
    private Task mockTask;
    @Mock
    private BaseRuntimeFlowableDelegate mockFlowDelegate;
    @Mock
    private FlowableElement mockFlowableElement;

    private FlowTaskInstance testInstance;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        testInstance = new FlowTaskInstance(mockEntity, mockConvertor, mockFlowableAdaptor,
                mockEventPublisher, mockTaskService, mockNodeRepo, mockSeqRepo, mockRepo);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAllocAndDealloc(){
        testInstance.alloc();
        assertNotNull(testInstance.getListener());

        testInstance.dealloc();
        assertNull(testInstance.getListener());
    }

    @Test
    public void testGetCoreFlowableElementType(){
        FlowableElementType type = testInstance.getCoreFlowableElementType();
        assertEquals(FlowableElementType.SERVICE_TASK, type);
    }

    @Test
    public void testTargetDelegateClass(){
        when(mockConvertor.map(any(TaskType.class))).thenReturn((Class) BaseRuntimeFlowableDelegate.class);
        Class<? extends BaseRuntimeFlowableDelegate<?>> result = testInstance.getTargetDelegateClass();
        assertNotNull(result);
    }

    @Test
    public void testCompleteFlowTask() {
        assertDoesNotThrow(() -> testInstance.completeFlowTask(false));
    }

    @Test
    public void testCancelFlowTask() {
        assertDoesNotThrow(() -> testInstance.cancel(true));
    }

    @Test
    public void testResultFlowTask() throws InterruptedException, ExecutionException {
        Object result = testInstance.getResult();
        assertNotNull(result);
    }

    @Test
    public void testResultFlowTaskInTime() throws InterruptedException, ExecutionException, TimeoutException {
        Object result = testInstance.getResult(1, TimeUnit.SECONDS);
        assertNotNull(result);
    }

}