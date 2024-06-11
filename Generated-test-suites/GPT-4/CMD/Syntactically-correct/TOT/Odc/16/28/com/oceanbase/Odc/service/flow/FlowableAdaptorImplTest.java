package com.oceanbase.odc.service.flow;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

   @InjectMocks
   private FlowableAdaptorImpl flowableAdaptor;

   @Mock
   private FlowInstanceRepository flowInstanceRepository;

   @Mock
   private TaskService taskService;

  // And all other @Autowired dependencies...

   @Before
   public void setUp() throws Exception {
       flowableAdaptor = new FlowableAdaptorImpl();
   }

   @Test
   public void setProcessInstanceId() {
       Long testFlowInstanceId = 1L;
       String testProcessInstanceId = "some-id";
       FlowInstanceEntity testEntity = new FlowInstanceEntity();
       testEntity.setId(testFlowInstanceId);
       Optional<FlowInstanceEntity> testEntityOptional = Optional.of(testEntity);

       when(flowInstanceRepository.findById(testFlowInstanceId)).thenReturn(testEntityOptional);
       when(flowInstanceRepository.updateProcessInstanceIdById(testFlowInstanceId, testProcessInstanceId)).thenReturn(1);

       flowableAdaptor.setProcessInstanceId(testFlowInstanceId, testProcessInstanceId);

       verify(flowInstanceRepository, times(1)).findById(testFlowInstanceId);
       verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(testFlowInstanceId, testProcessInstanceId);
   }

   // Similar tests can be created for remaining public methods

}