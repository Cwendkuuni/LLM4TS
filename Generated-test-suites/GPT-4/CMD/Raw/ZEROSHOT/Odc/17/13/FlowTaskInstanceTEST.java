
package com.oceanbase.odc.service.flow.instance;

import org.flowable.engine.TaskService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.BDDMockito.given;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntityRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceEntityRepository serviceTaskRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    private FlowableAdaptor flowableAdaptor;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;

    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Before
    public void setup() {
        serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setId(1L);
        serviceTaskInstanceEntity.setOrganizationId(10L);
        serviceTaskInstanceEntity.setFlowInstanceId(20L);
        serviceTaskInstanceEntity.setStatus(FlowNodeStatus.NEW);
        serviceTaskInstanceEntity.setStartEndpoint(true);
        serviceTaskInstanceEntity.setEndEndpoint(true);
        serviceTaskInstanceEntity.setCreateTime(new Date());
        serviceTaskInstanceEntity.setUpdateTime(new Date());
        serviceTaskInstanceEntity.setTaskType(TaskType.SINGLE);
    }

    @Test
    public void testCreate() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, delegateConvertor, flowableAdaptor,
                                  eventPublisher, taskService, nodeRepository, sequenceRepository, 
                                  serviceTaskRepository);
        flowTaskInstance.create();
        Assert.assertNotNull(flowTaskInstance.getId());
        Assert.assertNotNull(flowTaskInstance.getCreateTime());
        Assert.assertNotNull(flowTaskInstance.getUpdateTime());
        Assert.assertEquals(FlowNodeStatus.NEW, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, delegateConvertor, 
                                  flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, 
                                  serviceTaskRepository);
        flowTaskInstance.setId(1L);
        flowTaskInstance.update();
        Assert.assertEquals(1L, flowTaskInstance.getId().longValue());
    }

    @Test
    public void testDelete() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, delegateConvertor, 
                                  flowableAdaptor, eventPublisher, taskService, nodeRepository, 
                                  sequenceRepository, serviceTaskRepository);
        flowTaskInstance.setId(1L);
        flowTaskInstance.delete();
        Assert.assertNull(flowTaskInstance.getId());
    }

    @Test
    public void testAlloc() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, delegateConvertor, 
                                  flowableAdaptor, eventPublisher, taskService, nodeRepository, 
                                  sequenceRepository, serviceTaskRepository);
        flowTaskInstance.alloc();
        Assert.assertNotNull(flowTaskInstance.getId());
    }

    @Test
    public void testDealloc() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, delegateConvertor, 
                                  flowableAdaptor, eventPublisher, taskService, nodeRepository, 
                                  sequenceRepository, serviceTaskRepository);
        flowTaskInstance.dealloc();
        Assert.assertNull(flowTaskInstance.getId());
    }

    // repeat above pattern for all methods in class
}
