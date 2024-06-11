package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowElement;
import com.oceanbase.odc.core.flow.model.FlowElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
@Service
@SkipAuthorize("odc internal usage")
public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptorImpl flowableAdaptor;

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "PID001";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        Optional<String> retrievedProcessInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(processInstanceId, retrievedProcessInstanceId.orElse(null));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "PID002";
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertFalse(flowInstanceId.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 2L;
        String processDefinitionId = "DEF002";
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        Optional<String> retrievedProcessDefinitionId = flowableAdaptor.getProcessDefinitionIdByFlowInstanceId(flowInstanceId);
        assertEquals(processDefinitionId, retrievedProcessDefinitionId.orElse(null));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "DEF003";
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertFalse(flowInstanceId.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 3L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowElementType flowableElementType = FlowElementType.ELEMENT;
        List<FlowElement> flowElements = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertEquals(0, flowElements.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "ACT001";
        Long flowInstanceId = 4L;
        Optional<FlowTaskInstance> taskInstance = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(taskInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "ACT002";
        Long flowInstanceId = 5L;
        Optional<FlowApprovalInstance> approvalInstance = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(approvalInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "ApprovalTask";
        Long flowInstanceId = 6L;
        Optional<FlowApprovalInstance> approvalInstance = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertFalse(approvalInstance.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "ACT003";
        Long flowInstanceId = 7L;
        Optional<FlowGatewayInstance> gatewayInstance = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(gatewayInstance.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance(8L, FlowNodeType.TASK, 9L);
        FlowableElement flowableElement = new FlowableElement("ACT004", "TaskElement", FlowElementType.TASK);
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        List<NodeInstanceEntity> entities = nodeInstanceRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(), nodeInstance.getNodeType(), flowableElement.getType());
        assertTrue(entities.stream().anyMatch(entity -> entity.getActivityId().equals(flowableElement.getActivityId())));
    }

    private class BaseFlowNodeInstance {
        private Long id;
        private FlowNodeType nodeType;
        private Long flowInstanceId;

        public BaseFlowNodeInstance(Long id, FlowNodeType nodeType, Long flowInstanceId) {
            this.id = id;
            this.nodeType = nodeType;
            this.flowInstanceId = flowInstanceId;
        }

        public Long getId() {
            return id;
        }

        public FlowNodeType getNodeType() {
            return nodeType;
        }

        public Long getFlowInstanceId() {
            return flowInstanceId;
        }
    }

}