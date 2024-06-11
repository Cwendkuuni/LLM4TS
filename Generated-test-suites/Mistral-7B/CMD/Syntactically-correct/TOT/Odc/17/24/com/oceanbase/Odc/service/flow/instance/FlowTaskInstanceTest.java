import org.apache.commons.lang3.RandomUtils; import org.apache.commons.lang3.StringUtils; import org.flowable.engine.common.api.FlowableEngine; import org.flowable.engine.common.api.FlowableEngineConfiguration; import org.flowable.engine.common.api.FlowableSession; import org.flowable.engine.common.api.FlowableTransactionConfiguration; import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration; import org.flowable.engine.impl.interceptor.CommandExecutorImpl; import org.flowable.engine.impl.interceptor.CommandContext; import org.flowable.engine.impl.persistence.entity.ExecutionEntity; import org.flowable.engine.impl.persistence.entity.ExecutionContextEntity; import org.flowable.engine.impl.persistence.entity.ExecutionVariableEntity; import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity; import org.flowable.engine.impl.persistence.entity.TaskEntity; import org.flowable.engine.impl.test.Deployment; import org.flowable.engine.impl.test.ProcessTestCase;import org.flowable.engine.impl.test.annotation.FlowableTest; import org.junit.Before; import org.junit.Test;

import static org.junit.Assert.assertEquals; import static org.junit.Assert.assertFalse; import static org.junit.Assert.assertNotNull; import static org.junit.Assert.assertNull; import static org.junit.Assert.assertTrue; import static org.junit.Assert.fail;

import com.oceanbase.odc.core.flow.model.FlowNodeType; import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository; import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity; import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository; import com.oceanbase.odc.service.flow.FlowableAdaptor; import com.oceanbase.odc.service.flow.FlowTaskInstance; import com.oceanbase.odc.service.flow.FlowTaskInstanceEntity; import com.oceanbase.odc.service.flow.NodeInstanceEntity; import com.oceanbase.odc.service.flow.RuntimeDelegateMapper; import com.oceanbase.odc.service.flow.TaskServiceTestConfig;

@FlowableTest public class FlowTaskInstanceTest {

 private static final String DEPLOYMENT_KEY = "testFlowTaskInstance"; private static final String PROCESS_DEFINITION_KEY = "testProcessDefinition"; private static final String FLOW_INSTANCE_ID = "testFlowInstanceId"; private static final Long TASK_ID = 1L; private static final Long NODE_INSTANCE_ID = 1L; private static final Long TARGET_TASK_ID = 2L; private static final String TASK_NAME = "TestTask"; private static final String TASK_TYPE = "testTaskType"; private static final String EXECUTION_STRATEGY = "manual"; private static final long EXECUTION_TIME = 5000; private static final long PENDING_EXPIRE_INTERVAL_SECONDS = 1000; private NodeInstanceEntity nodeInstance; private ServiceTaskInstanceEntity serviceTaskInstance; private ServiceTaskInstanceRepository serviceTaskRepository; private NodeInstanceEntityRepository nodeInstanceRepository; private FlowableEngine engine; private FlowableAdaptor flowableAdaptor; private RuntimeDelegateMapper delegateConvertor; private FlowTaskInstance flowTaskInstance; private FlowableSession session; private CommandContext context;

 @Before public void setUp() { serviceTaskRepository = new ServiceTaskInstanceRepository(); nodeInstanceRepository = new NodeInstanceEntityRepository(); engine = new StandaloneProcessEngineConfiguration().buildProcessEngine(); flowableAdaptor = new FlowableAdaptor(engine); delegateConvertor = new RuntimeDelegateMapper(); }

 @Deployment public void deployment() { ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity(); processDefinition.setKey(PROCESS_DEFINITION_KEY); processDefinition.setName("Test Process Definition"); processDefinition.setResourceName("testProcessDefinition.bpmn20.xml"); processDefinition.setDefinitionVersion(1L); ProcessDefinitionEntity savedProcessDefinition = engine.getRepositoryService() .saveProcessDefinition(processDefinition);

 Deployment deployment = new Deployment(); deployment.setName(DEPLOYMENT_KEY); deployment.addResource("testProcessDefinition.bpmn20.xml"); engine.getRepositoryService().createDeployment(deployment);

 flowableAdaptor.init(engine); }

