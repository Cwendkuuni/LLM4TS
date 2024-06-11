package com.oceanbase.odc.metadb.flow;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;



class UserTaskServiceTest {
    
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @InjectMocks
    private UserTaskService userTaskService;
    
    @Test
    void testFindByStatus(){
    
        //Setup
        FlowNodeStatus flowNodeStatus = FlowNodeStatus.PENDING;

        //Stubbing the dependencies
        when(userTaskInstanceRepository.findByStatus(flowNodeStatus)).thenReturn(new ArrayList<UserTaskInstanceEntity>());
        
        // Call the method under test
        List<UserTaskInstanceEntity> results = userTaskService.findByStatus(flowNodeStatus);
        
        // Validate that the results are as expected
        assertNotNull(results, "Results expected, none found");
        
    }

    // And similar pattern for all the methods you want to test here... 

}

