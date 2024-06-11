package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {YourTestConfiguration.class})
public class OnlineSchemaChangeFlowableTaskTest {

    @Autowired
    private OnlineSchemaChangeFlowableTask implementation;

    @Mock 
    private DelegateExecution execution;
    
    @Mock
    private TaskService taskService;

    @Test
    public void testStart() throws Exception {
        Long taskId = 123L;
        Mockito.doNothing().when(taskService).start(taskId);
        implementation.start(taskId, taskService, execution);
        Mockito.verify(taskService).start(taskId);
    }

    /* .... Further tests for remaining methods .... */

}