 @Test public void testCreateFlowTaskInstance() { // Create a new FlowTaskInstance with a manual execution strategy // Create a new NodeInstance with a FlowableElement of type 'ServiceTask' and a 'FlowTaskInstance' child with the same execution strategy // Start the process and verify that the FlowTaskInstance is created and has the correct status // Verify that the NodeInstance and FlowTaskInstance are linked // Verify that the execution strategy is set correctly // Verify that the target task ID is set correctly // Verify that the execution time is set correctly // Verify that the pending expire interval is set correctly // Verify that the listener is registered // Test the cancel and abort methods // Test the getResult method // Test the isDone method

 nodeInstance = new NodeInstanceEntity(); nodeInstance.setId(RandomUtils.nextLong(1, 10000)); nodeInstance.setFlowInstanceId(FLOW_INSTANCE_ID); nodeInstance.setNodeType(FlowNodeType.SERVICE_TASK.name()); nodeInstance.setStatus(FlowNodeStatus.CREATED.name()); nodeInstanceRepository.save(nodeInstance);

 serviceTaskInstance = new ServiceTaskInstanceEntity(); serviceTaskInstance.setOrganizationId(nodeInstance.getOrganizationId()); serviceTaskInstance.setFlowInstanceId(nodeInstance.getFlowInstanceId()); serviceTaskInstance.setNodeInstanceId(nodeInstance.getId()); serviceTaskInstance.setStatus(FlowNodeStatus.PENDING.name()); serviceTaskInstance.setTaskType(TASK_TYPE); serviceTaskInstance.setStrategy(EXECUTION_STRATEGY); serviceTaskInstance.setWaitExecExpireIntervalSeconds(PENDING_EXPIRE_INTERVAL_SECONDS); serviceTaskInstance.setExecutionTime(EXECUTION_TIME); serviceTaskInstanceRepository.save(serviceTaskInstance);

 flowTaskInstance = new FlowTaskInstance(serviceTaskInstance, delegateConvertor, flowableAdaptor, nodeInstanceRepository, serviceTaskRepository);

 assertTrue(FlowNodeStatus.PENDING.name().equals(flowTaskInstance.getStatus())); assertNull(flowTaskInstance.getTargetTaskHandle()); assertNull(flowTaskInstance.getListener());

 session = engine.getRuntimeService().createProcessInstance(FLOW_INSTANCE_ID); context = engine.getCommandContext();

 assertEquals(FLOW_INSTANCE_ID, flowTaskInstance.getFlowInstanceId()); assertEquals(nodeInstance.getId(), flowTaskInstance.getNodeInstanceId()); assertFalse(flowTaskInstance.isPresentOnThisMachine());

 assertEquals(FlowNodeStatus.CREATED.name(), nodeInstance.getStatus()); assertEquals(FlowNodeStatus.PENDING.name(), serviceTaskInstance.getStatus());

 assertEquals(FlowNodeType.SERVICE_TASK.name(), nodeInstance.getNodeType()); assertEquals(FlowNodeType.SERVICE_TASK.name(), flowTaskInstance.getNodeType()); assertEquals(FlowNodeStatus.PENDING.name(), flowTaskInstance.getStatus());

 assertEquals(TASK_TYPE, flowTaskInstance.getTaskType()); assertEquals(EXECUTION_STRATEGY, flowTaskInstance.getStrategyConfig().getStrategy()); assertEquals(PENDING_EXPIRE_INTERVAL_SECONDS, flowTaskInstance.getStrategyConfig().getPendingExpireIntervalSeconds()); assertEquals(EXECUTION_TIME, flowTaskInstance.getStrategyConfig().getExecutionTime()); assertEquals(TARGET_TASK_ID, flowTaskInstance.getTargetTaskId());

 // Cancel and abort tests

 flowableAdaptor.init(engine); // Initialize the FlowableAdaptor again to simulate a new context

 try { flowTaskInstance.cancel(true); fail("FlowTaskInstance should not be able to be cancelled as it is not running"); } catch (Exception e) { /* expected */ }

 try { flowTaskInstance.abort(); fail("FlowTaskInstance should not be able to be aborted as it is not running"); } catch (Exception e) { /* expected */ }

 // GetResult tests

 try { Thread.sleep(EXECUTION_TIME + PENDING_EXPIRE_INTERVAL_SECONDS + 100); } catch (InterruptedException e) { /* ignore */ }

 flowTaskInstance.bindServiceTask(new DummyTaskHandle());

 Object result = flowTaskInstance.getResult(); assertEquals(StringUtils.EMPTY, result);

 try { result = flowTaskInstance.getResult(1, TimeUnit.SECONDS); fail("FlowTaskInstance should not be able to return a result within 1 second"); } catch (InterruptedException e) { /* ignore */ } catch (ExecutionException e) { /* ignore */ }

 try { result = flowTaskInstance.getResult(EXECUTION_TIME + PENDING_EXPIRE_INTERVAL_SECONDS + 100, TimeUnit.MILLISECONDS); assertNotNull(result); } catch (InterruptedException e) { /* ignore */ } catch (ExecutionException e) { /* ignore */ } catch (TimeoutException e) { fail("FlowTaskInstance should have returned a result within the given timeout"); }

 // IsDone tests

 try { Thread.sleep(EXECUTION_TIME + PENDING_EXPIRE_INTERVAL_SECONDS + 100); } catch (InterruptedException e) { /* ignore */ }

 assertTrue(flowTaskInstance.isDone());

 // Clean up

 flowTaskInstance.abort(); flowableAdaptor.deleteNodeInstance(nodeInstance); serviceTaskInstanceRepository.deleteById(serviceTaskInstance.getId()); nodeInstanceRepository.deleteById(nodeInstance.getId()); engine.shutdown(); }

 // Inner class for testing the cancel method without the need for a real task handle

 static class DummyTaskHandle implements BaseRuntimeFlowableDelegate<Object> { @Override public void cancel(boolean mayInterruptIfRunning) { /* empty */ } @Override public Object get() throws ExecutionException, InterruptedException { return null; } @Override public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return null; } @Override public void complete() { /* empty */ } }

}